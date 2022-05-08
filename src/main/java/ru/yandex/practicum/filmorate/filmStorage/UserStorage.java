package ru.yandex.practicum.filmorate.filmStorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> allUsers();

    User addUser(User user);

    User updateUser(User user);
}
