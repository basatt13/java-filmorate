package ru.yandex.practicum.filmorate.exception;

public class NotFoundIdException extends RuntimeException {

    private String parametr;

    public NotFoundIdException(String parametr) {
        this.parametr = parametr;
    }

    public String getParametr() {
        return parametr;
    }
}
