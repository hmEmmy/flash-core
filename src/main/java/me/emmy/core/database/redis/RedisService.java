package me.emmy.core.database.redis;

import com.google.gson.Gson;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.config.ConfigService;
import me.emmy.core.database.redis.packet.AbstractRedisPacket;
import me.emmy.core.util.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.function.Consumer;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
public class RedisService implements IService {
    protected final Flash plugin;
    private JedisPool jedisPool;
    private String channel;
    private Gson gson;

    /**
     * Constructor for the RedisService class.
     *
     * @param plugin  the plugin instance of Flash.
     */
    public RedisService(Flash plugin) {
        this.plugin = plugin;
        this.initialize();
    }

    @Override
    public void initialize() {
        FileConfiguration config = plugin.getServiceRepository().getService(ConfigService.class).getDatabaseConfig();

        String host = config.getString("database.redis.host");
        int port = config.getInt("database.redis.port");
        String password = config.getString("database.redis.password");

        this.channel = config.getString("database.redis.channel");
        this.gson = new Gson();
        this.jedisPool = new JedisPool(host, port);

        try (Jedis jedis = this.jedisPool.getResource()) {
            if (password.isEmpty()) {
                Logger.logInfo("No password provided for Redis. Proceeding without authentication.");
                return;
            }
            jedis.auth(password);
            Logger.logInfo("Successfully authenticated with Redis.");
        } catch (Exception exception) {
            Logger.logError("Failed to authenticate with Redis. Please check your password: " + exception.getMessage());
            return;
        }

        new Thread(this::subscribeToChannel).start();
    }

    @Override
    public void closure() {
        if (this.jedisPool == null) {
            return;
        }
        this.jedisPool.close();
        Logger.logInfo("Successfully closed Redis connection.");
    }

    /**
     * Subscribe to the Redis channel and handle incoming packets.
     */
    private void subscribeToChannel() {
        new Thread(() -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        try {
                            String[] parts = message.split("\\|\\|");
                            if (parts.length != 2) return;

                            Class<?> packetClass = Class.forName(parts[0]);
                            AbstractRedisPacket packet = (AbstractRedisPacket) gson.fromJson(parts[1], packetClass);

                            packet.onReceive();
                        } catch (Exception exception) {
                            Logger.logError("Failed to process incoming Redis packet: " + exception.getMessage());
                        }
                    }
                }, this.channel);
            }
        }).start();
    }

    /**
     * Sends a packet via Redis.
     *
     * @param packet the packet to send.
     */
    public void sendPacket(AbstractRedisPacket packet) {
        packet.onSend();
        String json = this.gson.toJson(packet);
        Logger.logInfo("Sending Redis Packet: " + json);

        new Thread(() -> this.process(jedis -> jedis.publish(this.channel, packet.getClass().getName() + "||" + json))).start();
    }


    /**
     * Execute a command using the Jedis connection pool.
     *
     * @param consumer the command to execute.
     */
    private void process(Consumer<Jedis> consumer) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            consumer.accept(jedis);
        }
    }
}