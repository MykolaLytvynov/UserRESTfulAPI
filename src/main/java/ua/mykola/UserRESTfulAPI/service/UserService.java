package ua.mykola.UserRESTfulAPI.service;

import ua.mykola.UserRESTfulAPI.rest.dto.UserDto;
import ua.mykola.UserRESTfulAPI.exception.NotFoundException;
import ua.mykola.UserRESTfulAPI.exception.UnderagePersonException;

import java.time.LocalDate;
import java.util.List;

/**
 * User Service.
 */
public interface UserService {

    /**
     * Saves a new user with the provided user data.
     *
     * @param userDto - the user data to save
     * @return the saved user data with new ID
     * @throws UnderagePersonException if the new user is under 18 years old
     */
    UserDto save(UserDto userDto);

    /**
     * Retrieves a list of all users.
     *
     * @return a list containing user data for all users
     */
    List<UserDto> getAll();

    /**
     * Retrieves user information by their ID.
     *
     * @param id - the ID of the user to retrieve
     * @return the found user
     * @throws NotFoundException if the user with the specified ID does not exist
     */
    UserDto getById(Long id);

    /**
     * Retrieves a list of users whose birth dates fall within the specified range.
     *
     * @param from - the start date of the birth date range
     * @param to - the end date of the birth date range
     * @return a list of users whose birth dates fall within the specified range
     */
    List<UserDto> getByBirthDateRange(LocalDate from, LocalDate to);

    /**
     * Updates user information by their ID with the provided user data.
     *
     * @param id - the ID of the user to update
     * @param userDto - the updated user data
     * @return the updated userDto
     * @throws NotFoundException if the user does not exist
     * @throws UnderagePersonException if the updated user's birth date makes them under 18 years old
     */
    UserDto update(long id, UserDto updatedUser);

    /**
     * Deletes a user by their ID.
     *
     * @param id - the ID of the user to delete
     * @throws NotFoundException if the user does not exist
     */
    void delete(long id);
}
