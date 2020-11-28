//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.ItemEndCrystal;
import me.zoom.xannax.util.friend.Friends;
import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.util.DamageUtils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import java.awt.Color;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.event.events.RenderEvent;
import net.minecraft.network.Packet;
import net.minecraft.init.Items;
import me.zoom.xannax.util.CrystalUtils;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.util.EnumHand;
import java.util.Iterator;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.init.MobEffects;
import net.minecraft.entity.Entity;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.util.TimerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.concurrent.ConcurrentHashMap;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.util.math.BlockPos;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class AutoCrystal extends Module
{
    /* synthetic */ Setting.Boolean cancelCrystal;
    /* synthetic */ Setting.Boolean autoSwitch;
    /* synthetic */ Setting.Integer checkValue;
    /* synthetic */ Setting.Boolean facePlaceCheck;
    /* synthetic */ BlockPos position;
    private /* synthetic */ boolean isRotating;
    /* synthetic */ Setting.Double facePlaceHp;
    /* synthetic */ Setting.Integer fillAlpha;
    /* synthetic */ BlockPos render;
    /* synthetic */ Setting.Boolean pauseWhileMining;
    /* synthetic */ Setting.Boolean rotate;
    /* synthetic */ Setting.Boolean renderCustomFont;
    /* synthetic */ Setting.Integer outlineRed;
    private static /* synthetic */ double pitch;
    /* synthetic */ Setting.Boolean place;
    /* synthetic */ Setting.Boolean armorCheck;
    /* synthetic */ Setting.Boolean renderPlacement;
    /* synthetic */ Setting.Boolean explode;
    private /* synthetic */ int oldSlot;
    /* synthetic */ Setting.Integer placeDelay;
    /* synthetic */ Setting.Integer outlineBlue;
    /* synthetic */ Setting.Integer outlineAlpha;
    /* synthetic */ Setting.Integer speed;
    /* synthetic */ Setting.Double maxSelfDmg;
    /* synthetic */ Setting.Integer fillGreen;
    @EventHandler
    private /* synthetic */ Listener<PacketEvent.Send> packetSendListener;
    /* synthetic */ Setting.Boolean placeUnderBlock;
    /* synthetic */ Setting.Boolean noSuicide;
    /* synthetic */ Setting.Double wallsRange;
    /* synthetic */ Setting.Boolean swingExploit;
    private final /* synthetic */ ConcurrentHashMap<EntityEnderCrystal, Integer> attackedCrystals;
    /* synthetic */ Setting.Integer armorPercent;
    /* synthetic */ Setting.Integer fillBlue;
    @EventHandler
    private /* synthetic */ Listener<PacketEvent.Receive> packetReceiveListener;
    /* synthetic */ Setting.Integer breakAttempts;
    /* synthetic */ Setting.Integer saturation;
    /* synthetic */ Setting.Integer outlineGreen;
    /* synthetic */ Setting.Double placeRange;
    /* synthetic */ Setting.Boolean renderRainbow;
    private /* synthetic */ int newSlot;
    /* synthetic */ Setting.Mode timer;
    /* synthetic */ boolean mainhand;
    /* synthetic */ Setting.Boolean antiWeakness;
    /* synthetic */ Setting.Boolean renderDamage;
    /* synthetic */ Setting.Double minDmg;
    private static /* synthetic */ boolean isSpoofingAngles;
    /* synthetic */ Setting.Boolean renderOutline;
    /* synthetic */ Setting.Double lineWidth;
    /* synthetic */ Setting.Boolean toggleMsg;
    public static /* synthetic */ EntityPlayer target;
    /* synthetic */ Setting.Boolean customOutline;
    /* synthetic */ Setting.Boolean facePlace;
    private /* synthetic */ boolean switchCooldown;
    /* synthetic */ Setting.Boolean renderFill;
    /* synthetic */ Setting.Integer fillRed;
    private /* synthetic */ boolean isAttacking;
    /* synthetic */ TimerUtils breakTimer;
    /* synthetic */ Setting.Boolean armorDestroy;
    /* synthetic */ Setting.Mode handBreak;
    /* synthetic */ Setting.Integer brightness;
    private /* synthetic */ boolean isPlacing;
    /* synthetic */ Setting.Double hitRange;
    /* synthetic */ TimerUtils placeTimer;
    /* synthetic */ Setting.Boolean spoofRotations;
    private static /* synthetic */ double yaw;
    /* synthetic */ Setting.Boolean pauseWhileEating;
    /* synthetic */ Setting.Integer hitDelay;
    /* synthetic */ boolean offhand;
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Breakplace");
        list.add("Placebreak");
        final ArrayList<String> list2 = new ArrayList<String>();
        list2.add("Mainhand");
        list2.add("Offhand");
        list2.add("Both");
        this.explode = this.registerBoolean("Explode", "Explode", true);
        this.hitDelay = this.registerInteger("HitDelay", "HitDelay", 0, 0, 1000);
        this.breakAttempts = this.registerInteger("Attempts", "Attempts", 1, 1, 6);
        this.handBreak = this.registerMode("Hand", "Hand", list2, "Mainhand");
        this.hitRange = this.registerDouble("HitRange", "HitRange", 5.0, 0.0, 10.0);
        this.antiWeakness = this.registerBoolean("AntiWeakness", "AntiWeakness", true);
        this.place = this.registerBoolean("Place", "Place", true);
        this.placeUnderBlock = this.registerBoolean("PlaceUnderBlock", "PlaceUnderBlock", false);
        this.autoSwitch = this.registerBoolean("AutoSwitch", "AutoSwitch", true);
        this.placeDelay = this.registerInteger("PlaceDelay", "PlaceDelay", 0, 0, 1000);
        this.placeRange = this.registerDouble("PlaceRange", "PlaceRange", 5.0, 0.0, 10.0);
        this.wallsRange = this.registerDouble("WallsRange", "WallsRange", 3.5, 0.0, 10.0);
        this.noSuicide = this.registerBoolean("NoSuicide", "NoSuicide", true);
        this.rotate = this.registerBoolean("Rotate", "Rotate", true);
        this.spoofRotations = this.registerBoolean("SpoofRotations", "SpoofRotations", true);
        this.minDmg = this.registerDouble("MinDMG", "MinDMG", 5.0, 0.0, 20.0);
        this.maxSelfDmg = this.registerDouble("MaxSelfDMG", "MaxSelfDMG", 10.0, 0.0, 36.0);
        this.cancelCrystal = this.registerBoolean("Cancel", "Cancel", true);
        this.swingExploit = this.registerBoolean("SwingExploit", "SwingExploit", true);
        this.pauseWhileEating = this.registerBoolean("PauseWhileEating", "PauseWhileEating", true);
        this.pauseWhileMining = this.registerBoolean("PauseWhileMining", "PauseWhileMining", true);
        this.timer = this.registerMode("Timer", "Timer", list, "Breakplace");
        this.toggleMsg = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
        this.facePlace = this.registerBoolean("Faceplace", "Faceplace", true);
        this.facePlaceCheck = this.registerBoolean("FaceplaceCheck", "FaceplaceCheck", true);
        this.facePlaceHp = this.registerDouble("FaceplaceHP", "FaceplaceHP", 12.0, 0.0, 36.0);
        this.armorDestroy = this.registerBoolean("ArmorDestroy", "ArmorDestroy", true);
        this.armorCheck = this.registerBoolean("ArmorCheck", "ArmorCheck", true);
        this.armorPercent = this.registerInteger("ArmorPercent", "ArmorPercent", 25, 0, 100);
        this.checkValue = this.registerInteger("CheckValue", "CheckValue", 30, 0, 100);
        this.renderPlacement = this.registerBoolean("Render", "Render", true);
        this.renderCustomFont = this.registerBoolean("CustomFont", "CustomFont", true);
        this.renderRainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        this.saturation = this.registerInteger("Saturation", "Saturation", 50, 0, 100);
        this.brightness = this.registerInteger("Brightness", "Brightness", 50, 0, 100);
        this.speed = this.registerInteger("Speed", "Speed", 50, 1, 100);
        this.renderFill = this.registerBoolean("Fill", "Fill", true);
        this.renderOutline = this.registerBoolean("Outline", "Outline", true);
        this.customOutline = this.registerBoolean("CustomOutline", "CustomOutline", true);
        this.renderDamage = this.registerBoolean("RenderDamage", "RenderDamage", true);
        this.fillRed = this.registerInteger("FillRed", "FillRed", 186, 0, 255);
        this.fillGreen = this.registerInteger("FillGreen", "FillGreen", 85, 0, 255);
        this.fillBlue = this.registerInteger("FillBlue", "FillBlue", 211, 0, 255);
        this.fillAlpha = this.registerInteger("FillAlpha", "FillAlpha", 53, 0, 255);
        this.lineWidth = this.registerDouble("LineWidth", "LineWidth", 1.5, 0.0, 5.0);
        this.outlineRed = this.registerInteger("OutlineRed", "OutlineRed", 255, 0, 255);
        this.outlineGreen = this.registerInteger("OutlineGreen", "OutlineGreen", 255, 0, 255);
        this.outlineBlue = this.registerInteger("OutlineBlue", "OutlineBlue", 255, 0, 255);
        this.outlineAlpha = this.registerInteger("OutlineAlpha", "OutlineAlpha", 255, 0, 255);
    }
    
    private boolean isHittingBlock() {
        return AutoCrystal.mc.playerController.isHittingBlock;
    }
    
    private static void resetRotation() {
        if (AutoCrystal.isSpoofingAngles) {
            AutoCrystal.yaw = AutoCrystal.mc.player.rotationYaw;
            AutoCrystal.pitch = AutoCrystal.mc.player.rotationPitch;
            AutoCrystal.isSpoofingAngles = false;
        }
    }
    
    public void explodeCrystal() {
        final EntityEnderCrystal bestCrystal = this.getBestCrystal();
        if (this.explode.getValue() && bestCrystal != null && AutoCrystal.mc.player.getDistance((Entity)bestCrystal) <= this.hitRange.getValue()) {
            if (this.breakTimer.passedMs(this.hitDelay.getValue())) {
                if (this.antiWeakness.getValue() && AutoCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    if (!this.isAttacking) {
                        this.oldSlot = AutoCrystal.mc.player.inventory.currentItem;
                        this.isAttacking = true;
                    }
                    for (int i = 0; i < 9; ++i) {
                        final ItemStack getStackInSlot = AutoCrystal.mc.player.inventory.getStackInSlot(i);
                        if (getStackInSlot != ItemStack.EMPTY) {
                            if (getStackInSlot.getItem() instanceof ItemSword) {
                                this.newSlot = i;
                                break;
                            }
                            if (getStackInSlot.getItem() instanceof ItemTool) {
                                this.newSlot = i;
                                break;
                            }
                        }
                    }
                    if (this.newSlot != -1) {
                        AutoCrystal.mc.player.inventory.currentItem = this.newSlot;
                        this.switchCooldown = true;
                    }
                }
                this.rotateToPos(bestCrystal.posX, bestCrystal.posY, bestCrystal.posZ, (EntityPlayer)AutoCrystal.mc.player);
                for (int j = 0; j < this.breakAttempts.getValue(); ++j) {
                    AutoCrystal.mc.playerController.attackEntity((EntityPlayer)AutoCrystal.mc.player, (Entity)bestCrystal);
                }
                this.addAttackedCrystal(bestCrystal);
                this.getSwingingHand(bestCrystal);
                if (this.cancelCrystal.getValue()) {
                    bestCrystal.setDead();
                }
                this.breakTimer.reset();
                this.isAttacking = false;
            }
        }
        else {
            resetRotation();
            if (this.oldSlot != -1) {
                AutoCrystal.mc.player.inventory.currentItem = this.oldSlot;
                this.oldSlot = -1;
            }
            this.isAttacking = false;
        }
    }
    
    private boolean isEatingGap() {
        return AutoCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold && AutoCrystal.mc.player.isHandActive();
    }
    
    public void onDisable() {
        this.attackedCrystals.clear();
        resetRotation();
        if (this.toggleMsg.getValue() && AutoCrystal.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.RED + "AutoCrystal has been toggled off!");
        }
    }
    
    public AutoCrystal() {
        super("AutoCrystal", "AutoCrystal", Category.Combat);
        this.attackedCrystals = new ConcurrentHashMap<EntityEnderCrystal, Integer>();
        this.oldSlot = -1;
        this.newSlot = -1;
        this.mainhand = false;
        this.offhand = false;
        this.switchCooldown = false;
        this.isRotating = false;
        this.isAttacking = false;
        this.isPlacing = false;
        this.position = null;
        this.breakTimer = new TimerUtils();
        this.placeTimer = new TimerUtils();
        final CPacketPlayer cPacketPlayer;
        this.packetSendListener = new Listener<PacketEvent.Send>(send -> {
            send.getPacket();
            if (cPacketPlayer instanceof CPacketPlayer && this.spoofRotations.getValue() && AutoCrystal.isSpoofingAngles) {
                cPacketPlayer.yaw = (float)AutoCrystal.yaw;
                cPacketPlayer.pitch = (float)AutoCrystal.pitch;
            }
            return;
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        SPacketSoundEffect sPacketSoundEffect;
        final Iterator<Entity> iterator;
        Entity entity;
        this.packetReceiveListener = new Listener<PacketEvent.Receive>(receive -> {
            if (receive.getPacket() instanceof SPacketSoundEffect) {
                sPacketSoundEffect = (SPacketSoundEffect)receive.getPacket();
                if (sPacketSoundEffect.getCategory() == SoundCategory.BLOCKS && sPacketSoundEffect.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    AutoCrystal.mc.world.loadedEntityList.iterator();
                    while (iterator.hasNext()) {
                        entity = iterator.next();
                        if (entity instanceof EntityEnderCrystal && entity.getDistance(sPacketSoundEffect.getX(), sPacketSoundEffect.getY(), sPacketSoundEffect.getZ()) <= 6.0) {
                            entity.setDead();
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    public void getSwingingHand(final EntityEnderCrystal entityEnderCrystal) {
        if (this.handBreak.getValue().equalsIgnoreCase("Mainhand")) {
            if (this.swingExploit.getValue()) {
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            else {
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
        else if (this.handBreak.getValue().equalsIgnoreCase("Offhand")) {
            AutoCrystal.mc.player.swingArm(EnumHand.OFF_HAND);
        }
        else if (ModuleManager.isModuleEnabled("OffhandSwing")) {
            AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
        else {
            AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
            AutoCrystal.mc.player.swingArm(EnumHand.OFF_HAND);
        }
    }
    
    private void rotateToPos(final double n, final double n2, final double n3, final EntityPlayer entityPlayer) {
        final double[] calculateLook = CrystalUtils.calculateLookAt(n, n2, n3, entityPlayer);
        if (this.rotate.getValue()) {
            this.isRotating = true;
            setYawAndPitch((float)calculateLook[0], (float)calculateLook[1]);
            this.isRotating = false;
        }
    }
    
    @Override
    public String getHudInfo() {
        if (AutoCrystal.target != null) {
            return "[" + ChatFormatting.GREEN + AutoCrystal.target.getName() + ChatFormatting.GRAY + "]";
        }
        return "[" + ChatFormatting.GREEN + "No target!" + ChatFormatting.GRAY + "]";
    }
    
    @Override
    public void onUpdate() {
        this.doAutoCrystal();
    }
    
    private static void setYawAndPitch(final float n, final float n2) {
        AutoCrystal.yaw = n;
        AutoCrystal.pitch = n2;
        AutoCrystal.isSpoofingAngles = true;
    }
    
    public void addAttackedCrystal(final EntityEnderCrystal entityEnderCrystal) {
        if (this.attackedCrystals.containsKey(entityEnderCrystal)) {
            this.attackedCrystals.put(entityEnderCrystal, this.attackedCrystals.get(entityEnderCrystal) + 1);
        }
        else {
            this.attackedCrystals.put(entityEnderCrystal, 1);
        }
    }
    
    private int findCrystalsHotbar() {
        for (int i = 0; i < 9; ++i) {
            if (AutoCrystal.mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                return i;
            }
        }
        return -1;
    }
    
    public void doAutoCrystal() {
        final String value = this.timer.getValue();
        switch (value) {
            case "Breakplace": {
                this.explodeCrystal();
                this.placeCrystal();
                break;
            }
            case "Placebreak": {
                this.placeCrystal();
                this.explodeCrystal();
                break;
            }
        }
    }
    
    public void onEnable() {
        this.attackedCrystals.clear();
        if (this.toggleMsg.getValue() && AutoCrystal.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.GREEN + "AutoCrystal has been toggled on!");
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent renderEvent) {
        if (this.render != null && this.renderPlacement.getValue()) {
            final Color color = new Color(RenderUtil.getRainbow(this.speed.getValue() * 100, 0, this.saturation.getValue() / 100.0f, this.brightness.getValue() / 100.0f));
            final Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue());
            Color.HSBtoRGB((new float[] { System.currentTimeMillis() % 2520L / 2520.0f })[0], 1.0f, 1.0f);
            final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.render.getX() - AutoCrystal.mc.getRenderManager().viewerPosX, this.render.getY() - AutoCrystal.mc.getRenderManager().viewerPosY + 1.0, this.render.getZ() - AutoCrystal.mc.getRenderManager().viewerPosZ, this.render.getX() + 1 - AutoCrystal.mc.getRenderManager().viewerPosX, this.render.getY() - AutoCrystal.mc.getRenderManager().viewerPosY, this.render.getZ() + 1 - AutoCrystal.mc.getRenderManager().viewerPosZ);
            if (AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                if (this.renderRainbow.getValue()) {
                    if (this.renderFill.getValue()) {
                        RenderUtil.drawESP(axisAlignedBB, (float)color2.getRed(), (float)color2.getGreen(), (float)color2.getBlue(), (float)this.fillAlpha.getValue());
                    }
                    if (this.renderOutline.getValue()) {
                        if (this.customOutline.getValue()) {
                            RenderUtil.drawESPOutline(axisAlignedBB, (float)color2.getRed(), (float)color2.getGreen(), (float)color2.getBlue(), (float)this.outlineAlpha.getValue(), 1.0f);
                        }
                        else {
                            RenderUtil.drawESPOutline(axisAlignedBB, (float)color2.getRed(), (float)color2.getGreen(), (float)color2.getBlue(), 255.0f, 1.0f);
                        }
                    }
                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    RenderHelper.disableStandardItemLighting();
                }
                else {
                    if (this.renderFill.getValue()) {
                        RenderUtil.drawESP(axisAlignedBB, (float)this.fillRed.getValue(), (float)this.fillGreen.getValue(), (float)this.fillBlue.getValue(), (float)this.fillAlpha.getValue());
                    }
                    if (this.renderOutline.getValue()) {
                        if (this.customOutline.getValue()) {
                            RenderUtil.drawESPOutline(axisAlignedBB, (float)this.outlineRed.getValue(), (float)this.outlineGreen.getValue(), (float)this.outlineBlue.getValue(), (float)this.outlineAlpha.getValue(), 1.0f);
                        }
                        else {
                            RenderUtil.drawESPOutline(axisAlignedBB, (float)this.fillRed.getValue(), (float)this.fillGreen.getValue(), (float)this.fillBlue.getValue(), 255.0f, 1.0f);
                        }
                    }
                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    RenderHelper.disableStandardItemLighting();
                }
                if (this.renderDamage.getValue()) {
                    GlStateManager.pushMatrix();
                    RenderUtil.glBillboardDistanceScaled(this.render.getX() + 0.5f, this.render.getY() + 0.5f, this.render.getZ() + 0.5f, (EntityPlayer)AutoCrystal.mc.player, 1.0f);
                    final double n = DamageUtils.calculateDamage(this.render.getX() + 0.5, this.render.getY() + 1, this.render.getZ() + 0.5, (Entity)AutoCrystal.target);
                    final String string = ((Math.floor(n) == n) ? Integer.valueOf((int)n) : String.format("%.1f", n)) + "";
                    GlStateManager.disableDepth();
                    GlStateManager.translate(-(AutoCrystal.mc.fontRenderer.getStringWidth(string) / 2.0), 0.0, 0.0);
                    FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), string, 0, 0, new Color(140, 140, 140, 255).getRGB());
                    GlStateManager.popMatrix();
                }
            }
        }
    }
    
    public EntityEnderCrystal getBestCrystal() {
        double n = 0.0;
        final double value = this.maxSelfDmg.getValue();
        EntityEnderCrystal entityEnderCrystal = null;
        for (final Entity entity : AutoCrystal.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) {
                continue;
            }
            final EntityEnderCrystal entityEnderCrystal2 = (EntityEnderCrystal)entity;
            if (AutoCrystal.mc.player.getDistance((Entity)entityEnderCrystal2) > (AutoCrystal.mc.player.canEntityBeSeen((Entity)entityEnderCrystal2) ? this.hitRange.getValue() : this.wallsRange.getValue())) {
                continue;
            }
            if (entityEnderCrystal2.isDead) {
                continue;
            }
            if (this.attackedCrystals.containsKey(entityEnderCrystal2) && this.attackedCrystals.get(entityEnderCrystal2) > 5) {
                continue;
            }
            for (final Entity entity2 : AutoCrystal.mc.world.playerEntities) {
                if (entity2 != AutoCrystal.mc.player) {
                    if (!(entity2 instanceof EntityPlayer)) {
                        continue;
                    }
                    if (Friends.isFriend(entity2.getName())) {
                        continue;
                    }
                    if (entity2.getDistance((Entity)AutoCrystal.mc.player) >= 11.0f) {
                        continue;
                    }
                    final EntityPlayer entityPlayer = (EntityPlayer)entity2;
                    if (entityPlayer.isDead) {
                        continue;
                    }
                    if (entityPlayer.getHealth() <= 0.0f) {
                        continue;
                    }
                    final boolean b = this.facePlaceCheck.getValue() && AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                    double value2;
                    if (this.facePlace.getValue() && entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount() <= this.facePlaceHp.getValue() && !b) {
                        value2 = 2.0;
                    }
                    else if (this.shouldActionArmor(entityPlayer, false) && !b && !this.shouldActionArmor((EntityPlayer)AutoCrystal.mc.player, true)) {
                        value2 = 2.0;
                    }
                    else {
                        value2 = this.minDmg.getValue();
                    }
                    final double n2 = DamageUtils.calculateDamage((Entity)entityEnderCrystal2, (Entity)entityPlayer);
                    final double n3 = DamageUtils.calculateDamage((Entity)entityEnderCrystal2, (Entity)AutoCrystal.mc.player);
                    if (n2 < value2) {
                        continue;
                    }
                    if (n3 > value) {
                        continue;
                    }
                    if (this.noSuicide.getValue() && AutoCrystal.mc.player.getHealth() + AutoCrystal.mc.player.getAbsorptionAmount() - n3 <= 0.5) {
                        continue;
                    }
                    if (n2 <= n) {
                        continue;
                    }
                    n = n2;
                    entityEnderCrystal = entityEnderCrystal2;
                }
            }
        }
        return entityEnderCrystal;
    }
    
    public void placeCrystal() {
        double n = 0.5;
        this.mainhand = (AutoCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal);
        this.offhand = (AutoCrystal.mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal);
        final List<BlockPos> possiblePlacePositions = CrystalUtils.possiblePlacePositions((float)this.placeRange.getValue(), this.placeUnderBlock.getValue(), true);
        for (final EntityPlayer target : AutoCrystal.mc.world.playerEntities) {
            if (Friends.isFriend(target.getName())) {
                continue;
            }
            if (target == AutoCrystal.mc.player) {
                continue;
            }
            if (!(target instanceof EntityPlayer)) {
                continue;
            }
            if (target.getDistance((Entity)AutoCrystal.mc.player) >= 11.0f) {
                continue;
            }
            if (target.isDead) {
                continue;
            }
            if (target.getHealth() <= 0.0f) {
                continue;
            }
            for (final BlockPos position : possiblePlacePositions) {
                final double n2 = DamageUtils.calculateDamage(position, (Entity)target);
                final double n3 = DamageUtils.calculateDamage(position, (Entity)AutoCrystal.mc.player);
                final double value = this.maxSelfDmg.getValue();
                final boolean b = this.facePlaceCheck.getValue() && AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                double value2;
                if (this.facePlace.getValue() && target.getHealth() + target.getAbsorptionAmount() <= this.facePlaceHp.getValue() && !b && !this.shouldActionArmor((EntityPlayer)AutoCrystal.mc.player, true)) {
                    value2 = 2.0;
                }
                else if (this.shouldActionArmor(target, false) && !b && !this.shouldActionArmor((EntityPlayer)AutoCrystal.mc.player, true)) {
                    value2 = 2.0;
                }
                else {
                    value2 = this.minDmg.getValue();
                }
                if (n2 > value2 && value > n3) {
                    if (n2 <= n) {
                        continue;
                    }
                    n = n2;
                    this.position = position;
                    AutoCrystal.target = target;
                }
            }
        }
        if (n == 0.5) {
            this.render = null;
            return;
        }
        if (this.place.getValue() && (this.offhand || (this.mainhand && this.placeTimer.passedMs(this.placeDelay.getValue())))) {
            final boolean b2 = this.pauseWhileEating.getValue() && this.isEatingGap();
            final boolean b3 = this.pauseWhileMining.getValue() && this.isHittingBlock();
            if (!this.offhand && AutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && this.autoSwitch.getValue() && !b2 && !b3) {
                if (this.findCrystalsHotbar() == -1) {
                    return;
                }
                AutoCrystal.mc.player.inventory.currentItem = this.findCrystalsHotbar();
            }
            else {
                this.render = this.position;
                final RayTraceResult rayTraceBlocks = AutoCrystal.mc.world.rayTraceBlocks(new Vec3d(AutoCrystal.mc.player.posX, AutoCrystal.mc.player.posY + AutoCrystal.mc.player.getEyeHeight(), AutoCrystal.mc.player.posZ), new Vec3d(this.position.getX() + 0.5, this.position.getY() - 0.5, this.position.getZ() + 0.5));
                final EnumFacing enumFacing = (rayTraceBlocks == null || rayTraceBlocks.sideHit == null) ? EnumFacing.UP : rayTraceBlocks.sideHit;
                this.isPlacing = true;
                AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.position, enumFacing, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                this.isPlacing = false;
            }
        }
    }
    
    public boolean shouldActionArmor(final EntityPlayer entityPlayer, final boolean b) {
        if (b) {
            for (final ItemStack itemStack : entityPlayer.getArmorInventoryList()) {
                if (itemStack == null || itemStack.getItem() == Items.AIR) {
                    return true;
                }
                final float n = (itemStack.getMaxDamage() - itemStack.getItemDamage()) / (float)itemStack.getMaxDamage() * 100.0f;
                if (this.armorCheck.getValue() && this.checkValue.getValue() >= n) {
                    return true;
                }
            }
        }
        else {
            for (final ItemStack itemStack2 : entityPlayer.getArmorInventoryList()) {
                if (itemStack2 == null || itemStack2.getItem() == Items.AIR) {
                    return true;
                }
                final float n2 = (itemStack2.getMaxDamage() - itemStack2.getItemDamage()) / (float)itemStack2.getMaxDamage() * 100.0f;
                if (this.armorDestroy.getValue() && this.armorPercent.getValue() >= n2) {
                    return true;
                }
            }
        }
        return false;
    }
}
