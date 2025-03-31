package me.emmy.core.profile.data;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
@Getter
@Setter
public class PermissionData {
    private List<String> personalPermissions;
    private List<String> rankPermissions;

    public PermissionData() {
        this.personalPermissions = new ArrayList<>();
        this.rankPermissions = new ArrayList<>();
    }

    /**
     * Checks if the player has a specific permission.
     *
     * @param permission The permission to check.
     * @return true if the player has the permission, false otherwise.
     */
    public boolean hasPermission(String permission) {
        return this.personalPermissions.contains(permission);
    }

    /**
     * Checks if the player has a specific rank permission.
     *
     * @param permission The rank permission to check.
     */
    public void addPermission(String permission) {
        this.personalPermissions.add(permission);
    }

    /**
     * Removes a specific permission from the player's permissions.
     *
     * @param permission The permission to remove.
     */
    public void removePermission(String permission) {
        this.personalPermissions.remove(permission);
    }

    /**
     * Adds a specific rank permission to the player's rank permissions.
     *
     * @param permission The rank permission to add.
     */
    public void addRankPermission(String permission) {
        this.rankPermissions.add(permission);
    }

    /**
     * Removes a specific rank permission from the player's rank permissions.
     *
     * @param permission The rank permission to remove.
     */
    public void removeRankPermission(String permission) {
        this.rankPermissions.remove(permission);
    }
}