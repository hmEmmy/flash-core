package me.emmy.core.feature.tag.enums;

import lombok.Getter;
import org.bukkit.Material;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
@Getter
public enum EnumTagCategory {
    TEXT("Text", "These are regular text tags.", Material.NAME_TAG, 0),
    EMOJI("Emoji", "These are emoji tags.", Material.PAPER, 0),
    PARTNER("Partner", "Tags from official partners.", Material.EMERALD, 0),
    EXCLUSIVE("Exclusive", "Only obtainable through special means.", Material.DIAMOND, 0),
    SPECIAL("Special", "Only given to certain players.", Material.GOLD_INGOT, 0),

    ;

    private final String name;
    private final String description;
    private final Material icon;
    private final int durability;

    /**
     * Constructor for EnumTagCategory.
     *
     * @param name the name of the tag category
     * @param description the description of the tag category
     * @param icon the icon of the tag category
     * @param durability the durability of the icon
     */
    EnumTagCategory(String name, String description, Material icon, int durability) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.durability = durability;
    }
}