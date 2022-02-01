package me.trobanko.collectit.utils;

import org.bukkit.ChatColor;

public class Format {

    public static String c(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
