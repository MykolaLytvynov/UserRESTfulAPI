package ua.mykola.UserRESTfulAPI.dao;

import ua.mykola.UserRESTfulAPI.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository to work with User entities
 */
public interface UserRepository {

    /**
     * Retrieves a list of all users
     *
     * @return a list of all users
     */
    List<User> getAll();

    /**
     * Saves a new user.
     *
     * @param user - the user to save
     * @return the saved user
     */
    User save(User user);

    /**
     * Retrieves a user by their ID.
     *
     * @param id - the ID of the user to retrieve
     * @return an Optional containing the user, if found
     */
    Optional<User> getById(long id);

    /**
     * Checks if a user with the specified ID exists.
     *
     * @param id - the ID of the user to check
     * @return true if the user exists, false otherwise
     */
    boolean isExist(long id);

    /**
     * Deletes a user by their ID.
     *
     * @param id - the ID of the user to delete
     */
    void delete(long id);

    /**
     * Updates an existing user.
     *
     * @param updatedUser - the updated user
     * @return the updated user
     */
    User update(User updatedUser);
}
