package com.asintoto.minestomacr;

import com.asintoto.minestomacr.annotations.AutoRegister;
import com.asintoto.minestomacr.annotations.DontRegister;
import com.asintoto.minestomacr.reflection.ReflectionUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MinestomACR {

    private static final Logger logger = LoggerFactory.getLogger(MinestomACR.class);

    /**
     * Initiate Automatic Command Registration.
     * Loop through all classes in the project to find valid classes.
     *
     * A command can be registered only if the class extends {@link Command}
     *
     * @param inverse If "false" only the class annotated with {@link AutoRegister} will be registered.
     *               If "true" every class extending {@link Command} will be registered unless they are annotated with {@link DontRegister}
     */
    public static void init(boolean inverse) {
        registerAll(inverse);
    }

    /**
     * Initiate Automatic Command Registration.
     * Loop through all classes in the project to find valid classes.
     *
     * A command can be registered only if the class extends {@link Command} and it is annotated with {@link AutoRegister}
     */
    public static void init() {
        init(false);
    }


    /**
     * The actual method to register the all commands.
     *
     * @param inverse
     */
    private static void registerAll(boolean inverse) {
        for (Class<?> clazz : ReflectionUtils.getClasses(ReflectionUtils.getPackageName(ReflectionUtils.getMainClass()))) {

            if(inverse) {
                if(!clazz.isAnnotationPresent(DontRegister.class))  {
                    registerClass(clazz);
                }
            } else {
                if(clazz.isAnnotationPresent(AutoRegister.class)) {
                    registerClass(clazz);
                }
            }
        }
    }


    /**
     * Register a class if it extends {@link Command}
     *
     * @param clazz
     */
    private static void registerClass(Class<?> clazz) {
        if (ReflectionUtils.isChildClassOf(clazz, Command.class)) {
            try {
                Constructor constructor = clazz.getConstructor();
                Object object = constructor.newInstance();
                MinecraftServer.getCommandManager().register((Command) object);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                     InstantiationException e) {
                logger.error(e.getMessage());
            }
        }
    }
}