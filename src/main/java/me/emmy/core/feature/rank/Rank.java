package me.emmy.core.feature.rank;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
@Getter
@Setter
public class Rank {
    private final String name;
    private String description;
    private String prefix;
    private String suffix;

    private ChatColor color;

    private int weight;
    private int cost;

    private boolean defaultRank;
    private boolean staffRank;
    private boolean hiddenRank;
    private boolean purchasable;

    private List<String> permissions;
    private List<String> inheritance;

    /**
     * Constructor for the Rank class.
     *
     * @param name the name of the rank
     */
    public Rank(String name) {
        this.name = name;
        this.description = "";
        this.prefix = "";
        this.suffix = "";

        this.color = ChatColor.WHITE;

        this.weight = 0;
        this.cost = 0;

        this.defaultRank = false;
        this.staffRank = false;
        this.hiddenRank = false;
        this.purchasable = false;

        this.permissions = new ArrayList<>();
        this.inheritance = new ArrayList<>();
    }
}