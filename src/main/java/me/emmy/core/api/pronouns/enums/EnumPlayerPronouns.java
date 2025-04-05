package me.emmy.core.api.pronouns.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project PronounsAPI
 * @since 05/04/2025
 */
@Getter
public enum EnumPlayerPronouns {
    FEMALE("Female", "she", "her", "hers", "she's"),
    MALE("Male", "he", "him", "his", "he's"),
    NOT_SPECIFIED("Not Specified", "they", "them", "theirs", "they're");

    ;

    private final String gender;
    private final String pronouns;
    private final String possessive;
    private final String possessivePronoun;
    private final String contractedPronoun;

    /**
     * Constructor for the EnumPlayerPronouns enum.
     *
     * @param gender the gender of the player
     * @param pronouns the pronouns of the player (e.g., "he", "she")
     * @param possessive the possessive form of the pronouns (e.g., "him", "her")
     * @param possessivePronoun the possessive pronoun (e.g., "his", "hers")
     * @param contractedPronoun the contracted form of the pronouns (e.g., "he's", "she's")
     */
    EnumPlayerPronouns(String gender, String pronouns, String possessive, String possessivePronoun, String contractedPronoun) {
        this.gender = gender;
        this.pronouns = pronouns;
        this.possessive = possessive;
        this.possessivePronoun = possessivePronoun;
        this.contractedPronoun = contractedPronoun;
    }
}