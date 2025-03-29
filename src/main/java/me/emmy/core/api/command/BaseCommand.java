package me.emmy.core.api.command;

import me.emmy.core.Flash;

public abstract class BaseCommand {
    public Flash flash;

    /**
     * Constructor for the BaseCommand class.
     */
    public BaseCommand() {
        this.flash = Flash.getInstance();
        this.flash.getCommandFramework().registerCommands(this);
    }

    /**
     * Method to be called when a command is executed.
     *
     * @param command The command.
     */
    public abstract void onCommand(CommandArgs command);
}