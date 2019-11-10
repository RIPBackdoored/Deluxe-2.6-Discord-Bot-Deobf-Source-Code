/*
 * Decompiled with CFR <Could not determine version>.
 * 
 * Could not load the following classes:
 *  javax.servlet.ServletContextEvent
 *  javax.servlet.ServletContextListener
 */
package org.apache.commons.logging.impl;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.LogFactory;

public class ServletContextCleaner
implements ServletContextListener {
    private static final Class[] RELEASE_SIGNATURE = new Class[]{class$java$lang$ClassLoader == null ? (class$java$lang$ClassLoader = ServletContextCleaner.class$("java.lang.ClassLoader")) : class$java$lang$ClassLoader};
    static /* synthetic */ Class class$java$lang$ClassLoader;

    public void contextDestroyed(ServletContextEvent sce) {
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        Object[] params = new Object[]{tccl};
        ClassLoader loader = tccl;
        do {
            if (loader == null) {
                LogFactory.release(tccl);
                return;
            }
            try {
                Class<?> logFactoryClass = loader.loadClass("org.apache.commons.logging.LogFactory");
                Method releaseMethod = logFactoryClass.getMethod("release", RELEASE_SIGNATURE);
                releaseMethod.invoke(null, params);
                loader = logFactoryClass.getClassLoader().getParent();
            }
            catch (ClassNotFoundException ex) {
                loader = null;
            }
            catch (NoSuchMethodException ex) {
                System.err.println("LogFactory instance found which does not support release method!");
                loader = null;
            }
            catch (IllegalAccessException ex) {
                System.err.println("LogFactory instance found which is not accessable!");
                loader = null;
            }
            catch (InvocationTargetException ex) {
                System.err.println("LogFactory instance release method failed!");
                loader = null;
                continue;
            }
            break;
        } while (true);
    }

    public void contextInitialized(ServletContextEvent sce) {
    }

    static /* synthetic */ Class class$(String x0) {
        try {
            return Class.forName(x0);
        }
        catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}

