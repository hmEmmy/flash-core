package me.emmy.core.feature.world;

import lombok.var;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.feature.world.generator.VoidChunkGeneratorImpl;
import me.emmy.core.server.ServerProperty;
import me.emmy.core.util.CC;
import me.emmy.core.util.Logger;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class WorldService implements IService {
    protected final Flash plugin;
    private final Server server;

    /**
     * Constructor for WorldService.
     *
     * @param plugin the Flash plugin instance.
     */
    public WorldService(Flash plugin) {
        this.plugin = plugin;
        this.server = this.plugin.getServer();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void closure() {

    }

    /**
     * Creates a new world with the specified name, environment, and type.
     *
     * @param name        the name of the world
     * @param environment the environment of the world (e.g., NORMAL, NETHER, THE_END)
     * @param type        the type of the world (e.g., DEFAULT, FLAT, AMPLIFIED)
     */
    public void createWorld(String name, World.Environment environment, WorldType type) {
        if (this.server.getWorld(name) != null) {
            Logger.logInfo("World " + name + " already exists!");
            return;
        }

        WorldCreator creator = new WorldCreator(name)
                .environment(environment)
                .type(type);

        this.server.createWorld(creator);
        Logger.logInfo("Created world: " + name);
    }

    /**
     * Creates a void world with the specified name and environment.
     *
     * @param name        the name of the world
     */
    public void createVoidWorld(String name) {
        if (this.server.getWorld(name) != null) {
            Logger.logInfo("World " + name + " already exists!");
            return;
        }

        WorldCreator creator = new WorldCreator(name)
                .environment(World.Environment.NORMAL)
                .generator(new VoidChunkGeneratorImpl());

        this.server.createWorld(creator);
        Logger.logInfo("Created void world: " + name);
    }

    /**
     * Deletes a world with the specified name.
     *
     * @param name        the name of the world to delete
     * @param deleteFiles whether to delete the world files from the server
     */
    public void deleteWorld(String name, boolean deleteFiles) {
        World world = this.server.getWorld(name);
        if (world == null) {
            Logger.logError("World " + name + " does not exist.");
            return;
        }

        World fallbackWorld = this.server.getWorlds().get(0);
        if (fallbackWorld == null) {
            world.getPlayers().forEach(player ->
                    player.kickPlayer(CC.translate("&cThe world you were in has been deleted! Please rejoin!"))
            );
        } else {
            world.getPlayers().forEach(player -> {
                player.teleport(fallbackWorld.getSpawnLocation());
                player.sendMessage(CC.translate("&cThe world you were in has been deleted! Sending you to the fallback world."));
            });
        }

        this.server.unloadWorld(world, false);
        Logger.logInfo("Unloaded world: " + name);

        if (deleteFiles) {
            File worldFolder = world.getWorldFolder();
            try (var paths = Files.walk(Paths.get(worldFolder.getPath()))) {
                paths.sorted((o1, o2) -> -o1.compareTo(o2))
                        .map(Path::toFile)
                        .forEach(file -> {
                            if (!file.delete()) {
                                Logger.logError("Failed to delete: " + file.getPath());
                            }
                        });
                Logger.logInfo("Deleted world files: " + name);
            } catch (Exception exception) {
                Logger.logError("Error deleting world files: " + exception.getMessage());
            }
        }
    }

    /**
     * Teleports a player to the spawn location of a specified world.
     *
     * @param player    the player to teleport
     * @param worldName the name of the world to teleport to
     */
    public void teleportPlayer(Player player, String worldName) {
        World world = this.server.getWorld(worldName);
        if (world == null) {
            player.sendMessage(CC.translate("&cWorld " + worldName + " does not exist!"));
            return;
        }

        ServerProperty serverProperty = this.plugin.getServiceRepository().getService(ServerProperty.class);
        Location worldSpawn = world.getSpawnLocation();

        player.teleport(serverProperty.getSpawn() == null ? worldSpawn : serverProperty.getSpawn());
    }
}