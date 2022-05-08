package ru.yandex.practicum.filmorate.exception;

public class ErrorResponse {
    public String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}