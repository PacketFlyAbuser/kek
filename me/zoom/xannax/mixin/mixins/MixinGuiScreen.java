//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.nbt.NBTTagCompound;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemShulkerBox;
import me.zoom.xannax.module.ModuleManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiScreen.class })
public class MixinGuiScreen
{
    /* synthetic */ FontRenderer fontRenderer;
    /* synthetic */ ResourceLocation resource;
    /* synthetic */ RenderItem itemRender;
    
    @Inject(method = { "renderToolTip" }, at = { @At("HEAD") }, cancellable = true)
    public void renderToolTip(final ItemStack itemStack, final int n, final int n2, final CallbackInfo callbackInfo) {
        this.resource = new ResourceLocation("textures/gui/container/shulker_box.png");
        if (ModuleManager.isModuleEnabled("ShulkerPreview") && itemStack.getItem() instanceof ItemShulkerBox) {
            final NBTTagCompound getTagCompound = itemStack.getTagCompound();
            if (getTagCompound != null && getTagCompound.hasKey("BlockEntityTag", 10)) {
                final NBTTagCompound getCompoundTag = getTagCompound.getCompoundTag("BlockEntityTag");
                if (getCompoundTag.hasKey("Items", 9)) {
                    callbackInfo.cancel();
                    final NonNullList withSize = NonNullList.withSize(27, (Object)ItemStack.EMPTY);
                    ItemStackHelper.loadAllItems(getCompoundTag, withSize);
                    GlStateManager.enableBlend();
                    GlStateManager.disableRescaleNormal();
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();
                    final int n3 = n + 4;
                    final int n4 = n2 - 30;
                    this.itemRender.zLevel = 300.0f;
                    Minecraft.getMinecraft().renderEngine.bindTexture(this.resource);
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(n3, n4, 7, 5, 162, 66);
                    this.fontRenderer.drawString(itemStack.getDisplayName(), n + 6, n2 - 28, Color.DARK_GRAY.getRGB());
                    GlStateManager.enableBlend();
                    GlStateManager.enableAlpha();
                    GlStateManager.enableTexture2D();
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    RenderHelper.enableGUIStandardItemLighting();
                    for (int i = 0; i < withSize.size(); ++i) {
                        final int n5 = n + 5 + i % 9 * 18;
                        final int n6 = n2 + 1 + (i / 9 - 1) * 18;
                        final ItemStack itemStack2 = (ItemStack)withSize.get(i);
                        this.itemRender.renderItemAndEffectIntoGUI(itemStack2, n5, n6);
                        this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemStack2, n5, n6, (String)null);
                    }
                    RenderHelper.disableStandardItemLighting();
                    this.itemRender.zLevel = 0.0f;
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    RenderHelper.enableStandardItemLighting();
                    GlStateManager.enableRescaleNormal();
                }
            }
        }
    }
    
    public MixinGuiScreen() {
        this.itemRender = Minecraft.getMinecraft().getRenderItem();
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
    }
}
