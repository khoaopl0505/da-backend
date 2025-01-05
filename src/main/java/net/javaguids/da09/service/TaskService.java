package net.javaguids.da09.service;


import net.javaguids.da09.dto.TaskDto;

public interface TaskService {
    TaskDto getListTask();
    TaskDto getTaskByIdUser(Long id);
    TaskDto deleteTaskById(Long id);
    TaskDto updateTask(Long id,TaskDto updateTask);
    TaskDto createTask(TaskDto createTask);
    TaskDto getListTaskByIdUser(Long id);
}
