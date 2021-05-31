package fr.dev.solari.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import fr.dev.solari.ModBlocker;
import fr.dev.solari.mods.ModData;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class Placeholders extends EZPlaceholderHook {

    private final ModBlocker plugin;

    public Placeholders(ModBlocker plugin) {
        super(plugin, "modblocker");

        this.plugin = plugin;

        hook();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return null;
        }

        if (identifier.equals("forge")) {
            return Boolean.toString(plugin.getModManager().isUsingForge(player));
        }

        if (identifier.equals("mods")) {
            ModData data = plugin.getModManager().getModData(player);

            return data == null ? "" : String.join(", ", data.getMods());
        }

        return null;
    }
}
