package me.emmy.core.api.menu.pagination.impl.menu;

import me.emmy.core.api.menu.pagination.PageFilter;
import me.emmy.core.api.menu.pagination.PaginatedMenu;
import me.emmy.core.api.menu.pagination.impl.button.PageFilterButton;
import lombok.Getter;
import lombok.Setter;
import me.emmy.core.api.menu.Button;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class FilterablePaginatedMenu<T> extends PaginatedMenu {
    private final List<PageFilter<T>> filters;
    private int scrollIndex;

    {
        this.filters = this.generateFilters();
        this.scrollIndex = 0;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(7, new PageFilterButton<>(this));
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        return getFilteredButtons(player);
    }

    public abstract Map<Integer, Button> getFilteredButtons(Player player);

    public List<PageFilter<T>> generateFilters() {
        return new ArrayList<>();
    }
}