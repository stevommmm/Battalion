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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.OfflinePlayer;

/**
 *
 * @author c45y
 */
public class RankManager {
    private Battalion _plugin;
    private TreeMap<Integer, BattalionTeam> _ranks;
    private ConcurrentHashMap<OfflinePlayer, Integer> _playerRanks;
    
    public RankManager(Battalion plugin) {
        _plugin = plugin;
        _ranks = new TreeMap<Integer, BattalionTeam>();
        _playerRanks = new ConcurrentHashMap<OfflinePlayer, Integer>();
        
        try {
            fromFile();
        } catch (IOException ex) {
            Logger.getLogger(RankManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        _plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    toFile();
                } catch (IOException ex) {
                    Logger.getLogger(RankManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 1200, 1200);
    }
    
    /* Team */
    public void putTeam(int min, BattalionTeam team) {
        _ranks.put(min, team);
    }
    
    public BattalionTeam getTeam(int rank) {
        Map.Entry<Integer, BattalionTeam> team = _ranks.floorEntry(rank);
        if (team != null) {
            return team.getValue();
        }
        return null;
    }
    
    public BattalionTeam getTeam(OfflinePlayer player) {
        int level = getPlayerRank(player);
        return getTeam(level);
    }
    
    /* Player */  
    public void setPlayerRank(OfflinePlayer player, int rank) {
        BattalionTeam oldTeam = getTeam(player);
        
        if (!_playerRanks.containsKey(player)) {
            _playerRanks.put(player, rank);
        } else {
            _playerRanks.replace(player, rank);
        }
        BattalionTeam newTeam = getTeam(player);
        if (!oldTeam.getName().equals(newTeam.getName())) {
            oldTeam.leaveTeam(player);
            newTeam.joinTeam(player);
            _plugin.getLogger().log(Level.INFO, "Shifted player from team {0} to {1}", new Object[]{oldTeam.getName(), newTeam.getName()});
        } else {
            _plugin.getLogger().log(Level.INFO, "Shifted player remains with rank {0}", rank);
        }
        
    }
    
    public int getPlayerRank(OfflinePlayer player) {
        return _playerRanks.getOrDefault(player, 0);
    }
    
    public void incrementPlayerRank(OfflinePlayer player) {
        setPlayerRank(player, getPlayerRank(player) + _plugin.config.EVENT_KILL);
    }
    
    public void decrementPlayerRank(OfflinePlayer player) {
        int decayedRank = getPlayerRank(player) - _plugin.config.EVENT_DEATH;
        setPlayerRank(player, decayedRank > 0 ? decayedRank : 0);
    }
    
    public void decayRanks() {
        int decay = _plugin.config.DECAY_VALUE;
        for (Map.Entry<OfflinePlayer, Integer> iter: _playerRanks.entrySet()) {
            int decayedRank = iter.getValue() - decay;
            setPlayerRank(iter.getKey(), decayedRank > 0 ? decayedRank : 0);
        }
    }
    
    /* Persist */
    public void toFile() throws IOException  {
        File out = new File(_plugin.getDataFolder(), "ranks.properties");
        Properties properties = new Properties();

        for (Map.Entry<OfflinePlayer, Integer> entry : _playerRanks.entrySet()) {
            properties.put(entry.getKey().getUniqueId().toString(), entry.getValue().toString());
        }

        properties.store(new FileOutputStream(out), null);
    }
    
    public void fromFile() throws IOException {
        File in = new File(_plugin.getDataFolder(), "ranks.properties");
        if (!in.exists()) {
            return;
        }
        Properties properties = new Properties();
        properties.load(new FileInputStream(in));

        for (String key : properties.stringPropertyNames()) {
            OfflinePlayer player = _plugin.getServer().getOfflinePlayer(UUID.fromString(key));
            int rank = Integer.valueOf(properties.get(key).toString());
            _playerRanks.put(player, rank);
        }
    }
}
