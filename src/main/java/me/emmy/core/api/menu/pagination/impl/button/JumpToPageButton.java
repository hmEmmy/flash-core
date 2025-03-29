package me.emmy.core.api.menu.pagination.impl.button;

import me.emmy.core.api.menu.pagination.PaginatedMenu;
import me.emmy.core.util.CC;
import lombok.AllArgsConstructor;
import me.emmy.core.api.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@AllArgsConstructor
public class JumpToPageButton extends Button {
    private int page;
    private PaginatedMenu menu;
    private boolean current;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(this.current ? Material.ENCHANTED_BOOK : Material.BOOK, this.page);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(CC.translate("&b&lPage " + this.page));

        if (this.current) {
            itemMeta.setLore(Arrays.asList(
                    "",
                    CC.translate("&bSelected!")
            ));
        } else {
            itemMeta.setLore(Arrays.asList(
                    "",
                    CC.translate("&bClick to open!")
            ));
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        this.menu.modPage(player, this.page - this.menu.getPage());
        Button.playNeutral(player);
    }

}
