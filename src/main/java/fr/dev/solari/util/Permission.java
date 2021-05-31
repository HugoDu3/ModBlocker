package fr.dev.solari.util;

import org.bukkit.command.CommandSender;

public enum Permission {
    ALL("*"),
    UPDATE_NOTIFICATION("update_notification"),
    BYPASS("bypass"),
    MAIN_COMMAND("main_command"),
    RELOAD_COMMAND("reload_command"),
    MODS_COMMAND("mods_command");

    Permission(String name) {
        this.node = "mb." + name;
    }

    private final String node;

    public static boolean hasPermission(CommandSender sender, Permission permission) {
        return sender.hasPermission(permission.node);
    }
}
