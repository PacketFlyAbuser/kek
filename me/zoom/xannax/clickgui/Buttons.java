//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui;

import me.zoom.xannax.clickgui.buttons.KeybindComponent;
import me.zoom.xannax.clickgui.buttons.IntegerComponent;
import me.zoom.xannax.clickgui.buttons.DoubleComponent;
import me.zoom.xannax.clickgui.buttons.BooleanComponent;
import me.zoom.xannax.clickgui.buttons.ModeComponent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.clickgui2.frame.Renderer;
import me.zoom.xannax.module.ModuleManager;
import java.awt.Color;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import me.zoom.xannax.module.Module;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;

public class Buttons extends Component
{
    private static final /* synthetic */ ResourceLocation opengui;
    private static final /* synthetic */ ResourceLocation closedgui;
    public /* synthetic */ int offset;
    public /* synthetic */ boolean open;
    /* synthetic */ boolean hovering;
    public /* synthetic */ Frames parent;
    private final /* synthetic */ ArrayList<Component> subcomponents;
    public /* synthetic */ Module mod;
    private /* synthetic */ boolean isHovered;
    private final /* synthetic */ int height;
    
    public void drawClosedRender(final int n, final int n2) {
        GlStateManager.enableAlpha();
        this.mc.getTextureManager().bindTexture(Buttons.closedgui);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        Gui.drawScaledCustomSizeModalRect(n, n2, 0.0f, 0.0f, 256, 256, 10, 10, 256.0f, 256.0f);
        GL11.glPopMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
    }
    
    @Override
    public void setOff(final int offset) {
        this.offset = offset;
        int off = this.offset + 16;
        final Iterator<Component> iterator = this.subcomponents.iterator();
        while (iterator.hasNext()) {
            iterator.next().setOff(off);
            off += 16;
        }
    }
    
    static {
        opengui = new ResourceLocation("minecraft:opengui.png");
        closedgui = new ResourceLocation("minecraft:closedgui.png");
    }
    
    @Override
    public void mouseClicked(final int n, final int n2, final int n3) {
        if (this.isMouseOnButton(n, n2) && n3 == 0) {
            this.mod.toggle();
        }
        if (this.isMouseOnButton(n, n2) && n3 == 1) {
            this.open = !this.open;
            this.parent.refresh();
        }
        final Iterator<Component> iterator = this.subcomponents.iterator();
        while (iterator.hasNext()) {
            iterator.next().mouseClicked(n, n2, n3);
        }
    }
    
    public void drawOpenRender(final int n, final int n2) {
        GlStateManager.enableAlpha();
        this.mc.getTextureManager().bindTexture(Buttons.opengui);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        Gui.drawScaledCustomSizeModalRect(n, n2, 0.0f, 0.0f, 256, 256, 10, 10, 256.0f, 256.0f);
        GL11.glPopMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
    }
    
    public boolean isMouseOnButton(final int n, final int n2) {
        return n > this.parent.getX() && n < this.parent.getX() + this.parent.getWidth() && n2 > this.parent.getY() + this.offset && n2 < this.parent.getY() + 16 + this.offset;
    }
    
    @Override
    public void updateComponent(final int n, final int n2) {
        this.isHovered = this.isMouseOnButton(n, n2);
        if (!this.subcomponents.isEmpty()) {
            final Iterator<Component> iterator = this.subcomponents.iterator();
            while (iterator.hasNext()) {
                iterator.next().updateComponent(n, n2);
            }
        }
    }
    
    @Override
    public int getHeight() {
        if (this.open) {
            return 16 * (this.subcomponents.size() + 1);
        }
        return 16;
    }
    
    @Override
    public void renderComponent() {
        ClickGUI.color = new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), ClickGuiModule.opacity.getValue()).getRGB();
        Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset + 1, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 16 + this.offset, this.isHovered ? (this.mod.isEnabled() ? ClickGUI.color : new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).darker().darker().getRGB()) : (this.mod.isEnabled() ? ClickGUI.color : new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).getRGB()));
        Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + this.offset + 1, new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).getRGB());
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.mod.getName(), this.parent.getX() + 2, this.parent.getY() + this.offset + 2 + 2, Renderer.getFontColor(this.parent.isHovering(), this.parent.getAnimation()).getRGB());
        if (this.subcomponents.size() > 1) {
            FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.open ? "-" : "+", this.parent.getX() + this.parent.getWidth() - 10, this.parent.getY() + this.offset + 2 + 2, Renderer.getFontColor(this.parent.isHovering(), this.parent.getAnimation()).getRGB());
        }
        if (this.open && !this.subcomponents.isEmpty()) {
            final Iterator<Component> iterator = this.subcomponents.iterator();
            while (iterator.hasNext()) {
                iterator.next().renderComponent();
            }
        }
    }
    
    @Override
    public void mouseReleased(final int n, final int n2, final int n3) {
        final Iterator<Component> iterator = this.subcomponents.iterator();
        while (iterator.hasNext()) {
            iterator.next().mouseReleased(n, n2, n3);
        }
    }
    
    @Override
    public void keyTyped(final char c, final int n) {
        final Iterator<Component> iterator = this.subcomponents.iterator();
        while (iterator.hasNext()) {
            iterator.next().keyTyped(c, n);
        }
    }
    
    public Buttons(final Module mod, final Frames parent, final int offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList<Component>();
        this.open = false;
        this.height = 16;
        this.hovering = false;
        int n = offset + 16;
        if (Xannax.getInstance().settingsManager.getSettingsForMod(mod) != null && !Xannax.getInstance().settingsManager.getSettingsForMod(mod).isEmpty()) {
            for (final Setting setting : Xannax.getInstance().settingsManager.getSettingsForMod(mod)) {
                switch (setting.getType()) {
                    case MODE: {
                        this.subcomponents.add(new ModeComponent((Setting.Mode)setting, this, mod, n));
                        n += 16;
                        continue;
                    }
                    case BOOLEAN: {
                        this.subcomponents.add(new BooleanComponent((Setting.Boolean)setting, this, n));
                        n += 16;
                        continue;
                    }
                    case DOUBLE: {
                        this.subcomponents.add(new DoubleComponent((Setting.Double)setting, this, n));
                        n += 16;
                        continue;
                    }
                    case INT: {
                        this.subcomponents.add(new IntegerComponent((Setting.Integer)setting, this, n));
                        n += 16;
                        continue;
                    }
                    default: {
                        continue;
                    }
                }
            }
        }
        this.subcomponents.add(new KeybindComponent(this, n));
    }
}
