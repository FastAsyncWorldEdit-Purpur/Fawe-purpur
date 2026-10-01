package com.fastasyncworldedit.bukkit.util;

import org.bukkit.Bukkit;

import java.util.Locale;

/**
 * Detects the server software WorldEdit is running on.
 *
 * <p>Paper forks (Purpur, Leaf, Pufferfish, ...) all expose the Paper API, so they must be treated
 * exactly like Paper: the async chunk preloader, {@code Bukkit#getCurrentTick()} and datapack
 * lookups are all available there. Relying on {@code PaperLib#isPaper()} alone is not enough,
 * because a fork can report itself as a plain Spigot/CraftBukkit server name.
 */
public final class BukkitPlatform {

    public enum Kind {
        /**
         * Paper or any fork of it (Purpur, Leaf, Pufferfish, Folia, ...).
         */
        PAPER,
        SPIGOT,
        CRAFTBUKKIT
    }

    private static final Kind KIND = detect();
    private static final String NAME = detectName();

    private BukkitPlatform() {
    }

    private static Kind detect() {
        // Paper and all of its forks keep these classes.
        if (hasClass("io.papermc.paper.configuration.Configuration")
                || hasClass("com.destroystokyo.paper.PaperConfig")
                || hasClass("io.papermc.paper.ServerBuildInfo")) {
            return Kind.PAPER;
        }
        if (hasClass("org.spigotmc.SpigotConfig")) {
            return Kind.SPIGOT;
        }
        return Kind.CRAFTBUKKIT;
    }

    private static String detectName() {
        try {
            return Bukkit.getName();
        } catch (Throwable ignored) {
            return "unknown";
        }
    }

    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException | LinkageError ignored) {
            return false;
        }
    }

    public static Kind getKind() {
        return KIND;
    }

    /**
     * @return the name the server software reports itself with, e.g. {@code Paper}, {@code Purpur}.
     */
    public static String getName() {
        return NAME;
    }

    /**
     * @return {@code true} on Paper and every Paper fork, including Purpur.
     */
    public static boolean isPaper() {
        return KIND == Kind.PAPER;
    }

    public static boolean isPurpur() {
        return "purpur".equalsIgnoreCase(NAME);
    }

    public static boolean isSpigot() {
        return KIND != Kind.CRAFTBUKKIT;
    }
}
