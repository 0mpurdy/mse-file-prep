package com.zerompurdy.mse.menu;

import com.zerompurdy.mse.olddata.PreparePlatform;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SystemsMenuFactory {
    public static Menu make() {
        List<MenuOption> options = Arrays.stream(PreparePlatform.values())
                .map(PreparePlatform::getName)
                .map(MenuOption::new)
                .collect(Collectors.toList());

        options.add(0, new MenuOption("Cancel"));

        return new Menu(options);
    }
}
