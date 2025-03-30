package me.emmy.core.feature.world.menu;

import lombok.AllArgsConstructor;
import me.emmy.core.api.menu.Button;
import me.emmy.core.api.menu.Menu;
import me.emmy.core.feature.world.menu.button.WorldDeleteCancelButton;
import me.emmy.core.feature.world.menu.button.WorldDeleteConfirmButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
@AllArgsConstructor
public class WorldDeleteConfirmMenu extends Menu {
    private final String worldName;
    private final boolean deleteFiles;

    @Override
    public String getTitle(Player player) {
        return "&c&lDelete World?";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(12, new WorldDeleteConfirmButton(this.worldName, this.deleteFiles));
        buttons.put(14, new WorldDeleteCancelButton());

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}