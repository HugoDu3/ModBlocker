package fr.dev.solari;

import fr.dev.solari.command.MainCommand;
import fr.dev.solari.command.ModsCommand;
import fr.dev.solari.mods.ModManager;
import fr.dev.solari.placeholder.Placeholders;
import fr.dev.solari.util.C;
import fr.dev.solari.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class ModBlocker extends JavaPlugin {
    private ModManager modManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initConfig();

        boolean placeholderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

        if (placeholderAPI) {
            new Placeholders(this);
        }

        getCommand("mb").setExecutor(new MainCommand(this));
        getCommand("mods").setExecutor(new ModsCommand(this));

        modManager = new ModManager(this);
    }

    @Override
    public void onDisable() {
        destroyTasks();
    }

    public void reload() {
        reloadConfig();

        initConfig();
        getModManager().loadConfigValues();
    }

    private void initConfig() {
        C.setPrefix();
        Message.init(this);
    }

    private void destroyTasks() {
        Bukkit.getScheduler().getPendingTasks().stream().filter(task -> task.getOwner() == this).forEach(BukkitTask::cancel);
    }

    public <T> T getConfig(String path) {
        return getConfig(path, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getConfig(String path, Object defaultValue) {
        return (T) getConfig().get(path, defaultValue);
    }

    public ModManager getModManager() {
        return modManager;
    }
}
