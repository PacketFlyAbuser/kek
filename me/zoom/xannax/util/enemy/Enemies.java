// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util.enemy;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class Enemies
{
    public static /* synthetic */ List<Enemy> enemies;
    
    public static void delEnemy(final String s) {
        Enemies.enemies.remove(getEnemyByName(s));
    }
    
    public static void addEnemy(final String s) {
        Enemies.enemies.add(new Enemy(s));
    }
    
    public Enemies() {
        Enemies.enemies = new ArrayList<Enemy>();
    }
    
    public static Enemy getEnemyByName(final String anotherString) {
        Enemy enemy = null;
        for (final Enemy enemy2 : getEnemies()) {
            if (enemy2.getName().equalsIgnoreCase(anotherString)) {
                enemy = enemy2;
            }
        }
        return enemy;
    }
    
    public static boolean isEnemy(final String anotherString) {
        boolean b = false;
        final Iterator<Enemy> iterator = getEnemies().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equalsIgnoreCase(anotherString)) {
                b = true;
            }
        }
        return b;
    }
    
    public static List<Enemy> getEnemies() {
        return Enemies.enemies;
    }
}
