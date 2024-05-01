package ua.mykola.UserRESTfulAPI.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import ua.mykola.UserRESTfulAPI.rest.validation.CreateValidation;
import ua.mykola.UserRESTfulAPI.rest.validation.UpdateValidation;
import ua.mykola.UserRESTfulAPI.entity.User;

import java.time.LocalDate;

/**
 * Data transfer object (DTO) for user-related information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class UserDto {
    private Long id;

    @NotBlank(groups = CreateValidation.class, message = "Email is required")
    @Email(groups = CreateValidation.class, message = "Invalid email format")
    private String email;

    @NotBlank(groups = CreateValidation.class, message = "First name is required")
    @Size(groups = {CreateValidation.class, UpdateValidation.class},
            min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @Pattern(groups = {CreateValidation.class, UpdateValidation.class},
            regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
    private String firstName;

    @NotBlank(groups = CreateValidation.class, message = "Last name is required")
    @Size(groups = {CreateValidation.class, UpdateValidation.class},
            min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @Pattern(groups = {CreateValidation.class, UpdateValidation.class},
            regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
    private String lastName;

    @Past(groups = {CreateValidation.class, UpdateValidation.class},
            message = "Birth date must be in the past")
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;


    public User toEntity() {
        return User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate)
                .address(address)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
