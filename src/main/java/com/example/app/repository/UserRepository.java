package com.example.app.repository;

import com.example.app.utils.model.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsUserEntityByLogin(String login);

    boolean existsUserEntityByLoginAndPassword(String login, String password);

}
