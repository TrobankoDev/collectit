package me.trobanko.collectit;

import me.mattstudios.mf.base.CommandManager;
import me.trobanko.collectit.collectorUtils.Collector;
import me.trobanko.collectit.commands.CollectItCommands;
import me.trobanko.collectit.listeners.BlockPlaceListener;
import me.trobanko.collectit.listeners.ItemDropListener;
import me.trobanko.collectit.listeners.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class CollectIt extends JavaPlugin {

    private static CollectIt PLUGIN;
    private static ArrayList<Collector> collectors = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        PLUGIN = this;

        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new ItemDropListener(), this);

        CommandManager commandManager = new CommandManager(this);
        commandManager.register(new CollectItCommands());

        getConfig().options().copyDefaults();
        saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ArrayList<Collector> getCollectors() {
        return collectors;
    }

    public static CollectIt getPlugin() {
        return PLUGIN;
    }
}
