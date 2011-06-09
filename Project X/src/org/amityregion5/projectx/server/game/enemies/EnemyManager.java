/**
 * Copyright (c) 2011 Amity AP CS A Students of 2010-2011.
 *
 * ex: set filetype=java expandtab tabstop=4 shiftwidth=4 :
 *
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation.
 */
package org.amityregion5.projectx.server.game.enemies;

import java.awt.Point;
import java.util.ArrayList;
import org.amityregion5.projectx.common.entities.characters.enemies.ArmoredEnemy;

import org.amityregion5.projectx.common.entities.characters.enemies.DefaultEnemy;
import org.amityregion5.projectx.common.entities.characters.enemies.Enemy;
import org.amityregion5.projectx.common.entities.characters.enemies.SuicideBomber;
import org.amityregion5.projectx.server.game.GameController;

/**
 * Class documentation.
 *
 * @author Michal Wenke
 * @author Mike DiBuduo
 */
public class EnemyManager {

    public static final int INITIAL_SPAWN_TIMEOUT = 4500; // how many ms before we start spawining

    private GeneratorThread gen;
    private EnemyWave wave; // Current wave
    private ArrayList<Point> spawnArea;
    private GameController controller;
    private final int NUM_WAVES = 666; // Completely arbitrary

    public EnemyManager(GameController c, ArrayList<Point> area)
    {
        controller = c;
        spawnArea = area;

        EnemyGroup group = createEnemyGroup(new DefaultEnemy(1, 0, 0), 5); // Arbitrary first wave with 5 enemies w/10 health
        EnemyGroup bomberGroup = createEnemyGroup(new SuicideBomber(100, 1, 0, 0), 3);
        EnemyGroup armorGroup = createEnemyGroup(new ArmoredEnemy(2, 1, 0, 0), 3);
        ArrayList<EnemyGroup> enemies = new ArrayList<EnemyGroup>();
        enemies.add(group);
        enemies.add(bomberGroup);
        enemies.add(armorGroup);
        wave = new EnemyWave(1, enemies);
        gen = new GeneratorThread(controller, spawnArea, this);
        gen.addWave(wave);
    }

    public EnemyGroup createEnemyGroup(Enemy en, int num)
    {
        return new EnemyGroup(en, num);
    }

    public void setWave(EnemyWave w)
    {
        wave = w;
    }

    /**
     * Starts the spawning thread. Adds an initial wave.
     * Note: starts in a new thread. This method returns immediately
     */
    public void startSpawning()
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(INITIAL_SPAWN_TIMEOUT);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                EnemyGroup group = createEnemyGroup(new DefaultEnemy(10, 0, 0), 5);
                EnemyGroup bomberGroup = createEnemyGroup(new SuicideBomber(100, 5, 0, 0), 3);
                EnemyGroup armorGroup = createEnemyGroup(new ArmoredEnemy(2, 10, 0, 0), 3);
                ArrayList<EnemyGroup> enemies = new ArrayList<EnemyGroup>();
                enemies.add(group);
                enemies.add(bomberGroup);
                enemies.add(armorGroup);
                wave = new EnemyWave(1, enemies);
                for (int i = 0; i < NUM_WAVES; i++)
                {
                    gen.addWave(wave);
                    wave = wave.nextWave();
                }
                gen.start();
            }
        }.start();
    }

    public void kill()
    {
        gen.kill();
    }

    public static int waveDelayTime(int wn)
    {
        double time = 10;
        time += 10 * wn * Math.log(2) / Math.log(wn + 1);
        return (int) (1000 * time);
    }
}
