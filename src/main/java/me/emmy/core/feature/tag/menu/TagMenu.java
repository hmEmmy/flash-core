package me.emmy.core.feature.tag.menu;

import lombok.AllArgsConstructor;
import me.emmy.core.api.menu.Button;
import me.emmy.core.api.menu.impl.BackButton;
import me.emmy.core.api.menu.pagination.PaginatedMenu;
import me.emmy.core.feature.tag.TagService;
import me.emmy.core.feature.tag.enums.EnumTagCategory;
import me.emmy.core.feature.tag.menu.button.TagButton;
import me.emmy.core.feature.tag.menu.button.TagCurrentButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
@AllArgsConstructor
public class TagMenu extends PaginatedMenu {
    private final EnumTagCategory category;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&3&lSelect Tag &8(&7" + this.category.getName() + "&8)";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);
        buttons.put(4, new BackButton(new TagCategoryMenu()));
        buttons.put(3, new TagCurrentButton());

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        TagService tagService = this.flash.getServiceRepository().getService(TagService.class);
        tagService.getTags().forEach(
                tag -> {
                    if (tag.getCategory() == this.category) {
                        buttons.put(buttons.size(), new TagButton(tag));
                    }
                }
        );

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public void onClose(Player player) {
        super.onClose(player);
    }
}