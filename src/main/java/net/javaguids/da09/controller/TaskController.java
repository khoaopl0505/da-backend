package net.javaguids.da09.controller;

import net.javaguids.da09.dto.TaskDto;
import net.javaguids.da09.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {
    @Autowired
    TaskService taskService;
    // create bug
    @PostMapping("/admin/create-task")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto createRequest){
        return ResponseEntity.ok(taskService.createTask(createRequest));
    }

    @GetMapping("/admin/list-task")
    public ResponseEntity<TaskDto> getListTask(){
        return ResponseEntity.ok(taskService.getListTask());
    }

    @GetMapping("/admin/get-task-by-id")
    public  ResponseEntity<TaskDto> getTaskById(@RequestParam Long id){
        return  ResponseEntity.ok(taskService.getTaskByIdUser(id));
    }

    @DeleteMapping("/admin/delete-task")
    public  ResponseEntity<TaskDto> deleteTask(@RequestParam Long id){
        return  ResponseEntity.ok(taskService.deleteTaskById(id));
    }

    @PutMapping("/admin/update-task")
    public ResponseEntity<TaskDto> updateTask(@RequestParam Long id, @RequestBody TaskDto updateRequest){
        return ResponseEntity.ok(taskService.updateTask(id, updateRequest));
    }
    @GetMapping("/group/get-task-by-id-user/{userId}")
    public ResponseEntity<TaskDto> getTasksByUserId(@PathVariable Long userId) {
        TaskDto taskDto = taskService.getListTaskByIdUser(userId);
        return ResponseEntity.ok(taskDto);
    }
}
