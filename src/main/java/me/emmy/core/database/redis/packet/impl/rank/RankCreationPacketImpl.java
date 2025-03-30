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
public class RankCreationPacketImpl extends AbstractRedisPacket {
    private final Rank rank;

    /**
     * Constructor for the RankCreationPacketImpl class.
     *
     * @param rank the newly created rank
     */
    public RankCreationPacketImpl(Rank rank) {
        this.rank = rank;
    }

    @Override
    public void onSend() {
        Flash.getInstance().getServiceRepository().getService(RankService.class).createRank(this.rank);
        this.sendMessage();
    }

    @Override
    public void onReceive() {
        Flash.getInstance().getServiceRepository().getService(RankService.class).createRank(this.rank);
        this.sendMessage();
    }

    @Override
    public void sendMessage() {
        RedisUtility.alert("&aA new rank called &b" + this.rank.getName() + " &ahas been created!", "flash.admin.packet.receive.rank");
    }
}