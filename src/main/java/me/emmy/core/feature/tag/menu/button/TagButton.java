package me.emmy.core.feature.tag.menu.button;

import lombok.AllArgsConstructor;
import me.emmy.core.Flash;
import me.emmy.core.api.menu.Button;
import me.emmy.core.feature.tag.Tag;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.util.ActionBarUtil;
import me.emmy.core.util.CC;
import me.emmy.core.util.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
@AllArgsConstructor
public class TagButton extends Button {
    private final Tag tag;

    @Override
    public ItemStack getButtonItem(Player player) {
        ProfileService profileService = Flash.getInstance().getServiceRepository().getService(ProfileService.class);
        boolean isSelected = profileService.getProfile(player.getUniqueId()).getTag().equals(this.tag.getName());

        String tagPermission = "flash.tag." + this.tag.getName().toLowerCase();
        String permissionLore = player.hasPermission(tagPermission) ? isSelected ? "&a&lSELECTED" : "&aClick to use!" : "&c&lNO PERMISSION";
        String description = this.tag.getDescription().isEmpty() ? "" : " &7(" + this.tag.getDescription() + ")";

        return new ItemBuilder(this.tag.getIcon())
                .name("&b&l" + this.tag.getName() + description)
                .lore(
                        "",
                        "&bAppearance:",
                        " &b&l▎ &f" + this.tag.getColor() + this.tag.getAppearance(),
                        "",
                        permissionLore
                )
                .durability(this.tag.getDurability())
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        ProfileService profileService = Flash.getInstance().getServiceRepository().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getTag().equals(this.tag.getName())) {
            player.sendMessage(CC.translate("&cYou already have this tag selected!"));
            return;
        }

        if (!player.hasPermission("flash.tag." + this.tag.getName().toLowerCase())) {
            player.sendMessage(CC.translate("&cYou do not have permission to use this tag!"));
            return;
        }

        profile.setTag(this.tag.getName());
        player.closeInventory();

        ActionBarUtil.sendMessage(player, "&aSELECTED TAG: &b" + this.tag.getColor() + this.tag.getName(), 10);
    }
}