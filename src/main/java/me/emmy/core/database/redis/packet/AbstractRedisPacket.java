package me.emmy.core.database.redis.packet;

import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.util.CC;
import me.emmy.core.util.ServerProperty;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
@Getter
public abstract class AbstractRedisPacket {
    protected final Flash plugin;
    private final String receivePermission;
    private final String prefix;

    public AbstractRedisPacket() {
        this.plugin = Flash.getInstance();
        this.receivePermission = "flash.admin.packet";
        this.prefix = CC.translate("&3(" + ServerProperty.serverName + ") &r");
    }

    public abstract void onSend();
    public abstract void onReceive();
}