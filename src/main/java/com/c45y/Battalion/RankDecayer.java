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

import java.util.logging.Level;

/**
 *
 * @author c45y
 */
public class RankDecayer implements Runnable {

    private Battalion _plugin;

    public RankDecayer(Battalion plugin) {
        _plugin = plugin;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                long now = System.currentTimeMillis();
                long future = _plugin.config.DECAY_LASTRUN + _plugin.config.DECAY_TIMER;
                if (future < now) {
                    _plugin.manager.decayRanks();
                    _plugin.getLogger().log(Level.INFO, "Rank decayer has completed a run");
                    _plugin.config.updateLastRun();
                    future = now + _plugin.config.DECAY_TIMER;
                }
                
                long sleeptime = future - now;
                _plugin.getLogger().log(Level.INFO, "Rank decayer is sleeping for {0}", sleeptime);
                Thread.sleep(sleeptime > 10 ? sleeptime : 10L);
            }
        } catch (InterruptedException ex) {
            
        }
    }
}
