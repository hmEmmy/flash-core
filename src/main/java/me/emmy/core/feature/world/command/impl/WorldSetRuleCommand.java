package me.emmy.core.feature.world.command.impl;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.util.ActionBarUtil;
import me.emmy.core.util.CC;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
public class WorldSetRuleCommand extends BaseCommand {
    @CommandData(name = "world.setrule", permission = "flash.command.world", aliases = "world.setrule", description = "Set a world rule", usage = "/world setrule <world> <rule> <value>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 3) {
            player.sendMessage(CC.translate("&cUsage: /world setrule <world> <rule> <value>"));
            return;
        }

        String worldName = args[0];

        World world = this.flash.getServer().getWorld(worldName);
        if (world == null) {
            player.sendMessage(CC.translate("&cWorld " + worldName + " does not exist!"));
            return;
        }

        String rule = args[1];
        if (!world.isGameRule(rule)) {
            player.sendMessage(CC.translate("&cInvalid game rule: " + rule));
            return;
        }

        String value = args[2];
        world.setGameRuleValue(rule, value);
        ActionBarUtil.sendMessage(player, "&a&lGAMERULE UPDATE: &b" + rule + " &ais now &b" + value + " &ain &b" + worldName + "&a!", 5);
    }
}