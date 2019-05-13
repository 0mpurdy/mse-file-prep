package com.zerompurdy.mse.menu;

import com.zerompurdy.mse.data.author.Author;

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
