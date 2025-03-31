package me.emmy.core.feature.grant.menu;

import lombok.AllArgsConstructor;
import me.emmy.core.Flash;
import me.emmy.core.api.menu.Button;
import me.emmy.core.api.menu.pagination.PaginatedMenu;
import me.emmy.core.feature.grant.Grant;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.util.CC;
import me.emmy.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;

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

        buttons.put(4, new ToggleInactiveGrantsButton());

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
                    .forEach(grant -> buttons.put(buttons.size(), new GrantsButton(this.target, grant)));
        } else {
            grants.stream()
                    .filter(Grant::isActive)
                    .sorted(Comparator.comparingLong(Grant::getAddedAt).reversed())
                    .forEach(grant -> buttons.put(buttons.size(), new GrantsButton(this.target, grant)));
        }

        return buttons;
    }

    @AllArgsConstructor
    private class GrantsButton extends Button {
        private final OfflinePlayer target;
        private final Grant grant;

        @Override
        public ItemStack getButtonItem(Player player) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            String addedAtFormatted = dateFormat.format(new Date(this.grant.getAddedAt()));
            String removedAtFormatted = this.grant.getRemovedAt() > 0 ? dateFormat.format(new Date(this.grant.getRemovedAt())) : "N/A";
            String expiresAtFormatted = this.grant.isPermanent() ? "Never" : dateFormat.format(new Date(this.grant.getAddedAt() + this.grant.getDuration()));

            List<String> activeLore = Arrays.asList(
                    "",
                    this.grant.getRank().getColor() + "Grant Info",
                    "&f● Expires at: &4" + this.grant.getRank().getColor() + expiresAtFormatted,
                    "&f● Reason: &4" + this.grant.getRank().getColor() + this.grant.getReason(),
                    "&f● Added by: &4" + this.grant.getRank().getColor() + this.grant.getIssuer(),
                    "&f● Added at: &4" + this.grant.getRank().getColor() + addedAtFormatted,
                    "&f● Permanent: &4" + this.grant.getRank().getColor() + this.grant.isPermanent(),
                    "",
                    "&aClick to remove this grant."
            );

            List<String> inActiveLore = Arrays.asList(
                    "",
                    this.grant.getRank().getColor() + "Grant Info",
                    "&f● Expires at: &4" + this.grant.getRank().getColor() + expiresAtFormatted,
                    "&f● Reason: &4" + this.grant.getRank().getColor() + this.grant.getReason(),
                    "&f● Added by: &4" + this.grant.getRank().getColor() + this.grant.getIssuer(),
                    "&f● Added at: &4" + this.grant.getRank().getColor() + addedAtFormatted,
                    "&f● Permanent: &4" + this.grant.getRank().getColor() + this.grant.isPermanent(),
                    "",
                    "&c&l● Removed by: &4" + this.grant.getRank().getColor() + this.grant.getRemover(),
                    "&c&l● Removed at: &4" + this.grant.getRank().getColor() + removedAtFormatted,
                    "",
                    "&cThis grant was already removed."
            );

            return new ItemBuilder(Material.PAPER)
                    .name(this.grant.getRank().getRankWithColor())
                    .lore(displayInactiveGrants ? inActiveLore : activeLore)
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            if (!this.grant.isActive()) {
                player.sendMessage(CC.translate("&cThis grant was already removed."));
                return;
            }

            if (this.grant.getRank().isDefaultRank()) {
                player.sendMessage(CC.translate("&cYou cannot remove the default rank."));
                return;
            }

            this.grant.setActive(false);
            this.grant.setRemovalReason("Removed by " + player.getName());
            this.grant.setRemovedAt(System.currentTimeMillis());
            this.grant.setRemover(player.getName());

            player.sendMessage(CC.translate("&aSuccessfully removed the &b" + this.grant.getRankName() + " &arank from &b" + this.target.getName() + "&a."));

            if (this.target.isOnline()) {
                this.target.getPlayer().sendMessage(CC.translate("&aYour &b" + this.grant.getRankName() + " &arank has been removed by &b" + player.getName() + "&a."));
            }

            new GrantsMenu(this.target, displayInactiveGrants).openMenu(player);
        }
    }

    private class ToggleInactiveGrantsButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            Material material = displayInactiveGrants ? Material.EMERALD : Material.REDSTONE;
            String status = displayInactiveGrants ? "Showing Inactive Grants" : "Showing Active Grants";

            return new ItemBuilder(material)
                    .name("&b" + status)
                    .lore(Collections.singletonList("&7Click to toggle."))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            displayInactiveGrants = !displayInactiveGrants;

            new GrantsMenu(target, displayInactiveGrants).openMenu(player);
        }
    }
}