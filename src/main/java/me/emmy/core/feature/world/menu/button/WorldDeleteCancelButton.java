package me.emmy.core.feature.world.menu.button;

import me.emmy.core.api.menu.Button;
import me.emmy.core.util.ActionBarUtil;
import me.emmy.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class WorldDeleteCancelButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.REDSTONE)
                .name("&c&lCancel")
                .lore(
                        "",
                        "&7&lClick to cancel."
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        player.closeInventory();
        ActionBarUtil.sendMessage(player, "&c&lWORLD REMOVAL: &7Cancelled", 5);
    }
}