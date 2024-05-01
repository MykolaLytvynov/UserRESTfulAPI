package ua.mykola.UserRESTfulAPI.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.mykola.UserRESTfulAPI.rest.dto.UserDto;
import ua.mykola.UserRESTfulAPI.dao.UserRepository;
import ua.mykola.UserRESTfulAPI.entity.User;
import ua.mykola.UserRESTfulAPI.exception.NotFoundException;
import ua.mykola.UserRESTfulAPI.exception.UnderagePersonException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of User Service.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userStorage;

    /**
     * Saves a new user with the provided user data.
     *
     * @param userDto - the user data to save
     * @return the saved user data with new ID
     * @throws UnderagePersonException if the new user is under 18 years old
     */
    @Override
    public UserDto save(UserDto userDto) {
        isUnderagePerson(userDto.getBirthDate());
        User savedUser = userStorage.save(userDto.toEntity());
        return UserDto.fromEntity(savedUser);
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a list containing user data for all users
     */
    @Override
    public List<UserDto> getAll() {
        List<User> obtainedUsers = userStorage.getAll();
        return obtainedUsers.stream()
                .map(user -> UserDto.fromEntity(user))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves user information by their ID.
     *
     * @param id - the ID of the user to retrieve
     * @return the found user
     * @throws NotFoundException if the user with the specified ID does not exist
     */
    @Override
    public UserDto getById(Long id) {
        User foundUser = userStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("User by id:" + id + " was not found"));
        return UserDto.fromEntity(foundUser);
    }

    /**
     * Retrieves a list of users whose birth dates fall within the specified range.
     *
     * @param from - the start date of the birth date range
     * @param to - the end date of the birth date range
     * @return a list of users whose birth dates fall within the specified range
     */
    @Override
    public List<UserDto> getByBirthDateRange(LocalDate from, LocalDate to) {
        return userStorage.getAll()
                .stream()
                .filter(user -> user.getBirthDate().isAfter(from)
                        && user.getBirthDate().isBefore(to))
                .map(user -> UserDto.fromEntity(user))
                .collect(Collectors.toList());
    }

    /**
     * Updates user information by their ID with the provided user data.
     *
     * @param id - the ID of the user to update
     * @param userDto - the updated user data
     * @return the updated userDto
     * @throws NotFoundException if the user does not exist
     * @throws UnderagePersonException if the updated user is under 18 years old
     */
    @Override
    public UserDto update(long id, UserDto userDto) {
        isUserExist(id);
        if (userDto.getBirthDate() != null) {
            isUnderagePerson(userDto.getBirthDate());
        }
        User updatedUser = userDto.toEntity();
        updatedUser.setId(id);
        updatedUser = userStorage.update(updatedUser);
        return UserDto.fromEntity(updatedUser);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id - the ID of the user to delete
     * @throws NotFoundException if the user does not exist
     */
    @Override
    public void delete(long id) {
        isUserExist(id);
        userStorage.delete(id);
    }

    /**
     * Checks if a person is underage based on their birth date.
     * Throws an exception if the person is under 18 years old.
     *
     * @param birthDate - the birth date of the person to check
     * @throws UnderagePersonException if the person is under 18 years old
     */
    private void isUnderagePerson(LocalDate birthDate) {
        LocalDate minPossibleDate = LocalDate.now().minusYears(18);
        if (birthDate.isAfter(minPossibleDate)) {
            throw new UnderagePersonException("Registration of users under 18 years of age is not permitted");
        }
    }

    /**
     * Checks if a user exists based on their ID.
     * Throws a NotFoundException if the user does not exist.
     *
     * @param id - the ID of the user to check
     * @throws NotFoundException if the user does not exist
     */
    private void isUserExist(long id) {
        if (!userStorage.isExist(id)) {
            throw new NotFoundException("User by id:" + id + " was not found");
        }
    }
}
