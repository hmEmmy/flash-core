package me.emmy.core.feature.grant.menu.button;

import lombok.AllArgsConstructor;
import me.emmy.core.Flash;
import me.emmy.core.api.menu.Button;
import me.emmy.core.feature.rank.Rank;
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
public class GrantButton extends Button {
    private final OfflinePlayer target;
    private final Rank rank;

    @Override
    public ItemStack getButtonItem(Player player) {
        String yes = CC.translate("&aYes");
        String no = CC.translate("&cNo");
        String notSet = CC.translate("&cNot Set");

        return new ItemBuilder(Material.PAPER)
                .name(this.rank.getRankWithColor())
                .lore(
                        " &3● &fPrefix: &b" + (this.rank.getPrefix().isEmpty() ? notSet : "&7'&r" + this.rank.getPrefix() + "&r&7'"),
                        " &3● &fSuffix: &b" + (this.rank.getSuffix().isEmpty() ? notSet : "&7'&r" + this.rank.getSuffix() + "&r&7'"),
                        " &3● &fColor: &b" + this.rank.getColor() + this.rank.getColor().name(),
                        " &3● &fStaff: &b" + (this.rank.isStaffRank() ? yes : no),
                        " &3● &fPurchasable: &b" + (this.rank.isPurchasable() ? yes : no),
                        " &3● &fHidden: &b" + (this.rank.isHiddenRank() ? yes : no),
                        "",
                        "&aClick to grant " + this.target.getName() + "!"
                )
                .hideMeta()
                .durability(0)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        ProfileService profileService = Flash.getInstance().getServiceRepository().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        Profile targetProfile = profileService.getProfile(this.target.getUniqueId());
        if (targetProfile.rankAlreadyGranted(this.rank)) {
            player.sendMessage(CC.translate("&cThat player already has this rank granted."));
            return;
        }

        GrantProcessData grantProcessData = new GrantProcessData(this.target, this.rank);
        profile.setGrantProcessData(grantProcessData);
        profile.setGranting(true);

        player.closeInventory();
        player.sendMessage(CC.translate("&4[Grant] &aPlease enter a reason. &7[Type &e'cancel' &7to cancel]"));
    }
}