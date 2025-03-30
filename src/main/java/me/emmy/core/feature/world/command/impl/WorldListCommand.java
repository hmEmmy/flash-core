package me.emmy.core.feature.world.command.impl;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.util.CC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class WorldListCommand extends BaseCommand {
    @CommandData(name = "world.list", aliases = "worlds", permission = "flash.command.worlds", description = "List all worlds", usage = "/worlds")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        List<World> worlds = this.flash.getServer().getWorlds();
        player.sendMessage("");
        player.sendMessage(CC.translate("  &3&lWorld List &8- &7Available Worlds"));

        worlds.forEach(world -> {
            BaseComponent[] worldInfo = new ComponentBuilder("")
                    .append(CC.translate("  &3&lWorld Information &8- &7" + world.getName())).append("\n")
                    .append(CC.translate("   &3● &fType: ")).append(CC.translate("&b" + world.getWorldType())).append("\n")
                    .append(CC.translate("   &3● &fPlayers: ")).append(CC.translate("&b" + world.getPlayers().size())).append("\n")
                    .append(CC.translate("   &3● &fEnvironment: ")).append(CC.translate("&b" + world.getEnvironment())).append("\n")
                    .append(CC.translate("   &3● &fSeed: ")).append(CC.translate("&b" + world.getSeed())).append("\n")
                    .append(CC.translate("   &3● &fTime: ")).append(CC.translate("&b" + world.getTime())).append("\n")
                    .append(CC.translate("   &3● &fDifficulty: ")).append(CC.translate("&b" + world.getDifficulty())).append("\n")
                    .append(CC.translate("   &3● &fWorld Spawn: ")).append(CC.translate("&b" + world.getSpawnLocation().getBlockX() + ", " + world.getSpawnLocation().getBlockY() + ", " + world.getSpawnLocation().getBlockZ()))
                    .create();

            ComponentBuilder message = new ComponentBuilder("   ");
            message.append("  ● ").color(ChatColor.AQUA);
            message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, worldInfo));
            message.append(world.getName()).color(ChatColor.WHITE);
            message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, worldInfo));
            message.append(" (" + world.getWorldType().name() + ")").color(ChatColor.GRAY);
            message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, worldInfo));

            player.spigot().sendMessage(message.create());
        });

        player.sendMessage("");
    }
}