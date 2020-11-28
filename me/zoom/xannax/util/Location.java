//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;

public class Location
{
    private /* synthetic */ double y;
    private /* synthetic */ boolean ground;
    private /* synthetic */ double x;
    private /* synthetic */ double z;
    
    public Location setZ(final double z) {
        this.z = z;
        return this;
    }
    
    public Location subtract(final int n, final int n2, final int n3) {
        this.x -= n;
        this.y -= n2;
        this.z -= n3;
        return this;
    }
    
    public Location(final double x, final double y, final double z, final boolean ground) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.ground = ground;
    }
    
    public Location setY(final double y) {
        this.y = y;
        return this;
    }
    
    public Block getBlock() {
        return Minecraft.getMinecraft().world.getBlockState(this.toBlockPos()).getBlock();
    }
    
    public Location setX(final double x) {
        this.x = x;
        return this;
    }
    
    public static Location fromBlockPos(final BlockPos blockPos) {
        return new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
    
    public Location(final int n, final int n2, final int n3) {
        this.x = n;
        this.y = n2;
        this.z = n3;
        this.ground = true;
    }
    
    public Location subtract(final double n, final double n2, final double n3) {
        this.x -= n;
        this.y -= n2;
        this.z -= n3;
        return this;
    }
    
    public BlockPos toBlockPos() {
        return new BlockPos(this.getX(), this.getY(), this.getZ());
    }
    
    public Location setOnGround(final boolean ground) {
        this.ground = ground;
        return this;
    }
    
    public double getX() {
        return this.x;
    }
    
    public Location add(final double n, final double n2, final double n3) {
        this.x += n;
        this.y += n2;
        this.z += n3;
        return this;
    }
    
    public Location add(final int n, final int n2, final int n3) {
        this.x += n;
        this.y += n2;
        this.z += n3;
        return this;
    }
    
    public double getY() {
        return this.y;
    }
    
    public boolean isOnGround() {
        return this.ground;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public Location(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.ground = true;
    }
}
