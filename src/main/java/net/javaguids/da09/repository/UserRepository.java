package net.javaguids.da09.repository;

import net.javaguids.da09.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User findByTokenMail(String tokenMail);
    Optional<User> findByName(String name);

    @Query(value = "SELECT u.* FROM user u JOIN user_group ug ON u.id_user_group = ug.id WHERE ug.id = :groupId", nativeQuery = true)
    List<User> findUsersByGroupId(@Param("groupId") Long groupId);

    @Query(value = "SELECT COUNT(id) FROM user u WHERE id_user_group = :groupId", nativeQuery = true)
    Long countUsersByGroupId(@Param("groupId") Long groupId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET id_user_group = NULL WHERE id_user_group = :groupId", nativeQuery = true)
    void updateUserGroupToNull(@Param("groupId") Long groupId);

}
