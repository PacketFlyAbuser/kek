//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import me.zoom.xannax.event.Event;

public class RenderEvent extends Event
{
    private final /* synthetic */ float partialTicks;
    private final /* synthetic */ Vec3d renderPos;
    private final /* synthetic */ Tessellator tessellator;
    
    public BufferBuilder getBuffer() {
        return this.tessellator.getBuffer();
    }
    
    public RenderEvent(final Tessellator tessellator, final Vec3d renderPos, final float partialTicks) {
        this.tessellator = tessellator;
        this.renderPos = renderPos;
        this.partialTicks = partialTicks;
    }
    
    public Vec3d getRenderPos() {
        return this.renderPos;
    }
    
    public void resetTranslation() {
        this.setTranslation(this.renderPos);
    }
    
    public Tessellator getTessellator() {
        return this.tessellator;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public void setTranslation(final Vec3d vec3d) {
        this.getBuffer().setTranslation(-vec3d.x, -vec3d.y, -vec3d.z);
    }
}
