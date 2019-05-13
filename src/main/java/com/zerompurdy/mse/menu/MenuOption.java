package com.zerompurdy.mse.menu;

public class MenuOption {

    /**
     * Name of the menu option dislayed to the user
     */
    private String name;

    /**
     * Action that the menu option performs
     */
    private Runnable action;

    /**
     * Menu item with no action
     * @param name Text to display to the user
     */
    public MenuOption(String name) {
        this.name = name;
        this.action = () -> {
            throw new UnsupportedOperationException();
        };
    }

    /**
     * Menu item with a corresponding action
     * @param name Text to display to the user
     * @param action Action to perform if the menu item is chosen
     */
    public MenuOption(String name, Runnable action) {
        this.name = name;
        this.action = action;
    }

    public String getName() {
        return this.name;
    }

    public void run() {
        this.action.run();
    }
}
