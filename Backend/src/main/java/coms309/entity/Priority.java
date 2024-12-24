package coms309.entity;
import jakarta.persistence.*;

@Table(name= "Priority")
public enum Priority {
    HIGH,
    MEDIUM,
    LOW
}