package me.emmy.core.profile.storage.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.emmy.core.Flash;
import me.emmy.core.api.pronouns.enums.EnumPlayerPronouns;
import me.emmy.core.feature.grant.Grant;
import me.emmy.core.feature.punishment.Punishment;
import me.emmy.core.feature.punishment.enums.EnumPunishmentType;
import me.emmy.core.feature.rank.RankService;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.profile.data.PermissionData;
import me.emmy.core.profile.storage.IProfileStorage;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
public class MongoProfileStorageImpl implements IProfileStorage {
    protected final Flash plugin;

    /**
     * Constructor for the MongoProfileStorageImpl class.
     *
     * @param plugin the plugin instance of Flash.
     */
    public MongoProfileStorageImpl(Flash plugin) {
        this.plugin = plugin;
    }

    /**
     * Saves the given profile to the database/storage.
     *
     * @param profile the profile to save
     */
    @Override
    public void saveProfile(Profile profile) {
        Document document = new Document();
        document.put("uuid", profile.getUuid().toString());
        document.put("username", profile.getUsername());
        document.put("coins", profile.getCoins());
        document.put("tag", profile.getTag());
        document.put("gender", profile.getGender().name());

        this.storeGrants(profile, document);
        this.storePermissions(profile, document);
        this.storePunishments(profile, document);

        this.plugin.getServiceRepository().getService(ProfileService.class).getCollection().replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

    /**
     * Loads the profile from the database/storage.
     *
     * @param profile the profile to load
     */
    @Override
    public void loadProfile(Profile profile) {
        Document document = this.plugin.getServiceRepository().getService(ProfileService.class).getCollection().find(Filters.eq("uuid", profile.getUuid().toString())).first();
        if (document == null) {
            this.saveProfile(profile);
            return;
        }

        profile.setUsername(document.getString("username"));
        profile.setCoins(document.containsKey("coins") ? document.getInteger("coins") : 0);
        profile.setTag(document.getString("tag") == null ? "" : document.getString("tag"));
        profile.setGender(EnumPlayerPronouns.valueOf(document.getString("gender")));

        this.setGrants(profile, document);
        this.setPermissions(profile, document);
        this.setPunishments(profile, document);
    }

    /**
     * Stores the grants for the profile in the document.
     *
     * @param profile  the profile to store grants for
     * @param document the document to store the grants in
     */
    private void storeGrants(Profile profile, Document document) {
        List<Document> grants = new ArrayList<>();
        RankService rankService = this.plugin.getServiceRepository().getService(RankService.class);
        for (Grant grant : profile.getGrants()) {
            if (rankService.getRank(grant.getRankName()) == null) {
                continue;
            }

            Document grantDoc = new Document()
                    .append("rank", grant.getRankName())
                    .append("server", grant.getServer())
                    .append("issuer", grant.getIssuer())
                    .append("reason", grant.getReason())
                    .append("remover", grant.getRemover())
                    .append("removalReason", grant.getRemovalReason())
                    .append("duration", grant.getDuration())
                    .append("addedAt", grant.getAddedAt())
                    .append("removedAt", grant.getRemovedAt())
                    .append("permanent", grant.isPermanent())
                    .append("active", grant.isActive());
            grants.add(grantDoc);
        }
        document.put("grants", grants);
    }

    /**
     * Stores the permissions for the profile in the document.
     *
     * @param profile  the profile to store permissions for
     * @param document the document to store the permissions in
     */
    private void storePermissions(Profile profile, Document document) {
        PermissionData permissionData = profile.getPermissionData();
        document.put("personalPermissions", permissionData.getPersonalPermissions());
        document.put("rankPermissions", permissionData.getRankPermissions());
    }

    /**
     * Stores the punishments for the profile in the document.
     *
     * @param profile  the profile to store punishments for
     * @param document the document to store the punishments in
     */
    private void storePunishments(Profile profile, Document document) {
        List<Document> punishmentDocs = new ArrayList<>();
        for (Punishment punishment : profile.getPunishments()) {
            Document punishmentDoc = new Document()
                    .append("type", punishment.getType().name())
                    .append("issuer", punishment.getIssuer())
                    .append("server", punishment.getServer())
                    .append("reason", punishment.getReason())
                    .append("remover", punishment.getRemover())
                    .append("removalReason", punishment.getRemovalReason())
                    .append("duration", punishment.getDuration())
                    .append("addedAt", punishment.getAddedAt())
                    .append("removedAt", punishment.getRemovedAt())
                    .append("permanent", punishment.isPermanent())
                    .append("silent", punishment.isSilent())
                    .append("active", punishment.isActive());
            punishmentDocs.add(punishmentDoc);
        }
        document.put("punishments", punishmentDocs);
    }

    /**
     * Sets the grants for the profile from the document.
     *
     * @param profile  the profile to set grants for
     * @param document the document containing grant data
     */
    @SuppressWarnings("unchecked")
    private void setGrants(Profile profile, Document document) {
        List<Grant> grants = new ArrayList<>();
        List<Document> grantDocs = (List<Document>) document.get("grants", List.class);
        RankService rankService = this.plugin.getServiceRepository().getService(RankService.class);

        if (grantDocs != null) {
            for (Document grantDoc : grantDocs) {
                String rankName = grantDoc.getString("rank");
                if (rankService.getRank(rankName) == null) {
                    continue;
                }

                Grant grant = new Grant();
                grant.setRankName(grantDoc.getString("rank"));
                grant.setServer(grantDoc.getString("server"));
                grant.setIssuer(grantDoc.getString("issuer"));
                grant.setReason(grantDoc.getString("reason"));
                grant.setRemover(grantDoc.getString("remover"));
                grant.setRemovalReason(grantDoc.getString("removalReason"));
                grant.setDuration(grantDoc.getLong("duration"));
                grant.setAddedAt(grantDoc.getLong("addedAt"));
                grant.setRemovedAt(grantDoc.getLong("removedAt"));
                grant.setPermanent(grantDoc.getBoolean("permanent"));
                grant.setActive(grantDoc.getBoolean("active"));
                grants.add(grant);
            }
        }
        profile.setGrants(grants);
    }

    /**
     * Sets the permissions for the profile from the document.
     *
     * @param profile  the profile to set permissions for
     * @param document the document containing permission data
     */
    @SuppressWarnings("unchecked")
    private void setPermissions(Profile profile, Document document) {
        PermissionData permissionData = new PermissionData();
        List<String> personalPermissions = (List<String>) document.get("personalPermissions", List.class);
        List<String> rankPermissions = (List<String>) document.get("rankPermissions", List.class);

        if (personalPermissions != null) {
            permissionData.getPersonalPermissions().addAll(personalPermissions);
        }
        if (rankPermissions != null) {
            permissionData.getRankPermissions().addAll(rankPermissions);
        }
        profile.setPermissionData(permissionData);
    }

    /**
     * Sets the punishments for the profile from the document.
     *
     * @param profile  the profile to set punishments for
     * @param document the document containing punishment data
     */
    @SuppressWarnings("unchecked")
    private void setPunishments(Profile profile, Document document) {
        List<Punishment> punishments = new ArrayList<>();
        List<Document> punishmentDocs = (List<Document>) document.get("punishments", List.class);

        if (punishmentDocs != null) {
            for (Document punishmentDoc : punishmentDocs) {
                Punishment punishment = new Punishment();
                punishment.setType(EnumPunishmentType.valueOf(punishmentDoc.getString("type")));
                punishment.setIssuer(punishmentDoc.getString("issuer"));
                punishment.setServer(punishmentDoc.getString("server"));
                punishment.setReason(punishmentDoc.getString("reason"));
                punishment.setRemover(punishmentDoc.getString("remover"));
                punishment.setRemovalReason(punishmentDoc.getString("removalReason"));
                punishment.setDuration(punishmentDoc.getLong("duration"));
                punishment.setAddedAt(punishmentDoc.getLong("addedAt"));
                punishment.setRemovedAt(punishmentDoc.getLong("removedAt"));
                punishment.setPermanent(punishmentDoc.getBoolean("permanent"));
                punishment.setSilent(punishmentDoc.getBoolean("silent"));
                punishment.setActive(punishmentDoc.getBoolean("active"));
                punishments.add(punishment);
            }
        }
        profile.setPunishments(punishments);
    }
}