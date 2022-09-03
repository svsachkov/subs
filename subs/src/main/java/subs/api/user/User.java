package subs.api.user;

import subs.api.UserRole;
import subs.api.file.File;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The class that describes the user.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity(name = "user")
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(name = "users_sequence", sequenceName = "users_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "The user must have a role")
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 1, max = 35, message = "Username must be between 1 and 35 characters")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Email(message = "Incorrect email address")
    @NotBlank(message = "Email address cannot be empty")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "Password field must be filled")
    @Column(name = "password", nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    public User(UserRole role, String username, String email, String password) {
        this.role = role;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Adds a user file to the list of user files.
     *
     * @param file file to add
     */
    public void addFile(File file) {
        if (!this.files.contains(file)) {
            this.files.add(file);
            file.setUser(this);
        }
    }

    /**
     * Removes the file from the user's files list.
     *
     * @param file file to remove
     */
    public void removeFile(File file) {
        if (this.files.contains(file)) {
            this.files.remove(file);
            file.setUser(null);
        }
    }

    /**
     * Gets the permissions granted to the user.
     *
     * @return collection of user permissions
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
