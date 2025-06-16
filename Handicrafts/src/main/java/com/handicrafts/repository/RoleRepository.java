package com.handicrafts.repository;

import com.handicrafts.entity.RoleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<RoleEntity> findByName(String name) {
        try {
            RoleEntity role = entityManager.createQuery(
                            "SELECT r FROM RoleEntity r WHERE r.name = :name", RoleEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(role);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public RoleEntity save(RoleEntity role) {
        if (role.getRoleID() == null) {
            entityManager.persist(role);
            return role;
        } else {
            return entityManager.merge(role);
        }
    }

    public Optional<RoleEntity> findById(Integer id) {
        RoleEntity role = entityManager.find(RoleEntity.class, id);
        return Optional.ofNullable(role);
    }
}
