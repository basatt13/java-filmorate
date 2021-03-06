package ru.yandex.practicum.filmorate;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestValidation {
    UserController userController = new UserController();
    FilmController filmController = new FilmController();

    Film film = Film.builder()
            .name("'От заката до рассвета'")
            .description("описание")
            .releaseDate("1991-08-13")
            .duration(90)
            .build();

    User user = User.builder()
            .birthday("1991-08-13")
            .login("Bas")
            .name("Denis")
            .email("denja25@yandex.ru")
            .build();

    // тестирую функционал по добавлению данных на граничных условия, тк структура методов по обновлению данных идентична
    @Test
    void shouldReturnExceptionIfLoginIsEmptyOrNull() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            user.setLogin("");
            userController.addUser(user);
        });

        Assertions.assertEquals("Ошибка данных запроса", thrown.getMessage());
    }

    @Test
    void shouldReturnExceptionIfNameContainsWhitespace() {
        user.setName("Den   i s");
        userController.addUser(user);
        Assertions.assertFalse(userController.getUsers().isEmpty());
    }

    @Test
    void shouldReturnExceptionIfNicknameIsEmpty() {
        user.setName("");
        userController.addUser(user);
        Assertions.assertEquals("Bas", user.getLogin());
        Assertions.assertEquals(1, userController.getUsers().size());
    }

    @Test
    void shouldReturnExceptionIfMailNotContainAt() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            user.setEmail("denis.ru");
            userController.addUser(user);
        });
        Assertions.assertEquals("Ошибка данных запроса", thrown.getMessage());
    }

    @Test
    void shouldReturnExceptionIfMailIsEmpty() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            user.setEmail("");
            userController.addUser(user);
        });
        Assertions.assertEquals("Ошибка данных запроса", thrown.getMessage());

    }

    @Test
    void shouldReturnExceptionIfDateOfBornIsNow() {
        user.setBirthday("2022-04-20");
        userController.addUser(user);
        Assertions.assertEquals("2022-04-20", user.getBirthday());
    }

    @Test
    void shouldReturnExceptionIfDateOfBornAfterNow() {
        user.setBirthday(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });
        Assertions.assertEquals("Ошибка данных запроса", thrown.getMessage());
    }

    @Test
    void shouldCreateUser() {
        userController.addUser(user);
        Assertions.assertEquals(userController.getUsers().get(user.getIdU()), user);
    }

    @Test
    void shouldUpdateUser() {
        userController.addUser(user);
        user.setLogin("Boo");
        userController.updateUser(user);
        Assertions.assertEquals(userController.getUsers().get(user.getIdU()), user);
    }

    @Test
    void shouldReturnExceptionIfFilmNameIsEmpty() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            film.setName("");
            filmController.addFilm(film);
        });
        Assertions.assertEquals("Ошибка данных запроса", thrown.getMessage());
    }

    @Test
    void shouldReturnExceptionIfDescriptionLenghtIs200() {
        film.setDescription(RandomString.make(200));
        filmController.addFilm(film);
        Assertions.assertFalse(filmController.getFilms().isEmpty());
    }

    @Test
    void shouldReturnExceptionIfDescriptionLengthIs201() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            film.setDescription(RandomString.make(201));
            filmController.addFilm(film);
        });
        Assertions.assertEquals("Ошибка данных запроса", thrown.getMessage());
    }

    @Test
    void shouldReturnExceptionIfDateIs28121895() {
        film.setReleaseDate("1895-12-28");
        filmController.addFilm(film);
        Assertions.assertFalse(filmController.getFilms().isEmpty());
    }

    @Test
    void shouldReturnExceptionIfDateIsBefore27121895() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            film.setReleaseDate("1895-12-27");
            filmController.addFilm(film);
        });

        Assertions.assertEquals("Ошибка данных запроса", thrown.getMessage());
    }

    @Test
    void shouldReturnExceptionIfDurationISZero() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            film.setDuration(0);
            filmController.addFilm(film);
        });
        Assertions.assertEquals("Ошибка данных запроса", thrown.getMessage());
    }

    @Test
    void shouldReturnExceptionIfDurationIsMinus1() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            film.setDuration(-1);
            filmController.addFilm(film);
        });
        Assertions.assertEquals("Ошибка данных запроса", thrown.getMessage());
    }
}
