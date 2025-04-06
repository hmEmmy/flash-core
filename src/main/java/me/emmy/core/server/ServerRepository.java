package me.emmy.core.server;

import lombok.Getter;
import lombok.SneakyThrows;
import me.emmy.core.Flash;
import me.emmy.core.database.redis.RedisService;
import me.emmy.core.database.redis.packet.impl.server.ServerStatusPacketImpl;
import me.emmy.core.server.property.ServerProperty;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 06/04/2025
 */
@Getter
public class ServerRepository {
    protected final Flash plugin;
    protected final ServerMonitor serverMonitor;
    private final List<Server> servers;

    /**
     * Constructor for the ServerRepository class.
     *
     * @param plugin The plugin instance of Flash.
     */
    @SneakyThrows
    public ServerRepository(Flash plugin) {
        this.plugin = plugin;
        this.serverMonitor = new ServerMonitor(plugin);
        this.servers = new ArrayList<>();

        String serverName = plugin.getServiceRepository().getService(ServerProperty.class).getName();
        int port = plugin.getServer().getPort();
        String address = InetAddress.getLocalHost().getHostAddress();

        this.createServer(serverName, address, port);
    }

    /**
     * Creates a new server with the specified name, IP, and port.
     *
     * @param name The name of the server.
     * @param address   The IP address of the server.
     * @param port The port of the server.
     */
    public void createServer(String name, String address, int port) {
        Server server = new Server(name, address, port);
        server.setWhitelisted(false);
        server.setOnline(true);

        ServerStatusPacketImpl packet = new ServerStatusPacketImpl(server);
        packet.setServerRepository(this);

        this.plugin.getServiceRepository().getService(RedisService.class).sendPacket(packet);
    }

    /**
     * Adds a server to the list of servers.
     *
     * @param server The server to be added.
     */
    public void addServer(Server server) {
        this.servers.add(server);
    }

    /**
     * Removes a server from the list of servers.
     *
     * @param server The server to be removed.
     */
    public void removeServer(Server server) {
        this.servers.remove(server);
    }

    /**
     * Retrieves a server by its name.
     *
     * @param name The name of the server to be retrieved.
     * @return The server with the specified name, or null if not found.
     */
    public Server getServerByName(String name) {
        return this.servers.stream()
                .filter(server -> server.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}