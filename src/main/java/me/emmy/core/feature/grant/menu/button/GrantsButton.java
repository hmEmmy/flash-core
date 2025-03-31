package me.emmy.core.feature.grant.menu.button;

import lombok.AllArgsConstructor;
import me.emmy.core.Flash;
import me.emmy.core.api.menu.Button;
import me.emmy.core.feature.grant.Grant;
import me.emmy.core.feature.grant.menu.GrantsMenu;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.util.CC;
import me.emmy.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
@AllArgsConstructor
public class GrantsButton extends Button {
    private final OfflinePlayer target;
    private final Grant grant;
    private final boolean displayInactiveGrants;

    @Override
    public ItemStack getButtonItem(Player player) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String addedAtFormatted = dateFormat.format(new Date(this.grant.getAddedAt()));
        String removedAtFormatted = this.grant.getRemovedAt() > 0 ? dateFormat.format(new Date(this.grant.getRemovedAt())) : "N/A";
        String expiresAtFormatted = this.grant.isPermanent() ? "Never" : dateFormat.format(new Date(this.grant.getAddedAt() + this.grant.getDuration()));

        List<String> activeLore = Arrays.asList(
                "&f● Rank: &b" + this.grant.getRank().getRankWithColor(),
                "&f● Expires at: &b" + expiresAtFormatted,
                "&f● Reason: &b" + this.grant.getReason(),
                "&f● Added by: &b" + this.grant.getIssuer(),
                "&f● Added at: &b" + addedAtFormatted,
                "&f● Permanent: &b" + this.grant.isPermanent(),
                "",
                "&aClick to remove this grant."
        );

        List<String> inActiveLore = Arrays.asList(
                "&f● Rank: &b" + this.grant.getRank().getRankWithColor(),
                "&f● Expires at: &c&lEXPIRED",
                "&f● Reason: &b" + this.grant.getReason(),
                "&f● Added by: &b" + this.grant.getIssuer(),
                "&f● Added at: &b" + addedAtFormatted,
                "&f● Permanent: &b" + this.grant.isPermanent(),
                "",
                "&c&l● Removed by: &b" + this.grant.getRemover(),
                "&c&l● Removed at: &b" + removedAtFormatted,
                "",
                "&cThis grant was already removed."
        );

        return new ItemBuilder(Material.PAPER)
                .name("&3&lGrant Info")
                .lore(this.displayInactiveGrants ? inActiveLore : activeLore)
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

        Profile profile = Flash.getInstance().getServiceRepository().getService(ProfileService.class).getProfile(this.target.getUniqueId());
        profile.saveProfile();

        player.sendMessage(CC.translate("&aSuccessfully removed the &b" + this.grant.getRankName() + " &arank from &b" + this.target.getName() + "&a."));

        if (this.target.isOnline()) {
            this.target.getPlayer().sendMessage(CC.translate("&aYour &b" + this.grant.getRankName() + " &arank has been removed by &b" + player.getName() + "&a."));
        }

        new GrantsMenu(this.target, this.displayInactiveGrants).openMenu(player);
    }
}
