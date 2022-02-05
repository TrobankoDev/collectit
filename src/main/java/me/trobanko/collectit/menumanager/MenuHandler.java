package me.trobanko.collectit.menumanager;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.trobanko.collectit.CollectIt;
import me.trobanko.collectit.collectorUtils.Collector;
import me.trobanko.collectit.collectorUtils.CollectorActions;
import me.trobanko.collectit.utils.Format;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class MenuHandler {
    public void openMainMenu(Player player, Collector collector){
        Gui gui = Gui.gui().title(Component.text("Collector")).rows(4).create();
        gui.setDefaultClickAction(event -> event.setCancelled(true));

        GuiItem remove = ItemBuilder.from(Material.REDSTONE_BLOCK).name(Component.text(Format.c("&cRetrieve Collector"))).asGuiItem(e -> {
            CollectorActions.breakCollector(collector, (Player) e.getWhoClicked());
            player.closeInventory();
        });

        GuiItem storageMenu = ItemBuilder.from(Material.CHEST).name(Component.text(Format.c("&eItems"))).asGuiItem(e -> {
            openStorageMenu((Player) e.getWhoClicked(), collector);
        });

        gui.setItem(2, 3, remove);
        gui.setItem(2, 5, storageMenu);
        gui.getFiller().fill(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem());

        gui.open(player);
    }
    public void openStorageMenu(Player player, Collector collector){
        PaginatedGui gui = Gui.paginated().title(Component.text("Chunk Collector")).rows(6).create();
        gui.setDefaultClickAction(event -> event.setCancelled(true));

        gui.getFiller().fillBorder(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem());
        // Previous item
        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).name(Component.text(Format.c("&cPrevious"))).asGuiItem(event -> gui.previous()));
        // Next item
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).name(Component.text(Format.c("&cNext"))).asGuiItem(event -> gui.next()));


        if(collector.getStoredItemsAmount() >= CollectIt.getPlugin().getConfig().getInt("upgrade-levels." + collector.getCollectorLevel() + ".items")) {
            gui.updateTitle("Chunk Collector - FULL");
        }

        if(collector.getStoredItems() != null) {
            for (Map.Entry<String, Integer> entry : collector.getStoredItems().entrySet()) {
                String itemName = entry.getKey();
                ItemStack itemToAdd = new ItemStack(Material.getMaterial(itemName));
                int stacks = entry.getValue()/64;
                int notFullStack = entry.getValue()%64;
                if(stacks > 0){
                    for (int i=0; i<stacks; i++){
                        ItemStack stack = new ItemStack(Material.getMaterial(itemName), 64);
                        GuiItem guiItem = new GuiItem(stack);
                        gui.addItem(guiItem);
                    }
                }
                if(notFullStack > 0) {;
                    ItemStack notFullStackStack = new ItemStack(Material.getMaterial(itemName), notFullStack);
                    GuiItem guiItem = new GuiItem(notFullStackStack);
                    gui.addItem(guiItem);
                }
            }
        }

        gui.open(player);

    }


}
