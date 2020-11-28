//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui2.frame;

import java.awt.Color;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.clickgui2.buttons.KeybindComponent;
import me.zoom.xannax.clickgui2.buttons.IntegerComponent;
import me.zoom.xannax.clickgui2.buttons.DoubleComponent;
import me.zoom.xannax.clickgui2.buttons.BooleanComponent;
import me.zoom.xannax.clickgui2.buttons.ModeComponent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.Xannax;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import me.zoom.xannax.module.Module;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;

public class Buttons extends Component
{
    public /* synthetic */ Frames parent;
    public /* synthetic */ int settingHeight;
    /* synthetic */ int nameWidth;
    /* synthetic */ int centeredNameCoords;
    /* synthetic */ boolean hovering;
    public /* synthetic */ int animating;
    public /* synthetic */ boolean open;
    private static final /* synthetic */ ResourceLocation opengui;
    private final /* synthetic */ ArrayList<Component> subcomponents;
    private final /* synthetic */ int height;
    private /* synthetic */ boolean isHovered;
    public /* synthetic */ int offset;
    /* synthetic */ Minecraft mc;
    public /* synthetic */ Module mod;
    
    static {
        opengui = new ResourceLocation("minecraft:opengui.png");
    }
    
    public void drawOpenRender(final int n, final int n2) {
        GlStateManager.enableAlpha();
        this.mc.getTextureManager().bindTexture(Buttons.opengui);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        Gui.drawScaledCustomSizeModalRect(n, n2, 0.0f, 0.0f, 512, 512, 8, 8, 512.0f, 512.0f);
        GL11.glPopMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
    }
    
    @Override
    public int getButtonHeight() {
        if (this.open) {
            return this.height;
        }
        return 12;
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
            return this.height;
        }
        return 12;
    }
    
    public Buttons(final Module mod, final Frames parent, final int offset) {
        this.mc = Minecraft.getMinecraft();
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList<Component>();
        this.open = false;
        int n = offset + 12;
        this.nameWidth = 0;
        this.centeredNameCoords = 0;
        this.settingHeight = 12;
        this.hovering = false;
        this.animating = 0;
        if (Xannax.getInstance().settingsManager.getSettingsForMod(mod) != null && !Xannax.getInstance().settingsManager.getSettingsForMod(mod).isEmpty()) {
            for (final Setting setting : Xannax.getInstance().settingsManager.getSettingsForMod(mod)) {
                switch (setting.getType()) {
                    case MODE: {
                        this.subcomponents.add(new ModeComponent((Setting.Mode)setting, this, mod, n));
                        break;
                    }
                    case BOOLEAN: {
                        this.subcomponents.add(new BooleanComponent((Setting.Boolean)setting, this, n));
                        break;
                    }
                    case DOUBLE: {
                        this.subcomponents.add(new DoubleComponent((Setting.Double)setting, this, n));
                        break;
                    }
                    case INT: {
                        this.subcomponents.add(new IntegerComponent((Setting.Integer)setting, this, n));
                        break;
                    }
                }
                n += 12;
                this.settingHeight += 12;
            }
        }
        this.subcomponents.add(new KeybindComponent(this, n));
        this.height = n + 12 - offset;
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
    
    @Override
    public void renderComponent() {
        if (this.mod.isEnabled()) {
            GlStateManager.pushMatrix();
            this.mc.renderEngine.bindTexture(new ResourceLocation("minecraft:rainbow.png"));
            Renderer.renderImage(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX(), this.parent.getY() + this.offset + this.parent.getRainbowOffset(), this.parent.getWidth(), 12, 1920.0f, 1080.0f);
            GlStateManager.popMatrix();
        }
        else {
            Renderer.drawRectStatic(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, Renderer.getTransColor(false));
        }
        this.nameWidth = FontUtils.getStringWidth(false, this.mod.getName());
        this.centeredNameCoords = (this.parent.getWidth() - this.nameWidth) / 2;
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.mod.getName(), this.parent.getX() + 2, this.parent.getY() + this.offset + 2, Renderer.getFontColor(this.parent.isHovering(), this.parent.getAnimation()).getRGB());
        this.drawOpenRender(this.parent.getX() + this.parent.getWidth() - 12, this.parent.getY() + this.offset + 2);
        if (this.isMouseOnButton(this.parent.getMouseX(), this.parent.getMouseY()) && this.mod.getDescription() != null) {
            Renderer.drawRectStatic(0, 0, FontUtils.getStringWidth(false, this.mod.getDescription()) + 5, 12, new Color(0, 0, 0, 150));
            FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.mod.getDescription(), 2, 2, new Color(255, 255, 255, 255).getRGB());
        }
        if (this.open && !this.subcomponents.isEmpty()) {
            final Iterator<Component> iterator = this.subcomponents.iterator();
            while (iterator.hasNext()) {
                iterator.next().renderComponent();
            }
        }
    }
    
    public boolean isMouseOnButton(final int n, final int n2) {
        return n > this.parent.getX() && n < this.parent.getX() + this.parent.getWidth() && n2 > this.parent.getY() + this.offset && n2 < this.parent.getY() + 12 + this.offset;
    }
    
    @Override
    public void mouseReleased(final int n, final int n2, final int n3) {
        final Iterator<Component> iterator = this.subcomponents.iterator();
        while (iterator.hasNext()) {
            iterator.next().mouseReleased(n, n2, n3);
        }
    }
    
    @Override
    public void setOff(final int offset) {
        this.offset = offset;
        int off = this.offset + 12;
        final Iterator<Component> iterator = this.subcomponents.iterator();
        while (iterator.hasNext()) {
            iterator.next().setOff(off);
            off += 12;
        }
    }
    
    @Override
    public void keyTyped(final char c, final int n) {
        final Iterator<Component> iterator = this.subcomponents.iterator();
        while (iterator.hasNext()) {
            iterator.next().keyTyped(c, n);
        }
    }
}
