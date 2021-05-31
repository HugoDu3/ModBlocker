package fr.dev.solari.mods;

import com.google.common.collect.Lists;
import fr.dev.solari.ModBlocker;
import fr.dev.solari.messaging.JoinListener;
import fr.dev.solari.messaging.MessageListener;
import fr.dev.solari.util.C;
import fr.dev.solari.util.Permission;
import fr.dev.solari.util.UtilServer;
import fr.dev.solari.util.UtilString;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ModManager {
    private final ModBlocker plugin;

    public ModManager(ModBlocker plugin) {
        this.plugin = plugin;

        new JoinListener(plugin);
        new MessageListener(plugin);

        loadConfigValues();
    }

    public void loadConfigValues() {
        loadMode();

        blockForge = plugin.getConfig("block-forge", false);
        modList = plugin.getConfig("mod-list", new ArrayList<>());

        disallowedCommands = plugin.getConfig("disallowed-mods-commands", Lists.newArrayList("kick %player% &cLe/les mod(s) que vous utilisez est/sont interdit - %disallowed_mods%"));
        disallowedCommands.replaceAll(C::colour);
    }

    private void loadMode() {
        Mode mode = EnumUtils.getEnum(Mode.class, ((String) plugin.getConfig("mode")).toUpperCase());
        this.mode = mode == null ? Mode.BLACKLIST : mode;
    }

    private enum Mode {
        WHITELIST((mod, modList) -> modList.contains(mod)),
        BLACKLIST((mod, modList) -> !modList.contains(mod));

        Mode(BiFunction<String, List<String>, Boolean> function) {
            this.function = function;
        }

        private final BiFunction<String, List<String>, Boolean> function;

        public boolean isAllowed(String mod, List<String> modList) {
            if (mod.toLowerCase().equalsIgnoreCase("fml") || mod.toLowerCase().equalsIgnoreCase("mcp") || mod.toLowerCase().equalsIgnoreCase("forge") || mod.toLowerCase().equalsIgnoreCase("minecraft")) {
                return true;
            }

            return function.apply(mod, modList);
        }
    }

    private Mode mode;

    private boolean isDisallowed(String mod) {
        return !mode.isAllowed(mod, modList);
    }

    private boolean blockForge;

    private List<String> modList;

    private List<String> disallowedCommands;

    private final Map<Player, ModData> playerData = new HashMap<>();

    public boolean isUsingForge(Player player) {
        return playerData.containsKey(player);
    }

    public ModData getModData(Player player) {
        return playerData.get(player);
    }

    public void addPlayer(Player player, ModData data) {
        playerData.put(player, data);

        checkForDisallowed(player, data.getMods());
    }

    private void checkForDisallowed(Player player, Set<String> mods) {
        if (Permission.hasPermission(player, Permission.BYPASS)) {
            return;
        }

        Set<String> disallowed = mods.stream().filter(this::isDisallowed).collect(Collectors.toSet());

        if (disallowed.size() > 0 || (mods.size() > 0 && blockForge)) {
            String modsString = String.join(", ", mods);
            String disallowedString = String.join(", ", disallowed);

            sendDisallowedCommand(player, modsString, disallowedString);
        }
    }

    private void sendDisallowedCommand(Player player, String mods, String disallowedMods) {
        disallowedCommands.forEach(command -> {
            command = formatCommand(command, player, mods, disallowedMods);

            String[] args = command.split(" ");

            if (args[0].equalsIgnoreCase("[bungeekick]")) {
                String reason = UtilString.combine(args, 1);
                UtilServer.writeBungee("KickPlayer", player.getName(), reason);

                return;
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    private String formatCommand(String command, Player player, String mods, String disallowedMods) {
        return command.replace("%player%", player.getName()).replace("%mods%", mods).replace("%disallowed_mods%", disallowedMods);
    }

    public void removePlayer(Player player) {
        playerData.remove(player);
    }
}
