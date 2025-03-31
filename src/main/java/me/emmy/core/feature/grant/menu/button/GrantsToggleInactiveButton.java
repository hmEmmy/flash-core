package me.emmy.core.feature.grant.menu.button;

import lombok.AllArgsConstructor;
import me.emmy.core.api.menu.Button;
import me.emmy.core.feature.grant.menu.GrantsMenu;
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
public class GrantsToggleInactiveButton extends Button {
    private final OfflinePlayer target;
    private boolean displayInactiveGrants;

    @Override
    public ItemStack getButtonItem(Player player) {
        Material material = this.displayInactiveGrants ? Material.EMERALD : Material.REDSTONE;
        String status = this.displayInactiveGrants ? "&c&lShowing Inactive Grants" : "&a&lShowing Active Grants";

        return new ItemBuilder(material)
                .name(status)
                .lore(
                        "",
                        "&7&lClick to view " + (this.displayInactiveGrants ? "active" : "inactive") + " grants.")
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        this.displayInactiveGrants = !this.displayInactiveGrants;

        new GrantsMenu(this.target, this.displayInactiveGrants).openMenu(player);
    }
}
