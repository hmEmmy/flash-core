package me.emmy.core.database.redis.packet.impl;

import lombok.*;
import me.emmy.core.database.redis.packet.AbstractRedisPacket;
import me.emmy.core.database.redis.packet.enums.EnumRankPacketType;
import me.emmy.core.feature.rank.Rank;
import me.emmy.core.feature.rank.RankService;
import me.emmy.core.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
@Builder
public class RankPacketImpl extends AbstractRedisPacket {
    private final String rankName;
    private final EnumRankPacketType packetType;

    private ChatColor color;

    private int intValue;
    private boolean booleanValue;
    private String content;

    @Override
    public void onSend() {
        switch (this.packetType) {
            case CREATE:
                this.createRank();
                break;
            case DELETE:
                this.performRankRemoval();
                break;
            case COLOR:
                this.performColorUpdate();
                break;
            case COST:
                this.performCostUpdate();
                break;
            case DEFAULT:
                this.performDefaultRankUpdate();
                break;
            case DESCRIPTION:
                this.performDescriptionUpdate();
                break;
        }
    }

    @Override
    public void onReceive() {
        switch (this.packetType) {
            case DELETE:
                this.performRankRemoval();
                break;
            case COLOR:
                this.performColorUpdate();
                break;
            case COST:
                this.performCostUpdate();
                break;
            case DEFAULT:
                this.performDefaultRankUpdate();
                break;
            case DESCRIPTION:
                this.performDescriptionUpdate();
                break;
        }
    }

    private void createRank() {
        Rank rank = new Rank(this.rankName);
        this.plugin.getServiceRepository().getService(RankService.class).createRank(rank);
        this.notifyPlayersWithPermission("&aThe rank &b" + rank.getName() + " &ahas been created!");
    }

    private void performRankRemoval() {
        RankService rankService = this.plugin.getServiceRepository().getService(RankService.class);
        Rank rankToDelete = rankService.getRank(this.rankName);
        if (rankToDelete == null) {
            return;
        }

        rankService.deleteRank(rankToDelete);
        this.notifyPlayersWithPermission("&aThe rank &b" + this.rankName + " &ahas been deleted!");
    }

    private void performColorUpdate() {
        RankService rankService = this.plugin.getServiceRepository().getService(RankService.class);
        Rank rankToUpdate = rankService.getRank(this.rankName);
        if (rankToUpdate == null) {
            return;
        }

        rankToUpdate.setColor(this.color);
        rankService.saveRank(rankToUpdate);
        this.notifyPlayersWithPermission("&aThe color of the rank &b" + rankToUpdate.getName() + " &ahas been set to &b" + this.color + this.color.name() + "&a!");
    }

    private void performCostUpdate() {
        RankService rankService = this.plugin.getServiceRepository().getService(RankService.class);
        Rank rankToUpdate = rankService.getRank(this.rankName);
        if (rankToUpdate == null) {
            return;
        }

        rankToUpdate.setCost(this.intValue);
        rankService.saveRank(rankToUpdate);
        this.notifyPlayersWithPermission("&aThe cost of the rank &b" + rankToUpdate.getName() + " &ahas been set to &b" + this.intValue + "&a!");
    }

    private void performDefaultRankUpdate() {
        RankService rankService = this.plugin.getServiceRepository().getService(RankService.class);
        Rank rankToUpdate = rankService.getRank(this.rankName);
        if (rankToUpdate == null) {
            return;
        }

        rankToUpdate.setDefaultRank(this.booleanValue);
        rankService.saveRank(rankToUpdate);
        this.notifyPlayersWithPermission("&aThe default rank has been set to &b" + rankToUpdate.getName() + "&a!");
    }

    private void performDescriptionUpdate() {
        RankService rankService = this.plugin.getServiceRepository().getService(RankService.class);
        Rank rankToUpdate = rankService.getRank(this.rankName);
        if (rankToUpdate == null) {
            return;
        }

        rankToUpdate.setDescription(this.content);
        rankService.saveRank(rankToUpdate);
        this.notifyPlayersWithPermission("&aThe description of the rank &b" + rankToUpdate.getName() + " &ahas been set to &b" + this.content + "&a!");
    }

    /**
     * Notifies players with the specified permission about the rank operation.
     *
     * @param message The message to send to players.
     */
    private void notifyPlayersWithPermission(String message) {
        List<Player> playersToNotify = Bukkit.getServer().getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(this.getReceivePermission() + ".rank"))
                .collect(Collectors.toList());

        for (Player player : playersToNotify) {
            player.sendMessage(this.getPrefix() + CC.translate(message));
        }
    }
}