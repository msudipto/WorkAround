package coms309.controller;

import coms309.image.Image;
import coms309.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.core.io.InputStreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;

// OpenAPI 3 annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@EnableWebSocketMessageBroker
@RequestMapping("/images")
@Tag(name = "Image Management", description = "Operations for uploading, retrieving, and managing images")
public class ImageController {

    @Value("${image.dir}")
    private String directory;

    @Autowired
    private ImageRepository imageRepository;

    /**
     * Retrieves an image by its ID.
     *
     * @param id The ID of the image.
     * @return The image file in byte array format.
     */
    @Operation(summary = "Retrieve an image by its ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved image"),
            @ApiResponse(responseCode = "404", description = "Image not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageById(
            @Parameter(description = "ID of the image to retrieve", required = true)
            @PathVariable Long id) {
        Optional<Image> imageOpt = imageRepository.findById(id);
        if (imageOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Image image = imageOpt.get();
        File imageFile = new File(image.getFilePath());

        if (!imageFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            return ResponseEntity.ok(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Handles the upload of an image file.
     *
     * @param imageFile The uploaded file.
     * @return A success or failure message.
     */
    @Operation(summary = "Handle the upload of an image", responses = {
            @ApiResponse(responseCode = "201", description = "Successfully uploaded image"),
            @ApiResponse(responseCode = "415", description = "Unsupported file type"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<String> handleFileUpload(
            @Parameter(description = "Image file to be uploaded", required = true)
            @RequestParam("image") MultipartFile imageFile) {
        if (!isImageFile(imageFile)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("Only JPEG or PNG files are supported.");
        }

        try {
            File destinationFile = new File(directory + File.separator + imageFile.getOriginalFilename());
            imageFile.transferTo(destinationFile);

            Image image = new Image(destinationFile.getAbsolutePath());
            imageRepository.save(image);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("File uploaded successfully: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * Converts an image file path to a MultipartFile-like InputStreamResource.
     *
     * @param imagePath The path to the image file.
     * @return The image as an InputStreamResource.
     * @throws IOException if the file cannot be read.
     */
    @Operation(summary = "Convert image file path to InputStreamResource", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully converted the image file"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public InputStreamResource loadImageAsResource(String imagePath) throws IOException {
        File file = new File(imagePath);
        InputStream input = new FileInputStream(file);
        return new InputStreamResource(input);
    }

    /**
     * Checks if the uploaded file is an image.
     *
     * @param file The file to check.
     * @return True if the file is an image, false otherwise.
     */
    @Operation(summary = "Check if the uploaded file is an image", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully checked if the file is an image"),
            @ApiResponse(responseCode = "400", description = "Invalid file")
    })
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals(MediaType.IMAGE_JPEG_VALUE) ||
                contentType.equals(MediaType.IMAGE_PNG_VALUE));
    }
}
