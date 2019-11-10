/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(value={ElementType.METHOD, ElementType.TYPE})
@Retention(value=RetentionPolicy.CLASS)
public @interface Experimental {
}

