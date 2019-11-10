/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.util;

import java.lang.reflect.Method;

@Deprecated
public final class ExceptionUtils {
    private static final Method INIT_CAUSE_METHOD = ExceptionUtils.getInitCauseMethod();

    private static Method getInitCauseMethod() {
        try {
            Class[] paramsClasses = new Class[]{Throwable.class};
            return Throwable.class.getMethod("initCause", paramsClasses);
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static void initCause(Throwable throwable, Throwable cause) {
        if (INIT_CAUSE_METHOD == null) return;
        try {
            INIT_CAUSE_METHOD.invoke(throwable, cause);
            return;
        }
        catch (Exception e) {
            // empty catch block
        }
    }

    private ExceptionUtils() {
    }
}

