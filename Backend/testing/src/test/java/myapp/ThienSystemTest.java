package myapp;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {coms309.Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)


public class ThienSystemTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void getAllUserProfilesTest() {
        // Send GET request to retrieve all user profiles
        Response response = RestAssured.given()
                .when()
                .get("/api/userprofile/all");

        // Check status code
        assertEquals(200, response.getStatusCode(), "Expected HTTP status 200");

        // Ensure response body is not empty
        assertNotNull(response.getBody().asString(), "Response body should not be null or empty");

        // Optionally check content type
        assertEquals("application/json", response.getContentType(), "Expected content type to be application/json");
    }


    @Test
    public void getUserProfileByIdTest() {
        // Assuming a user with ID 1 exists
        long userId = 9;

        // Send GET request to retrieve user profile by ID
        Response response = RestAssured.given()
                .when()
                .get("/api/userprofile/{id}", userId);

        // Check status code
        assertEquals(200, response.getStatusCode(), "Expected HTTP status 200");

        // Ensure response body is not empty
        assertNotNull(response.getBody().asString(), "Response body should not be null or empty");

        // Optionally check if the profile contains user ID in response
        assertEquals(userId, response.jsonPath().getLong("userId"), "User ID should match");
    }
    @Test
    public void loginTest() {
        // Prepare login credentials including email
        String username = "johndoe";
        String password = "password123?";
        String email = "john.doe@example.com";

        // Send GET request to login with query parameters
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam("username", username)
                .queryParam("password", password)
                .queryParam("email", email)
                .when()
                .get("http://localhost:8080/login");

        // Check for successful login
        assertEquals(200, response.getStatusCode(), "Expected HTTP status 200 (OK)");

        // Optionally check response message for confirmation
        String expectedMessage = "Login successful";  // Adjust as needed based on your actual response
        assertEquals(expectedMessage, response.getBody().asString(), "Login message should match");
    }


    @Test
    public void loginInvalidCredentialsTest() {
        // Prepare invalid login credentials including email
        String username = "johndoe";
        String password = "wrongpassword";
        String email = "johndoe@example.com";

        // Send GET request with invalid credentials as query parameters
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam("username", username)
                .queryParam("password", password)
                .queryParam("email", email)
                .when()
                .get("http://localhost:8080/login");  // Correct URL

        // Check for failed login (401 Unauthorized for invalid credentials)
        assertEquals(401, response.getStatusCode(), "Expected HTTP status 401 (Unauthorized) for invalid credentials");

        // Optionally check response message for error
        String expectedErrorMessage = "Login failed. Invalid credentials.";  // Adjust based on the actual response message
        assertEquals(expectedErrorMessage, response.getBody().asString(), "Error message should match for invalid credentials");
    }



//    @Test
//    public void getAllProjectsTest() {
//        // Send GET request to retrieve all projects
//        Response response = RestAssured.given()
//                .when()
//                .get("/project/all");
//
//        // Check if the status code is 200 OK
//        assertEquals(200, response.getStatusCode(), "Expected HTTP status 200");
//
//        // Ensure response body is not empty (i.e., there are projects)
//        assertEquals(true, response.getBody().asString().length() > 0, "Response body should not be empty");
//    }

    @Test
    public void getAllSalariesForUserTest() {
        Long userId = 9L;  // User ID for which all salary details are being requested

        // Send GET request to the salary endpoint with the specified userId
        Response response = RestAssured.given()
                .when()
                .get("http://localhost:8080/api/salary/all/" + userId);

        // Verifying if the response status code is 200 OK
        assertEquals(200, response.getStatusCode(), "Expected HTTP status 200");

        // Verifying that the response body is not empty
        assertNotNull(response.getBody().asString(), "Response body should not be empty");

        // Verify that at least one salary record is returned
        assertTrue(response.jsonPath().getList("$").size() > 0, "The response should contain at least one salary record");

        // Verify that the response contains the expected fields for the salary record
        assertNotNull(response.jsonPath().getString("[0].salaryId"), "Salary ID should not be null");
        assertNotNull(response.jsonPath().getString("[0].username"), "Username should not be null");
        assertNotNull(response.jsonPath().getString("[0].hoursWorked"), "Hours worked should not be null");
        assertNotNull(response.jsonPath().getString("[0].payRate"), "Pay rate should not be null");
        assertNotNull(response.jsonPath().getString("[0].bonusPay"), "Bonus pay should not be null");
        assertNotNull(response.jsonPath().getString("[0].deductibles"), "Deductibles should not be null");
        assertNotNull(response.jsonPath().getString("[0].grossPay"), "Gross pay should not be null");
        assertNotNull(response.jsonPath().getString("[0].takeHomePay"), "Take home pay should not be null");
    }


    @Test
    public void getSalaryByUsernameTest() {
        String username = "johndoe";  // Replace with a valid username
        Response response = RestAssured.given()
                .when()
                .get("/api/salary/username/" + username);

        assertEquals(200, response.getStatusCode(), "Expected HTTP status 200");
        assertNotNull(response.getBody().asString(), "Response body should not be empty");
    }


}
