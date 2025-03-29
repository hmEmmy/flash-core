package me.emmy.core;

import lombok.Getter;
import me.emmy.core.api.command.CommandFramework;
import me.emmy.core.service.ServiceRepository;
import me.emmy.core.command.CommandService;
import me.emmy.core.config.ConfigService;
import me.emmy.core.database.mongo.MongoService;
import me.emmy.core.database.redis.RedisService;
import me.emmy.core.profile.ProfileService;
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

        Logger.logStartupInfo(this, startTime);
    }

    @Override
    public void onDisable() {
        this.serviceRepository.runSaveMethods();
    }

    private void initializeServices() {
        this.serviceRepository.registerAndMeasureService(ConfigService.class.getSimpleName(), new ConfigService(this));
        this.serviceRepository.registerAndMeasureService(CommandService.class.getSimpleName(), new CommandService());
        this.serviceRepository.registerAndMeasureService(MongoService.class.getSimpleName(), new MongoService(this));
        this.serviceRepository.registerAndMeasureService(RedisService.class.getSimpleName(), new RedisService(this));
        this.serviceRepository.registerAndMeasureService(ProfileService.class.getSimpleName(), new ProfileService(this));
    }
}
