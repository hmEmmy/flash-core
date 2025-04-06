package me.emmy.core.database.redis.packet.impl.server;

import com.google.gson.annotations.Expose;
import lombok.Setter;
import me.emmy.core.database.redis.packet.AbstractRedisPacket;
import me.emmy.core.server.Server;
import me.emmy.core.server.ServerRepository;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 06/04/2025
 */
public class ServerStatusPacketImpl extends AbstractRedisPacket {
    @Expose
    private final Server server;

    @Setter
    private transient ServerRepository serverRepository;

    /**
     * Constructor for the ServerStatusPacketImpl class.
     *
     * @param server The server instance.
     */
    public ServerStatusPacketImpl(Server server) {
        this.server = server;
    }

    @Override
    public void onSend() {
        this.serverRepository.addServer(this.server);
        this.sendMessage();
    }

    @Override
    public void onReceive() {
        this.serverRepository.addServer(this.server);
        this.sendMessage();
    }

    @Override
    public void sendMessage() {
        this.serverRepository.getServerMonitor().broadcastOnlineStatus(this.server.getName(), true);
    }
}