package org.sevencraft.itemblocker;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.stream.Collectors;

public class BlockItemMove implements Listener {
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(event.getPlayer().hasPermission("itemblocker.bypass")) return;
        List<Material> bannedMaterials =  Main.getConfiguration().getStringList("banned-items").stream().map(materialName ->Material.getMaterial(materialName)).collect(Collectors.toList());
        boolean disablePlayerDrop  =  Main.getConfiguration().getBoolean("disable-player-drop");
        if (bannedMaterials.contains(event.getItemDrop().getItemStack().getType()) && disablePlayerDrop) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getConfiguration().getString("drop-message")));
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onInventoryPickupItemEvent(InventoryPickupItemEvent event) {
        boolean disableHoppers  =  Main.getConfiguration().getBoolean("disable-hoppers");
        boolean playerPickup  =  Main.getConfiguration().getBoolean("disable-player-pickup");
        if((disableHoppers && event.getInventory().getType() == InventoryType.HOPPER) || (playerPickup && event.getInventory().getType() == InventoryType.PLAYER) ) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {
        if(event.getWhoClicked().hasPermission("itemblocker.bypass")) return;
        if(event.getInventory() instanceof PlayerInventory) return;
        List<Material> bannedMaterials =  Main.getConfiguration().getStringList("banned-items").stream().map(materialName ->Material.getMaterial(materialName)).collect(Collectors.toList());
        if(bannedMaterials.contains(event.getOldCursor().getType()) && !event.getWhoClicked().getOpenInventory().getType().equals(InventoryType.PLAYER)) {
            finishPlayerBlock(event.getWhoClicked());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if(event.getWhoClicked().hasPermission("itemblocker.bypass")) return;
        List<Material> bannedMaterials =  Main.getConfiguration().getStringList("banned-items").stream().map(materialName ->Material.getMaterial(materialName)).collect(Collectors.toList());
        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && bannedMaterials.contains(event.getCurrentItem().getType())) {
            finishPlayerBlock(event.getWhoClicked());
            event.setCancelled(true);
            return;
        }

        // Cursor prev, current post
        if((event.getClickedInventory() instanceof PlayerInventory) || event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
        if((bannedMaterials.contains(event.getCurrentItem().getType()) || bannedMaterials.contains(event.getCursor().getType())) && !event.getWhoClicked().getOpenInventory().getType().equals(InventoryType.PLAYER)) {
            finishPlayerBlock(event.getWhoClicked());
            event.setCancelled(true);

        }
    }

    @EventHandler
    public void onInventoryMoveItemEvent(InventoryMoveItemEvent event) {
        List<Material> bannedMaterials =  Main.getConfiguration().getStringList("banned-items").stream().map(materialName ->Material.getMaterial(materialName)).collect(Collectors.toList());
        if(bannedMaterials.contains(event.getItem().getType())){
            event.setCancelled(true);
            return;
        }
    }

    private void finishPlayerBlock(HumanEntity player){
        boolean closeInventory  =  Main.getConfiguration().getBoolean("close-on-block");
        String message = Main.getConfiguration().getString("block-message");
        if(closeInventory) player.closeInventory();
        if(message != null) player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
    }
}