package net.javaguids.da09.service.imp;

import net.javaguids.da09.dto.ProjectDto;
import net.javaguids.da09.model.Project;
import net.javaguids.da09.repository.ProjectRepository;
import net.javaguids.da09.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImp implements ProjectService {
    @Autowired
    ProjectService projectService;
    @Autowired
    ProjectRepository projectRepository;

    ProjectDto  projectDto = new ProjectDto();
    @Override
    public ProjectDto getListProject() {
        List<Project> listProject = projectRepository.findAll();
        projectDto.setProjectList(listProject);
        return  projectDto;
    }

    @Override
    public ProjectDto getProjectByIdUser(Long id) {
        Project projectById = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        projectDto.setProject(projectById);
        return projectDto;
    }

    @Override
    public ProjectDto deleteProjectById(Long id) {
        Optional<Project> deleteProjectById = projectRepository.findById(id);
        if (deleteProjectById.isPresent()) {
            projectRepository.deleteById(id);
            projectDto.setMessage("Delete success");
        } else {
            projectDto.setMessage("Delete fail");
        }
        return projectDto;
    }

    @Override
    public ProjectDto updateProject(Long id, ProjectDto updateProject) {
        Optional<Project> updateOptional = projectRepository.findById(id);
        if (updateOptional.isPresent()) {
            Project project = updateOptional.get();
            project.setProjectName(updateProject.getProjectName());
            project.setDeadline(updateProject.getDeadline());
            project.setDescription(updateProject.getDescription());
            Project saveProject = projectRepository.save(project);
            projectDto.setMessage("Update success");
            projectDto.setProject(saveProject);
        } else {
            projectDto.setMessage("Update fail");
        }
        return projectDto;
    }

    @Override
    public ProjectDto createProject(ProjectDto createProject) {
        ProjectDto responseDto = new ProjectDto();
        if(projectRepository.existsProjectByProjectName(createProject.getProjectName())){
            responseDto.setMessage("The name group already exists");
            return responseDto;
        }else {
            Project project = new Project();
            project.setProjectName(createProject.getProjectName());
            project.setDeadline(createProject.getDeadline());
            project.setDescription(createProject.getDescription());
            project.setImplementationTime(LocalDateTime.now());
            Project saveProject = projectRepository.save(project);
            if(saveProject.getId() != null){
                projectDto.setMessage("Create success");
                projectDto.setProject(saveProject);
            }
        }
        return projectDto;
    }
}
