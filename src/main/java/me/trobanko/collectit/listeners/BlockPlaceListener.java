package me.trobanko.collectit.listeners;

import me.trobanko.collectit.CollectIt;
import me.trobanko.collectit.collectorUtils.Collector;
import me.trobanko.collectit.utils.Format;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Player player = e.getPlayer();
        ItemStack placedItem = player.getInventory().getItemInMainHand();

        if(!placedItem.getType().equals(Material.END_PORTAL_FRAME)) return;
        if(!placedItem.hasItemMeta()) return;
        if(!ChatColor.stripColor(placedItem.getItemMeta().getDisplayName()).contains("Chunk Collector")) return;


        Collector collector = new Collector(e.getPlayer().getUniqueId().toString(), e.getBlock().getLocation(), e.getBlock().getBlockData(), 1);
        CollectIt.getCollectors().add(collector);
        player.sendMessage(Format.c("&aSuccessfully placed chunk collector"));


    }


}
