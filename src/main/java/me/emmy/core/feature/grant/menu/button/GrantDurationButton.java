package me.emmy.core.feature.grant.menu.button;

import lombok.AllArgsConstructor;
import me.emmy.core.Flash;
import me.emmy.core.api.menu.Button;
import me.emmy.core.feature.grant.Grant;
import me.emmy.core.feature.grant.menu.GrantConfirmMenu;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.profile.data.GrantProcessData;
import me.emmy.core.server.property.ServerProperty;
import me.emmy.core.util.DateUtils;
import me.emmy.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
@AllArgsConstructor
public class GrantDurationButton extends Button {
    private final GrantProcessData grantData;
    private final String duration;
    private final Material material;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.material)
                .name("&3&lLimited Duration")
                .lore(
                        " &3● &fGranting: &b" + this.grantData.getTarget().getName(),
                        " &3● &fDuration: &b" + this.duration,
                        " &3● &fRank: &b" + this.grantData.getRank().getRankWithColor(),
                        "",
                        "&aClick to grant!"
                )
                .hideMeta()
                .durability(0)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        String server = Flash.getInstance().getServiceRepository().getService(ServerProperty.class).getName();

        Grant grant = new Grant();
        grant.setRankName(this.grantData.getRank().getName());
        grant.setServer(server);
        grant.setIssuer(player.getName());
        grant.setReason(this.grantData.getReason());
        grant.setDuration(DateUtils.parseTime(this.duration));
        grant.setAddedAt(System.currentTimeMillis());
        grant.setPermanent(false);
        grant.setActive(true);

        Profile targetProfile = Flash.getInstance().getServiceRepository().getService(ProfileService.class).getProfile(this.grantData.getTarget().getUniqueId());
        new GrantConfirmMenu(targetProfile, grant).openMenu(player);
    }
}