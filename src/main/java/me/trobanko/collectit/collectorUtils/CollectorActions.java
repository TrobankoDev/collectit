package me.trobanko.collectit.collectorUtils;

import me.trobanko.collectit.CollectIt;
import me.trobanko.collectit.database.Database;
import me.trobanko.collectit.utils.Format;
import me.trobanko.collectit.utils.ItemMaker;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CollectorActions {

    public static void breakCollector(Collector c, Player p){
        c.getLocation().getBlock().setType(Material.AIR);
        c.setPlaced(false);
        ItemStack collectorToDrop = new ItemMaker(Material.END_PORTAL_FRAME).setName("&6Chunk Collector").hideAttributes().addLore(Format.c("&7" + c.getCollectorUUID())).get();
        ItemMeta meta = collectorToDrop.getItemMeta();

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

        dataContainer.set(CollectIt.getNamespacedKey(), PersistentDataType.STRING, c.getCollectorUUID());

        collectorToDrop.setItemMeta(meta);

        Database.setNotPlaced(c);

        if(p.getInventory().firstEmpty() == -1){
            p.getWorld().dropItemNaturally(p.getLocation(), collectorToDrop);
        }else{
            p.getInventory().addItem(collectorToDrop);
        }
        p.sendMessage(Format.c("&aYou have broken this Collector!"));
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
    }


}
