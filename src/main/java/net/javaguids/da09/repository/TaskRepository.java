package net.javaguids.da09.repository;

import net.javaguids.da09.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsTaskByTaskName(String taskName);
    @Query(value ="SELECT t.* FROM task t WHERE t.id_user = :idUser", nativeQuery = true)
    List<Task> findTasksByUserId(@Param("idUser") Long idUser);
}
