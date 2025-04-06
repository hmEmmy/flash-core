package me.emmy.core.server;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 06/04/2025
 */
@Getter
@Setter
public class Server {
    private final String name;
    private String address;
    private int port;

    private List<Player> onlineStaff;
    private List<Player> onlinePlayers;

    private boolean whitelisted;
    private boolean online;

    /**
     * Constructor for the Server class.
     *
     * @param name The name of the server.
     * @param address The address of the server.
     * @param port The port of the server.
     */
    public Server(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.onlineStaff = new ArrayList<>();
        this.onlinePlayers = new ArrayList<>();
        this.whitelisted = false;
        this.online = false;
    }
}