package fr.dev.solari.util;

import fr.dev.solari.ModBlocker;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum Message {
    NO_PERMISSION,
    MODS_COMMAND_USAGE,
    PLAYER_OFFLINE,
    PLAYER_NOT_USING_FORGE,
    PLAYER_MODS,
    MODS_FORMAT,
    PLUGIN_RELOADED;

    private String msg;

    private void setValue(String msg) {
        assert this.msg == null : "Le message est déjà défini";

        this.msg = msg;
    }

    public String value() {
        return msg;
    }

    public static void send(CommandSender sender, Message message, Object... params) {
        String msg = message.value() == null ? message.name() : message.value();

        sender.sendMessage(String.format(msg, params));
    }

    public static void init(ModBlocker plugin) {
        String messagesPath = "messages";

        for (Message message : values()) {
            String value = plugin.getConfig(messagesPath + "." + message.name().toLowerCase());

            if (value == null) {
                Logs.severe("Aucune valeur trouvée pour le message " + message);
                continue;
            }

            message.setValue(ChatColor.translateAlternateColorCodes('&', value));
        }
    }
}
