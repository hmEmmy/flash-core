package me.emmy.core.api.menu.pagination.impl.button;

import me.emmy.core.api.menu.MenuUtil;
import me.emmy.core.api.menu.pagination.PaginatedMenu;
import me.emmy.core.api.menu.pagination.impl.menu.ViewAllPagesMenu;
import me.emmy.core.util.ItemBuilder;
import lombok.AllArgsConstructor;
import me.emmy.core.api.menu.Button;
import me.emmy.core.util.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 24/01/2025
 */
@AllArgsConstructor
public class PageButton extends Button {
    private PaginatedMenu menu;
    private int offset;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.offset > 0) {
            if (MenuUtil.hasNext(player, offset, this.menu)) {
                return new ItemBuilder(Material.MELON)
                        .name("&b&lNext Page &7" + (this.menu.getPage() + "/" + this.menu.getPages(player)))
                        .lore(Arrays.asList(
                                "&7Right-Click:",
                                " &7▶ View all",
                                "",
                                "&bClick to view!"
                        ))
                        .hideMeta()
                        .build();
            } else {
                return new ItemBuilder(Material.MELON)
                        .name(CC.translate("&c&lNext Page"))
                        .lore(Arrays.asList(
                                "&cThere is no available",
                                "&cnext page."
                        ))
                        .hideMeta()
                        .build();
            }
        } else {
            if (MenuUtil.hasPrevious(offset, this.menu)) {
                return new ItemBuilder(Material.SPECKLED_MELON)
                        .name("&6&lLast Page &7" + (this.menu.getPage() + "/" + this.menu.getPages(player)))
                        .lore(Arrays.asList(
                                "&7Right-Click:",
                                " &7▶ View all",
                                "",
                                "&6Click to view!"
                        ))
                        .hideMeta()
                        .build();
            } else {
                return new ItemBuilder(Material.SPECKLED_MELON)
                        .name(CC.translate("&c&lLast Page"))
                        .lore(Arrays.asList(
                                "&cThere is no available",
                                "&clast page."
                        ))
                        .hideMeta()
                        .build();
            }
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            new ViewAllPagesMenu(this.menu).openMenu(player);
        } else if (clickType != ClickType.LEFT) {
            if (this.offset > 0) {
                if (MenuUtil.hasNext(player, offset, this.menu)) {
                    this.menu.modPage(player, this.offset);
                    playNeutral(player);
                } else {
                    playFail(player);
                }
            } else {
                if (MenuUtil.hasPrevious(offset, this.menu)) {
                    this.menu.modPage(player, this.offset);
                    playNeutral(player);
                } else {
                    playFail(player);
                }
            }
        }
    }
}