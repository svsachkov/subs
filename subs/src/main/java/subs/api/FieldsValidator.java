package subs.api;

import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import java.util.Set;

/**
 * The class used to check class fields for correctness (validity).
 *
 * @param <T> the class whose object will be checked
 */
@NoArgsConstructor
public class FieldsValidator<T> {

    /**
     * Checks the fields of the object for correctness (validity), in accordance with the annotations indicated above them.
     *
     * @param entity object (entity) whose fields need to be checked
     * @return Set of errors that were identified as a result of the check
     */
    public Set<ConstraintViolation<T>> validate(T entity) {
        return Validation.buildDefaultValidatorFactory().getValidator().validate(entity);
    }
}
