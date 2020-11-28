//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import me.zoom.xannax.util.ColorHolder;
import me.zoom.xannax.util.friend.Friends;
import net.minecraft.nbt.NBTTagList;
import me.zoom.xannax.util.DamageUtils;
import net.minecraft.enchantment.Enchantment;
import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import me.zoom.xannax.event.events.RenderEvent;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.entity.Entity;
import me.zoom.xannax.util.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import java.awt.Color;
import java.awt.Rectangle;
import org.lwjgl.opengl.GL11;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Nametags extends Module
{
    /* synthetic */ Setting.Boolean heldStackName;
    /* synthetic */ Setting.Boolean gamemode;
    /* synthetic */ Setting.Integer blue;
    /* synthetic */ Setting.Boolean ping;
    /* synthetic */ Setting.Boolean entityID;
    private static /* synthetic */ Nametags INSTANCE;
    /* synthetic */ Setting.Double scaling;
    /* synthetic */ Setting.Boolean armor;
    /* synthetic */ Setting.Boolean outline;
    /* synthetic */ Setting.Boolean smartScale;
    /* synthetic */ Setting.Boolean sneak;
    /* synthetic */ Setting.Double oWidth;
    /* synthetic */ Setting.Double factor;
    /* synthetic */ Setting.Boolean health;
    /* synthetic */ Setting.Boolean whiter;
    /* synthetic */ Setting.Integer green;
    /* synthetic */ Setting.Integer red;
    /* synthetic */ Setting.Boolean scaleing;
    /* synthetic */ Setting.Boolean invisibles;
    
    public static void glColor(final float n, final int n2, final int n3, final int n4) {
        GL11.glColor4f(0.003921569f * n2, 0.003921569f * n3, 0.003921569f * n4, n);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void drawRect(final float n, final float n2, final float n3, final float n4, final int n5) {
        enableGL2D();
        glColor(n5);
        drawRect(n, n2, n3, n4);
        disableGL2D();
    }
    
    public static void drawRect(final Rectangle rectangle, final int n) {
        drawRect((float)rectangle.x, (float)rectangle.y, (float)(rectangle.x + rectangle.width), (float)(rectangle.y + rectangle.height), n);
    }
    
    public static void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    private String getDisplayTag(final EntityPlayer entityPlayer) {
        String getFormattedText = entityPlayer.getDisplayName().getFormattedText();
        if (getFormattedText.contains(Nametags.mc.getSession().getUsername())) {
            getFormattedText = "You";
        }
        if (!this.health.getValue()) {
            return getFormattedText;
        }
        final float health = EntityUtil.getHealth((Entity)entityPlayer);
        String s;
        if (health > 18.0f) {
            s = TextFormatting.GREEN.toString();
        }
        else if (health > 16.0f) {
            s = TextFormatting.DARK_GREEN.toString();
        }
        else if (health > 12.0f) {
            s = TextFormatting.YELLOW.toString();
        }
        else if (health > 8.0f) {
            s = TextFormatting.GOLD.toString();
        }
        else if (health > 5.0f) {
            s = TextFormatting.RED.toString();
        }
        else {
            s = TextFormatting.DARK_RED.toString();
        }
        String string = "";
        if (this.ping.getValue()) {
            try {
                string = string + Objects.requireNonNull(Nametags.mc.getConnection()).getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime() + "ms ";
            }
            catch (Exception ex) {}
        }
        String string2 = "";
        if (this.entityID.getValue()) {
            string2 = string2 + "ID: " + entityPlayer.getEntityId() + " ";
        }
        String s2 = "";
        if (this.gamemode.getValue()) {
            if (entityPlayer.isCreative()) {
                s2 += "[C] ";
            }
            else if (entityPlayer.isSpectator() || entityPlayer.isInvisible()) {
                s2 += "[I] ";
            }
            else {
                s2 += "[S] ";
            }
        }
        String str;
        if (Math.floor(health) == health) {
            str = getFormattedText + s + " " + ((health > 0.0f) ? Integer.valueOf((int)Math.floor(health)) : "dead");
        }
        else {
            str = getFormattedText + s + " " + ((health > 0.0f) ? Integer.valueOf((int)health) : "dead");
        }
        return string + string2 + s2 + str;
    }
    
    private void setInstance() {
        Nametags.INSTANCE = this;
    }
    
    public static void drawRect(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final float n8) {
        enableGL2D();
        GL11.glColor4f(n5, n6, n7, n8);
        drawRect(n, n2, n3, n4);
        disableGL2D();
    }
    
    @Override
    public void onWorldRender(final RenderEvent renderEvent) {
        for (final EntityPlayer entityPlayer : Nametags.mc.world.playerEntities) {
            if (entityPlayer != null && !entityPlayer.equals((Object)Nametags.mc.player) && entityPlayer.isEntityAlive() && (!entityPlayer.isInvisible() || this.invisibles.getValue())) {
                this.renderNameTag(entityPlayer, this.interpolate(entityPlayer.lastTickPosX, entityPlayer.posX, renderEvent.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosX, this.interpolate(entityPlayer.lastTickPosY, entityPlayer.posY, renderEvent.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosY, this.interpolate(entityPlayer.lastTickPosZ, entityPlayer.posZ, renderEvent.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosZ, renderEvent.getPartialTicks());
            }
        }
    }
    
    private void renderItemStack(final ItemStack itemStack, final int n, final int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        Nametags.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        Nametags.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, n, n2);
        Nametags.mc.getRenderItem().renderItemOverlays(Nametags.mc.fontRenderer, itemStack, n, n2);
        Nametags.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(itemStack, n, n2);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }
    
    public static Nametags getInstance() {
        if (Nametags.INSTANCE == null) {
            Nametags.INSTANCE = new Nametags();
        }
        return Nametags.INSTANCE;
    }
    
    public static void drawRect(final float n, final float n2, final float n3, final float n4) {
        GL11.glBegin(7);
        GL11.glVertex2f(n, n4);
        GL11.glVertex2f(n3, n4);
        GL11.glVertex2f(n3, n2);
        GL11.glVertex2f(n, n2);
        GL11.glEnd();
    }
    
    private void renderEnchantmentText(final ItemStack itemStack, final int n, final int n2) {
        int n3 = n2 - 8;
        if (itemStack.getItem() == Items.GOLDEN_APPLE && itemStack.hasEffect()) {
            FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), "god", n * 2, n3, -3977919);
            n3 -= 8;
        }
        final NBTTagList getEnchantmentTagList = itemStack.getEnchantmentTagList();
        for (int i = 0; i < getEnchantmentTagList.tagCount(); ++i) {
            final short getShort = getEnchantmentTagList.getCompoundTagAt(i).getShort("id");
            final short getShort2 = getEnchantmentTagList.getCompoundTagAt(i).getShort("lvl");
            final Enchantment getEnchantmentByID = Enchantment.getEnchantmentByID((int)getShort);
            if (getEnchantmentByID != null) {
                FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), (getEnchantmentByID.isCurse() ? (TextFormatting.RED + getEnchantmentByID.getTranslatedName((int)getShort2).substring(11).substring(0, 1).toLowerCase()) : getEnchantmentByID.getTranslatedName((int)getShort2).substring(0, 1).toLowerCase()) + getShort2, n * 2, n3, -1);
                n3 -= 8;
            }
        }
        if (DamageUtils.hasDurability(itemStack)) {
            final int roundedDamage = DamageUtils.getRoundedDamage(itemStack);
            String str;
            if (roundedDamage >= 60) {
                str = TextFormatting.GREEN.toString();
            }
            else if (roundedDamage >= 25) {
                str = TextFormatting.YELLOW.toString();
            }
            else {
                str = TextFormatting.RED.toString();
            }
            FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), str + roundedDamage + "%", n * 2, n3, -1);
        }
    }
    
    private int getDisplayColour(final EntityPlayer entityPlayer) {
        int n = -5592406;
        if (this.whiter.getValue()) {
            n = -1;
        }
        if (Friends.isFriend(entityPlayer.getName())) {
            return -11157267;
        }
        if (entityPlayer.isInvisible()) {
            n = -1113785;
        }
        else if (entityPlayer.isSneaking() && this.sneak.getValue()) {
            n = -6481515;
        }
        return n;
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void glColor(final int n) {
        GL11.glColor4f((n >> 16 & 0xFF) / 255.0f, (n >> 8 & 0xFF) / 255.0f, (n & 0xFF) / 255.0f, (n >> 24 & 0xFF) / 255.0f);
    }
    
    static {
        Nametags.INSTANCE = new Nametags();
    }
    
    private double interpolate(final double n, final double n2, final float n3) {
        return n + (n2 - n) * n3;
    }
    
    public Nametags() {
        super("Nametags", "Nametags", Category.Render);
    }
    
    public static void drawBorderedRectReliant(final float n, final float n2, final float n3, final float n4, final float n5, final int n6, final int n7) {
        enableGL2D();
        drawRect(n, n2, n3, n4, n6);
        glColor(n7);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(n5);
        GL11.glBegin(3);
        GL11.glVertex2f(n, n2);
        GL11.glVertex2f(n, n4);
        GL11.glVertex2f(n3, n4);
        GL11.glVertex2f(n3, n2);
        GL11.glVertex2f(n, n2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        disableGL2D();
    }
    
    private void renderNameTag(final EntityPlayer entityPlayer, final double n, final double n2, final double n3, final float n4) {
        final double n5 = n2 + (entityPlayer.isSneaking() ? 0.5 : 0.7);
        final Entity getRenderViewEntity = Nametags.mc.getRenderViewEntity();
        assert getRenderViewEntity != null;
        final double posX = getRenderViewEntity.posX;
        final double posY = getRenderViewEntity.posY;
        final double posZ = getRenderViewEntity.posZ;
        getRenderViewEntity.posX = this.interpolate(getRenderViewEntity.prevPosX, getRenderViewEntity.posX, n4);
        getRenderViewEntity.posY = this.interpolate(getRenderViewEntity.prevPosY, getRenderViewEntity.posY, n4);
        getRenderViewEntity.posZ = this.interpolate(getRenderViewEntity.prevPosZ, getRenderViewEntity.posZ, n4);
        final String displayTag = this.getDisplayTag(entityPlayer);
        final double getDistance = getRenderViewEntity.getDistance(n + Nametags.mc.getRenderManager().viewerPosX, n2 + Nametags.mc.getRenderManager().viewerPosY, n3 + Nametags.mc.getRenderManager().viewerPosZ);
        final int n6 = FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), displayTag) / 2;
        double n7 = (0.0018 + this.scaling.getValue() * (getDistance * this.factor.getValue())) / 1000.0;
        if (getDistance <= 8.0 && this.smartScale.getValue()) {
            n7 = 0.0245;
        }
        if (!this.scaleing.getValue()) {
            n7 = this.scaling.getValue() / 100.0;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)n, (float)n5 + 1.4f, (float)n3);
        GlStateManager.rotate(-Nametags.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(Nametags.mc.getRenderManager().playerViewX, (Nametags.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-n7, -n7, n7);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        if (this.outline.getValue()) {
            drawBorderedRectReliant((float)(-n6 - 1), (float)(-Nametags.mc.fontRenderer.FONT_HEIGHT), (float)(n6 + 2), 1.0f, (float)this.oWidth.getValue(), 1426064384, ColorHolder.toHex(this.red.getValue(), this.green.getValue(), this.blue.getValue()));
        }
        else {
            drawBorderedRectReliant((float)(-n6 - 1), (float)(-Nametags.mc.fontRenderer.FONT_HEIGHT), (float)(n6 + 2), 1.0f, 1.8f, 1426064384, 855638016);
        }
        GlStateManager.disableBlend();
        final ItemStack copy = entityPlayer.getHeldItemMainhand().copy();
        if (copy.hasEffect() && (copy.getItem() instanceof ItemTool || copy.getItem() instanceof ItemArmor)) {
            copy.stackSize = 1;
        }
        if (this.heldStackName.getValue() && !copy.isEmpty && copy.getItem() != Items.AIR) {
            final String getDisplayName = copy.getDisplayName();
            final int n8 = FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), getDisplayName) / 2;
            GL11.glPushMatrix();
            GL11.glScalef(0.75f, 0.75f, 0.0f);
            FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), getDisplayName, -n8, (int)(-(this.getBiggestArmorTag(entityPlayer) + 20.0f)), -1);
            GL11.glScalef(1.5f, 1.5f, 1.0f);
            GL11.glPopMatrix();
        }
        if (this.armor.getValue()) {
            GlStateManager.pushMatrix();
            int n9 = -8;
            final Iterator iterator = entityPlayer.inventory.armorInventory.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() != null) {
                    n9 -= 8;
                }
            }
            n9 -= 8;
            final ItemStack copy2 = entityPlayer.getHeldItemOffhand().copy();
            if (copy2.hasEffect() && (copy2.getItem() instanceof ItemTool || copy2.getItem() instanceof ItemArmor)) {
                copy2.stackSize = 1;
            }
            this.renderItemStack(copy2, n9, -26);
            n9 += 16;
            for (final ItemStack itemStack : entityPlayer.inventory.armorInventory) {
                if (itemStack != null) {
                    final ItemStack copy3 = itemStack.copy();
                    if (copy3.hasEffect() && (copy3.getItem() instanceof ItemTool || copy3.getItem() instanceof ItemArmor)) {
                        copy3.stackSize = 1;
                    }
                    this.renderItemStack(copy3, n9, -26);
                    n9 += 16;
                }
            }
            this.renderItemStack(copy, n9, -26);
            GlStateManager.popMatrix();
        }
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), displayTag, -n6, -(FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) - 1), this.getDisplayColour(entityPlayer));
        getRenderViewEntity.posX = posX;
        getRenderViewEntity.posY = posY;
        getRenderViewEntity.posZ = posZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }
    
    @Override
    public void setup() {
        this.health = this.registerBoolean("Health", "Health", true);
        this.armor = this.registerBoolean("Armor", "Armor", true);
        this.scaling = this.registerDouble("Size", "Size", 0.30000001192092896, 0.10000000149011612, 20.0);
        this.invisibles = this.registerBoolean("Invisibles", "Invisibles", false);
        this.ping = this.registerBoolean("Ping", "Ping", true);
        this.gamemode = this.registerBoolean("Gamemode", "Gamemode", false);
        this.entityID = this.registerBoolean("ID", "ID", false);
        this.sneak = this.registerBoolean("SneakColor", "SneakColor", false);
        this.heldStackName = this.registerBoolean("StackName", "StackName", false);
        this.whiter = this.registerBoolean("White", "White", false);
        this.scaleing = this.registerBoolean("Scale", "Scale", false);
        this.factor = this.registerDouble("Factor", "Factor", 0.30000001192092896, 0.10000000149011612, 1.0);
        this.smartScale = this.registerBoolean("SmartScale", "SmartScale", false);
        this.outline = this.registerBoolean("Outline", "Outline", false);
        this.oWidth = this.registerDouble("OWidth", "OWidth", 1.5, 0.0, 3.0);
        this.red = this.registerInteger("Red", "Red", 255, 0, 255);
        this.green = this.registerInteger("Green", "Green", 255, 0, 255);
        this.blue = this.registerInteger("Blue", "Blue", 255, 0, 255);
        this.setInstance();
    }
    
    private float getBiggestArmorTag(final EntityPlayer entityPlayer) {
        float n = 0.0f;
        boolean b = false;
        for (final ItemStack itemStack : entityPlayer.inventory.armorInventory) {
            float n2 = 0.0f;
            if (itemStack != null) {
                final NBTTagList getEnchantmentTagList = itemStack.getEnchantmentTagList();
                for (int i = 0; i < getEnchantmentTagList.tagCount(); ++i) {
                    if (Enchantment.getEnchantmentByID((int)getEnchantmentTagList.getCompoundTagAt(i).getShort("id")) != null) {
                        n2 += 8.0f;
                        b = true;
                    }
                }
            }
            if (n2 > n) {
                n = n2;
            }
        }
        final ItemStack copy = entityPlayer.getHeldItemMainhand().copy();
        if (copy.hasEffect()) {
            float n3 = 0.0f;
            final NBTTagList getEnchantmentTagList2 = copy.getEnchantmentTagList();
            for (int j = 0; j < getEnchantmentTagList2.tagCount(); ++j) {
                if (Enchantment.getEnchantmentByID((int)getEnchantmentTagList2.getCompoundTagAt(j).getShort("id")) != null) {
                    n3 += 8.0f;
                    b = true;
                }
            }
            if (n3 > n) {
                n = n3;
            }
        }
        final ItemStack copy2 = entityPlayer.getHeldItemOffhand().copy();
        if (copy2.hasEffect()) {
            float n4 = 0.0f;
            final NBTTagList getEnchantmentTagList3 = copy2.getEnchantmentTagList();
            for (int k = 0; k < getEnchantmentTagList3.tagCount(); ++k) {
                if (Enchantment.getEnchantmentByID((int)getEnchantmentTagList3.getCompoundTagAt(k).getShort("id")) != null) {
                    n4 += 8.0f;
                    b = true;
                }
            }
            if (n4 > n) {
                n = n4;
            }
        }
        return (b ? 0 : 20) + n;
    }
}
