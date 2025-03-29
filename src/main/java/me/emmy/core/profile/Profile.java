package me.emmy.core.profile;

import lombok.Getter;
import lombok.Setter;
import me.emmy.core.Flash;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * @author Emmy
 * @project Flash
 * @since 29/03/2025
 */
@Getter
@Setter
public class Profile {
    private UUID uuid;

    private String username;
    private String tag;

    private boolean online;

    private int coins;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.username = Bukkit.getOfflinePlayer(this.uuid).getName();
        this.tag = "";
        this.online = false;
        this.coins = 0;
    }

    public void loadProfile() {
        Flash.getInstance().getServiceRepository().getService(ProfileService.class).getProfileStorage().loadProfile(this);
    }

    public void saveProfile() {
        Flash.getInstance().getServiceRepository().getService(ProfileService.class).getProfileStorage().saveProfile(this);
    }
}