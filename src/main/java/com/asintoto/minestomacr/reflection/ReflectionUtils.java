package com.asintoto.minestomacr.reflection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtils {
    /**
     *
     * @param clazz
     * @return The package name of the given class
     */
    public static String getPackageName(Class<?> clazz) {
        return clazz.getPackage().getName();
    }

    /**
     * Find all classes and subclasses
     *
     * @param directory
     * @param packageName
     * @param classes
     */
    private static void findClasses(File directory, String packageName, Set<Class<?>> classes){
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                String subPackageName = packageName + "." + file.getName();
                findClasses(file, subPackageName, classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     *
     * @param packageName
     * @return All classes contained in a given package
     */
    public static Set<Class<?>> getClasses(String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = null;
        try {
            resources = classLoader.getResources(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.getFile());
            if (directory.exists()) {
                findClasses(directory, packageName, classes);
            }
        }

        return classes;
    }


    /**
     *
     * @param child
     * @param parent
     * @return Whether a class extends (directly or indirectly) another class.
     */
    public static boolean isChildClassOf(Class<?> child, Class<?> parent) {
        if(child == null) return false;
        if(child.getSuperclass() == parent) return true;
        return isChildClassOf(child.getSuperclass(), parent);
    }


    /**
     *
     * @return The class from which the project is started.
     */
    public static Class<?> getMainClass() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        try {
            return Class.forName(stackTrace[stackTrace.length - 1].getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }
    }
}
