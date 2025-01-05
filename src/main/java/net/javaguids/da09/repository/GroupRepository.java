package net.javaguids.da09.repository;

import net.javaguids.da09.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GroupRepository extends JpaRepository<UserGroup, Long> {
    boolean existsGroupByGroupName(String groupName);
}
