package tech.stark.webapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Submission")
public class Submission {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;

    private String assignment_id;
    private String submission_url;
    private String submission_date;
    private String submission_updated;
    @JsonIgnore
    private String user_email;
}
