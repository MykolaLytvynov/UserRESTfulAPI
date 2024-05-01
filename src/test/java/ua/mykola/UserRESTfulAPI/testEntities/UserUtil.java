package ua.mykola.UserRESTfulAPI.testEntities;

import ua.mykola.UserRESTfulAPI.rest.dto.UserDto;
import ua.mykola.UserRESTfulAPI.entity.User;

import java.time.LocalDate;

public class UserUtil {

    public static UserDto getMarkBohnDto() {
        return UserDto.builder()
                .firstName("Mark")
                .lastName("Bohn")
                .email("mark@gmail.com")
                .phoneNumber("1234")
                .address("str. Street")
                .birthDate(LocalDate.now().minusYears(20))
                .build();
    }

    public static User getMarkBohnPersisted() {
        return User.builder()
                .id(1l)
                .firstName("Mark")
                .lastName("Bohn")
                .email("mark@gmail.com")
                .phoneNumber("1234")
                .address("str. Street")
                .birthDate(LocalDate.now().minusYears(20))
                .build();
    }

    public static UserDto getMarkBohnPersistedDto() {
        return UserDto.fromEntity(getMarkBohnPersisted());
    }

    public static UserDto getLeoUnder18YearsDto() {
        return UserDto.builder()
                .firstName("Leo")
                .lastName("Bohn")
                .email("leo@gmail.com")
                .phoneNumber("1234")
                .address("str. Street")
                .birthDate(LocalDate.now().minusYears(17))
                .build();
    }

    public static UserDto getMarkBohnToUpdateDto() {
        return UserDto
                .builder()
                .firstName("Markiyan")
                .phoneNumber("12345")
                .build();
    }

    public static User getUpdatedMarkBohn() {
        return User
                .builder()
                .id(1l)
                .firstName("Markiyan")
                .lastName("Bohn")
                .email("mark@gmail.com")
                .phoneNumber("12345")
                .address("str. Street")
                .birthDate(LocalDate.now().minusYears(20))
                .build();
    }

    public static UserDto getLeoUnder18YearsToUpdateDto() {
        return UserDto
                .builder()
                .firstName("new Leo")
                .lastName("new Bohn")
                .birthDate(LocalDate.now().minusYears(17))
                .build();
    }

    public static User getLisa40YearsPersisted() {
        return User
                .builder()
                .id(1l)
                .firstName("Lisa")
                .lastName("Bohn")
                .email("lisa@gmail.com")
                .phoneNumber("1234532")
                .address("str. Street")
                .birthDate(LocalDate.now().minusYears(40))
                .build();
    }

    public static User getAlex30YearsPersisted() {
        return User
                .builder()
                .id(2l)
                .firstName("Alex")
                .lastName("Hotel")
                .email("hotel@gmail.com")
                .phoneNumber("1234532")
                .address("str. Street")
                .birthDate(LocalDate.now().minusYears(30))
                .build();
    }
}
