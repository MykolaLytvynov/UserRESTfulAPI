package ua.mykola.UserRESTfulAPI.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ua.mykola.UserRESTfulAPI.rest.dto.UserDto;
import ua.mykola.UserRESTfulAPI.exception.NotFoundException;
import ua.mykola.UserRESTfulAPI.exception.UnderagePersonException;
import ua.mykola.UserRESTfulAPI.service.UserService;
import ua.mykola.UserRESTfulAPI.testEntities.UserUtil;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private final String underagePersonMessage = "Registration of users under 18 years of age is not permitted";
    private final String notFoundMessage = "User by id:1 was not found";

    @Test
    @DisplayName("Registration user")
    public void givenUserDto_whenRegisterUser_thenSuccessResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getMarkBohnDto();
        UserDto savedUserDto = UserUtil.getMarkBohnPersistedDto();
        given(userService.save(any(UserDto.class))).willReturn(savedUserDto);

        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(savedUserDto.getEmail())));
    }

    @Test
    @DisplayName("Registration user with incorrect first name using number in name")
    public void givenUserDtoWithIncorrectFirstNameUsingNumber_whenRegisterUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getMarkBohnDto();
        userToSave.setFirstName("Fif12");
        String errorMessage = "First name must contain only letters";

        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Registration user with empty first name")
    public void givenUserDtoWithEmptyFirstName_whenRegisterUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getMarkBohnDto();
        userToSave.setFirstName(null);
        String errorMessage = "First name is required";

        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Registration user with long first name")
    public void givenUserDtoWithLongFirstName_whenRegisterUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getMarkBohnDto();
        userToSave.setFirstName("H".repeat(55));
        String errorMessage = "First name must be between 1 and 50 characters";

        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Registration user with incorrect last name using number in last name")
    public void givenUserDtoWithIncorrectLastNameUsingNumber_whenRegisterUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getMarkBohnDto();
        userToSave.setLastName("Fif12");
        String errorMessage = "Last name must contain only letters";

        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Registration user with empty last name")
    public void givenUserDtoWithEmptyLastName_whenRegisterUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getMarkBohnDto();
        userToSave.setLastName(null);
        String errorMessage = "Last name is required";

        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Registration user with long last name")
    public void givenUserDtoWithLongLastName_whenRegisterUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getMarkBohnDto();
        userToSave.setLastName("H".repeat(55));
        String errorMessage = "Last name must be between 1 and 50 characters";

        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Registration user with incorrect email")
    public void givenUserWithIncorrectEmailDto_whenRegisterUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getLeoUnder18YearsDto();
        userToSave.setEmail("qew.com");
        String errorMessage = "Invalid email format";

        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Registration user with empty email")
    public void givenUserWithEmptyEmailDto_whenRegisterUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getLeoUnder18YearsDto();
        userToSave.setEmail(null);
        String errorMessage = "Email is required";

        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Registration user with a future birth date")
    public void givenUserWithIncorrectBirthDateDto_whenRegisterUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getLeoUnder18YearsDto();
        userToSave.setBirthDate(LocalDate.now().plusYears(1));
        String errorMessage = "Birth date must be in the past";

        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Registration user under 18 years")
    public void givenUserUnder18YearsDto_whenRegisterUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getLeoUnder18YearsDto();
        given(userService.save(any(UserDto.class))).willThrow(new UnderagePersonException(underagePersonMessage));

        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(underagePersonMessage)));
    }

    @Test
    @DisplayName("Getting user by id")
    public void givenId_whenGetById_thenSuccessResponse() throws Exception {
        //given
        UserDto user = UserUtil.getMarkBohnPersistedDto();
        given(userService.getById(anyLong())).willReturn(user);

        //when
        ResultActions result = mockMvc.perform(get("/users/1"));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(user.getEmail())));
    }

    @Test
    @DisplayName("Getting user by non-existent id")
    public void givenNonExistentId_whenGetById_thenErrorResponse() throws Exception {
        //given
        given(userService.getById(anyLong())).willThrow(new NotFoundException(notFoundMessage));

        //when
        ResultActions result = mockMvc.perform(get("/users/1"));

        //then
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.NOT_FOUND.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(notFoundMessage)));
    }

    @Test
    @DisplayName("Deleting user by id")
    void givenId_whenDelete_thenSuccessResponse() throws Exception {
        //given
        doNothing().when(userService).delete(anyLong());

        //when
        ResultActions result = mockMvc.perform(delete("/users/1"));

        //then
        verify(userService, times(1)).delete(anyLong());
        result.andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.content().string("User was deleted"));
    }

    @Test
    @DisplayName("Deleting user by non-existent id")
    void givenNonExistentId_whenDelete_thenErrorResponse() throws Exception {
        //given
        doThrow(new NotFoundException(notFoundMessage)).when(userService).delete(anyLong());

        //when
        ResultActions result = mockMvc.perform(delete("/users/1"));

        //then
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.NOT_FOUND.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(notFoundMessage)));
    }

    @Test
    @DisplayName("Getting all users")
    public void givenTwoUsers_whenGetAll_thenSuccessResponse() throws Exception {
        //given
        UserDto user1 = UserDto.fromEntity(UserUtil.getLisa40YearsPersisted());
        UserDto user2 = UserDto.fromEntity(UserUtil.getAlex30YearsPersisted());
        List<UserDto> users = List.of(user1, user2);
        given(userService.getAll()).willReturn(users);

        //when
        ResultActions result = mockMvc.perform(get("/users"));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", CoreMatchers.is(user1.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email", CoreMatchers.is(user2.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(users.size())));
    }

    @Test
    @DisplayName("Getting users by birth date range")
    public void givenTwoUsers_whenGetByBirthDateRange_thenSuccessResponse() throws Exception {
        //given
        UserDto user1 = UserDto.fromEntity(UserUtil.getLisa40YearsPersisted());
        UserDto user2 = UserDto.fromEntity(UserUtil.getAlex30YearsPersisted());
        List<UserDto> users = List.of(user1, user2);
        given(userService.getByBirthDateRange(any(LocalDate.class), any(LocalDate.class))).willReturn(users);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("from", "1950-01-01");
        params.add("to", "1999-01-01");

        //when
        ResultActions result = mockMvc.perform(get("/users/birthdate-range")
                .params(params));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", CoreMatchers.is(user1.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email", CoreMatchers.is(user2.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(users.size())));
    }

    @Test
    @DisplayName("Updating user by changing only the name and phone number fields.")
    public void givenUserDto_whenUpdateUser_thenSuccessResponse() throws Exception {
        //given
        UserDto userToUpdate = UserUtil.getMarkBohnToUpdateDto();
        UserDto updatedUserDto = UserDto.fromEntity(UserUtil.getUpdatedMarkBohn());
        given(userService.update(anyLong(), any(UserDto.class))).willReturn(updatedUserDto);

        //when
        ResultActions result = mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToUpdate)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updatedUserDto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updatedUserDto.getFirstName())));
    }

    @Test
    @DisplayName("Updating user with incorrect first name using number in name")
    public void givenUserDtoWithIncorrectFirstNameUsingNumber_whenUpdateUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToUpdate = UserUtil.getMarkBohnToUpdateDto();
        userToUpdate.setFirstName("Fif12");
        String errorMessage = "First name must contain only letters";

        //when
        ResultActions result = mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToUpdate)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Updating user with long first name")
    public void givenUserDtoWithLongFirstNameUsingNumber_whenUpdateUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToUpdate = UserUtil.getMarkBohnToUpdateDto();
        userToUpdate.setFirstName("H".repeat(55));
        String errorMessage = "First name must be between 1 and 50 characters";

        //when
        ResultActions result = mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToUpdate)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Updating user with incorrect last name using number in last name")
    public void givenUserDtoWithIncorrectLastNameUsingNumber_whenUpdateUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToUpdate = UserUtil.getMarkBohnToUpdateDto();
        userToUpdate.setLastName("Fif12");
        String errorMessage = "Last name must contain only letters";

        //when
        ResultActions result = mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToUpdate)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Updating user with long last name")
    public void givenUserDtoWithLongLastNameUsingNumber_whenUpdateUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToUpdate = UserUtil.getMarkBohnToUpdateDto();
        userToUpdate.setLastName("H".repeat(55));
        String errorMessage = "Last name must be between 1 and 50 characters";

        //when
        ResultActions result = mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToUpdate)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Updating user with a future birth date")
    public void givenUserWithIncorrectBirthDateDto_whenUpdateUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToUpdate = UserUtil.getMarkBohnToUpdateDto();
        userToUpdate.setBirthDate(LocalDate.now().plusYears(1));
        String errorMessage = "Birth date must be in the past";

        //when
        ResultActions result = mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToUpdate)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Updating user under 18 years")
    public void givenUserUnder18YearsDto_whenUpdateUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToUpdate = UserUtil.getMarkBohnToUpdateDto();
        userToUpdate.setBirthDate(LocalDate.now().minusYears(16));
        given(userService.update(anyLong(), any(UserDto.class))).willThrow(new UnderagePersonException(underagePersonMessage));

        //when
        ResultActions result = mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToUpdate)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(underagePersonMessage)));
    }

}
