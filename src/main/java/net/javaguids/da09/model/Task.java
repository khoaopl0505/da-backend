package net.javaguids.da09.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "task")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "task_name")
    String taskName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_project")
    @JsonIgnore
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_file")
    @JsonIgnore
    private File file;

    @Column(name = "description")
    String description;

    @Column(name = "completion_schedule")
    String completionSchedule;

    @Column(name = "ins_date")
    LocalDateTime insDate;

    @Column(name = "upd_date")
    LocalDateTime updDate;

    @Column(name = "deadline")
    LocalDateTime deadline;
}
