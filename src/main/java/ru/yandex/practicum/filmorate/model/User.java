package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private String email;
    private String login;
    private String birthday;
    private String name;

    @Builder.Default
    private long id = 0;
    @Builder.Default
    private Set<User> friends = new HashSet<>();

}
