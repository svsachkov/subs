package subs.api.file;

import subs.api.CrudService;
import subs.api.FieldsValidator;
import subs.api.user.UserService;
import subs.exception.ApiRequestException;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FileService implements CrudService<File> {

    private final FileRepository fileRepository;
    private final FieldsValidator<File> fieldsValidator = new FieldsValidator<>();
    private final UserService userService;

    @Override
    public void create(File entity) throws ApiRequestException {

    }

    @Override
    public List<File> readAll() {
        return null;
    }

    @Override
    public File read(Integer id) {
        return null;
    }

    @Override
    public boolean update(Integer id, File updated) throws ApiRequestException {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
