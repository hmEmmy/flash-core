package me.emmy.core.feature.grant.menu.button;

import com.mysql.jdbc.profiler.ProfilerEvent;
import lombok.AllArgsConstructor;
import me.emmy.core.Flash;
import me.emmy.core.api.menu.Button;
import me.emmy.core.feature.grant.Grant;
import me.emmy.core.feature.grant.GrantService;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.profile.data.GrantProcessData;
import me.emmy.core.util.CC;
import me.emmy.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
@AllArgsConstructor
public class GrantConfirmButton extends Button {
    private final Profile targetProfile;
    private final Grant grant;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.EMERALD)
                .name("&c&lConfirm")
                .lore(
                        "",
                        "&7Are you sure you want to grant",
                        "&7the &b" + this.grant.getRank() + " &7rank?",
                        "",
                        "&7The rank will be granted to",
                        "&7the player &b" + this.targetProfile.getUsername() + "&7.",
                        "",
                        "&cClick to confirm!"
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        GrantService grantService = Flash.getInstance().getServiceRepository().getService(GrantService.class);
        grantService.addGrant(targetProfile, this.grant);

        player.sendMessage(CC.translate("&4[Grant] &aYou have successfully granted the rank &b" + this.grant.getRank() + "&a to &b" + this.targetProfile.getUsername() + "&a."));
        player.closeInventory();
    }
}