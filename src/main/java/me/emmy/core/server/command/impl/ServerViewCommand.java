package me.emmy.core.server.command.impl;

import lombok.SneakyThrows;
import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.server.Server;
import me.emmy.core.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 06/04/2025
 */
public class ServerViewCommand extends BaseCommand {
    @SneakyThrows
    @CommandData(name = "server.view", permission = "flashcore.command.serverinfo", description = "View server information.", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        if (command.getArgs().length == 0) {
            sender.sendMessage(CC.translate("&cUsage: /server view <name>"));
            return;
        }

        String name = command.getArgs(0);
        Server server = this.flash.getServerRepository().getServerByName(name);

        if (server == null) {
            sender.sendMessage(CC.translate("&cNo server found with that name."));
            return;
        }

        boolean isOp = sender.isOp();
        String address = server.getAddress();

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&3&lServer Information &8- &f" + server.getName()));
        sender.sendMessage(CC.translate(" &3● &fIP: &c" + (server.getAddress().isEmpty() ? "N/A" : isOp ? address : "&k" + address)));
        sender.sendMessage(CC.translate(" &3● &fPort: &b" + server.getPort()));
        sender.sendMessage(CC.translate(" &3● &fOnline: " + (server.isOnline() ? "&aYes" : "&cNo")));
        sender.sendMessage(CC.translate(" &3● &fWhitelisted: " + (server.isWhitelisted() ? "&aYes" : "&cNo")));
        sender.sendMessage("");

        if (!isOp) {
            sender.sendMessage("");
            sender.sendMessage(CC.translate("&c&lNO PERMISSION TO VIEW THE IP!"));
            sender.sendMessage("");
        }
    }
}