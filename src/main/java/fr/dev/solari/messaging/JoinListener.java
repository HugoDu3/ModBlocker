package fr.dev.solari.messaging;

import fr.dev.solari.ModBlocker;
import fr.dev.solari.util.UtilServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener {

    private final ModBlocker plugin;

    public JoinListener(ModBlocker plugin) {
        this.plugin = plugin;

        UtilServer.registerListener(this);
        UtilServer.registerOutgoingChannel("FML|HS");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                sendFmlPacket(player, (byte) -2, (byte) 0);
                sendFmlPacket(player, (byte) 0, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
                sendFmlPacket(player, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
            }
        }.runTaskLater(plugin, 20L);
    }

    private void sendFmlPacket(Player player, byte... data) {
        player.sendPluginMessage(plugin, "FML|HS", data);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getModManager().removePlayer(event.getPlayer());
    }
}
