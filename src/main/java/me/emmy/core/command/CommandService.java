package me.emmy.core.command;

import me.emmy.core.api.service.IService;
import me.emmy.core.command.impl.FlashCommand;

/**
 * @author Emmy
 * @project Flash
 * @since 26/03/2025
 */
public class CommandService implements IService {

    public CommandService() {
        this.initialize();
    }

    public void initialize() {
        new FlashCommand();
    }

    @Override
    public void save() {

    }
}
