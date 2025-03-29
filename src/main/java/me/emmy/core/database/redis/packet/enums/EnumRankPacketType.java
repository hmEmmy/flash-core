package me.emmy.core.database.redis.packet.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
@Getter
public enum EnumRankPacketType {
    CREATE,
    DELETE,
    COLOR,
    COST,
    DEFAULT,
    DESCRIPTION,
    HIDDEN,
    PREFIX,
    PURCHASABLE,
    STAFF,
    SUFFIX,
    WEIGHT,
    PERMISSION,
    INHERITANCE;
}