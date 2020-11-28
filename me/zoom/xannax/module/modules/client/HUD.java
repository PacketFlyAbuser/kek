//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.client;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import java.util.Objects;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.function.ToIntFunction;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import me.zoom.xannax.util.BlockUtils;
import me.zoom.xannax.Xannax;
import net.minecraft.init.Items;
import net.minecraft.potion.PotionEffect;
import java.util.Iterator;
import me.zoom.xannax.util.ColorHolder;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Comparator;
import net.minecraft.client.resources.I18n;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import me.zoom.xannax.util.TpsUtils;
import me.zoom.xannax.util.MathUtil;
import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.client.gui.ScaledResolution;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.util.RenderUtil;
import net.minecraft.item.ItemStack;
import java.awt.Color;
import java.text.DecimalFormat;
import me.zoom.xannax.setting.Setting;
import net.minecraft.client.renderer.RenderItem;
import me.zoom.xannax.module.Module;

public class HUD extends Module
{
    private static final /* synthetic */ RenderItem itemRender;
    /* synthetic */ Setting.Double playerScale;
    /* synthetic */ Setting.Integer holex;
    /* synthetic */ DecimalFormat format2;
    /* synthetic */ int potCount;
    /* synthetic */ Setting.Boolean Direction;
    /* synthetic */ Setting.Integer red;
    /* synthetic */ Setting.Integer playerViewerX;
    /* synthetic */ int sort;
    /* synthetic */ Setting.Boolean ArrayList;
    /* synthetic */ Setting.Boolean Hole;
    /* synthetic */ Setting.Boolean Watermark;
    /* synthetic */ Setting.Boolean pSortUp;
    /* synthetic */ int counter;
    /* synthetic */ Setting.Boolean Speed;
    /* synthetic */ Setting.Boolean FPS;
    /* synthetic */ Setting.Boolean Potions;
    /* synthetic */ Setting.Integer playerViewerY;
    /* synthetic */ Setting.Integer blue;
    /* synthetic */ Setting.Boolean ArrayListHot;
    /* synthetic */ Setting.Integer holey;
    /* synthetic */ Setting.Integer brightness;
    /* synthetic */ Setting.Boolean Greeter;
    /* synthetic */ Setting.Boolean tots;
    /* synthetic */ Setting.Boolean Ping;
    /* synthetic */ Setting.Integer GreeterX;
    /* synthetic */ Setting.Integer GreeterY;
    /* synthetic */ Setting.Boolean sortUp;
    /* synthetic */ Color color;
    private static final /* synthetic */ ItemStack totemm;
    /* synthetic */ Setting.Integer green;
    /* synthetic */ int modCount;
    /* synthetic */ Setting.Integer saturation;
    /* synthetic */ Setting.Boolean ArmorHud;
    /* synthetic */ Setting.Boolean XYZ;
    /* synthetic */ Setting.Boolean TPS;
    /* synthetic */ Setting.Integer speed;
    /* synthetic */ Setting.Boolean Time;
    /* synthetic */ Setting.Boolean WatermarkRainbow;
    /* synthetic */ Setting.Boolean playerViewer;
    public static /* synthetic */ Setting.Boolean potionIcons;
    /* synthetic */ Setting.Boolean rainbow;
    
    @Override
    public void onRender() {
        final boolean equals = HUD.mc.world.getBiome(HUD.mc.player.getPosition()).getBiomeName().equals("Hell");
        final int i = (int)HUD.mc.player.posX;
        final int j = (int)HUD.mc.player.posY;
        final int k = (int)HUD.mc.player.posZ;
        final float n = equals ? 8.0f : 0.125f;
        final int l = (int)(HUD.mc.player.posX * n);
        final int m = (int)(HUD.mc.player.posZ * n);
        this.counter = 0;
        final Color color = this.rainbow.getValue() ? new Color(RenderUtil.getRainbow(this.speed.getValue() * 100, 0, this.saturation.getValue() / 100.0f, this.brightness.getValue() / 100.0f)) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue());
        final Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue());
        this.color = new Color(color2.getRed(), color2.getGreen(), color2.getBlue());
        if (this.XYZ.getValue()) {
            this.drawStringWithShadow("XYZ " + ChatFormatting.GRAY + i + ", " + j + ", " + k + " " + ChatFormatting.RESET + "[" + ChatFormatting.GRAY + l + ", " + m + ChatFormatting.RESET + "]", 2, new ScaledResolution(HUD.mc).getScaledHeight() - (HUD.mc.ingameGUI.getChatGUI().getChatOpen() ? (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 14) : (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2)), this.color.getRGB());
        }
        if (this.Direction.getValue()) {
            this.drawStringWithShadow(ChatFormatting.GRAY + this.getFacing() + " " + ChatFormatting.RESET + "[" + ChatFormatting.GRAY + this.getTowards() + ChatFormatting.RESET + "]", 2, new ScaledResolution(HUD.mc).getScaledHeight() - (HUD.mc.ingameGUI.getChatGUI().getChatOpen() ? (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 14) : (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2)) - (this.XYZ.getValue() ? (FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2) : 0), this.color.getRGB());
        }
        if (this.Watermark.getValue()) {
            if (this.WatermarkRainbow.getValue()) {
                RenderUtil.drawRect(0.0f, 0.0f, (float)(FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "XannaX 0.6") + 4), (float)(FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 4), new Color(0, 0, 0, 110).getRGB());
                RenderUtil.drawRect(0.0f, 0.0f, (float)(FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "XannaX 0.6") + 4), 1.0f, this.color.getRGB());
                this.drawStringWithShadow("XannaX 0.6", 2, 2, this.color.getRGB());
            }
            else {
                this.drawStringWithShadow("XannaX 0.6", 2, 2, this.color.getRGB());
            }
        }
        if (this.Greeter.getValue()) {
            this.drawStringWithShadow(MathUtil.getTimeOfDay() + HUD.mc.player.getName(), this.GreeterX.getValue(), this.GreeterY.getValue(), this.color.getRGB());
        }
        if (this.Hole.getValue()) {
            this.renderHole(this.holex.getValue(), this.holey.getValue());
        }
        if (this.tots.getValue()) {
            this.renderTotemHUD();
        }
        if (this.Ping.getValue()) {
            if (this.pSortUp.getValue()) {
                this.drawStringWithShadow("Ping " + ChatFormatting.GRAY + this.getPing() + "ms", new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Ping " + ChatFormatting.GRAY + this.getPing() + "ms"), 0 + (this.Potions.getValue() ? (this.potCount * 10) : (this.counter * 10)), this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
            else {
                this.drawStringWithShadow("Ping " + ChatFormatting.GRAY + this.getPing() + "ms", new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Ping " + ChatFormatting.GRAY + this.getPing() + "ms"), new ScaledResolution(HUD.mc).getScaledHeight() + (this.Potions.getValue() ? (this.potCount * -10) : (this.counter * -10)) - 10, this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
        }
        if (this.TPS.getValue()) {
            if (this.pSortUp.getValue()) {
                this.drawStringWithShadow("TPS " + ChatFormatting.GRAY + String.format("%.2f", TpsUtils.getTickRate()), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "TPS " + ChatFormatting.GRAY + String.format("%.2f", TpsUtils.getTickRate())), 0 + (this.Potions.getValue() ? (this.potCount * 10) : (this.counter * 10)), this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
            else {
                this.drawStringWithShadow("TPS " + ChatFormatting.GRAY + String.format("%.2f", TpsUtils.getTickRate()), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "TPS " + ChatFormatting.GRAY + String.format("%.2f", TpsUtils.getTickRate())), new ScaledResolution(HUD.mc).getScaledHeight() + (this.Potions.getValue() ? (this.potCount * -10) : (this.counter * -10)) - 10, this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
        }
        if (this.Speed.getValue()) {
            final DecimalFormat decimalFormat = new DecimalFormat("#.#");
            final double n2 = Minecraft.getMinecraft().player.posX - Minecraft.getMinecraft().player.prevPosX;
            final double n3 = Minecraft.getMinecraft().player.posZ - Minecraft.getMinecraft().player.prevPosZ;
            final String format = decimalFormat.format(MathHelper.sqrt(n2 * n2 + n3 * n3) / (Minecraft.getMinecraft().timer.tickLength / 1000.0f) * 3.6);
            if (this.pSortUp.getValue()) {
                this.drawStringWithShadow("Speed " + ChatFormatting.GRAY + format + "km/h", new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Speed " + ChatFormatting.GRAY + format + "km/h"), 0 + (this.Potions.getValue() ? (this.potCount * 10) : (this.counter * 10)), this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
            else {
                this.drawStringWithShadow("Speed " + ChatFormatting.GRAY + format + "km/h", new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Speed " + ChatFormatting.GRAY + format + "km/h"), new ScaledResolution(HUD.mc).getScaledHeight() + (this.Potions.getValue() ? (this.potCount * -10) : (this.counter * -10)) - 10, this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
        }
        if (this.Time.getValue()) {
            if (this.pSortUp.getValue()) {
                this.drawStringWithShadow("Time " + ChatFormatting.GRAY + new SimpleDateFormat("h:mm a").format(new Date()), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Time " + ChatFormatting.GRAY + new SimpleDateFormat("h:mm a").format(new Date())), 0 + (this.Potions.getValue() ? (this.potCount * 10) : (this.counter * 10)), this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
            else {
                this.drawStringWithShadow("Time " + ChatFormatting.GRAY + new SimpleDateFormat("h:mm a").format(new Date()), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Time " + ChatFormatting.GRAY + new SimpleDateFormat("h:mm a").format(new Date())), new ScaledResolution(HUD.mc).getScaledHeight() + (this.Potions.getValue() ? (this.potCount * -10) : (this.counter * -10)) - 10, this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
        }
        if (this.playerViewer.getValue()) {
            this.drawPlayer();
        }
        if (this.FPS.getValue()) {
            if (this.pSortUp.getValue()) {
                this.drawStringWithShadow("FPS " + ChatFormatting.GRAY + Minecraft.getDebugFPS(), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "FPS " + ChatFormatting.GRAY + Minecraft.getDebugFPS()), 0 + (this.Potions.getValue() ? (this.potCount * 10) : (this.counter * 10)), this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
            else {
                this.drawStringWithShadow("FPS " + ChatFormatting.GRAY + Minecraft.getDebugFPS(), new ScaledResolution(HUD.mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "FPS " + ChatFormatting.GRAY + Minecraft.getDebugFPS()), new ScaledResolution(HUD.mc).getScaledHeight() + (this.Potions.getValue() ? (this.potCount * -10) : (this.counter * -10)) - 10, this.color.getRGB());
                ++this.potCount;
                ++this.counter;
            }
        }
        final float[] array = { System.currentTimeMillis() % 11520L / 11520.0f };
        if (this.Potions.getValue()) {
            this.potCount = 0;
            final ScaledResolution scaledResolution = new ScaledResolution(HUD.mc);
            try {
                final double n4;
                final int i2;
                final String str;
                final String s;
                final ScaledResolution scaledResolution2;
                final int n5;
                HUD.mc.player.getActivePotionEffects().forEach(potionEffect -> {
                    I18n.format(potionEffect.getPotion().getName(), new Object[0]);
                    n4 = potionEffect.getDuration() / 19.99f;
                    i2 = potionEffect.getAmplifier() + 1;
                    potionEffect.getPotion().getLiquidColor();
                    new StringBuilder().append(str).append(" ").append(i2).append(ChatFormatting.GRAY).append(" ").append((int)n4 / 60).append(":").append(this.format2.format(n4 % 60.0)).toString();
                    if (this.pSortUp.getValue()) {
                        this.drawStringWithShadow(s, scaledResolution2.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), s), 0 + this.potCount * 10, n5);
                        ++this.potCount;
                    }
                    else {
                        this.drawStringWithShadow(s, scaledResolution2.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), s), scaledResolution2.getScaledHeight() + this.potCount * -10 - 10, n5);
                        ++this.potCount;
                    }
                    return;
                });
            }
            catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
        if (this.ArrayList.getValue()) {
            final ScaledResolution scaledResolution3 = new ScaledResolution(HUD.mc);
            if (this.sortUp.getValue()) {
                this.sort = -1;
            }
            else {
                this.sort = 1;
            }
            this.modCount = 0;
            final Color color3;
            final ScaledResolution scaledResolution4;
            final Object o;
            final int n6;
            final int n7;
            ModuleManager.getModules().stream().filter(Module::isEnabled).filter(Module::isDrawn).sorted(Comparator.comparing(module -> FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), module.getName() + ChatFormatting.GRAY + " " + module.getHudInfo()) * -1)).forEach(module2 -> {
                this.color = new Color(color3.getRed(), color3.getGreen(), color3.getBlue());
                if (this.sortUp.getValue()) {
                    if (this.ArrayListHot.getValue()) {
                        RenderUtil.drawRect((float)(scaledResolution4.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), module2.getName() + ChatFormatting.GRAY + module2.getHudInfo()) - 4), (float)(0 + this.modCount * 10 - 1), (float)(this.getWidth(module2.getName() + ChatFormatting.GRAY + module2.getHudInfo()) + 4), 11.0f, new Color(21, 21, 21, 100).getRGB());
                        RenderUtil.drawRect((float)(scaledResolution4.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), module2.getName() + ChatFormatting.GRAY + module2.getHudInfo()) - 4), (float)(0 + this.modCount * 10 - 1), 2.0f, 11.0f, this.color.getRGB());
                    }
                    this.drawStringWithShadow(module2.getName() + ChatFormatting.GRAY + module2.getHudInfo(), scaledResolution4.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), module2.getName() + ChatFormatting.GRAY + module2.getHudInfo()), 0 + this.modCount * 10, this.color.getRGB());
                    o[n6] += 0.02f;
                    ++this.modCount;
                }
                else {
                    if (this.ArrayListHot.getValue()) {
                        RenderUtil.drawRect((float)(scaledResolution4.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), module2.getName() + ChatFormatting.GRAY + module2.getHudInfo()) - 4), (float)(scaledResolution4.getScaledHeight() + this.modCount * -10 - 1 - 10), (float)(this.getWidth(module2.getName() + ChatFormatting.GRAY + module2.getHudInfo()) + 4), 11.0f, new Color(21, 21, 21, 100).getRGB());
                        RenderUtil.drawRect((float)(scaledResolution4.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), module2.getName() + ChatFormatting.GRAY + module2.getHudInfo()) - 4), (float)(scaledResolution4.getScaledHeight() + this.modCount * -10 - 1 - 10), 2.0f, 11.0f, this.color.getRGB());
                    }
                    this.drawStringWithShadow(module2.getName() + ChatFormatting.GRAY + module2.getHudInfo(), scaledResolution4.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), module2.getName() + ChatFormatting.GRAY + module2.getHudInfo()), scaledResolution4.getScaledHeight() + this.modCount * -10 - 10, this.color.getRGB());
                    o[n7] += 0.02f;
                    ++this.modCount;
                }
                return;
            });
        }
        if (this.ArmorHud.getValue()) {
            GlStateManager.enableTexture2D();
            final ScaledResolution scaledResolution5 = new ScaledResolution(HUD.mc);
            final int n8 = scaledResolution5.getScaledWidth() / 2;
            int n9 = 0;
            final int n10 = scaledResolution5.getScaledHeight() - 55 - (HUD.mc.player.isInWater() ? 10 : 0);
            for (final ItemStack itemStack : HUD.mc.player.inventory.armorInventory) {
                ++n9;
                if (itemStack.isEmpty()) {
                    continue;
                }
                final int n11 = n8 - 90 + (9 - n9) * 20 + 2;
                GlStateManager.enableDepth();
                HUD.itemRender.zLevel = 200.0f;
                HUD.itemRender.renderItemAndEffectIntoGUI(itemStack, n11, n10);
                HUD.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, itemStack, n11, n10, "");
                HUD.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                final String s2 = (itemStack.getCount() > 1) ? (itemStack.getCount() + "") : "";
                HUD.mc.fontRenderer.drawStringWithShadow(s2, (float)(n11 + 19 - 2 - HUD.mc.fontRenderer.getStringWidth(s2)), (float)(n10 + 9), 16777215);
                final int n12 = 100 - (int)((1.0f - (itemStack.getMaxDamage() - (float)itemStack.getItemDamage()) / itemStack.getMaxDamage()) * 100.0f);
                this.drawStringWithShadow(n12 + "", n11 + 8 - HUD.mc.fontRenderer.getStringWidth(n12 + "") / 2, n10 - 11, ColorHolder.toHex(color2.getRed(), color2.getGreen(), color2.getBlue()));
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    
    static {
        tickRates = new float[20];
        itemRender = Minecraft.getMinecraft().getRenderItem();
        totemm = new ItemStack(Items.TOTEM_OF_UNDYING);
    }
    
    private int getWidth(final String s) {
        if (ModuleManager.isModuleEnabled("CustomFont")) {
            return Xannax.fontRenderer.getStringWidth(s);
        }
        return HUD.mc.fontRenderer.getStringWidth(s);
    }
    
    public static void sexyRainbow() {
        final String s = "XannaX 0.1";
        int n = 0;
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), String.valueOf(char1), 2 + n, 2, RenderUtil.generateRainbowFadingColor(s.length() - 1 - i, true));
            n += FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), String.valueOf(char1));
        }
    }
    
    private boolean eastObby() {
        return HUD.mc.world.getBlockState(new BlockPos(BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f)).east()).getBlock() == Blocks.OBSIDIAN;
    }
    
    public String getTowards() {
        switch (MathHelper.floor(HUD.mc.player.rotationYaw * 8.0f / 360.0f + 0.5) & 0x7) {
            case 0: {
                return "+Z";
            }
            case 1: {
                return "+Z";
            }
            case 2: {
                return "-X";
            }
            case 3: {
                return "-X";
            }
            case 4: {
                return "-Z";
            }
            case 5: {
                return "-Z";
            }
            case 6: {
                return "+X";
            }
            case 7: {
                return "+X";
            }
            default: {
                return "Invalid";
            }
        }
    }
    
    private void renderHole(final double n, final double n2) {
        final double n3 = n2 + 16.0;
        final double n4 = n + 16.0;
        final double n5 = n + 32.0;
        final double n6 = n2 + 16.0;
        final double n7 = n + 16.0;
        final double n8 = n2 + 32.0;
        final BlockPos blockPos = new BlockPos(BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f));
        switch (HUD.mc.getRenderViewEntity().getHorizontalFacing()) {
            case NORTH: {
                if (this.northObby() || this.northBrock()) {
                    this.renderItem(n4, n2, new ItemStack(HUD.mc.world.getBlockState(blockPos.north()).getBlock()));
                }
                if (this.westObby() || this.westBrock()) {
                    this.renderItem(n, n3, new ItemStack(HUD.mc.world.getBlockState(blockPos.west()).getBlock()));
                }
                if (this.eastObby() || this.eastBrock()) {
                    this.renderItem(n5, n6, new ItemStack(HUD.mc.world.getBlockState(blockPos.east()).getBlock()));
                }
                if (this.southObby() || this.southBrock()) {
                    this.renderItem(n7, n8, new ItemStack(HUD.mc.world.getBlockState(blockPos.south()).getBlock()));
                    break;
                }
                break;
            }
            case SOUTH: {
                if (this.southObby() || this.southBrock()) {
                    this.renderItem(n4, n2, new ItemStack(HUD.mc.world.getBlockState(blockPos.south()).getBlock()));
                }
                if (this.eastObby() || this.eastBrock()) {
                    this.renderItem(n, n3, new ItemStack(HUD.mc.world.getBlockState(blockPos.east()).getBlock()));
                }
                if (this.westObby() || this.westBrock()) {
                    this.renderItem(n5, n6, new ItemStack(HUD.mc.world.getBlockState(blockPos.west()).getBlock()));
                }
                if (this.northObby() || this.northBrock()) {
                    this.renderItem(n7, n8, new ItemStack(HUD.mc.world.getBlockState(blockPos.north()).getBlock()));
                    break;
                }
                break;
            }
            case WEST: {
                if (this.westObby() || this.westBrock()) {
                    this.renderItem(n4, n2, new ItemStack(HUD.mc.world.getBlockState(blockPos.west()).getBlock()));
                }
                if (this.southObby() || this.southBrock()) {
                    this.renderItem(n, n3, new ItemStack(HUD.mc.world.getBlockState(blockPos.south()).getBlock()));
                }
                if (this.northObby() || this.northBrock()) {
                    this.renderItem(n5, n6, new ItemStack(HUD.mc.world.getBlockState(blockPos.north()).getBlock()));
                }
                if (this.eastObby() || this.eastBrock()) {
                    this.renderItem(n7, n8, new ItemStack(HUD.mc.world.getBlockState(blockPos.east()).getBlock()));
                    break;
                }
                break;
            }
            case EAST: {
                if (this.eastObby() || this.eastBrock()) {
                    this.renderItem(n4, n2, new ItemStack(HUD.mc.world.getBlockState(blockPos.east()).getBlock()));
                }
                if (this.northObby() || this.northBrock()) {
                    this.renderItem(n, n3, new ItemStack(HUD.mc.world.getBlockState(blockPos.north()).getBlock()));
                }
                if (this.southObby() || this.southBrock()) {
                    this.renderItem(n5, n6, new ItemStack(HUD.mc.world.getBlockState(blockPos.south()).getBlock()));
                }
                if (this.westObby() || this.westBrock()) {
                    this.renderItem(n7, n8, new ItemStack(HUD.mc.world.getBlockState(blockPos.west()).getBlock()));
                    break;
                }
                break;
            }
        }
    }
    
    private void drawStringWithShadow(final String s, final int n, final int n2, final int n3) {
        if (ModuleManager.isModuleEnabled("CustomFont")) {
            Xannax.fontRenderer.drawStringWithShadow(s, n, n2, n3);
        }
        else {
            HUD.mc.fontRenderer.drawStringWithShadow(s, (float)n, (float)n2, n3);
        }
    }
    
    private boolean southObby() {
        return HUD.mc.world.getBlockState(new BlockPos(BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f)).south()).getBlock() == Blocks.OBSIDIAN;
    }
    
    public void renderTotemHUD() {
        final ScaledResolution scaledResolution = new ScaledResolution(HUD.mc);
        final int getScaledWidth = scaledResolution.getScaledWidth();
        final int getScaledHeight = scaledResolution.getScaledHeight();
        int sum = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            sum += HUD.mc.player.getHeldItemOffhand().getCount();
        }
        if (sum > 0) {
            GlStateManager.enableTexture2D();
            final int n = getScaledWidth / 2;
            final int n2 = getScaledHeight - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            final int n3 = n - 189 + 180 + 2;
            GlStateManager.enableDepth();
            HUD.itemRender.zLevel = 200.0f;
            HUD.itemRender.renderItemAndEffectIntoGUI(HUD.totemm, n3, n2);
            HUD.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.totemm, n3, n2, "");
            HUD.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.drawStringWithShadow(sum + "", n3 + 19 - 2 - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), sum + ""), n2 + 9, 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    
    @Override
    public void setup() {
        this.ArrayList = this.registerBoolean("ArrayList", "ArrayList", false);
        this.ArrayListHot = this.registerBoolean("ArrayList Hot", "ArrayListHot", true);
        this.sortUp = this.registerBoolean("Array Sort Up", "ArraySortUp", false);
        this.Potions = this.registerBoolean("Potions", "Potions", false);
        this.pSortUp = this.registerBoolean("Potions Sort Up", "PotionsSortUp", false);
        this.Watermark = this.registerBoolean("Watermark", "Watermark", false);
        this.WatermarkRainbow = this.registerBoolean("Watermark2", "Watermark2", false);
        this.Greeter = this.registerBoolean("Greeter", "Greeter", false);
        this.GreeterX = this.registerInteger("Greeter X", "GreeterX", 100, 0, 1000);
        this.GreeterY = this.registerInteger("Greeter Y", "GreeterY", 100, 0, 1000);
        this.ArmorHud = this.registerBoolean("ArmorHud", "ArmorHud", false);
        this.Hole = this.registerBoolean("Hole", "Hole", false);
        this.holex = this.registerInteger("Hole X", "HoleX", 0, 0, 1000);
        this.holey = this.registerInteger("Hole Y", "HoleY", 0, 0, 1000);
        this.tots = this.registerBoolean("Totems", "TotemsHUD", false);
        this.Ping = this.registerBoolean("Ping", "Ping", false);
        this.FPS = this.registerBoolean("FPS", "FPS", false);
        this.TPS = this.registerBoolean("TPS", "TPS", false);
        this.Time = this.registerBoolean("Time", "Time", false);
        this.Speed = this.registerBoolean("Speed", "Speed", false);
        this.XYZ = this.registerBoolean("XYZ", "XYZ", false);
        this.Direction = this.registerBoolean("Direction", "Direction", false);
        this.playerViewer = this.registerBoolean("Player Viewer", "PlayerViewer", false);
        this.playerViewerX = this.registerInteger("Player Viewer X", "PlayerViewerX", 0, 0, 1000);
        this.playerViewerY = this.registerInteger("Player Viewer Y", "PlayerViewerY", 0, 0, 1000);
        this.playerScale = this.registerDouble("Player Scale", "PlayerScale", 1.0, 0.1, 2.0);
        HUD.potionIcons = this.registerBoolean("PotionIcons", "PotionIcons", false);
        this.red = this.registerInteger("Red", "Red", 255, 0, 255);
        this.green = this.registerInteger("Green", "Green", 255, 0, 255);
        this.blue = this.registerInteger("Blue", "Blue", 255, 0, 255);
        this.rainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        this.saturation = this.registerInteger("Saturation", "Saturation", 50, 0, 100);
        this.brightness = this.registerInteger("Brightness", "Brightness", 50, 0, 100);
        this.speed = this.registerInteger("Speed", "Speed", 50, 1, 100);
    }
    
    public HUD() {
        super("HUD", "HUD", Category.Client);
        this.format2 = new DecimalFormat("00");
    }
    
    public int getPing() {
        int getResponseTime;
        if (HUD.mc.player == null || HUD.mc.getConnection() == null || HUD.mc.getConnection().getPlayerInfo(HUD.mc.player.getName()) == null) {
            getResponseTime = -1;
        }
        else {
            HUD.mc.player.getName();
            getResponseTime = Objects.requireNonNull(HUD.mc.getConnection().getPlayerInfo(HUD.mc.player.getName())).getResponseTime();
        }
        return getResponseTime;
    }
    
    private boolean eastBrock() {
        return HUD.mc.world.getBlockState(new BlockPos(BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f)).east()).getBlock() == Blocks.BEDROCK;
    }
    
    private boolean northBrock() {
        return HUD.mc.world.getBlockState(new BlockPos(BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f)).north()).getBlock() == Blocks.BEDROCK;
    }
    
    public String getFacing() {
        switch (MathHelper.floor(HUD.mc.player.rotationYaw * 8.0f / 360.0f + 0.5) & 0x7) {
            case 0: {
                return "South";
            }
            case 1: {
                return "South";
            }
            case 2: {
                return "West";
            }
            case 3: {
                return "West";
            }
            case 4: {
                return "North";
            }
            case 5: {
                return "North";
            }
            case 6: {
                return "East";
            }
            case 7: {
                return "East";
            }
            default: {
                return "Invalid";
            }
        }
    }
    
    private boolean westObby() {
        return HUD.mc.world.getBlockState(new BlockPos(BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f)).west()).getBlock() == Blocks.OBSIDIAN;
    }
    
    private boolean southBrock() {
        return HUD.mc.world.getBlockState(new BlockPos(BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f)).south()).getBlock() == Blocks.BEDROCK;
    }
    
    public void drawPlayer() {
        final EntityPlayerSP player = HUD.mc.player;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0f, 0.0f, 5.0f, 0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.playerViewerX.getValue() + 25), (float)(this.playerViewerY.getValue() + 25), 50.0f);
        GlStateManager.scale(-50.0 * this.playerScale.getValue(), 50.0 * this.playerScale.getValue(), 50.0 * this.playerScale.getValue());
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(this.playerViewerY.getValue() / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager getRenderManager = HUD.mc.getRenderManager();
        getRenderManager.setPlayerViewY(180.0f);
        getRenderManager.setRenderShadow(false);
        try {
            getRenderManager.renderEntity((Entity)player, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        }
        catch (Exception ex) {}
        getRenderManager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }
    
    private boolean westBrock() {
        return HUD.mc.world.getBlockState(new BlockPos(BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f)).west()).getBlock() == Blocks.BEDROCK;
    }
    
    private void renderItem(final double n, final double n2, final ItemStack itemStack) {
        RenderHelper.enableGUIStandardItemLighting();
        HUD.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int)n, (int)n2);
        RenderHelper.disableStandardItemLighting();
    }
    
    private boolean northObby() {
        return HUD.mc.world.getBlockState(new BlockPos(BlockUtils.getInterpolatedPos((Entity)HUD.mc.player, 0.0f)).north()).getBlock() == Blocks.OBSIDIAN;
    }
}
