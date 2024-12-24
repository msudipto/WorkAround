package coms309.repository;

import coms309.entity.Chat;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    /**
     * Find all messages sent by a specific user.
     * @param userName the username to filter messages by
     * @return a list of messages sent by the given user
     **/
    List<Chat> findByUserName(String userName);

    /**
     * Find the most recent messages, limited by a given count.
     * @param limit the number of recent messages to retrieve
     * @return a list of recent messages, limited by the specified count
     **/
    @Query(value = "SELECT m FROM Chat m ORDER BY m.sent DESC")
    List<Chat> findRecentMessages(@Param("limit") int limit);

    /**
     * Find a message by ID.
     * @param id the ID of the message
     * @return an optional containing the found message, or empty if not found
     **/
    @NotNull Optional<Chat> findById(@NotNull Long id);

    /**
     * Find all chat messages associated with a specific image ID.
     * @param imageId the ID of the image
     * @return a list of chat messages associated with the given image
     **/
    List<Chat> findByImage_Id(@NotNull Long imageId);
}
