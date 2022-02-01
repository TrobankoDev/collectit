package me.trobanko.collectit.listeners;

import me.trobanko.collectit.CollectIt;
import me.trobanko.collectit.collectorUtils.Collector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDropListener implements Listener {

    @EventHandler
    public void onItemDrop(EntityDeathEvent e){
        Collector collector = null;
        for(Collector c : CollectIt.getCollectors()) {
            if(e.getEntity().getLocation().getChunk().contains(c.getBlockData())){
                collector = c;
            }
        }

        if(collector == null) return;

        if(collector.getStoredItemsAmount() >= CollectIt.getPlugin().getConfig().getInt("upgrade-levels." + collector.getCollectorLevel() + ".items")) return;

        for(ItemStack drop : e.getDrops()) {
            collector.addItemStack(drop);
        }
        e.getDrops().clear();
    }


}
