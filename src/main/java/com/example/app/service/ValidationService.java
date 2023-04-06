package com.example.app.service;

import com.example.app.utils.exceptions.NotValidUserException;
import com.example.app.utils.model.entities.User;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ValidationService {



    public void validateLogin(User user) throws NotValidUserException {
        if (
                ((user.getLogin() == null || user.getLogin().length() <= 6 || user.getLogin().length() > 320) &&
                        (user.getEmail() == null || !validateEmail(user.getEmail()) || user.getEmail().length() < 6 || user.getEmail().length() > 320)) ||
                        user.getPassword() == null || user.getPassword().length() < 8
        ) throw new NotValidUserException();
    }

    public void validateRegistration(User user) throws NotValidUserException {
        if (
                user.getLogin() == null || user.getLogin().length() <= 6 || user.getLogin().length() > 320 ||
                        user.getEmail() == null || !validateEmail(user.getEmail()) ||
                        user.getPassword() == null || user.getPassword().length() < 8
        ) throw new NotValidUserException();
    }

    public boolean validateEmail(String email) {
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@" +
                        "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

}
