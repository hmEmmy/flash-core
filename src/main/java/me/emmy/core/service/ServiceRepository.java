package me.emmy.core.service;

import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.util.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Flash
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

    public void runSaveMethods() {
        this.services.values().forEach(IService::save);
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
    public void registerAndMeasureService(String taskName, IService service) {
        long start = System.currentTimeMillis();
        this.services.put(service.getClass(), service);
        long end = System.currentTimeMillis();
        Logger.logInfo("Registered " + taskName + " in " + (end - start) + "ms.");
    }
}