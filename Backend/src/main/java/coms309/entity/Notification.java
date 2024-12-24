package coms309.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Getter
@Setter

@Entity
@Table(name= "Notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    private String type;

    private String message;

    private LocalDateTime timeStamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserProfile userProfile;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Projects project;


    public Notification(){}
    public Notification(String message, String type, UserProfile userProfile, Projects project){
        this.message = message;
        this.type = type;
        this.userProfile = userProfile;
        this.project = project;
        this.timeStamp = LocalDateTime.now();
    }

}
