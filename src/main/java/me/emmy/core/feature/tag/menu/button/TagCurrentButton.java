package me.emmy.core.feature.tag.menu.button;

import lombok.AllArgsConstructor;
import me.emmy.core.Flash;
import me.emmy.core.api.menu.Button;
import me.emmy.core.feature.tag.Tag;
import me.emmy.core.feature.tag.TagService;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.util.ActionBarUtil;
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
public class TagCurrentButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        ProfileService profileService = Flash.getInstance().getServiceRepository().getService(ProfileService.class);
        String tagName = profileService.getProfile(player.getUniqueId()).getTag();
        TagService tagService = Flash.getInstance().getServiceRepository().getService(TagService.class);
        Tag tag = tagService.getTag(tagName);
        if (tag == null) {
            return new ItemBuilder(Material.INK_SACK)
                    .name("&3&lCurrent Tag: &c&lNone")
                    .lore(
                            "",
                            "&c&lNo Tag Selected."
                    )
                    .durability(8)
                    .build();
        }

        return new ItemBuilder(Material.REDSTONE)
                .name("&3&lCurrent Tag: " + tag.getAppearance())
                .lore(
                        "&3● &fCategory: &b" + tag.getCategory().getName(),
                        "&3● &fDescription: &b" + (tag.getDescription().isEmpty() ? "&cNo Description" : tag.getDescription()),
                        "",
                        "&c&lClick to unequip your tag."
                )
                .durability(0)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        ProfileService profileService = Flash.getInstance().getServiceRepository().getService(ProfileService.class);
        String tagName = profileService.getProfile(player.getUniqueId()).getTag();
        Tag tag = Flash.getInstance().getServiceRepository().getService(TagService.class).getTag(tagName);
        if (tag == null) {
            return;
        }

        profileService.getProfile(player.getUniqueId()).setTag("");
        ActionBarUtil.sendMessage(player, "&c&lUNEQUIPPED TAG: &7" + tag.getColor() + tag.getName(), 5);
    }
}