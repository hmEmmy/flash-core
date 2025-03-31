package me.emmy.core.feature.grant.menu;

import lombok.AllArgsConstructor;
import me.emmy.core.Flash;
import me.emmy.core.api.menu.Button;
import me.emmy.core.api.menu.pagination.PaginatedMenu;
import me.emmy.core.feature.grant.Grant;
import me.emmy.core.feature.grant.menu.button.GrantsButton;
import me.emmy.core.feature.grant.menu.button.GrantsToggleInactiveButton;
import me.emmy.core.profile.ProfileService;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
@AllArgsConstructor
public class GrantsMenu extends PaginatedMenu {
    private final OfflinePlayer target;
    private boolean displayInactiveGrants;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&3&lGrants " + this.target.getName() + " &8(&7" + (this.displayInactiveGrants ? "Inactive" : "Active") + "&8)";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);

        buttons.put(4, new GrantsToggleInactiveButton(this.target, this.displayInactiveGrants));

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Grant> grants = Flash.getInstance().getServiceRepository().getService(ProfileService.class).getProfile(this.target.getUniqueId()).getGrants();

        if (this.displayInactiveGrants) {
            grants.stream()
                    .filter(grant -> !grant.isActive())
                    .sorted(Comparator.comparingLong(Grant::getRemovedAt).reversed())
                    .forEach(grant -> buttons.put(buttons.size(), new GrantsButton(this.target, grant, true)));
        } else {
            grants.stream()
                    .filter(Grant::isActive)
                    .sorted(Comparator.comparingLong(Grant::getAddedAt).reversed())
                    .forEach(grant -> buttons.put(buttons.size(), new GrantsButton(this.target, grant, false)));
        }

        return buttons;
    }
}