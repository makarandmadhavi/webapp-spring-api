package tech.stark.webapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Assignment")
public class Assignment {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;
    @Nonnull
    private String name;
    @Nonnull
    private Integer points;
    @Nonnull
    private Integer num_of_attempts;
    @Nonnull
    private String deadline;
    private String assignment_created;
    private String assignment_updated;
    @JsonIgnore
    private String user_email;
}
