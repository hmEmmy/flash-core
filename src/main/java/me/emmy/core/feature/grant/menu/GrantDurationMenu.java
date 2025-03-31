package me.emmy.core.feature.grant.menu;

import lombok.AllArgsConstructor;
import me.emmy.core.Flash;
import me.emmy.core.api.menu.Button;
import me.emmy.core.api.menu.Menu;
import me.emmy.core.api.menu.impl.CancelButton;
import me.emmy.core.feature.grant.menu.button.GrantDurationButton;
import me.emmy.core.feature.grant.menu.button.GrantDurationPermanentButton;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.profile.data.GrantProcessData;
import me.emmy.core.util.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
@AllArgsConstructor
public class GrantDurationMenu extends Menu {
    private GrantProcessData grantData;

    @Override
    public String getTitle(Player player) {
        return "&3&lGrant Duration";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new CancelButton("&c&lGRANT: &7Cancelled"));
        buttons.put(11, new GrantDurationButton(this.grantData, "1d", Material.IRON_BLOCK));
        buttons.put(12, new GrantDurationButton(this.grantData, "7d", Material.GOLD_BLOCK));
        buttons.put(13, new GrantDurationButton(this.grantData, "30d", Material.EMERALD_BLOCK));
        buttons.put(14, new GrantDurationButton(this.grantData, "365d", Material.DIAMOND_BLOCK));
        buttons.put(15, new GrantDurationPermanentButton(this.grantData));

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}