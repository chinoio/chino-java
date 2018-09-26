package io.chino.api.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that is used to mark indexed fields when passing
 * a {@link Class} as structure for a new {@link io.chino.api.userschema.UserSchema UserSchema}
 * or {@link io.chino.api.schema.Schema Schema}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface indexed {}
