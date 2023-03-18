package com.example.app.service.impl;

import com.example.app.repository.SaltRepository;
import com.example.app.repository.UserRepository;
import com.example.app.service.AuthService;
import com.example.app.utils.exceptions.UserAlreadyRegisteredException;
import com.example.app.utils.exceptions.UserNotFoundException;
import com.example.app.utils.model.entities.Salt;
import com.example.app.utils.model.entities.UserEntity;
import com.example.app.security.Crypto;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    SaltRepository saltRepository;
    Crypto crypto;


    @Override
    public void loginUser(@NonNull UserEntity user) throws UserNotFoundException {
        String hashedPassword = getHash(user);
        if(!userRepository.existsUserEntityByLoginAndPassword(user.getLogin(), hashedPassword))
            throw new UserNotFoundException("Пользователь с данным логином и паролем не найден");
    }

    @Override
    public void registration(@NonNull UserEntity user) throws UserAlreadyRegisteredException {
        if(userRepository.existsUserEntityByLogin(user.getLogin())) throw new UserAlreadyRegisteredException();
        String hashedPassword = doHash(user);
        System.out.println("Регистрация) Пароль после хэширования: " + hashedPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    private String getHash(UserEntity user) throws UserNotFoundException {
        Salt salts = saltRepository.getSaltByOwner(user.getLogin());
        if(salts == null) throw new UserNotFoundException("Пользователь с данным логином и паролем не найден");
        System.out.println(salts.getSalt1() + " " + salts.getSalt2());
        String salt1 = salts.getSalt1();
        String salt2 = salts.getSalt2();
        String hash = crypto.md5(salt1 + user.getPassword() + salt2);
        return crypto.md5(hash);
    }


    private String doHash(UserEntity user){
        String salt1 = crypto.generateSalt();
        String salt2 = crypto.generateSalt();
        Salt salt = new Salt(salt1, salt2, user.getLogin());
        saltRepository.save(salt);
        String hash = crypto.md5(salt1 + user.getPassword() + salt2);
        return crypto.md5(hash);
    }

}
