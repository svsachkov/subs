package subs.api.user;

import subs.api.UserRole;
import subs.api.CrudService;
import subs.api.FieldsValidator;
import subs.exception.ApiRequestException;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.ConstraintViolation;

import java.util.List;

/**
 * The service that implements CRUD (Create, Read, Update, Delete) operations on users.
 */
@Service
@AllArgsConstructor
public class UserService implements CrudService<User>, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FieldsValidator<User> fieldsValidator = new FieldsValidator<>();

    public void register(UserRequest request) throws ApiRequestException {
        // Create a new user.
        User user = new User(
                UserRole.ROLE_USER,
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        create(user);
    }

    public boolean update(Integer id, UserRequest request) throws ApiRequestException {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return false;
        }

        // Create a new user.
        User updated = new User(
                user.getRole(),
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        return update(id, updated);
    }

    @Override
    public void create(User user) throws ApiRequestException {
        // We check the user fields for correctness.
        for (ConstraintViolation<User> violation : fieldsValidator.validate(user)) {
            throw new ApiRequestException(violation.getMessage());
        }

        // If the user with the specified email is already registered.
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ApiRequestException("This email address is already taken");
        }

        // If the user with the specified username is already registered.
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ApiRequestException("This username is already taken");
        }

        // We encode the password for storage in the database.
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public User read(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public boolean update(Integer id, User updated) throws ApiRequestException {
        User user = userRepository.findById(id).orElse(null);

        // If the user for the update was found.
        if (user != null) {
            // We check all user fields, according to which we need to update the data.
            for (ConstraintViolation<User> violation : fieldsValidator.validate(updated)) {
                throw new ApiRequestException(violation.getMessage());
            }

            User current = userRepository.findByUsername(updated.getUsername()).orElse(null);
            if (current != null && !current.getId().equals(id)) {
                throw new ApiRequestException("This username is already taken");
            }

            current = userRepository.findByEmail(updated.getEmail()).orElse(null);
            if (current != null && !current.getId().equals(id)) {
                throw new ApiRequestException("This email address is already taken");
            }

            updated.setId(id);
            updated.setFiles(user.getFiles());
            userRepository.save(updated);

            return true;
        }

        return false;
    }

    @Override
    public boolean delete(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
        }

        return user;
    }
}
