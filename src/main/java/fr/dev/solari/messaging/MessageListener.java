package fr.dev.solari.messaging;

import fr.dev.solari.ModBlocker;
import fr.dev.solari.mods.ModData;
import fr.dev.solari.util.UtilServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MessageListener implements PluginMessageListener
{

    private final ModBlocker plugin;

    public MessageListener(ModBlocker plugin) {
        this.plugin = plugin;

        UtilServer.registerIncomingChannel("FML|HS", this);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] data) {
       
        if (data[0] == 2) {
            ModData modData = getModData(data);
            plugin.getModManager().addPlayer(player, modData);
        }
    }

    private ModData getModData(byte[] data) {
        Map<String, String> mods = new HashMap<>();

        boolean store = false;
        String tempName = null;

        for (int i = 2; i < data.length; store = !store) {
            int end = i + data[i] + 1;
            byte[] range = Arrays.copyOfRange(data, i + 1, end);

            String string = new String(range);

            if (store) {
                mods.put(tempName, string);
            }
            else {
                tempName = string;
            }

            i = end;
        }

        return new ModData(mods);
    }
}
