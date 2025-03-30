package me.emmy.core.command;

import me.emmy.core.api.service.IService;
import me.emmy.core.command.impl.FlashCommand;
import me.emmy.core.feature.rank.command.RankCommand;
import me.emmy.core.feature.rank.command.impl.appearance.RankSetColorCommand;
import me.emmy.core.feature.rank.command.impl.appearance.RankSetDescriptionCommand;
import me.emmy.core.feature.rank.command.impl.appearance.RankSetPrefixCommand;
import me.emmy.core.feature.rank.command.impl.appearance.RankSetSuffixCommand;
import me.emmy.core.feature.rank.command.impl.manage.*;
import me.emmy.core.feature.rank.command.impl.type.RankSetDefaultCommand;
import me.emmy.core.feature.rank.command.impl.type.RankSetHiddenCommand;
import me.emmy.core.feature.rank.command.impl.type.RankSetPurchasableCommand;
import me.emmy.core.feature.rank.command.impl.type.RankSetStaffCommand;
import me.emmy.core.feature.rank.command.impl.inheritance.RankAddInheritanceCommand;
import me.emmy.core.feature.rank.command.impl.inheritance.RankRemoveInheritanceCommand;
import me.emmy.core.feature.rank.command.impl.permission.RankAddPermissionCommand;
import me.emmy.core.feature.rank.command.impl.permission.RankRemovePermissionCommand;
import me.emmy.core.feature.tag.command.TagCommand;
import me.emmy.core.feature.world.command.WorldCommand;
import me.emmy.core.feature.world.command.impl.*;
import me.emmy.core.server.command.SetSpawnCommand;
import me.emmy.core.server.command.SpawnCommand;

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
        new RankSetDefaultCommand();
        new RankSetDescriptionCommand();
        new RankSetHiddenCommand();
        new RankSetPrefixCommand();
        new RankSetPurchasableCommand();
        new RankSetStaffCommand();
        new RankSetSuffixCommand();
        new RankSetWeightCommand();
        new RankAddPermissionCommand();
        new RankRemovePermissionCommand();
        new RankAddInheritanceCommand();
        new RankRemoveInheritanceCommand();

        new TagCommand();

        new WorldCommand();
        new WorldCreateCommand();
        new WorldCreateVoidWorldCommand();
        new WorldDeleteCommand();
        new WorldListCommand();
        new WorldSetRuleCommand();
        new WorldTeleportCommand();

        new SpawnCommand();
        new SetSpawnCommand();
    }

    @Override
    public void closure() {

    }
}
