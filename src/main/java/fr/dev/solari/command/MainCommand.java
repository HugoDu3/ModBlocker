package fr.dev.solari.command;

import fr.dev.solari.ModBlocker;
import fr.dev.solari.util.Message;
import fr.dev.solari.util.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public class MainCommand implements CommandExecutor {
    private final ModBlocker plugin;

    public MainCommand(ModBlocker plugin) {
        this.plugin = plugin;

        PluginDescriptionFile description = plugin.getDescription();
        this.msg = String.format("ModBlocker de Solari");
    }

    private final String msg;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!Permission.hasPermission(sender, Permission.MAIN_COMMAND)) {
            Message.send(sender, Message.NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(msg);
        }
        else {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (!Permission.hasPermission(sender, Permission.RELOAD_COMMAND)) {
                    Message.send(sender, Message.NO_PERMISSION);
                    return true;
                }

                plugin.reload();
                Message.send(sender, Message.PLUGIN_RELOADED);

                return true;
            }
        }

        sendUsage(sender);
        return true;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Commandes:");
        sender.sendMessage(ChatColor.YELLOW + "/mb reload - Reload le plugin");
    }
}
