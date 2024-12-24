package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.File;

/**
 * Entity class representing an Image.
 **/

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "File path cannot be null.")
    @NotEmpty(message = "File path cannot be empty.")
    private String filePath;

    /**
     * Default constructor for Image.
     **/

    public Image() {}

    /**
     * Parameterized constructor for Image.
     * @param filePath The file path of the image.
     **/

    public Image(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the ID of the image.
     * @return The ID of the image.
     **/

    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the image.
     * @param id The ID to set.
     **/

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the file path of the image.
     * @return The file path of the image.
     **/

    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the file path of the image.
     * @param filePath The file path to set.
     **/

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Checks if the image file exists at the specified file path.
     * @return True if the file exists, false otherwise.
     **/

    public boolean isFileExists() {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        File file = new File(filePath);
        return file.exists();
    }
}