package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DemoApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ImageRepository imageRepository;

	private Long testImageId;

	@BeforeEach
	void setUp() throws Exception {
		// Prepare a test image file
		Path tempFile = Files.createTempFile("test-image", ".jpg");
		File testImageFile = tempFile.toFile();
		testImageFile.deleteOnExit(); // Delete the file after the test completes
		Files.write(tempFile, "test content".getBytes()); // Add some content to the file

		// Save the image in the repository
		Image image = new Image(testImageFile.getAbsolutePath());
		imageRepository.save(image);
		testImageId = image.getId();

		// Verify image insertion
		Optional<Image> savedImage = imageRepository.findById(testImageId);
		assertThat(savedImage).isPresent().withFailMessage("Image was not saved in the repository");
	}

	@Test
	void contextLoads() {
		// This test will pass if the context loads successfully
	}

	@Test
	void testGetImageById() {
		ResponseEntity<byte[]> response = restTemplate.getForEntity("/images/" + testImageId, byte[].class);

		// Assert that the status is 200 OK
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		// Assert that the response body is not null
		assertThat(response.getBody()).isNotNull().withFailMessage("Response body is null, expected image bytes");
	}
}