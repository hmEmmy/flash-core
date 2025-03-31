package me.emmy.core.feature.grant.menu;

import lombok.AllArgsConstructor;
import me.emmy.core.Flash;
import me.emmy.core.api.menu.Button;
import me.emmy.core.api.menu.Menu;
import me.emmy.core.api.menu.impl.CancelButton;
import me.emmy.core.feature.grant.Grant;
import me.emmy.core.feature.grant.menu.button.GrantConfirmButton;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.util.CC;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
@AllArgsConstructor
public class GrantConfirmMenu extends Menu {
    private final Profile targetProfile;
    private final Grant grant;

    @Override
    public String getTitle(Player player) {
        return "&c&lConfirm Grant?";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        String message = "&c&lGRANT: &7Cancelled";

        int[] grantButtonPositions = {
                10, 11, 12, 19, 20, 21, 28, 29, 30
        };

        int[] cancelButtonPositions = {
                14, 15, 16, 23, 24, 25, 32, 33, 34
        };

        for (int pos : grantButtonPositions) {
            buttons.put(pos, new GrantConfirmButton(this.targetProfile, this.grant));
        }

        for (int pos : cancelButtonPositions) {
            buttons.put(pos, new CancelButton(message));
        }

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }

    @Override
    public void onClose(Player player) {
        ProfileService profileService = Flash.getInstance().getServiceRepository().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.setGrantProcessData(null);

        player.sendMessage(CC.translate("&c&lGRANT: &7Cancelled"));
    }
}
