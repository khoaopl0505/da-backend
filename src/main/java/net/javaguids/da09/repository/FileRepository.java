package net.javaguids.da09.repository;

import net.javaguids.da09.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    File findByFileName(String fileName);
}
