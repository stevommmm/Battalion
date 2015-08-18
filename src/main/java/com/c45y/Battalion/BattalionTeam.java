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

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 *
 * @author c45y
 */
public class BattalionTeam {
    private Battalion _plugin;
    private String _name;

    private ChatColor _color;
    private int _min;
    private Team _team;
    
    public BattalionTeam(Battalion plugin, String name, ChatColor color, int min, Scoreboard board) {
        _plugin = plugin;
        _name = name;
        _color = color;
        _min = min;
        _team = board.registerNewTeam(name);
        _team.setPrefix(color + "");
    }
    
    public String getName() {
        return _name;
    }
    
    public int getMinRank() {
        return _min;
    }
    
    public ChatColor getColor() {
        return _color;
    }
    
    public boolean shouldReward(BattalionTeam team) {
        return _min <= team.getMinRank();
    }
    
    public boolean shouldPunish(BattalionTeam team) {
        return _min >= team.getMinRank();
    }
    
    public boolean canJoin(int rank) {
        return rank > _min;
    }
    
    public void joinTeam(OfflinePlayer player) {
        joinTeam(player, true);
    }
    
    public void joinTeam(OfflinePlayer player, boolean notify) {
        _team.addEntry(player.getName());
        System.out.println("Player " + player.getName() + " is " + (player.isOnline() ? "online" : "offline"));
        if (player.isOnline()) {
            Player oplayer = (Player) player;
            attachArmor(oplayer);
            
            if (notify) {
                oplayer.sendMessage("Welcome " + oplayer.getName() + ", you are in rank " + _color + _name + ChatColor.RESET + " with " + _plugin.manager.getPlayerRank(player) + " points");
            }
        }
    }
    
    public boolean inTeam(OfflinePlayer player) {
        return _team.hasEntry(player.getName());
    }
    
    public void leaveTeam(OfflinePlayer player) {
        _team.removeEntry(player.getName());
    }
    
    public int lowestRank() {
        return _min;
    }
    
    public void attachArmor(Player player) {
        Color color = Util.CHAT_COLOR_MAP.get(_color);

        /* Player Helmet */
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta lhelmet = (LeatherArmorMeta) helmet.getItemMeta();
        lhelmet.setColor(color);
        helmet.setItemMeta(lhelmet);
        /* Player Chestplate */
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta lchestplate = (LeatherArmorMeta) chestplate.getItemMeta();
        lchestplate.setColor(color);
        chestplate.setItemMeta(lchestplate);
        /* Player Leggings */
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        LeatherArmorMeta lleggings = (LeatherArmorMeta) leggings.getItemMeta();
        lleggings.setColor(color);
        leggings.setItemMeta(lleggings);
        /* Player Chestplate */
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        LeatherArmorMeta lboots = (LeatherArmorMeta) boots.getItemMeta();
        lboots.setColor(color);
        boots.setItemMeta(lboots);

        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
    }
    
}
