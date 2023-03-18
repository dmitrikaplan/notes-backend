package com.example.app.repository;

import com.example.app.utils.model.entities.Salt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaltRepository extends CrudRepository<Salt, Long> {
    Salt getSaltByOwner(String owner);
}
