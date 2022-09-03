package subs.api;

import subs.exception.ApiRequestException;

import java.util.List;

/**
 * An interface that describes CRUD (Create, Read, Update, Delete) operations on an object (entity).
 *
 * @param <T> object (entity) class
 */
public interface CrudService<T> {

    /**
     * Creates a new object (entity).
     *
     * @param entity passed object (entity)
     * @throws ApiRequestException thrown if it was not possible to create a new object (entity)
     */
    void create(T entity) throws ApiRequestException;

    /**
     * Returns a list of existing objects (entities).
     *
     * @return list of objects (entities)
     */
    List<T> readAll();

    /**
     * Returns an object (entity) by the given id.
     *
     * @param id - object (entity) id
     * @return object (entity) with the given id
     */
    T read(Integer id);

    /**
     * Updates the object (entity) according to the passed object (entity).
     *
     * @param id      - id of the object (entity) to update
     * @param updated - object (entity), according to which it is necessary to update
     * @return true - data updated, otherwise false (no object (entity) to update was found)
     * @throws ApiRequestException thrown if an error occurred while updating (failed to update)
     */
    boolean update(Integer id, T updated) throws ApiRequestException;

    /**
     * Deletes an object (entity) with the given id.
     *
     * @param id - id of the object (entity) to delete
     * @return true - the object has been deleted, otherwise false (the object to be deleted could not be found)
     */
    boolean delete(Integer id);
}
