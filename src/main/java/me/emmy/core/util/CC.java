package me.emmy.core.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 26/03/2025
 */
@UtilityClass
public class CC {
    /**
     * Translate a string with the '&' character to a string with the ChatColor character.
     *
     * @param string the string to translate
     * @return the translated string
     */
    public String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}