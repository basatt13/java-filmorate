package ru.yandex.practicum.filmorate.exception;

public class NotFoundIdException extends RuntimeException {

    public NotFoundIdException(String message) {
        super(message);
    }
}
