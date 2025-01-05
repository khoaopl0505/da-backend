package net.javaguids.da09.controller;

import net.javaguids.da09.dto.ProjectDto;
import net.javaguids.da09.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @PostMapping("/admin/create-project")
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto createProject){
        return ResponseEntity.ok(projectService.createProject(createProject));
    }

    @GetMapping("/admin/list-project")
    public ResponseEntity<ProjectDto> getListProject(){
        return ResponseEntity.ok(projectService.getListProject());
    }

    @GetMapping("/admin/get-project-by-id")
    public  ResponseEntity<ProjectDto> getProjectById(@RequestParam Long id){
        return  ResponseEntity.ok(projectService.getProjectByIdUser(id));
    }

    @DeleteMapping("/admin/delete-project")
    public  ResponseEntity<ProjectDto> deleteProject(@RequestParam Long id){
        return  ResponseEntity.ok(projectService.deleteProjectById(id));
    }

    @PutMapping("/admin/update-project")
    public ResponseEntity<ProjectDto> updateProject(@RequestParam Long id, @RequestBody ProjectDto updateRequest){
        return ResponseEntity.ok(projectService.updateProject(id, updateRequest));
    }
}
