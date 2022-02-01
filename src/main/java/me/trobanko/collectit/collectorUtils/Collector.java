package me.trobanko.collectit.collectorUtils;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.UUID;

public class Collector {

    private final String collectorUUID;
    private final String ownerUUID;
    private final Location location;
    private final BlockData blockData;
    private final int collectorLevel;
    private HashMap<String, Integer> storedItems = new HashMap<>();

    public Collector(String ownerUUID, Location location, BlockData blockData, int collectorLevel) {
        this.ownerUUID = ownerUUID;
        this.location = location;
        this.blockData = blockData;
        this.collectorUUID = UUID.randomUUID().toString();
        this.collectorLevel = collectorLevel;
    }

    public Collector(String ownerUUID, Location location, BlockData blockData, int collectorLevel, HashMap<String, Integer>  storedItems) {
        this.ownerUUID = ownerUUID;
        this.location = location;
        this.blockData = blockData;
        this.storedItems = storedItems;
        this.collectorUUID = UUID.randomUUID().toString();
        this.collectorLevel = collectorLevel;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public Location getLocation() {
        return location;
    }

    public HashMap<String, Integer>  getStoredItems() {
        return storedItems;
    }

    public String getCollectorUUID() {
        return collectorUUID;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    public int getCollectorLevel() {
        return collectorLevel;
    }

    public void addItemStack(ItemStack itemStack){
        String name = itemStack.getType().toString();
        if(storedItems.containsKey(name)){
            storedItems.replace(name, storedItems.get(name)+itemStack.getAmount());
        }else{
            storedItems.put(name, itemStack.getAmount());
        }
    }

    public int getStoredItemsAmount(){
        int amount = 0;
        for(Integer i : storedItems.values()){
            amount+=i;
        }
        return amount;
    }
}
