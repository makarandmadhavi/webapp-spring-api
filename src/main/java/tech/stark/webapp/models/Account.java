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
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;
    private String first_name;
    private String last_name;
    @Column(unique=true)
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String account_created;
    @JsonIgnore
    private String account_updated;
}
