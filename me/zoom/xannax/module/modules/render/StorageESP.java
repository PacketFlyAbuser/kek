//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import org.lwjgl.opengl.GL11;
import me.zoom.xannax.event.events.RenderEvent;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntityEnderChest;
import me.zoom.xannax.util.RenderUtil;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntityChest;
import java.awt.Color;
import me.zoom.xannax.setting.Setting;
import net.minecraft.tileentity.TileEntity;
import java.util.concurrent.ConcurrentHashMap;
import me.zoom.xannax.module.Module;

public class StorageESP extends Module
{
    /* synthetic */ ConcurrentHashMap<TileEntity, String> chests;
    /* synthetic */ Setting.Integer w;
    
    @Override
    public void setup() {
        this.w = this.registerInteger("Width", "Width", 2, 1, 10);
    }
    
    @Override
    public void onUpdate() {
        StorageESP.mc.world.loadedTileEntityList.forEach(key -> this.chests.put(key, ""));
    }
    
    public StorageESP() {
        super("StorageESP", "StorageESP", Category.Render);
        this.chests = new ConcurrentHashMap<TileEntity, String>();
    }
    
    @Override
    public void onWorldRender(final RenderEvent renderEvent) {
        final Color color = new Color(255, 255, 0, 255);
        final Color color2 = new Color(180, 70, 200, 255);
        final Color color3 = new Color(150, 150, 150, 255);
        final Color color4 = new Color(255, 0, 0, 255);
        if (this.chests != null && this.chests.size() > 0) {
            RenderUtil.prepareGL();
            GL11.glEnable(2848);
            final Color color5;
            final Color color6;
            final Color color7;
            final Color color8;
            this.chests.forEach((tileEntity, p5) -> {
                if (StorageESP.mc.world.loadedTileEntityList.contains(tileEntity)) {
                    if (tileEntity instanceof TileEntityChest) {
                        RenderUtil.drawBoundingBox(StorageESP.mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox((World)StorageESP.mc.world, tileEntity.getPos()), (float)this.w.getValue(), color5.getRGB());
                    }
                    if (tileEntity instanceof TileEntityEnderChest) {
                        RenderUtil.drawBoundingBox(StorageESP.mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox((World)StorageESP.mc.world, tileEntity.getPos()), (float)this.w.getValue(), color6.getRGB());
                    }
                    if (tileEntity instanceof TileEntityShulkerBox) {
                        RenderUtil.drawBoundingBox(StorageESP.mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox((World)StorageESP.mc.world, tileEntity.getPos()), (float)this.w.getValue(), color7.getRGB());
                    }
                    if (tileEntity instanceof TileEntityDispenser || tileEntity instanceof TileEntityFurnace || tileEntity instanceof TileEntityHopper) {
                        RenderUtil.drawBoundingBox(StorageESP.mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox((World)StorageESP.mc.world, tileEntity.getPos()), (float)this.w.getValue(), color8.getRGB());
                    }
                }
                return;
            });
            RenderUtil.releaseGL();
        }
    }
}
