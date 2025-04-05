package me.emmy.core.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 20:16
 */
@UtilityClass
public class ClickableUtil {
    /**
     * Create a clickable text component.
     *
     * @param message the message
     * @param command the command
     * @param hoverText the hover text
     * @return the text component
     */
    public TextComponent createComponent(String message, String command, String hoverText) {
        TextComponent clickableMessage = new TextComponent(CC.translate(message));

        if (command != null) {
            clickableMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        }

        String hover = CC.translate(hoverText);
        BaseComponent[] hoverComponent = new ComponentBuilder(hover).create();
        clickableMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));
        return clickableMessage;
    }

    /**
     * Converts a list of strings into a hover component with proper line breaks.
     *
     * @param lines the list of lines
     * @return BaseComponent[] with formatted hover text
     */
    public BaseComponent[] fromLines(List<String> lines) {
        ComponentBuilder builder = new ComponentBuilder("");

        for (int i = 0; i < lines.size(); i++) {
            String line = CC.translate(lines.get(i));
            builder.append(line);

            if (i < lines.size() - 1) {
                builder.append("\n");
            }
        }

        return builder.create();
    }

    /**
     * Sends a list of clickable text components to a player.
     *
     * @param player the player to send the message to
     * @param lines the list of lines to send
     * @param clickCommand the command to run on click
     * @param hoverTitle the title to show on hover
     */
    public void sendList(Player player, List<String> lines, String clickCommand, String hoverTitle) {
        for (String line : lines) {
            TextComponent component = new TextComponent(CC.translate(line));

            if (clickCommand != null) {
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickCommand));
            }

            if (hoverTitle != null && !hoverTitle.isEmpty()) {
                BaseComponent[] hover = fromLines(Arrays.asList(hoverTitle.split("\n")));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover));
            }

            player.spigot().sendMessage(component);
        }
    }

}