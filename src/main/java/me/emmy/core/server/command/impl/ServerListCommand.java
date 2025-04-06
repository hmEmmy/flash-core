package me.emmy.core.server.command.impl;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.server.Server;
import me.emmy.core.util.CC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ServerListCommand extends BaseCommand {
    @CommandData(name = "servers", aliases = "server.list", permission = "flash.command.servers", description = "View the server list.", usage = "/servers", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        List<Server> servers = this.flash.getServerRepository().getServers();
        if (servers.isEmpty()) {
            sender.sendMessage(CC.translate("&cNo servers available."));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&3&lServer List"));

        for (Server server : servers) {
            String status = server.isOnline() ? "&aOnline" : "&cOffline";
            String commandString = "/server view " + server.getName();

            TextComponent component = new TextComponent(" ● ");
            component.setColor(ChatColor.DARK_AQUA);

            TextComponent nameComponent = new TextComponent(server.getName());
            nameComponent.setColor(ChatColor.WHITE);
            nameComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandString));
            nameComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Click to view info about " + server.getName()).color(ChatColor.AQUA).create()));

            TextComponent separator = new TextComponent(" - ");
            separator.setColor(ChatColor.GRAY);

            TextComponent statusComponent = new TextComponent(CC.translate(status));
            statusComponent.setColor(ChatColor.WHITE);

            component.addExtra(nameComponent);
            component.addExtra(separator);
            component.addExtra(statusComponent);
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Click to view info about " + server.getName()).color(ChatColor.AQUA).create()));

            if (sender instanceof Player) {
                ((Player) sender).spigot().sendMessage(component);
            } else {
                sender.sendMessage(CC.translate(" ● " + server.getName() + " - " + status));
            }
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate(" &7Use &3/serverinfo &7for more info about servers."));
        sender.sendMessage("");
    }
}
