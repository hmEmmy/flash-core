package me.emmy.core.feature.tag.menu;

import me.emmy.core.api.menu.Button;
import me.emmy.core.api.menu.Menu;
import me.emmy.core.feature.tag.enums.EnumTagCategory;
import me.emmy.core.feature.tag.menu.button.TagCategoryButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class TagCategoryMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&3&Choose Category";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map <Integer, Button> buttons = new HashMap<>();

        buttons.put(10, new TagCategoryButton(EnumTagCategory.TEXT));
        buttons.put(11, new TagCategoryButton(EnumTagCategory.EMOJI));
        buttons.put(12, new TagCategoryButton(EnumTagCategory.PARTNER));
        buttons.put(13, new TagCategoryButton(EnumTagCategory.EXCLUSIVE));
        buttons.put(16, new TagCategoryButton(EnumTagCategory.SPECIAL));

        this.addBorder(buttons, (byte) 15, 3);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}