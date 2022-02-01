package me.trobanko.collectit.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Utility class that makes creating ItemStacks easier.
 *
 * @author MisterMel
 */
public class ItemMaker {
    private ItemStack item;

    private String name;
    private boolean hideAttributes;
    private List<String> lore = new ArrayList<String>();

    /**
     * Creates a new ItemMaker with the specified material.
     *
     * @param material The material
     */
    public ItemMaker(Material material) {
        this.item = new ItemStack(material);
    }

    /**
     * Creates a new ItemMaker with the specified material
     * and item data.
     *
     * @param material The material
     * @param data The item data
     */
    public ItemMaker(Material material, short data) {
        this.item = new ItemStack(material, 1, data);
    }

    /**
     * Sets the amount of the item.
     *
     * @param amount Item amount
     * @return This ItemMaker
     */
    public ItemMaker setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Sets the name of the item.
     *
     * @param name Item name
     * @return This ItemMaker
     */
    public ItemMaker setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Adds an empty lore line.
     * This can be used to skip a line,
     * a.k.a. a spacer.
     *
     * @return This ItemMaker
     */
    public ItemMaker addLoreSpacer() {
        this.lore.add("");
        return this;
    }

    /**
     * Adds an empty lore line, but only if
     * the requirement boolean is true.
     * This can be used to skip a line,
     * a.k.a. a spacer.
     *
     * @param requirement The method is only executed if this is true
     * @return This ItemMaker, even when requirement is false
     */
    public ItemMaker addLoreSpacer(boolean requirement) {
        if(!requirement)
            return this;

        return this.addLoreSpacer();
    }

    /**
     * Adds the specified lore line to the
     * item. If there are newline characters
     * in the string, multiple lines are added.
     *
     * @param lore Line to add to the lore.
     * @return This ItemMaker
     */
    public ItemMaker addLore(String lore) {
        String[] lores = lore.split("\\\\n");
        for(String splitLore : lores) {
            this.lore.add(splitLore);
        }
        return this;
    }

    /**
     * Adds the specified lore line to the
     * item, but only if the requirement
     * boolean is true. If there are newline
     * characters in the string, multiple lines
     * are added.
     *
     * @param requirement The method is only executed if this is true
     * @param lore The lore line to add
     * @return This ItemMaker, even when requirement is false
     */
    public ItemMaker addLore(boolean requirement, String lore) {
        if(!requirement)
            return this;

        return this.addLore(lore);
    }

    /**
     * Hides this item's attributes, like damage
     * stats on swords.
     *
     * @return This ItemMaker
     */
    public ItemMaker hideAttributes() {
        this.hideAttributes = true;
        return this;
    }

    /**
     * Returns an ItemStack with the settings
     * specified in this ItemMaker.
     *
     * This returns a clone, so modifying
     * the ItemStack further afterwards does
     * not affect this ItemMaker.
     *
     * @return A clone of the ItemStack
     */
    public ItemStack get() {
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(lore);
        if(hideAttributes)
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
        return item.clone();
    }

}
 