//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import me.zoom.xannax.util.RenderUtil;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.util.math.RayTraceResult;
import java.awt.Color;
import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class BlockHighlight extends Module
{
    /* synthetic */ Setting.Integer green;
    /* synthetic */ int c2;
    /* synthetic */ Setting.Boolean shade;
    /* synthetic */ Setting.Integer w;
    /* synthetic */ int c;
    /* synthetic */ Setting.Integer blue;
    /* synthetic */ Setting.Integer red;
    /* synthetic */ Setting.Integer alpha;
    
    @Override
    public void setup() {
        this.shade = this.registerBoolean("Fill", "Fill", false);
        this.w = this.registerInteger("Width", "Width", 2, 1, 10);
        this.red = this.registerInteger("Red", "RedBH", 255, 0, 255);
        this.green = this.registerInteger("Green", "GreenBH", 255, 0, 255);
        this.blue = this.registerInteger("Blue", "BlueBH", 255, 0, 255);
        this.alpha = this.registerInteger("Alpha", "AlphaBH", 50, 0, 255);
    }
    
    @Override
    public void onWorldRender(final RenderEvent renderEvent) {
        final RayTraceResult objectMouseOver = BlockHighlight.mc.objectMouseOver;
        this.c = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 255).getRGB();
        this.c2 = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()).getRGB();
        if (objectMouseOver != null && objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            final BlockPos getBlockPos = objectMouseOver.getBlockPos();
            final AxisAlignedBB getSelectedBoundingBox = BlockHighlight.mc.world.getBlockState(getBlockPos).getSelectedBoundingBox((World)BlockHighlight.mc.world, getBlockPos);
            if (getSelectedBoundingBox != null && getBlockPos != null && BlockHighlight.mc.world.getBlockState(getBlockPos).getMaterial() != Material.AIR) {
                RenderUtil.prepareGL();
                GL11.glEnable(2848);
                RenderUtil.drawBoundingBox(getSelectedBoundingBox, (float)this.w.getValue(), this.c);
                RenderUtil.releaseGL();
                if (this.shade.getValue()) {
                    RenderUtil.prepare(7);
                    RenderUtil.drawBox(getSelectedBoundingBox, this.c2, 63);
                    RenderUtil.release();
                }
            }
        }
    }
    
    public BlockHighlight() {
        super("BlockHighlight", "BlockHighlight", Category.Render);
    }
}
