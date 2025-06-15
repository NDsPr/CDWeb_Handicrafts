package com.handicrafts.repository;

import com.handicrafts.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Integer> {
    Optional<RoleEntity> findByName(String name);

    @Query(value = "select r.name from role r join roleuser ru on r.roleID=ru.roleID where ru.userID=?1", nativeQuery = true)
    List<String> findAllByUserID(int id);
}
