package me.emmy.core.feature.grant.menu;

import lombok.AllArgsConstructor;
import me.emmy.core.api.menu.Button;
import me.emmy.core.api.menu.pagination.PaginatedMenu;
import me.emmy.core.feature.grant.menu.button.GrantButton;
import me.emmy.core.feature.rank.Rank;
import me.emmy.core.feature.rank.RankService;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
@AllArgsConstructor
public class GrantMenu extends PaginatedMenu {
    private final OfflinePlayer target;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&3&lGrant " + this.target.getName();
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Rank> sortedRanks = this.flash.getServiceRepository().getService(RankService.class).getRanks().stream()
                .filter(rank -> !rank.isDefaultRank())
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                .collect(Collectors.toList());

        for (int i = 0; i < sortedRanks.size(); i++) {
            Rank rank = sortedRanks.get(i);
            buttons.put(i, new GrantButton(this.target, rank));
        }

        return buttons;
    }
}