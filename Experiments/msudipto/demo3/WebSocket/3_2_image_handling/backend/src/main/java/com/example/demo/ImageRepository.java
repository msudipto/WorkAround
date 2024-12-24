package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    /**
     * Finds an image by its ID.
     * @param id The ID of the image.
     * @return An Optional containing the image if found, or empty if not.
     */

    @NonNull
    Optional<Image> findById(@NonNull Long id);

    /**
     * Finds images by the file path.
     * @param filePath The file path of the image.
     * @return A list of images matching the file path.
     **/

    @NonNull
    List<Image> findByFilePath(@NonNull String filePath);

    /**
     * Custom query to find images by file path (case-insensitive).
     * @param filePath The file path to search for.
     * @return A list of matching images.
     **/

    @NonNull
    List<Image> findImagesByFilePathIgnoreCase(@NonNull String filePath);
}