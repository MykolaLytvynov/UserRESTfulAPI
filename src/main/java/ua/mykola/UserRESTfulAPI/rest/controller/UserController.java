package ua.mykola.UserRESTfulAPI.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.mykola.UserRESTfulAPI.rest.dto.UserDto;
import ua.mykola.UserRESTfulAPI.rest.validation.CreateValidation;
import ua.mykola.UserRESTfulAPI.rest.validation.UpdateValidation;
import ua.mykola.UserRESTfulAPI.exception.ValidationException;
import ua.mykola.UserRESTfulAPI.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Controller for handling user-related HTTP requests.
 * This controller provides endpoints for interacting with user data.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Retrieves a list of all users.
     *
     * @return a ResponseEntity containing a list of user data
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> obtainedUsers = userService.getAll();
        return ResponseEntity.ok(obtainedUsers);
    }

    /**
     * Retrieves user information by ID.
     *
     * @param id - the ID of the user to retrieve
     * @return a ResponseEntity containing the user data
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable long id) {
        UserDto obtainedUser = userService.getById(id);
        return ResponseEntity.ok(obtainedUser);
    }

    /**
     * Retrieves users whose birth dates fall within a specified range.
     *
     * @param from - the start date of the birth date range
     * @param to - the end date of the birth date range
     * @return a ResponseEntity containing a list of user data
     */
    @GetMapping("/birthdate-range")
    public ResponseEntity<List<UserDto>> getByBirthDateRange(
            @RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate from,
            @RequestParam("to") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate to) {
        List<UserDto> obtainedUsers = userService.getByBirthDateRange(from, to);
        return ResponseEntity.ok(obtainedUsers);
    }

    /**
     * Registers a new user.
     *
     * @param userDto - the user data to register
     * @param bindingResult - the result of the validation
     * @return a ResponseEntity containing the saved user data
     * @throws ValidationException if validation fails
     */
    @PostMapping
    public ResponseEntity<UserDto> register(
            @Validated(CreateValidation.class) @RequestBody UserDto userDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new ValidationException(errorMessages);
        }
        UserDto savedUser = userService.save(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }

    /**
     * Updates user information by ID.
     *
     * @param id - the ID of the user to update
     * @param userDto - the updated user data
     * @param bindingResult - the result of the validation
     * @return a ResponseEntity containing the updated user data
     * @throws ValidationException if validation fails
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable("id") long id,
            @Validated(UpdateValidation.class) @RequestBody UserDto userDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new ValidationException(errorMessages);
        }

        UserDto updatedUser = userService.update(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id - the ID of the user to delete
     * @return a ResponseEntity indicating success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        userService.delete(id);
        return ResponseEntity.ok("User was deleted");
    }
}
