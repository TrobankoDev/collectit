package me.trobanko.collectit.listeners;

import me.trobanko.collectit.CollectIt;
import me.trobanko.collectit.collectorUtils.Collector;
import me.trobanko.collectit.menumanager.StorageMenu;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class PlayerInteractListener implements Listener {

    private final StorageMenu storageMenu = new StorageMenu();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(!(Objects.requireNonNull(e.getClickedBlock()).getType().equals(Material.END_PORTAL_FRAME))) return;
        Collector collector = null;
        for(Collector c : CollectIt.getCollectors()){
            if(c.getLocation().equals(e.getClickedBlock().getLocation())){
                collector = c;
            }
        }
        if(collector == null) return;

        storageMenu.openStorageMenu(e.getPlayer(), collector);

    }

}
