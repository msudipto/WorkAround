
package coms309.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Table(name = "user_type")
public enum UserType {
    EMPLOYEE,
    EMPLOYER,
    ADMIN
}
