package com.example.app.repository;

import com.example.app.utils.model.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    boolean existsUserEntityByLogin(String login);

    boolean existsUserEntityByLoginAndPassword(String login, String password);

}
