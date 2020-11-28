//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import java.util.Iterator;
import me.zoom.xannax.util.BlockUtils;
import me.zoom.xannax.event.events.RenderEvent;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3i;
import me.zoom.xannax.util.RenderUtil;
import net.minecraft.util.math.BlockPos;
import io.netty.util.internal.ConcurrentSet;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class VoidESP extends Module
{
    /* synthetic */ Setting.Integer speed;
    /* synthetic */ Setting.Integer oW;
    /* synthetic */ Setting.Mode renderMode;
    /* synthetic */ Setting.Integer gV;
    /* synthetic */ Setting.Integer bV;
    private /* synthetic */ ConcurrentSet<BlockPos> voidHoles;
    /* synthetic */ Setting.Integer activeYValue;
    /* synthetic */ Setting.Integer saturation;
    /* synthetic */ Setting.Integer brightness;
    /* synthetic */ Setting.Integer rV;
    /* synthetic */ Setting.Integer renderDistance;
    /* synthetic */ Setting.Boolean rainbow;
    /* synthetic */ Setting.Integer alpha;
    /* synthetic */ Setting.Mode renderType;
    
    public void drawOutline(final BlockPos blockPos, final int n, final int n2, final int n3, final int n4) {
        if (this.renderType.getValue().equalsIgnoreCase("Outline") || this.renderType.getValue().equalsIgnoreCase("Both")) {
            final float[] array = { System.currentTimeMillis() % 11520L / 11520.0f };
            final int n5 = 0;
            array[n5] += 0.02f;
            if (this.renderMode.getValue().equalsIgnoreCase("Box")) {
                RenderUtil.drawBoundingBoxBlockPos(blockPos, (float)n, n2, n3, n4, 255);
            }
            if (this.renderMode.getValue().equalsIgnoreCase("Flat")) {
                RenderUtil.drawBoundingBoxBottom2(blockPos, (float)n, n2, n3, n4, 255);
            }
        }
    }
    
    private boolean isAnyBedrock(final BlockPos blockPos, final BlockPos[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (VoidESP.mc.world.getBlockState(blockPos.add((Vec3i)array[i])).getBlock().equals(Blocks.BEDROCK)) {
                return true;
            }
        }
        return false;
    }
    
    private void drawBox(final BlockPos blockPos, final int r, final int g, final int b) {
        if (this.renderType.getValue().equalsIgnoreCase("Fill") || this.renderType.getValue().equalsIgnoreCase("Both")) {
            VoidESP.mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)VoidESP.mc.world, blockPos);
            RenderUtil.drawBox(blockPos, new Color(r, g, b, this.alpha.getValue()).getRGB(), 63);
        }
    }
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Outline");
        list.add("Fill");
        list.add("Both");
        final ArrayList<String> list2 = new ArrayList<String>();
        list2.add("Box");
        list2.add("Flat");
        this.rV = this.registerInteger("Red", "RedVoid", 255, 0, 255);
        this.gV = this.registerInteger("Green", "GreenVoid", 255, 0, 255);
        this.bV = this.registerInteger("Blue", "BlueVoid", 255, 0, 255);
        this.rainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        this.saturation = this.registerInteger("Saturation", "Saturation", 50, 0, 100);
        this.brightness = this.registerInteger("Brightness", "Brightness", 50, 0, 100);
        this.speed = this.registerInteger("Speed", "Speed", 50, 1, 100);
        this.alpha = this.registerInteger("Alpha", "Alpha", 50, 0, 255);
        this.oW = this.registerInteger("OutlineW", "OutlineWVoid", 2, 1, 10);
        this.renderDistance = this.registerInteger("Distance", "Distance", 10, 1, 40);
        this.activeYValue = this.registerInteger("Activate Y", "ActivateY", 20, 0, 256);
        this.renderType = this.registerMode("Render", "Render", list, "Both");
        this.renderMode = this.registerMode("Mode", "Mode", list2, "Flat");
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(VoidESP.mc.player.posX), Math.floor(VoidESP.mc.player.posY), Math.floor(VoidESP.mc.player.posZ));
    }
    
    public VoidESP() {
        super("VoidESP", "VoidESP", Category.Render);
    }
    
    @Override
    public void onWorldRender(final RenderEvent renderEvent) {
        final Color color = this.rainbow.getValue() ? new Color(RenderUtil.getRainbow(this.speed.getValue() * 100, 0, this.saturation.getValue() / 100.0f, this.brightness.getValue() / 100.0f)) : new Color(this.rV.getValue(), this.gV.getValue(), this.bV.getValue());
        final Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue());
        if (VoidESP.mc.player == null || this.voidHoles == null) {
            return;
        }
        if (VoidESP.mc.player.getPosition().getY() > this.activeYValue.getValue()) {
            return;
        }
        if (this.voidHoles.isEmpty()) {
            return;
        }
        final Color color3;
        this.voidHoles.forEach(blockPos -> {
            RenderUtil.prepare(7);
            if (this.renderMode.getValue().equalsIgnoreCase("Box")) {
                this.drawBox(blockPos, color3.getRed(), color3.getGreen(), color3.getBlue());
            }
            else {
                this.drawFlat(blockPos, color3.getRed(), color3.getGreen(), color3.getBlue());
            }
            RenderUtil.release();
            RenderUtil.prepare(7);
            this.drawOutline(blockPos, this.oW.getValue(), color3.getRed(), color3.getGreen(), color3.getBlue());
            RenderUtil.release();
        });
    }
    
    @Override
    public void onUpdate() {
        if (VoidESP.mc.player.dimension == 1) {
            return;
        }
        if (VoidESP.mc.player.getPosition().getY() > this.activeYValue.getValue()) {
            return;
        }
        if (this.voidHoles == null) {
            this.voidHoles = (ConcurrentSet<BlockPos>)new ConcurrentSet();
        }
        else {
            this.voidHoles.clear();
        }
        for (final BlockPos blockPos : BlockUtils.getCircle(getPlayerPos(), 0, (float)this.renderDistance.getValue(), false)) {
            if (VoidESP.mc.world.getBlockState(blockPos).getBlock().equals(Blocks.BEDROCK)) {
                continue;
            }
            if (this.isAnyBedrock(blockPos, Offsets.center)) {
                continue;
            }
            this.voidHoles.add((Object)blockPos);
        }
    }
    
    public void drawFlat(final BlockPos blockPos, final int r, final int g, final int b) {
        if (this.renderType.getValue().equalsIgnoreCase("Fill") || this.renderType.getValue().equalsIgnoreCase("Both")) {
            VoidESP.mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)VoidESP.mc.world, blockPos);
            if (this.renderMode.getValue().equalsIgnoreCase("Flat")) {
                RenderUtil.drawBox(blockPos, new Color(r, g, b, this.alpha.getValue()).getRGB(), 1);
            }
        }
    }
    
    private static class Offsets
    {
        static final /* synthetic */ BlockPos[] center;
        
        static {
            center = new BlockPos[] { new BlockPos(0, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, 2, 0) };
        }
    }
}
