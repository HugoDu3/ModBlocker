package fr.dev.solari.util;

import fr.dev.solari.ModBlocker;
import org.bukkit.ChatColor;


public final class C {
    private C(){}

    public static String PREFIX = null;

    public static void setPrefix() {
        assert PREFIX == null : "Le préfixe est déjà défini";

        ModBlocker plugin = UtilServer.getPlugin();

        PREFIX = C.colour(plugin.getConfig("prefix", "&cModBlocker ➤")) + " ";
    }

    public static String colour(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
