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
@RequestMapping(value = "/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> allUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        validateUser(user);
            user.setIdU(users.size() + 1);
            users.put(user.getIdU(), user);
            log.info("Пользователь с именем " + user.getName() + " добавлен");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validateUser(user);
        if (!users.containsKey(user.getIdU())) {
            log.error("Пользователь с ID " + user.getIdU() + " не найден");
        } else {
            users.put(user.getIdU(), user);
            log.info("Пользователь с именем " + user.getName() + " добавлен");
        }
        return user;
    }


    void validateUser(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Ошибка данных запроса");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым или содержать пробелы");
            throw new ValidationException("Ошибка данных запроса");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isEmpty()) {
            log.error("Не указана дата рождения");
            throw new ValidationException("Ошибка данных запроса");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(user.getBirthday(), formatter);
        if (date.isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Ошибка данных запроса");
        }
    }

}
