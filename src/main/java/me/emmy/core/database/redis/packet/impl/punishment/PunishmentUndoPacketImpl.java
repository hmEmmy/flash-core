package me.emmy.core.database.redis.packet.impl.punishment;

import me.emmy.core.database.redis.RedisUtility;
import me.emmy.core.database.redis.packet.AbstractRedisPacket;
import me.emmy.core.feature.punishment.Punishment;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 06/04/2025
 */
public class PunishmentUndoPacketImpl extends AbstractRedisPacket {
    private final Punishment punishment;
    private final String target;

    /**
     * Constructor for the PunishmentUndoPacketImpl class.
     *
     * @param punishment the punishment object
     * @param target     the target of the punishment
     */
    public PunishmentUndoPacketImpl(Punishment punishment, String target) {
        this.punishment = punishment;
        this.target = target;
    }

    @Override
    public void onSend() {
        this.sendMessage();
    }

    @Override
    public void onReceive() {
        this.sendMessage();
    }

    @Override
    public void sendMessage() {
        RedisUtility.alertClickableList(
                Arrays.asList(
                        "",
                        "&7&m--------------------------------------",
                        "&c&l" + this.punishment.getType().getPardonActionUpperCase() + (this.punishment.isRemovalSilent() ? " &7(SILENT)" : ""),
                        " &4" + this.punishment.getIssuer() + " &fhas pardoned &4" + this.target + "&f!",
                        "&7&m--------------------------------------",
                        ""
                ),
                "flash.admin.packet.receive.punishment",
                "&4&lPunishment Pardon Details" + "\n" +
                        " &4● &fTarget: &c" + this.target + "\n" +
                        " &4● &fIssued by: &c" + this.punishment.getIssuer() + "\n" +
                        " &4● &fRemoval Reason: &c" + this.punishment.getRemovalReason() + "\n" +
                        " &4● &fSilently removed: &c" + (this.punishment.isRemovalSilent() ? "Yes" : "No")
        );

        if (!this.punishment.isSilent()) {
            RedisUtility.alertPubliclyWithoutPrefix(
                    "&a" + this.punishment.getIssuer() + " has &c&l" + this.punishment.getType().getPardonActionUpperCase() + " &a" + this.target + "."
            );
        }
    }
}
