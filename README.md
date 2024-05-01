# User RESTful API

A RESTful API for managing the user entities.  Code is covered by unit tests using Spring.

## Requirements
1. **Fields:**
    - Email (required): Validates against email pattern;
    - First name (required);
    - Last name (required);
    - Birth date (required): Value must be earlier than current date;
    - Address (optional);
    - Phone number (optional).

2. **Functionality:**
    - Create user: Registers users who are more than 18 years old;
    - Get user;
    - Update user fields;
    - Delete user;
    - Search for users by birth date range.

3. **Unit Tests:** Code is covered by unit tests using Spring.

4. **Error Handling:** Code has error handling for REST.

5. **JSON Responses:** API responses are in JSON format.

6. **Data Persistence:** Use of database is not necessary.

## Setup Instructions
1. Clone this repository to your local machine.
2. Open the project in your preferred IDE.
3. Run the application using Maven or your IDE's run configuration.

## API Endpoints
- **GET /users:** Retrieve a list of users.
- **GET /users/{id}:** Retrieve a user by ID.
- **GET /users/birthdate-range?from=1990-12-21&to=2000-12-21:** Search for users by birth date range. Use two parameters(from, to)
- **POST /users:** Create a new user.
- **PATCH /users/{id}:** Update specific fields of a user by ID.
- **DELETE /users/{id}:** Delete a user by ID.


## Example JSON Request to create user
```json
{
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "birthDate": "2003-04-27",
  "address": "Main Street 1",
  "phoneNumber": "+1234567890"
}
```

## Technologies Used
- Java
- Spring Boot
- Spring Web
- Spring Boot Starter Validation
- JUnit
- Mockito
- Lombok
- Maven


Thank you!
