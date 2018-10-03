package io.chino.api.application;

/**
 * The types of Applications that are available.
 * @author Andrea Arighi [andrea@chino.io]
 */
public enum ClientType {
    /**
     * Identifies a "public" client, which cannot hold a "app_secret".
     */
    PUBLIC,
    /**
     * Identifies a "confidential" client, which can hold securely an "app_secret".
     */
    CONFIDENTIAL
}
