package me.trobanko.collectit;

import me.mattstudios.mf.base.CommandManager;
import me.trobanko.collectit.collectorUtils.Collector;
import me.trobanko.collectit.commands.CollectItCommands;
import me.trobanko.collectit.database.Database;
import me.trobanko.collectit.listeners.BlockPlaceListener;
import me.trobanko.collectit.listeners.ItemDropListener;
import me.trobanko.collectit.listeners.PlayerInteractListener;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class CollectIt extends JavaPlugin {

    private static CollectIt PLUGIN;
    private static ArrayList<Collector> collectors = new ArrayList<>();
    private static String databasePath;
    private static NamespacedKey namespacedKey;
    @Override
    public void onEnable() {
        // Plugin startup logic
        PLUGIN = this;

        databasePath = "jdbc:sqlite:" + getDataFolder().getAbsolutePath() + "/data/database.db";

        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new ItemDropListener(), this);

        Database.initDatabase();
        Database.addCollectors();
        Database.updateContainersTimer();

        namespacedKey = new NamespacedKey(this, "collector");

        CommandManager commandManager = new CommandManager(this);
        commandManager.register(new CollectItCommands());

        getConfig().options().copyDefaults();
        saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Database.updateContainers();
    }

    public static ArrayList<Collector> getCollectors() {
        return collectors;
    }

    public static CollectIt getPlugin() {
        return PLUGIN;
    }

    public static String getDatabasePath() {
        return databasePath;
    }

    public static NamespacedKey getNamespacedKey(){
        return namespacedKey;
    }

    public static Collector getCollectorByUUID(String UUID){
        for(Collector c : collectors){
            if (c.getCollectorUUID().equals(UUID))
                return c;
        }
        return null;
    }
}
