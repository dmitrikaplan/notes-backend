package com.example.app.repository;

import com.example.app.utils.model.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsUserByLogin(String login);
    boolean existsUserByLoginOrEmailAndActivated(String login,String email, boolean activated);

    boolean existsUserByLoginAndPasswordAndEmailAndActivated(String login, String password,String email, boolean activated);

    User getUserByEmail(String email);

    User getUserByLogin(String login);

    boolean existsUserByActivationCode(String activationCode);

    User getUserByActivationCode(String code);

    @Modifying
    @Query("update User u set u.activated = :activated where u.activationCode = :activationCode")
    void updateActivatedByActivationCode(
            @Param("activated") boolean activated,
            @Param("activationCode") String activationCode
    );

}
