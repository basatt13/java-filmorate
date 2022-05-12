package ru.yandex.practicum.filmorate.filmStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    public Map<Long, User> getUsers() {
        return users;
    }

    public List<User> allUsers() {
        return new ArrayList<>(users.values());
    }


    public User addUser(User user) {
        validateUser(user);
        user.setId(users.size() + 1);
        users.put(user.getId(), user);
        log.info("Пользователь с именем " + user.getName() + " добавлен");
        return user;
    }


    public User updateUser(User user) {
        validateUser(user);
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с ID " + user.getId() + " не найден");
        } else {
            users.put(user.getId(), user);
            log.info("Пользователь с именем " + user.getName() + " добавлен");
        }
        return user;
    }


    void validateUser(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationUserException("Ошибка данных запроса");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым или содержать пробелы");
            throw new ValidationUserException("Ошибка данных запроса");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isEmpty()) {
            log.error("Не указана дата рождения");
            throw new ValidationUserException("Ошибка данных запроса");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(user.getBirthday(), formatter);
        if (date.isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationUserException("Ошибка данных запроса");
        }
    }

}
