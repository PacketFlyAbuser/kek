//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.util.math.Vec3i;
import net.minecraft.init.Blocks;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.event.events.RenderEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import me.zoom.xannax.util.RenderUtil;
import java.awt.Color;
import net.minecraft.world.World;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.util.math.BlockPos;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class HoleESP extends Module
{
    /* synthetic */ Setting.Integer greeno;
    private final /* synthetic */ BlockPos[] surroundOffset;
    /* synthetic */ Setting.Integer blueo;
    /* synthetic */ Setting.Integer outlineW;
    /* synthetic */ Setting.Integer greenb;
    /* synthetic */ Setting.Mode type;
    /* synthetic */ Setting.Integer blueb;
    /* synthetic */ Setting.Boolean hideOwn;
    /* synthetic */ Setting.Integer redb;
    public static /* synthetic */ Setting.Integer rangeS;
    /* synthetic */ Setting.Mode mode;
    /* synthetic */ Setting.Boolean rainbow;
    private /* synthetic */ ConcurrentHashMap<BlockPos, Boolean> safeHoles;
    /* synthetic */ Setting.Integer redo;
    /* synthetic */ Setting.Boolean flatOwn;
    /* synthetic */ Setting.Integer alpha;
    
    public void drawFlat(final BlockPos blockPos, final int r, final int g, final int b) {
        if (this.type.getValue().equalsIgnoreCase("Fill") || this.type.getValue().equalsIgnoreCase("Both")) {
            HoleESP.mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)HoleESP.mc.world, blockPos);
            if (this.mode.getValue().equalsIgnoreCase("Flat")) {
                RenderUtil.drawBox(blockPos, new Color(r, g, b, this.alpha.getValue()).getRGB(), 1);
            }
        }
    }
    
    public void drawSlab(final BlockPos blockPos, final int n, final int n2, final int n3) {
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos.getX() - HoleESP.mc.getRenderManager().viewerPosX, blockPos.getY() + 0.1 - HoleESP.mc.getRenderManager().viewerPosY, blockPos.getZ() - HoleESP.mc.getRenderManager().viewerPosZ, blockPos.getX() + 1 - HoleESP.mc.getRenderManager().viewerPosX, blockPos.getY() - HoleESP.mc.getRenderManager().viewerPosY, blockPos.getZ() + 1 - HoleESP.mc.getRenderManager().viewerPosZ);
        if (RenderUtil.isInViewFrustrum(new AxisAlignedBB(axisAlignedBB.minX + HoleESP.mc.getRenderManager().viewerPosX, axisAlignedBB.minY + HoleESP.mc.getRenderManager().viewerPosY, axisAlignedBB.minZ + HoleESP.mc.getRenderManager().viewerPosZ, axisAlignedBB.maxX + HoleESP.mc.getRenderManager().viewerPosX, axisAlignedBB.maxY + HoleESP.mc.getRenderManager().viewerPosY, axisAlignedBB.maxZ + HoleESP.mc.getRenderManager().viewerPosZ))) {
            if (this.type.getValue().equalsIgnoreCase("Fill") || this.type.getValue().equalsIgnoreCase("Both")) {
                RenderUtil.drawESP(axisAlignedBB, (float)n, (float)n2, (float)n3, (float)this.alpha.getValue());
            }
            if (this.type.getValue().equalsIgnoreCase("Outline") || this.type.getValue().equalsIgnoreCase("Both")) {
                RenderUtil.drawESPOutline(axisAlignedBB, (float)n, (float)n2, (float)n3, 255.0f, (float)this.outlineW.getValue());
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent renderEvent) {
        if (HoleESP.mc.player == null || this.safeHoles == null) {
            return;
        }
        if (this.safeHoles.isEmpty()) {
            return;
        }
        RenderUtil.prepare(7);
        if (this.mode.getValue().equalsIgnoreCase("Air")) {
            this.safeHoles.forEach((blockPos, b) -> {
                if (b) {
                    this.drawBox(blockPos, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
                }
                else {
                    this.drawBox(blockPos, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
                }
                return;
            });
        }
        if (this.mode.getValue().equalsIgnoreCase("Ground")) {
            this.safeHoles.forEach((blockPos2, b2) -> {
                if (b2) {
                    this.drawBox2(blockPos2, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
                }
                else {
                    this.drawBox2(blockPos2, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
                }
                return;
            });
        }
        if (this.mode.getValue().equalsIgnoreCase("Flat")) {
            this.safeHoles.forEach((blockPos3, b3) -> {
                if (b3) {
                    this.drawFlat(blockPos3, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
                }
                else {
                    this.drawFlat(blockPos3, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
                }
                return;
            });
        }
        if (this.mode.getValue().equalsIgnoreCase("Slab")) {
            this.safeHoles.forEach((blockPos4, b4) -> {
                if (b4) {
                    this.drawSlab(blockPos4, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
                }
                else {
                    this.drawSlab(blockPos4, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
                }
                return;
            });
        }
        if (this.mode.getValue().equalsIgnoreCase("Sexc")) {
            this.safeHoles.forEach((blockPos5, b5) -> {
                if (b5) {
                    RenderUtil.drawGradientFilledBox(blockPos5, new Color(this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue(), 144), new Color(this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue(), 0));
                }
                else {
                    RenderUtil.drawGradientFilledBox(blockPos5, new Color(this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue(), 144), new Color(this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue(), 0));
                }
                return;
            });
        }
        RenderUtil.release();
        RenderUtil.prepare(7);
        if (this.mode.getValue().equalsIgnoreCase("Air")) {
            this.safeHoles.forEach((blockPos6, b6) -> {
                if (b6) {
                    this.drawOutline(blockPos6, 1, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
                }
                else {
                    this.drawOutline(blockPos6, 1, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
                }
                return;
            });
        }
        if (this.mode.getValue().equalsIgnoreCase("Ground")) {
            this.safeHoles.forEach((blockPos7, b7) -> {
                if (b7) {
                    this.drawOutline(blockPos7, 1, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
                }
                else {
                    this.drawOutline(blockPos7, 1, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
                }
                return;
            });
        }
        if (this.mode.getValue().equalsIgnoreCase("Flat")) {
            this.safeHoles.forEach((blockPos8, b8) -> {
                if (b8) {
                    this.drawOutline(blockPos8, 1, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
                }
                else {
                    this.drawOutline(blockPos8, 1, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
                }
                return;
            });
        }
        if (this.mode.getValue().equalsIgnoreCase("Sexc")) {
            this.safeHoles.forEach((blockPos9, b9) -> {
                if (b9) {
                    RenderUtil.drawGradientBlockOutline(blockPos9, new Color(this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue(), 0), new Color(this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue(), 144), 1.0f);
                }
                else {
                    RenderUtil.drawGradientBlockOutline(blockPos9, new Color(this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue(), 0), new Color(this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue(), 144), 1.0f);
                }
                return;
            });
        }
        RenderUtil.release();
    }
    
    private void drawBox(final BlockPos blockPos, final int r, final int g, final int b) {
        if (this.type.getValue().equalsIgnoreCase("Fill") || this.type.getValue().equalsIgnoreCase("Both")) {
            HoleESP.mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)HoleESP.mc.world, blockPos);
            final Color color = new Color(r, g, b, this.alpha.getValue());
            if (this.mode.getValue().equalsIgnoreCase("Air")) {
                if (this.flatOwn.getValue() && blockPos.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ))) {
                    RenderUtil.drawBox(blockPos, color.getRGB(), 1);
                }
                else {
                    RenderUtil.drawBox(blockPos, color.getRGB(), 63);
                }
            }
        }
    }
    
    @Override
    public void setup() {
        HoleESP.rangeS = this.registerInteger("Range", "Range", 5, 1, 20);
        this.rainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        this.hideOwn = this.registerBoolean("Hide Own", "HideOwn", false);
        this.flatOwn = this.registerBoolean("Flat Own", "FlatOwn", false);
        this.redb = this.registerInteger("Red Brock", "RedBrock", 255, 0, 255);
        this.greenb = this.registerInteger("Green Brock", "GreenBrock", 255, 0, 255);
        this.blueb = this.registerInteger("Blue Brock", "BlueBrock", 255, 0, 255);
        this.redo = this.registerInteger("Red Obi", "Red Obi", 255, 0, 255);
        this.greeno = this.registerInteger("Green Obi", "Green Obi", 255, 0, 255);
        this.blueo = this.registerInteger("Blue Obi", "Blue Obi", 255, 0, 255);
        this.alpha = this.registerInteger("Alpha", "AlphaHoleESP", 50, 0, 255);
        this.outlineW = this.registerInteger("OutlineW", "OutlineW", 2, 1, 12);
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Outline");
        list.add("Fill");
        list.add("Both");
        final ArrayList<String> list2 = new ArrayList<String>();
        list2.add("Air");
        list2.add("Ground");
        list2.add("Flat");
        list2.add("Slab");
        list2.add("Sexc");
        this.type = this.registerMode("Render", "Render", list, "Both");
        this.mode = this.registerMode("Mode", "Mode", list2, "Air");
    }
    
    public List<BlockPos> getSphere(final BlockPos blockPos, final float n, final int n2, final boolean b, final boolean b2, final int n3) {
        final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        final int getX = blockPos.getX();
        final int getY = blockPos.getY();
        final int getZ = blockPos.getZ();
        for (int n4 = getX - (int)n; n4 <= getX + n; ++n4) {
            for (int n5 = getZ - (int)n; n5 <= getZ + n; ++n5) {
                for (int n6 = b2 ? (getY - (int)n) : getY; n6 < (b2 ? (getY + n) : ((float)(getY + n2))); ++n6) {
                    final double n7 = (getX - n4) * (getX - n4) + (getZ - n5) * (getZ - n5) + (b2 ? ((getY - n6) * (getY - n6)) : 0);
                    if (n7 < n * n && (!b || n7 >= (n - 1.0f) * (n - 1.0f))) {
                        list.add(new BlockPos(n4, n6 + n3, n5));
                    }
                }
            }
        }
        return list;
    }
    
    public void drawOutline(final BlockPos blockPos, final int n, final int n2, final int n3, final int n4) {
        if (this.type.getValue().equalsIgnoreCase("Outline") || this.type.getValue().equalsIgnoreCase("Both")) {
            final float[] array = { System.currentTimeMillis() % 11520L / 11520.0f };
            final int n5 = 0;
            array[n5] += 0.02f;
            if (this.mode.getValue().equalsIgnoreCase("Air")) {
                if (this.flatOwn.getValue() && blockPos.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ))) {
                    RenderUtil.drawBoundingBoxBottom2(blockPos, (float)this.outlineW.getValue(), n2, n3, n4, 255);
                }
                else {
                    RenderUtil.drawBoundingBoxBlockPos(blockPos, (float)this.outlineW.getValue(), n2, n3, n4, 255);
                }
            }
            if (this.mode.getValue().equalsIgnoreCase("Flat")) {
                RenderUtil.drawBoundingBoxBottom2(blockPos, (float)this.outlineW.getValue(), n2, n3, n4, 255);
            }
            if (this.mode.getValue().equalsIgnoreCase("Ground")) {
                RenderUtil.drawBoundingBoxBlockPos2(blockPos, (float)this.outlineW.getValue(), n2, n3, n4, 255);
            }
        }
    }
    
    public void drawBox2(final BlockPos blockPos, final int r, final int g, final int b) {
        if (this.type.getValue().equalsIgnoreCase("Fill") || this.type.getValue().equalsIgnoreCase("Both")) {
            HoleESP.mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)HoleESP.mc.world, blockPos);
            final Color color = new Color(r, g, b, this.alpha.getValue());
            if (this.mode.getValue().equalsIgnoreCase("Ground")) {
                RenderUtil.drawBox2(blockPos, color.getRGB(), 63);
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.safeHoles == null) {
            this.safeHoles = new ConcurrentHashMap<BlockPos, Boolean>();
        }
        else {
            this.safeHoles.clear();
        }
        final int n = (int)Math.ceil(HoleESP.rangeS.getValue());
        for (final BlockPos key : this.getSphere(getPlayerPos(), (float)n, n, false, true, 0)) {
            if (!HoleESP.mc.world.getBlockState(key).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleESP.mc.world.getBlockState(key.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleESP.mc.world.getBlockState(key.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (this.hideOwn.getValue() && key.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ))) {
                continue;
            }
            boolean b = true;
            boolean b2 = true;
            final BlockPos[] surroundOffset = this.surroundOffset;
            for (int length = surroundOffset.length, i = 0; i < length; ++i) {
                final Block getBlock = HoleESP.mc.world.getBlockState(key.add((Vec3i)surroundOffset[i])).getBlock();
                if (getBlock != Blocks.BEDROCK) {
                    b2 = false;
                }
                if (getBlock != Blocks.BEDROCK && getBlock != Blocks.OBSIDIAN && getBlock != Blocks.ENDER_CHEST && getBlock != Blocks.ANVIL) {
                    b = false;
                    break;
                }
            }
            if (!b) {
                continue;
            }
            this.safeHoles.put(key, b2);
        }
    }
    
    @Override
    public String getHudInfo() {
        return "[" + ChatFormatting.WHITE + this.mode.getValue() + ", " + this.type.getValue() + ChatFormatting.GRAY + "]";
    }
    
    public HoleESP() {
        super("HoleESP", "HoleESP", Category.Render);
        this.surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(HoleESP.mc.player.posX), Math.floor(HoleESP.mc.player.posY), Math.floor(HoleESP.mc.player.posZ));
    }
}
