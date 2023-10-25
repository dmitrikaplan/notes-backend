package ru.kaplaan.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.kaplaan.domain.entity.user.User

@Transactional
@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun existsUserByUsername(username: String): Boolean

    fun existsUserByActivationCode(activationCode: String): Boolean

    fun existsUserByUsernameOrEmailAndActivated(username: String, email: String, activated: Boolean): Boolean

    fun existsUserByUsernameAndPasswordAndEmailAndActivated(
        username: String,
        password: String,
        email: String,
        activated: Boolean
    ): Boolean

    fun findByEmail(email: String): User?

    fun findByUsername(username: String): User?

    fun getUserByActivationCode(code: String): User?

    @Modifying
    @Query("update User u set u.activated = :activated where u.activationCode = :activationCode")
    fun updateActivatedByActivationCode(
        @Param("activated") activated: Boolean,
        @Param("activationCode") activationCode: String
    )

}
