//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui;

import java.io.IOException;
import java.awt.Color;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import java.util.Iterator;
import me.zoom.xannax.module.Module;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class ClickGUI extends GuiScreen
{
    public static /* synthetic */ int color;
    public static /* synthetic */ ArrayList<Frames> frames;
    
    public void initGui() {
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public ClickGUI() {
        ClickGUI.frames = new ArrayList<Frames>();
        int x = 5;
        final Module.Category[] values = Module.Category.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            final Frames e = new Frames(values[i]);
            e.setX(x);
            ClickGUI.frames.add(e);
            x += e.getWidth() + 10;
        }
    }
    
    protected void mouseReleased(final int n, final int n2, final int n3) {
        final Iterator<Frames> iterator = ClickGUI.frames.iterator();
        while (iterator.hasNext()) {
            iterator.next().setDrag(false);
        }
        for (final Frames frames : ClickGUI.frames) {
            if (frames.isOpen() && !frames.getComponents().isEmpty()) {
                final Iterator<Component> iterator3 = frames.getComponents().iterator();
                while (iterator3.hasNext()) {
                    iterator3.next().mouseReleased(n, n2, n3);
                }
            }
        }
    }
    
    public static Frames getFrameByName(final String s) {
        Frames frames = null;
        for (final Frames frames2 : getFrames()) {
            if (s.equalsIgnoreCase(String.valueOf(frames2.category))) {
                frames = frames2;
            }
        }
        return frames;
    }
    
    static {
        ClickGUI.color = -1;
    }
    
    public static ArrayList<Frames> getFrames() {
        return ClickGUI.frames;
    }
    
    protected void keyTyped(final char c, final int n) {
        for (final Frames frames : ClickGUI.frames) {
            if (frames.isOpen() && !frames.getComponents().isEmpty()) {
                final Iterator<Component> iterator2 = frames.getComponents().iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().keyTyped(c, n);
                }
            }
        }
        if (n == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }
    
    public void drawScreen(final int n, final int n2, final float n3) {
        ClickGUI.color = new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), ClickGuiModule.opacity.getValue()).getRGB();
        for (final Frames frames : ClickGUI.frames) {
            frames.renderGUIFrame(this.fontRenderer);
            frames.updatePosition(n, n2);
            frames.updateMouseWheel();
            final Iterator<Component> iterator2 = frames.getComponents().iterator();
            while (iterator2.hasNext()) {
                iterator2.next().updateComponent(n, n2);
            }
        }
    }
    
    protected void mouseClicked(final int n, final int n2, final int n3) throws IOException {
        for (final Frames frames : ClickGUI.frames) {
            if (frames.isWithinHeader(n, n2) && n3 == 0) {
                frames.setDrag(true);
                frames.dragX = n - frames.getX();
                frames.dragY = n2 - frames.getY();
            }
            if (frames.isWithinHeader(n, n2) && n3 == 1) {
                frames.setOpen(!frames.isOpen());
            }
            if (frames.isOpen() && !frames.getComponents().isEmpty()) {
                final Iterator<Component> iterator2 = frames.getComponents().iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().mouseClicked(n, n2, n3);
                }
            }
        }
    }
}
