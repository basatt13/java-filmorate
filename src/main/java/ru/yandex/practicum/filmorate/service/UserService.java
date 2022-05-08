package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundIdException;
import ru.yandex.practicum.filmorate.filmStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addToFriends(Integer idUser1, Integer idUser2) {
        User user1 = getUserById(idUser1, inMemoryUserStorage);
        User user2 = getUserById(idUser2, inMemoryUserStorage);
        Set<User> list1;
        Set<User> list2;
        if (user1.getFriends() == null) {
            list1 = new HashSet<>();
        } else {
            list1 = user1.getFriends();
        }
        list1.add(user2);
        user1.setFriends(list1);
        if (user2.getFriends() == null) {
            list2 = new HashSet<>();
        } else {
            list2 = user1.getFriends();
        }
        user2.setFriends(list2);
    }

    public void removeFromFriends(Integer idUser1, Integer idUser2) {
        User user1 = getUserById(idUser1, inMemoryUserStorage);
        User user2 = getUserById(idUser2, inMemoryUserStorage);
        user1.getFriends().remove(user2);
        user2.getFriends().remove(user1);
    }

    public List<User> listOfMutualFriends(Integer idUser1, Integer idUser2) {
        User user1 = getUserById(idUser1, inMemoryUserStorage);
        User user2 = getUserById(idUser2, inMemoryUserStorage);
        Map<User, Integer> allFriends = new HashMap<>();
        List<User> friends = new ArrayList<>();
        friends.addAll(user1.getFriends());
        friends.addAll(user2.getFriends());
        for (User user : friends) {
            if (allFriends.containsKey(user)) {
                allFriends.put(user, allFriends.get(user) + 1);
            }
        }
        List<User> listOfFriends = new ArrayList<>();
        listOfFriends.addAll(allFriends.keySet());
        return listOfFriends;
    }

    public static User getUserById(Integer id, InMemoryUserStorage inMemoryUserStorage) {
        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            throw new NotFoundIdException("Некорректные данные" + id);
        }
        return inMemoryUserStorage.getUsers().get(id);
    }
}
