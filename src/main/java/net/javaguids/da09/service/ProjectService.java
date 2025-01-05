package net.javaguids.da09.service;

import net.javaguids.da09.dto.ProjectDto;

public interface ProjectService {
    ProjectDto getListProject();
    ProjectDto getProjectByIdUser(Long id);
    ProjectDto deleteProjectById(Long id);
    ProjectDto updateProject(Long id,ProjectDto updateProject);
    ProjectDto createProject(ProjectDto createProject);
}
