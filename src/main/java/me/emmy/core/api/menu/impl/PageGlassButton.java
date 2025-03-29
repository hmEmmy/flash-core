package me.emmy.core.api.menu.impl;

import lombok.AllArgsConstructor;
import me.emmy.core.api.menu.Button;
import me.emmy.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PageGlassButton extends Button {
    private int durability;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .durability(this.durability)
                .build();
    }
}
