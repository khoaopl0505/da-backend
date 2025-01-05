package net.javaguids.da09.service.imp;

import net.javaguids.da09.dto.TaskDto;
import net.javaguids.da09.model.File;
import net.javaguids.da09.model.Project;
import net.javaguids.da09.model.Task;
import net.javaguids.da09.model.User;
import net.javaguids.da09.repository.FileRepository;
import net.javaguids.da09.repository.ProjectRepository;
import net.javaguids.da09.repository.TaskRepository;
import net.javaguids.da09.repository.UserRepository;
import net.javaguids.da09.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImp implements TaskService {

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FileRepository fileRepository;

    TaskDto taskDto = new TaskDto();
    @Override
    public TaskDto getListTask() {
        List<Task> listTask = taskRepository.findAll();
        taskDto.setTaskList(listTask);
        return  taskDto;
    }

    @Override
    public TaskDto getTaskByIdUser(Long id) {
        Task groupByIdUser = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        taskDto.setTask(groupByIdUser);
        return taskDto;
    }

    @Override
    public TaskDto deleteTaskById(Long id) {
        Optional<Task> deleteTaskById = taskRepository.findById(id);
        if (deleteTaskById.isPresent()) {
            taskRepository.deleteById(id);
            taskDto.setMessage("Delete success");
        } else {
            throw new RuntimeException("Not found");
        }
        return taskDto;
    }

    @Override
    public TaskDto updateTask(Long id, TaskDto updateTask) {
        Optional<Task> updateOptional = taskRepository.findById(id);
        if (updateOptional.isPresent()) {
            Task task = updateOptional.get();
            task.setTaskName(updateTask.getTaskName());
            task.setCompletionSchedule(updateTask.getCompletionSchedule());
            task.setDescription(updateTask.getDescription());
            task.setUpdDate(LocalDateTime.now());
            if (updateTask.getIdUser() != null) {
                User user = userRepository.findById(updateTask.getIdUser())
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + updateTask.getIdUser()));
                task.setUser(user);
            }


            if (updateTask.getIdProject() != null) {
                Project project = projectRepository.findById(updateTask.getIdProject())
                        .orElseThrow(() -> new RuntimeException("Project not found with id: " + updateTask.getIdProject()));
                task.setProject(project);
            }


            if (updateTask.getIdFile() != null) {
                File file = fileRepository.findById(updateTask.getIdFile())
                        .orElseThrow(() -> new RuntimeException("File not found with id: " + updateTask.getIdFile()));
                task.setFile(file);
            }
            Task saveTask = taskRepository.save(task);
            taskDto.setTask(saveTask);
            taskDto.setMessage("Update success");
        } else {
            throw new RuntimeException("Not found");
        }
        return taskDto;
    }

    @Override
    public TaskDto createTask(TaskDto createTask) {
        TaskDto taskDto = new TaskDto();
        if (taskRepository.existsTaskByTaskName(createTask.getTaskName())){
            taskDto.setMessage("The name task already exists");
            return taskDto;
        }else {
            Task task = new Task();
            task.setTaskName(createTask.getTaskName());
            task.setCompletionSchedule(createTask.getCompletionSchedule());
            task.setDescription(createTask.getDescription());
            task.setDeadline(createTask.getDeadline());
            task.setInsDate(LocalDateTime.now());
            if (createTask.getIdUser() != null) {
                User user = userRepository.findById(createTask.getIdUser())
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + createTask.getIdUser()));
                task.setUser(user);
            }

            // Lấy Project từ idProject
            if (createTask.getIdProject() != null) {
                Project project = projectRepository.findById(createTask.getIdProject())
                        .orElseThrow(() -> new RuntimeException("Project not found with id: " + createTask.getIdProject()));
                task.setProject(project);
            }

            // Lấy File từ idFile
            if (createTask.getIdFile() != null) {
                File file = fileRepository.findById(createTask.getIdFile())
                        .orElseThrow(() -> new RuntimeException("File not found with id: " + createTask.getIdFile()));
                task.setFile(file);
            }
            Task saveTask = taskRepository.save(task);
            if(saveTask.getId() != null){
                taskDto.setMessage("Create success");
                taskDto.setTask(saveTask);
            }
        }
        return taskDto;
    }

    @Override
    public TaskDto getListTaskByIdUser(Long id) {
        TaskDto taskDto = new TaskDto();
        List<Task> tasks = taskRepository.findTasksByUserId(id);
        taskDto.setIdUser(id);
        taskDto.setTaskList(tasks);
        taskDto.setMessage("Tasks retrieved successfully.");
        return taskDto;
    }
}
