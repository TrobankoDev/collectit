package me.trobanko.collectit.listeners;

import me.trobanko.collectit.CollectIt;
import me.trobanko.collectit.collectorUtils.Collector;
import me.trobanko.collectit.database.Database;
import me.trobanko.collectit.utils.Format;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Player player = e.getPlayer();
        ItemStack placedItem = player.getInventory().getItemInMainHand();

        if(!placedItem.getType().equals(Material.END_PORTAL_FRAME)) return;
        if(!placedItem.hasItemMeta()) return;
        if(!ChatColor.stripColor(placedItem.getItemMeta().getDisplayName()).contains("Chunk Collector")) return;

        for(Collector c : CollectIt.getCollectors()){
            if (c.isPlaced() && e.getBlock().getLocation().getChunk().contains(c.getBlockData())) {
                e.setCancelled(true);
                player.sendMessage(Format.c("&cYou cannot place this chunk collector as one already exists in this chunk."));
                return;
            }
        }

        if(e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()){
            String collectorID = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(CollectIt.getNamespacedKey(), PersistentDataType.STRING);
            Collector collector = CollectIt.getCollectorByUUID(collectorID);
            if(collector != null) {
                collector.setPlaced(true);
                collector.setLocation(e.getBlock().getLocation());
                collector.setOwnerUUID(e.getPlayer().getUniqueId().toString());
                Database.setPlaced(collector);
                player.sendMessage(Format.c("&aSuccessfully re-placed chunk collector"));
                return;
            }

        }

        Collector collector = new Collector(e.getPlayer().getUniqueId().toString(), e.getBlock().getLocation(), e.getBlock().getBlockData(), 1, true);

        // add collector to database
        boolean addToDB = Database.createCollector(collector.getCollectorUUID(), collector.getOwnerUUID(), collector.getLocation(), collector.getBlockData(), collector.getCollectorLevel(), true);

        if (!addToDB){
            player.sendMessage(Format.c("&cThere was a problem adding this chunk collector"));
            return;
        }

        CollectIt.getCollectors().add(collector);
        player.sendMessage(Format.c("&aSuccessfully placed chunk collector"));


    }


}
