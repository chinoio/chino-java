package io.chino.api.application;

/**
 * The types of {@link Application Applications} that are available.
 * The values of this enum represent the types of clients that can be used for authentication
 * in the OAuth2 "client credentials" workflow.
 *
 * @author Andrea Arighi [andrea@chino.io]
 */
public enum ClientType {
    /**
     * Identifies a "public" client, which cannot hold a "app_secret". This Client Type
     * identifies software that runs on a client platform (e.g. a mobile phone or a web browser).
     */
    PUBLIC,
    /**
     * Identifies a "confidential" client, which can hold securely an "app_secret".
     * This Client Type may be used only to identify trusted software that runs on a backend.
     * If your code is meant to be distributed, you should use {@link #PUBLIC} instead.
     */
    CONFIDENTIAL;

    /**
     * @return this {@link ClientType} as a String
     */
    public String toString() {
        return this.name().toLowerCase();
    }
}
