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

import com.c45y.Battalion.command.BattalionCommand;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

public class Battalion extends JavaPlugin {
    private static Battalion _instance;
    private BattalionListener _listener;
    public Configuration config;
    public RankManager manager;
    public Scoreboard _board;

    public static Battalion getInstance() {
        return _instance;
    }
    
    @Override
    public void onEnable() {
        /* Create a static reference to ourself */
        _instance = this;
        
        config = new Configuration(this);
        config.load();
        
        manager = new RankManager(this);
        
        /* Register our listener(s) */
        _listener = new BattalionListener(this);
        getServer().getPluginManager().registerEvents(_listener, this);
        getCommand("battalion").setExecutor(new BattalionCommand(this));
        
        _board = getServer().getScoreboardManager().getNewScoreboard();
        
        /* Set up our ranks from the config */
        for(Map.Entry<Integer, ChatColor> iter: config.TEAM_COLORS.entrySet()) {
            BattalionTeam team = new BattalionTeam(this, iter.getValue().name(), iter.getValue(), iter.getKey(), _board);
            manager.putTeam(iter.getKey(), team);
        }
        
        
        getServer().getScheduler().runTaskAsynchronously(this, new RankDecayer(this));
        
        /* Remove armor recipes */
        Iterator<Recipe> recipes = getServer().recipeIterator();
        Recipe recipe;
        while (recipes.hasNext()) {
            recipe = recipes.next();
            if (recipe != null && Util.disabledMaterials.contains(recipe.getResult().getType())) {
                recipes.remove();
            }
        }
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        try {
            manager.toFile();
        } catch (IOException ex) {
            Logger.getLogger(Battalion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void linkScoreboard() {
        for (Player p: getServer().getOnlinePlayers()) {
            p.setScoreboard(_board);
        }
    }
}
