// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import net.minecraft.util.EnumFacing;
import java.util.HashMap;

public final class GeometryMasks
{
    public static final /* synthetic */ HashMap<EnumFacing, Integer> FACEMAP;
    
    static {
        (FACEMAP = new HashMap<EnumFacing, Integer>()).put(EnumFacing.DOWN, 1);
        GeometryMasks.FACEMAP.put(EnumFacing.WEST, 16);
        GeometryMasks.FACEMAP.put(EnumFacing.NORTH, 4);
        GeometryMasks.FACEMAP.put(EnumFacing.SOUTH, 8);
        GeometryMasks.FACEMAP.put(EnumFacing.EAST, 32);
        GeometryMasks.FACEMAP.put(EnumFacing.UP, 2);
    }
    
    public static final class Quad
    {
        static {
            ALL = 63;
            NORTH = 4;
            EAST = 32;
            DOWN = 1;
            WEST = 16;
            SOUTH = 8;
            UP = 2;
        }
    }
    
    public static final class Line
    {
        static {
            UP_NORTH = 6;
            NORTH_EAST = 36;
            DOWN_SOUTH = 9;
            UP_WEST = 18;
            UP_SOUTH = 10;
            ALL = 63;
            DOWN_EAST = 33;
            SOUTH_WEST = 24;
            SOUTH_EAST = 40;
            DOWN_WEST = 17;
            DOWN_NORTH = 5;
            UP_EAST = 34;
            NORTH_WEST = 20;
        }
    }
}
