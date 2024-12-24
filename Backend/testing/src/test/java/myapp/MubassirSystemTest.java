
package myapp;

import coms309.Application;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;	// SBv3
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class MubassirSystemTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // LoginController Endpoints
    @Test
    public void testLogin() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // Updated to match query parameter handling

        HttpEntity<String> request = new HttpEntity<>(null, headers); // No body needed for GET requests

        ResponseEntity<String> response = restTemplate.exchange(
                "/login?username=janedoe&password=EncodedPassword123!",
                HttpMethod.GET,
                request,
                String.class
        );

        // Validate status code
        assertEquals(200, response.getStatusCodeValue());

        // Validate response body
        assertNotNull(response.getBody());
        assertEquals("Login successful", response.getBody()); // Match the response message
    }

    @Test
    public void testSignup() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Updated JSON body based on the screenshot
        String body = "{\"full_name\":\"Emma Watson\", \"email\":\"emma.watson@example.com\", \"username\":\"ewatson\", \"password\":\"P@ssw0rd123\"}";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // Sending POST request to the signup endpoint
        ResponseEntity<String> response = restTemplate.postForEntity("/login/signup", request, String.class);

        // Assertions to verify the response
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testForgotPassword() {
        // Arrange
        String email = "janedoe@example.com";
        String url = "/login/forgotPassword?email=" + email;

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("User exists.", response.getBody()); // Validate expected response content
    }

    // TaskController Endpoints
    @Test
    public void testCreateTask() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = "{"
                + "\"name\":\"Mission Impossible: Bug Fix Sprint\","
                + "\"description\":\"Eliminate critical bugs\","
                + "\"status\":\"Assigned\","
                + "\"progress\":0,"
                + "\"projectId\":103,"
                + "\"employeeAssignedTo\":\"ethanhunt\","
                + "\"employerAssignedTo\":\"directork\""
                + "}";
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange("/tasks/create", HttpMethod.POST, request, String.class);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetTasksByStatus() {
        ResponseEntity<String> response = restTemplate.getForEntity("/tasks/status/pending", String.class);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    // ScheduleController Endpoints
    @Test
    public void testCreateSchedule() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = "{"
                + "\"eventType\":\"Team Meeting\","
                + "\"startTime\":\"2024-12-10T10:00:00.000Z\","
                + "\"endTime\":\"2024-12-10T12:00:00.000Z\","
                + "\"userId\":101,"
                + "\"projectId\":101,"
                + "\"employeeAssignedTo\":\"Michael Brown\","
                + "\"employerAssignedTo\":\"Sarah Smith\""
                + "}";
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/schedules/create", request, String.class);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetUserSchedules() {
        ResponseEntity<String> response = restTemplate.getForEntity("/schedules/user/5", String.class);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    // TimeWorkedController Endpoints
    @Test
    public void testGetTimeWorkedById() {
        ResponseEntity<String> response = restTemplate.getForEntity("/timeWorked/9", String.class);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}
