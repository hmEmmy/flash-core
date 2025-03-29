package me.emmy.core.command;

import me.emmy.core.api.service.IService;
import me.emmy.core.command.impl.FlashCommand;
import me.emmy.core.feature.rank.command.RankCommand;
import me.emmy.core.feature.rank.command.impl.*;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 26/03/2025
 */
public class CommandService implements IService {

    public CommandService() {
        this.initialize();
    }

    public void initialize() {
        new FlashCommand();

        new RankCommand();
        new RankCreateCommand();
        new RankDeleteCommand();
        new RankInfoCommand();
        new RankListCommand();
        new RankSetColorCommand();
        new RankSetCostCommand();
        new RankSetDescriptionCommand();
        new RankSetPrefixCommand();
        new RankSetSuffixCommand();
        new RankSetWeightCommand();
    }

    @Override
    public void closure() {

    }
}
