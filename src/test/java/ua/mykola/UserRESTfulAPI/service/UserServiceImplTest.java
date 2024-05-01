package ua.mykola.UserRESTfulAPI.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import ua.mykola.UserRESTfulAPI.rest.dto.UserDto;
import ua.mykola.UserRESTfulAPI.dao.UserRepository;
import ua.mykola.UserRESTfulAPI.entity.User;
import ua.mykola.UserRESTfulAPI.exception.NotFoundException;
import ua.mykola.UserRESTfulAPI.exception.UnderagePersonException;
import ua.mykola.UserRESTfulAPI.testEntities.UserUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final String under18ExceptionMessage = "Registration of users under 18 years " +
            "of age is not permitted";
    private final String userNotFoundExceptionMessage = "User by id:1 was not found";

    @Test
    @DisplayName("Adding user")
    void givenUserToSave_whenAddUser_thenRepositoryIsCalled() {
        //given
        UserDto userDto = UserUtil.getMarkBohnDto();
        given(userRepository.save(any(User.class)))
                .willReturn(UserUtil.getMarkBohnPersisted());

        //when
        UserDto savedUser = userService.save(userDto);

        //then
        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Adding user under 18 years")
    void givenUserUnder18YearsToSave_whenAddUser_thenExceptionIsThrown() {
        //given
        UserDto userDto = UserUtil.getLeoUnder18YearsDto();

        //when
        UnderagePersonException ex = assertThrows(UnderagePersonException.class, () -> userService.save(userDto));

        //then
        assertEquals(under18ExceptionMessage, ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Updating user")
    void givenUserToUpdate_whenUpdateUser_thenRepositoryIsCalled() {
        //given
        UserDto userToUpdate = UserUtil.getMarkBohnToUpdateDto();
        given(userRepository.isExist(anyLong())).willReturn(true);
        given(userRepository.update(any(User.class)))
                .willReturn(UserUtil.getUpdatedMarkBohn());

        //when
        UserDto updatedUser = userService.update(1l, userToUpdate);

        //then
        assertNotNull(updatedUser);
        verify(userRepository, times(1)).update(any(User.class));
    }

    @Test
    @DisplayName("Updating non-existent user")
    void givenNonExistentUser_whenUpdateUser_thenExceptionIsThrown() {
        //given
        UserDto userToUpdate = UserUtil.getMarkBohnToUpdateDto();
        given(userRepository.isExist(anyLong())).willReturn(false);

        //when
        NotFoundException ex = assertThrows(NotFoundException.class, () -> userService.update(1l, userToUpdate));

        //then
        assertEquals(userNotFoundExceptionMessage, ex.getMessage());
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    @DisplayName("Updating user under 18 Years")
    void givenUserUnder18YearsToUpdate_whenUpdateUser_thenRepositoryIsCalled() {
        //given
        UserDto userToUpdate = UserUtil.getLeoUnder18YearsToUpdateDto();
        given(userRepository.isExist(anyLong())).willReturn(true);

        //when
        UnderagePersonException ex = assertThrows(UnderagePersonException.class,
                () -> userService.update(1l, userToUpdate));

        //then
        assertEquals(under18ExceptionMessage, ex.getMessage());
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    @DisplayName("Getting all users")
    void givenTwoUsers_whenGetAll_thenAllUsersIsReturned() {
        //given
        User user1 = UserUtil.getLisa40YearsPersisted();
        User user2 = UserUtil.getAlex30YearsPersisted();
        List<User> users = List.of(user1, user2);
        given(userRepository.getAll()).willReturn(users);

        //when
        List<UserDto> obtainedUsers = userService.getAll();

        //then
        List<UserDto> expectedUsers = List.of(UserDto.fromEntity(user1), UserDto.fromEntity(user2));
        assertFalse(CollectionUtils.isEmpty(obtainedUsers));
        assertArrayEquals(expectedUsers.toArray(), obtainedUsers.toArray());
    }

    @Test
    @DisplayName("Getting user by id")
    void givenId_whenGetUserById_thenUserIsReturned() {
        //given
        Optional<User> user = Optional.of(UserUtil.getMarkBohnPersisted());
        given(userRepository.getById(anyLong())).willReturn(user);

        //when
        UserDto obtainedUser = userService.getById(1l);

        //then
        assertNotNull(obtainedUser);
        verify(userRepository, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("Getting non-existent user by id")
    void givenNonExistentId_whenGetUserById_thenExceptionIsThrown() {
        //given
        given(userRepository.getById(anyLong())).willReturn(Optional.empty());

        //when
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.getById(1l));

        //then
        assertEquals(userNotFoundExceptionMessage, ex.getMessage());
    }

    @Test
    @DisplayName("Getting users by date of birth range")
    void givenTwoDates_whenGetUserById_thenExceptionIsThrown() {
        //given
        LocalDate from = LocalDate.now().minusYears(35);
        LocalDate to = LocalDate.now();
        User user1 = UserUtil.getLisa40YearsPersisted();
        User user2 = UserUtil.getAlex30YearsPersisted();
        given(userRepository.getAll()).willReturn(List.of(user1, user2));

        //when
        List<UserDto> obtainedUsers = userService.getByBirthDateRange(from, to);

        //then
        UserDto expectedUser = UserDto.fromEntity(user2);
        assertEquals(1, obtainedUsers.size());
        assertEquals(expectedUser, obtainedUsers.get(0));
    }

    @Test
    @DisplayName("Deleting user by id")
    void givenId_whenGetUserById_thenRepositoryIsCalled() {
        //given
        given(userRepository.isExist(anyLong())).willReturn(true);

        //when
        userService.delete(1l);

        //then
        verify(userRepository, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("Deleting non-existent user by id")
    void givenNonExistentId_whenDelete_thenExceptionIsThrown() {
        //given
        given(userRepository.isExist(anyLong())).willReturn(false);

        //when
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.delete(1l));

        //then
        assertEquals(userNotFoundExceptionMessage, ex.getMessage());
    }
}
