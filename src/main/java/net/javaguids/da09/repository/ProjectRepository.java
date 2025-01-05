package net.javaguids.da09.repository;

import net.javaguids.da09.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsProjectByProjectName(String projectName);
}
