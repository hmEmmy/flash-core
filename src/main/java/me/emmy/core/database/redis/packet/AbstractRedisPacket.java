package me.emmy.core.database.redis.packet;

import lombok.Getter;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
@Getter
public abstract class AbstractRedisPacket {
    public abstract void onSend();
    public abstract void onReceive();
    public abstract void sendMessage();
}