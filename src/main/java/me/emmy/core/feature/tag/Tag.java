package me.emmy.core.feature.tag;

import lombok.Getter;
import lombok.Setter;
import me.emmy.core.feature.tag.enums.EnumTagCategory;
import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
@Getter
@Setter
public class Tag {
    private final String name;
    private String appearance;
    private String description;

    private EnumTagCategory category;

    private ChatColor color;

    private Material icon;
    private int durability;

    private boolean purchasable;

    private int cost;

    /**
     * Constructor for the Tag class.
     *
     * @param name the name of the tag
     * @param appearance the appearance of the tag
     */
    public Tag(String name, String appearance) {
        this.name = name;
        this.appearance = appearance;
        this.description = "";
        this.category = EnumTagCategory.TEXT;
        this.color = ChatColor.WHITE;
        this.icon = Material.NAME_TAG;
        this.durability = 0;
        this.purchasable = false;
        this.cost = 0;
    }
}