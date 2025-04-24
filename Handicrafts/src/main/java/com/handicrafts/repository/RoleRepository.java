package com.handicrafts.repository;

import com.handicrafts.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity,Integer> {
    public RoleEntity findByName(String name);

    @Query(value = "select r.name from role r join roleuser ru on r.roleID=ru.roleID where re.userID=?1",nativeQuery = true)
    public List<String> findAllUserID(int id);
}
