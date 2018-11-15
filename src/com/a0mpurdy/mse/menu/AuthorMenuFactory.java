package com.a0mpurdy.mse.menu;

import com.a0mpurdy.mse.data.author.Author;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorMenuFactory {
    public static Menu make() {
        List<MenuOption> options = Arrays.stream(Author.values())
                        .map(Author::getName)
                        .map(MenuOption::new)
                        .collect(Collectors.toList());
        return new Menu(options);
    }
}
