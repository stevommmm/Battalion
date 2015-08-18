/*
 * The MIT License
 *
 * Copyright 2015 c45y.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.c45y.Battalion;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author c45y
 */
public class BattalionListener implements Listener {
    private final Battalion _plugin;
    
    public BattalionListener(Battalion plugin) {
        _plugin = plugin;
    }
    
    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlotType().equals(SlotType.ARMOR)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCraftItemEvent(CraftItemEvent event) {
        if (Util.disabledMaterials.contains(event.getRecipe().getResult().getType())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        
        /* Don't drop spawned armor everywhere */
        List<ItemStack> toRemove = new ArrayList<ItemStack>();
        for (ItemStack item: event.getDrops()) {
            if (item.getType().equals(Material.LEATHER_HELMET) ||
                item.getType().equals(Material.LEATHER_CHESTPLATE) ||
                item.getType().equals(Material.LEATHER_LEGGINGS) ||
                item.getType().equals(Material.LEATHER_BOOTS)) {
                toRemove.add(item);
            }
        }
        event.getDrops().removeAll(toRemove);
        
        if (!(victim.getKiller() instanceof Player)) {
            return;
        }
        Player killer = victim.getKiller();
        
        BattalionTeam victimTeam = _plugin.manager.getTeam(victim);
        BattalionTeam killerTeam = _plugin.manager.getTeam(killer);
        
        if (killerTeam.shouldReward(victimTeam)) {
            _plugin.manager.incrementPlayerRank(killer);
        }
        if (victimTeam.shouldPunish(killerTeam)) {
            _plugin.manager.decrementPlayerRank(victim);
        }
        
        event.setDeathMessage(victimTeam.getColor() + victim.getName() + ChatColor.RESET + " was slain by " + killerTeam.getColor() + killer.getName() + ChatColor.RESET);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        BattalionTeam team = _plugin.manager.getTeam(player);
        
        if (team != null) {
            team.joinTeam(player);
        }
        
        _plugin.linkScoreboard();      
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        BattalionTeam team = _plugin.manager.getTeam(player);
        if (team != null) {
            team.attachArmor(player);
        }
        
        _plugin.linkScoreboard();      
    }
}
