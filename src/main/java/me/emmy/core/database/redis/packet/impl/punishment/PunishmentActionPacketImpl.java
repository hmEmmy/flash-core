package me.emmy.core.database.redis.packet.impl.punishment;

import me.emmy.core.database.redis.RedisUtility;
import me.emmy.core.database.redis.packet.AbstractRedisPacket;
import me.emmy.core.feature.punishment.Punishment;
import me.emmy.core.util.DateUtils;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 05/04/2025
 */
public class PunishmentActionPacketImpl extends AbstractRedisPacket {
    private final Punishment punishment;
    private final String target;

    /**
     * Constructor for the PunishmentActionPacketImpl class.
     *
     * @param punishment the punishment object
     * @param target     the target of the punishment
     */
    public PunishmentActionPacketImpl(Punishment punishment, String target) {
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
        String punishedFor = this.punishment.isPermanent() ? "&c&lPERMANENTLY " + this.punishment.getType().getAction().toUpperCase() : "&c&l" + this.punishment.getType().getAction().toUpperCase() + " FOR " + DateUtils.formatTimeMillis(punishment.getDuration()).toUpperCase();
        RedisUtility.alertClickableList(
                Arrays.asList(
                        "",
                        "&7&m--------------------------------------",
                        punishedFor + (this.punishment.isSilent() ? " &7(SILENT)" : ""),
                        " &4" + this.punishment.getIssuer() + " &fhas issued a punishment on &4" + this.target + "&f!",
                        "&7&m--------------------------------------",
                        ""
                ),
                "flash.admin.packet.receive.punishment",
                "&4&lPunishment Details" + "\n" +
                        " &4● &fTarget: &c" + this.target + "\n" +
                        " &4● &fIssued by: &c" + this.punishment.getIssuer() + "\n" +
                        " &4● &fReason: &c" + this.punishment.getReason() + "\n" +
                        " &4● &fDuration: &c" + (this.punishment.getDuration() == -1 ? "Permanent" : DateUtils.formatTimeMillis(this.punishment.getDuration())) + "\n" +
                        " &4● &fIssued server: &c" + this.punishment.getServer()
        );

        if (!this.punishment.isSilent()) {
            RedisUtility.alertPubliclyWithoutPrefix(
                    "&a" + this.punishment.getIssuer() + " has &c&l" + this.punishment.getType().getAction().toUpperCase() + " &a" + this.target + "."
            );
        }
    }
}
