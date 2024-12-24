package com.cs309.websocket3.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Find all messages sent by a specific user.
     * @param userName the username to filter messages by
     * @return a list of messages sent by the given user
     **/

    List<Message> findByUserName(String userName);

    /**
     * Find the most recent messages, limited by a given count.
     * @param limit the number of recent messages to retrieve
     * @return a list of recent messages, limited by the specified count
     **/

    @Query(value = "SELECT m FROM Message m ORDER BY m.sent DESC")
    List<Message> findRecentMessages(@Param("limit") int limit);

    /**
     * Find a message by ID.
     * @param id the ID of the message
     * @return an optional containing the found message, or empty if not found
     **/

    Optional<Message> findById(Long id);
}