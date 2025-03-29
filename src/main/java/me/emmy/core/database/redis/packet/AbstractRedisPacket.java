package me.emmy.core.database.redis.packet;

/**
 * @author Emmy
 * @project Flash
 * @since 29/03/2025
 */
public abstract class AbstractRedisPacket {
    public abstract void onSend();
    public abstract void onReceive();
}