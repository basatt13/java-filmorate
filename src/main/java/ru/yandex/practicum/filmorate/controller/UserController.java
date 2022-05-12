package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.filmStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@Slf4j
@Getter
@RequestMapping
public class UserController {
    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> allUsers() {
        return inMemoryUserStorage.allUsers();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long id) {
        if (id <= 0) {
            throw new NotFoundIdException(id + " не может быть отрицательным числом");
        }
        return inMemoryUserStorage.getUsers().get(id);
    }

    @GetMapping("/users/{id}/friends")
    public Set<User> getFriendsByUser(@PathVariable("id") Long id) {
        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            throw new NotFoundIdException("Некорректные данные: " + id);
        }
        return inMemoryUserStorage.getUsers().get(id).getFriends();
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {

        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            throw new NotFoundIdException("Некорректные данные: " + id);
        }

        if (!inMemoryUserStorage.getUsers().containsKey(otherId)) {
            throw new NotFoundIdException("Некорректные данные: " + otherId);
        }

        return userService.listOfMutualFriends(id, otherId);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {

        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            throw new NotFoundIdException("Некорректные данные: " + id);
        }

        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            throw new NotFoundIdException("Некорректные данные: " + friendId);
        }

        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {

        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            throw new NotFoundIdException("Некорректные данные: " + friendId);
        }

        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            throw new NotFoundIdException("Некорректные данные: " + friendId);
        }

        userService.removeFromFriends(id, friendId);
    }
}
