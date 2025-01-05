package net.javaguids.da09.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "project")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "project_name")
    String projectName;

    @Column(name = "implementation_time")
    LocalDateTime implementationTime;

    @Column(name = "deadline")
    LocalDateTime deadline;

    @Column(name = "description")
    String description;
    //them khoa phu id_group
}
