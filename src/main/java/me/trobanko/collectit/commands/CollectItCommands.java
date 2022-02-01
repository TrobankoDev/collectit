package me.trobanko.collectit.commands;

import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import me.trobanko.collectit.utils.Format;
import me.trobanko.collectit.utils.ItemMaker;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Command("collectit")
public class CollectItCommands extends CommandBase {

    @SubCommand("give")
    public void giveCollector(final Player player){
        player.getInventory().addItem(new ItemMaker(Material.END_PORTAL_FRAME).setName("&6Chunk Collector").get());
        player.sendMessage(Format.c("&aYou have been given the chunk collector"));
    }


}
