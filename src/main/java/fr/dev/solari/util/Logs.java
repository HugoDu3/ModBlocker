package fr.dev.solari.util;

import java.util.Arrays;
import java.util.logging.Logger;

public final class Logs
{
    private Logs(){}

    public static void info(String msg) {
        UtilServer.getPlugin().getLogger().info(msg);
    }

    public static void info(String... messages) {
        Arrays.stream(messages).forEach(Logs::info);
    }

    public static void severe(String msg) {
        UtilServer.getPlugin().getLogger().severe(msg);
    }
}
