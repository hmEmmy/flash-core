package me.emmy.core;

import lombok.Getter;
import me.emmy.core.api.command.CommandFramework;
import me.emmy.core.api.menu.MenuListener;
import me.emmy.core.command.CommandService;
import me.emmy.core.config.ConfigService;
import me.emmy.core.database.mongo.MongoService;
import me.emmy.core.database.redis.RedisService;
import me.emmy.core.feature.chat.ChatService;
import me.emmy.core.feature.grant.GrantService;
import me.emmy.core.feature.rank.RankService;
import me.emmy.core.feature.tag.TagService;
import me.emmy.core.feature.world.WorldService;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.server.ServerProperty;
import me.emmy.core.service.ServiceRepository;
import me.emmy.core.util.Logger;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Flash extends JavaPlugin {

    @Getter
    private static Flash instance;
    private CommandFramework commandFramework;
    private ServiceRepository serviceRepository;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        instance = this;

        this.commandFramework = new CommandFramework(this);
        this.serviceRepository = new ServiceRepository(this);
        this.initializeServices();
        this.registerListeners();

        Logger.logStartupInfo(this, startTime);
    }

    @Override
    public void onDisable() {
        this.serviceRepository.close();
    }

    private void initializeServices() {
        this.serviceRepository.registerService(new ConfigService(this));
        this.serviceRepository.registerService(new ServerProperty(this));
        this.serviceRepository.registerService(new CommandService());
        this.serviceRepository.registerService(new MongoService(this));
        this.serviceRepository.registerService(new RedisService(this));
        this.serviceRepository.registerService(new RankService(this));
        this.serviceRepository.registerService(new TagService(this));
        this.serviceRepository.registerService(new ProfileService(this));
        this.serviceRepository.getService(ProfileService.class).loadProfiles();
        this.serviceRepository.registerService(new WorldService(this));
        this.serviceRepository.registerService(new GrantService(this));
        this.serviceRepository.registerService(new ChatService(this));
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }
}