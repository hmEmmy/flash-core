package me.emmy.core.feature.chat;

import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
public class ChatService implements IService {
    protected final Flash plugin;

    /**
     * Constructor for ChatService.
     *
     * @param plugin The plugin instance of Flash.
     */
    public ChatService(Flash plugin) {
        this.plugin = plugin;
        this.initialize();
    }

    @Override
    public void initialize() {
        this.plugin.getServer().getPluginManager().registerEvents(new ChatListener(this.plugin), this.plugin);
    }

    @Override
    public void closure() {

    }
}
