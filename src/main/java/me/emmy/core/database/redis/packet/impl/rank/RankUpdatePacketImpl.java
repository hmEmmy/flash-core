package me.emmy.core.database.redis.packet.impl.rank;

import me.emmy.core.Flash;
import me.emmy.core.database.redis.RedisUtility;
import me.emmy.core.database.redis.packet.AbstractRedisPacket;
import me.emmy.core.feature.rank.Rank;
import me.emmy.core.feature.rank.RankService;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class RankUpdatePacketImpl extends AbstractRedisPacket {
    private final Rank rank;

    /**
     * Constructor for the RankUpdatePacketImpl class.
     *
     * @param rank the rank to be updated
     */
    public RankUpdatePacketImpl(Rank rank) {
        this.rank = rank;
    }

    @Override
    public void onSend() {
        Flash.getInstance().getServiceRepository().getService(RankService.class).reloadRank(this.rank);
        this.sendMessage();
    }

    @Override
    public void onReceive() {
        Flash.getInstance().getServiceRepository().getService(RankService.class).reloadRank(this.rank);
        this.sendMessage();
    }

    @Override
    public void sendMessage() {
        RedisUtility.alert("&aThe &b" + this.rank.getName() + " &arank has gotten an update.", "flash.admin.packet.receive.rank");
    }
}