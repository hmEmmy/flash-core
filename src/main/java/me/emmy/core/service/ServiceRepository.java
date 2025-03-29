package me.emmy.core.service;

import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.command.CommandService;
import me.emmy.core.config.ConfigService;
import me.emmy.core.database.mongo.MongoService;
import me.emmy.core.database.redis.RedisService;
import me.emmy.core.feature.rank.RankService;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.util.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 26/03/2025
 */
@Getter
public class ServiceRepository {
    private final Flash plugin;
    private final Map<Class<? extends IService>, IService> services;

    /**
     * Constructor for ServiceRepository.
     *
     * @param plugin the main class of the plugin
     */
    public ServiceRepository(Flash plugin) {
        this.plugin = plugin;
        this.services = new LinkedHashMap<>();
    }

    public void close() {
        this.getService(ConfigService.class).closure();
        this.getService(CommandService.class).closure();
        this.getService(RedisService.class).closure();
        this.getService(ProfileService.class).closure();
        this.getService(RankService.class).closure();
        this.getService(MongoService.class).closure();
    }

    /**
     * Retrieves a service by its class.
     *
     * @param clazz the class of the service
     * @return the service
     */
    public <T extends IService> T getService(Class<T> clazz) {
        return clazz.cast(this.services.get(clazz));
    }

    /**
     * Registers a service and logs the time taken to register it.
     *
     * @param taskName the name of the task
     * @param service the service to register
     */
    public void registerService(String taskName, IService service) {
        long start = System.currentTimeMillis();
        this.services.put(service.getClass(), service);
        long end = System.currentTimeMillis();
        Logger.logInfo("Registered " + taskName + " in " + (end - start) + "ms.");
    }
}