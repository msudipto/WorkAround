package com.cs309.demo.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    // Find a tag by its name
    Optional<Tag> findByName(String name);

    // Check if a tag exists by its name
    boolean existsByName(String name);

    // Find all tags that contain a specific substring in their name (case-insensitive)
    List<Tag> findByNameContainingIgnoreCase(String name);
}