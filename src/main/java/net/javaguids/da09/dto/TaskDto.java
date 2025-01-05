package net.javaguids.da09.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.javaguids.da09.model.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDto {

    Long id;
    String taskName;
    Long idUser;
    Long idProject;
    Long idFile;
    private Task task;
    String description;
    String completionSchedule;
    List<Task> taskList;
    String message;
    LocalDateTime insDate;
    LocalDateTime updDate;
    LocalDateTime deadline;
}