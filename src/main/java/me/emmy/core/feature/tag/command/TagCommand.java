package me.emmy.core.feature.tag.command;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.feature.tag.menu.TagCategoryMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class TagCommand extends BaseCommand {
    @CommandData(name = "tags", description = "Tag command to choose tags", usage = "/tag")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new TagCategoryMenu().openMenu(player);
    }
}