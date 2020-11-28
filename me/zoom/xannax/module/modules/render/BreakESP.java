//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import java.util.HashMap;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import me.zoom.xannax.util.MathUtil;
import me.zoom.xannax.util.RenderUtil;
import net.minecraft.init.Blocks;
import me.zoom.xannax.event.events.RenderEvent;
import java.util.List;
import io.netty.util.internal.ConcurrentSet;
import java.util.ArrayList;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class BreakESP extends Module
{
    /* synthetic */ Setting.Integer blue;
    /* synthetic */ Setting.Boolean fade;
    private /* synthetic */ Map alphaMap;
    /* synthetic */ Setting.Boolean ignoreSelf;
    public /* synthetic */ ConcurrentSet breaking;
    /* synthetic */ Setting.Mode mode;
    /* synthetic */ Setting.Boolean onlyObby;
    private /* synthetic */ ConcurrentSet test;
    /* synthetic */ Setting.Integer red;
    /* synthetic */ Setting.Integer alpha;
    /* synthetic */ Setting.Integer green;
    /* synthetic */ Setting.Integer alphaF;
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Solid");
        list.add("Outline");
        list.add("Full");
        this.ignoreSelf = this.registerBoolean("IgnoreSelf", "IgnoreSelf", false);
        this.onlyObby = this.registerBoolean("OnlyObi", "OnlyObi", true);
        this.alpha = this.registerInteger("Alpha", "Alpha", 50, 0, 255);
        this.alphaF = this.registerInteger("AlphaF", "AlphaF", 50, 0, 255);
        this.fade = this.registerBoolean("Fade", "Fade", false);
        this.red = this.registerInteger("Red", "Red", 255, 0, 255);
        this.green = this.registerInteger("Green", "Green", 255, 0, 255);
        this.blue = this.registerInteger("Blue", "Blue", 255, 0, 255);
        this.mode = this.registerMode("Mode", "Mode", list, "Solid");
    }
    
    @Override
    public void onWorldRender(final RenderEvent renderEvent) {
        final int n2;
        final IBlockState blockState;
        final Vec3d vec3d;
        final IBlockState blockState2;
        final Vec3d vec3d2;
        BreakESP.mc.renderGlobal.damagedBlocks.forEach((n, destroyBlockProgress) -> {
            if (destroyBlockProgress != null && (!(boolean)this.ignoreSelf.getValue() || BreakESP.mc.world.getEntityByID((int)n) != BreakESP.mc.player) && (!(boolean)this.onlyObby.getValue() || BreakESP.mc.world.getBlockState(destroyBlockProgress.getPosition()).getBlock() == Blocks.OBSIDIAN)) {
                (int)(((boolean)this.fade.getValue()) ? this.alphaMap.get(destroyBlockProgress.getPartialBlockDamage()) : Integer.valueOf(this.alpha.getValue()));
                if (this.mode.getValue().equalsIgnoreCase("Solid")) {
                    RenderUtil.prepare(7);
                    RenderUtil.drawBox(destroyBlockProgress.getPosition(), this.red.getValue(), this.green.getValue(), this.blue.getValue(), n2, 63);
                    RenderUtil.release();
                }
                else if (this.mode.getValue().equalsIgnoreCase("Full")) {
                    BreakESP.mc.world.getBlockState(destroyBlockProgress.getPosition());
                    MathUtil.interpolateEntity((Entity)BreakESP.mc.player, BreakESP.mc.getRenderPartialTicks());
                    RenderUtil.drawFullBox(blockState.getSelectedBoundingBox((World)BreakESP.mc.world, destroyBlockProgress.getPosition()).grow(0.0020000000949949026).offset(-vec3d.x, -vec3d.y, -vec3d.z), destroyBlockProgress.getPosition(), 1.5f, this.red.getValue(), this.green.getValue(), this.blue.getValue(), n2, this.alphaF.getValue());
                }
                else if (this.mode.getValue().equalsIgnoreCase("Outline")) {
                    BreakESP.mc.world.getBlockState(destroyBlockProgress.getPosition());
                    MathUtil.interpolateEntity((Entity)BreakESP.mc.player, BreakESP.mc.getRenderPartialTicks());
                    RenderUtil.drawBoundingBox(blockState2.getSelectedBoundingBox((World)BreakESP.mc.world, destroyBlockProgress.getPosition()).grow(0.0020000000949949026).offset(-vec3d2.x, -vec3d2.y, -vec3d2.z), 1.5f, this.red.getValue(), this.green.getValue(), this.blue.getValue(), n2);
                }
                else {
                    RenderUtil.prepare(7);
                    RenderUtil.drawBox(destroyBlockProgress.getPosition(), this.red.getValue(), this.green.getValue(), this.blue.getValue(), n2, 63);
                    RenderUtil.release();
                }
            }
        });
    }
    
    public BreakESP() {
        super("BreakESP", "BreakESP", Category.Render);
        this.test = new ConcurrentSet();
        this.breaking = new ConcurrentSet();
        this.alphaMap = new HashMap();
        this.alphaMap.put(0, 28);
        this.alphaMap.put(1, 56);
        this.alphaMap.put(2, 84);
        this.alphaMap.put(3, 112);
        this.alphaMap.put(4, 140);
        this.alphaMap.put(5, 168);
        this.alphaMap.put(6, 196);
        this.alphaMap.put(7, 224);
        this.alphaMap.put(8, 255);
        this.alphaMap.put(9, 255);
    }
}
