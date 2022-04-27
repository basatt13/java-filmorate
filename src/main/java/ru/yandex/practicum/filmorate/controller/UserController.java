package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Slf4j
@Getter
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> allUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User addUser(@RequestBody User user){
            if (!validateAddUser(user)) {
                throw new ValidationException("Проверьте данные запроса");
            } else {
                user.setIdU(users.size()+1);
                users.put(user.getIdU(), user);
                log.info("Пользователь с именем " + user.getName() + " добавлен");
            }
            return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user)  {
        try {
            if (!validateUpdateUser(user)) {
                throw new ValidationException("Проверьте данные запроса");
            } else {
                users.put(user.getIdU(), user);
                log.info("Пользователь с именем " + user.getName() + " добавлен");
            }
        }
        catch (ValidationException exception){
            exception.getMessage();
        }
        return user;
    }

    boolean validateAddUser(User user){
        boolean check = true;
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            System.out.println("Электронная почта не может быть пустой и должна содержать символ @");
            check = false;
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            System.out.println("Логин не может быть пустым или содержать пробелы");
            check = false;
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isEmpty()) {
            System.out.println("Не указана дата рождения");
            check = false;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(user.getBirthday(), formatter);
        if (date.isAfter(LocalDate.now())) {
            System.out.println("Дата рождения не может быть в будущем");
            check = false;
        }
        return check;
    }

    boolean validateUpdateUser(User user){
        boolean check = true;
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            System.out.println("Электронная почта не может быть пустой и должна содержать символ @");
            check = false;
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            System.out.println("Логин не может быть пустым или содержать пробелы");
            check = false;
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isEmpty()) {
            System.out.println("Не указана дата рождения");
            check = false;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(user.getBirthday(), formatter);
        if (date.isAfter(LocalDate.now())) {
            System.out.println("Дата рождения не может быть в будущем");
            check = false;
        }

        if (!users.containsKey(user.getIdU())) {
            System.out.println("Пользователь с ID " + user.getIdU() + " не найден");
            check = false;
        }
        return check;
    }


}
