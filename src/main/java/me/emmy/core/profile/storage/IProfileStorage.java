package me.emmy.core.profile.storage;

import me.emmy.core.profile.Profile;

/**
 * @author Emmy
 * @project Flash
 * @since 29/03/2025
 */
public interface IProfileStorage {
    /**
     * Saves the given profile to the database/storage.
     *
     * @param profile the profile to save
     */
    void saveProfile(Profile profile);

    /**
     * Loads the profile from the database/storage.
     *
     * @param profile the profile to load
     */
    void loadProfile(Profile profile);
}