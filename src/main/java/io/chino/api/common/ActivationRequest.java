package io.chino.api.common;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

public abstract class ActivationRequest {
    private boolean mustActivate = false;

    /**
     * This method is invoked in the {@code update(boolean, ...)} API call.<br>
     * It will set {@code "is_active": true} in the update request.
     */
    public void activateResource() {
        mustActivate = true;
    }

    /**
     * This method is invoked during serialization with {@link com.fasterxml.jackson.databind.ObjectMapper}.
     * If {@link #activateResource()} was called, the serializer will receive "true" and set {@code "is_active": true},
     * otherwise it will receive "null" and ignore this parameter.
     *
     * @return {@code true} if {@link #activateResource()} was called on this {@link ActivationRequest},
     * otherwise {@code null}
     */
    @JsonGetter("is_active")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean isActive() {
        return mustActivate ? true : null;
    }

    public void resetActivationStatus() {
        mustActivate = false;
    }
}
