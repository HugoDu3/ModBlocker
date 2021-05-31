package fr.dev.solari.util;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.dev.solari.ModBlocker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public final class UtilServer {
    private UtilServer(){}

    private static final ModBlocker plugin = JavaPlugin.getPlugin(ModBlocker.class);

    static ModBlocker getPlugin() {
        return plugin;
    }

    public static void broadcast(Permission permission, String... messages) {
        broadcast(permission, true, messages);
    }
    
    public static void broadcast(Permission permission, boolean prefix, String... messages) {
        if (prefix) {
            for (int i = 0; i < messages.length; i++) {
                messages[i] = C.PREFIX + messages[i];
            }
        }

        Bukkit.getOnlinePlayers().stream().filter(player -> Permission.hasPermission(player, permission)).forEach(player -> player.sendMessage(messages));
    }

    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public static void registerOutgoingChannel(String channel) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, channel);
    }

    public static void registerIncomingChannel(String channel, PluginMessageListener messageListener) {
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, channel, messageListener);
    }

    public static void writeBungee(String... args) {
        assert args.length > 0 : "La longueur des arguments doit être d'au moins 1";

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

        if (player == null) {
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        for (String arg : args) {
            out.writeUTF(arg);
        }

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
