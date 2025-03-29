package me.emmy.core.api.menu.pagination.impl.menu;

import me.emmy.core.api.menu.MenuUtil;
import me.emmy.core.api.menu.pagination.PaginatedMenu;
import me.emmy.core.api.menu.pagination.impl.button.JumpToPageButton;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.emmy.core.api.menu.Button;
import me.emmy.core.api.menu.Menu;
import me.emmy.core.api.menu.impl.BackButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ViewAllPagesMenu extends Menu {

    @NonNull
    PaginatedMenu menu;

    @Override
    public String getTitle(Player player) {
        return "&b&lSelect A Page";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int index = 10;
        for (int i = 1; i <= menu.getPages(player); i++) {
            buttons.put(index++, new JumpToPageButton(i, menu, menu.getPage() == i));

            if ((index - 8) % 9 == 0) {
                index += 2;
            }
        }

        this.addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

}
