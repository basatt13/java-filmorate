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
    void shouldReturnExceptionIfLoginIsEmptyOrNull () {
        user.setLogin("");
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(userController.getUsers().isEmpty());
    }

    @Test
    void shouldReturnExceptionIfNameContainsWhitespace() {
        user.setName("Den   i s");
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(userController.getUsers().isEmpty());
    }

    @Test
    void shouldReturnExceptionIfNicknameIsEmpty(){
        user.setName("");
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals("Bas",user.getLogin());
        Assertions.assertEquals(1,userController.getUsers().size());
    }

    @Test
    void shouldReturnExceptionIfMailNotContainAt () {
        user.setEmail("denis.ru");
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(userController.getUsers().isEmpty());
    }

    @Test
    void shouldReturnExceptionIfMailIsEmpty () {
        user.setEmail("");
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(userController.getUsers().isEmpty());
    }

    @Test
    void shouldReturnExceptionIfDateOfBornIsNow () {
        user.setBirthday("2022-04-20");
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals("2022-04-20",user.getBirthday());
    }

    @Test
    void shouldReturnExceptionIfDateOfBornAfterNow () {
        user.setBirthday(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(userController.getUsers().isEmpty());
    }

    @Test
    void shouldCreateUser() {
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(userController.getUsers().get(user.getIdU()),user);
    }

    @Test
    void shouldUpdateUser() throws ValidationException{
        userController.addUser(user);
        user.setLogin("Boo");
        userController.updateUser(user);
        Assertions.assertEquals(userController.getUsers().get(user.getIdU()),user);
    }

    @Test
    void shouldReturnExceptionIfFilmNameIsEmpty()  {
        film.setName("");
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(filmController.getFilms().isEmpty());
    }

    @Test
    void shouldReturnExceptionIfDescriptionLenghtIs200() {
        film.setDescription(RandomString.make(200));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(filmController.getFilms().isEmpty());
    }

    @Test
    void shouldReturnExceptionIfDescriptionLengthIs201() {
        film.setDescription(RandomString.make(201));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(filmController.getFilms().isEmpty());
    }

    @Test
    void shouldReturnExceptionIfDateIs28121895()  {
        film.setReleaseDate("1895-12-28");
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(filmController.getFilms().isEmpty());
    }

    @Test
    void shouldReturnExceptionIfDateIsBefore27121895()  {
        film.setReleaseDate("1895-12-27");
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(filmController.getFilms().isEmpty());
    }

    @Test
    void shouldReturnExceptionIfDurationISZero()  {
        film.setDuration(0);
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(filmController.getFilms().isEmpty());
    }

    @Test
    void shouldReturnExceptionIfDurationIsMinus1()  {
        film.setDuration(-1);
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(filmController.getFilms().isEmpty());
    }
}
