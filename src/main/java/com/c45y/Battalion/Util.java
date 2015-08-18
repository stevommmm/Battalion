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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

/**
 *
 * @author c45y
 */
public class Util {
    
    public static List<Material> disabledMaterials = Arrays.asList(new Material[] {
        Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
        Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
        Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS,
        Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS
    });
    
    public static final Map<ChatColor, Color> CHAT_COLOR_MAP = new HashMap<ChatColor, Color>();
    static {
        CHAT_COLOR_MAP.put(ChatColor.BLACK, Color.fromRGB(0, 0, 0));
        CHAT_COLOR_MAP.put(ChatColor.DARK_BLUE, Color.fromRGB(0, 0, 170));
        CHAT_COLOR_MAP.put(ChatColor.DARK_GREEN, Color.fromRGB(0, 170, 0));
        CHAT_COLOR_MAP.put(ChatColor.DARK_AQUA, Color.fromRGB(0, 170, 170));
        CHAT_COLOR_MAP.put(ChatColor.DARK_RED, Color.fromRGB(170, 0, 0));
        CHAT_COLOR_MAP.put(ChatColor.DARK_PURPLE, Color.fromRGB(170, 0, 170));
        CHAT_COLOR_MAP.put(ChatColor.GOLD, Color.fromRGB(255, 170, 0));
        CHAT_COLOR_MAP.put(ChatColor.GRAY, Color.fromRGB(170, 170, 170));
        CHAT_COLOR_MAP.put(ChatColor.DARK_GRAY, Color.fromRGB(85, 85, 85));
        CHAT_COLOR_MAP.put(ChatColor.BLUE, Color.fromRGB(85, 85, 255));
        CHAT_COLOR_MAP.put(ChatColor.GREEN, Color.fromRGB(85, 255, 85));
        CHAT_COLOR_MAP.put(ChatColor.AQUA, Color.fromRGB(85, 255, 255));
        CHAT_COLOR_MAP.put(ChatColor.RED, Color.fromRGB(255, 85, 85));
        CHAT_COLOR_MAP.put(ChatColor.LIGHT_PURPLE, Color.fromRGB(255, 85, 255));
        CHAT_COLOR_MAP.put(ChatColor.YELLOW, Color.fromRGB(255, 255, 85));
        CHAT_COLOR_MAP.put(ChatColor.WHITE, Color.fromRGB(255, 255, 255));
    }
}
