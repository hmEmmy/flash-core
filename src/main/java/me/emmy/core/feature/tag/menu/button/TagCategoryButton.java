package me.emmy.core.feature.tag.menu.button;

import me.emmy.core.Flash;
import me.emmy.core.api.menu.Button;
import me.emmy.core.feature.tag.TagService;
import me.emmy.core.feature.tag.enums.EnumTagCategory;
import me.emmy.core.feature.tag.menu.TagMenu;
import me.emmy.core.util.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class TagCategoryButton extends Button {
    private final EnumTagCategory category;

    /**
     * Constructor for the TagCategoryButton class.
     *
     * @param category The tag category associated with this button.
     */
    public TagCategoryButton(EnumTagCategory category) {
        this.category = category;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.category.getIcon())
                .name("&b&l" + this.category.getName() + " Tags")
                .lore(
                        "",
                        "&bDescription:",
                        " &b&l▎ &f" + this.category.getDescription(),
                        "",
                        "&bAmount:",
                        " &b&l▎ &f" + Flash.getInstance().getServiceRepository().getService(TagService.class).getAmountInCategory(this.category),
                        "",
                        "&aClick to view all!"
                )
                .durability(this.category.getDurability())
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        new TagMenu(this.category).openMenu(player);
    }
}