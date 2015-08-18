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

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author c45y
 */
public class Configuration {
    private Battalion _plugin;
    public String DATABASE__URI;
    public String DATABASE__CLASS;
    public String DATABASE__USERNAME;
    public String DATABASE__PASSWORD;
    public boolean DATABASE__ISOLATION;
    
    
    public int DECAY_TIMER;
    public int DECAY_VALUE;
    public long DECAY_LASTRUN;
    
    public int EVENT_KILL;
    public int EVENT_DEATH;
    
    public HashMap<Integer, ChatColor> TEAM_COLORS;
    
    public Configuration(Battalion plugin) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
        }
        _plugin = plugin;
    }
    
    public void load() {
        _plugin.reloadConfig();
        DATABASE__URI = _plugin.getConfig().getString("database.uri");
        DATABASE__CLASS = _plugin.getConfig().getString("database.class");
        DATABASE__USERNAME = _plugin.getConfig().getString("database.username");
        DATABASE__PASSWORD = _plugin.getConfig().getString("database.password");
        DATABASE__ISOLATION = _plugin.getConfig().getBoolean("database.isolate");
        
        DECAY_TIMER = _plugin.getConfig().getInt("decay.timer");
        DECAY_VALUE = _plugin.getConfig().getInt("decay.value");
        DECAY_LASTRUN = _plugin.getConfig().getLong("decay.lastrun");
        
        EVENT_KILL = _plugin.getConfig().getInt("eventvalue.kill");
        EVENT_DEATH = _plugin.getConfig().getInt("eventvalue.death");
        
        TEAM_COLORS = new HashMap<Integer, ChatColor>();
        ConfigurationSection section = _plugin.getConfig().getConfigurationSection("ranks");
        if(section != null) {
            for(String rankName: section.getKeys(false)) {
                TEAM_COLORS.put(section.getInt(rankName), ChatColor.valueOf(rankName));
            }
        }
    }
    
    public void save() {
        _plugin.saveConfig();
    }
        
    public void updateLastRun() {
        long now = System.currentTimeMillis();
        _plugin.getConfig().set("decay.lastrun", now);
        DECAY_LASTRUN = now;
        save();
    }
}
