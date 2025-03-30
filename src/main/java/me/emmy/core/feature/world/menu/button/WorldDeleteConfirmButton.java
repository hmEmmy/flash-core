package me.emmy.core.feature.world.menu.button;

import lombok.AllArgsConstructor;
import me.emmy.core.Flash;
import me.emmy.core.api.menu.Button;
import me.emmy.core.feature.world.WorldService;
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
@AllArgsConstructor
public class WorldDeleteConfirmButton extends Button {
    private final String worldName;
    private final boolean deleteFiles;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.EMERALD)
                .name("&c&lConfirm")
                .lore(
                        "",
                        "&7Are you sure you want to delete",
                        "&7the &b" + this.worldName + " &7world?",
                        "",
                        "&7This action cannot be undone.",
                        this.deleteFiles ?
                                "&7All files will be deleted." :
                                "&7The world will be unloaded.",
                        "",
                        "&cClick to confirm!"
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        Flash.getInstance().getServiceRepository().getService(WorldService.class).deleteWorld(this.worldName, this.deleteFiles);
        ActionBarUtil.sendMessage(player, "&c&lWORLD REMOVAL: &aConfirmed", 5);
    }
}