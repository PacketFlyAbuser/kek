// 
// Decompiled by Procyon v0.5.36
// 

package club.minnced.discord.rpc;

import com.sun.jna.Native;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import com.sun.jna.Library;

public interface DiscordRPC extends Library
{
    void Discord_Respond(@Nonnull final String p0, final int p1);
    
    void Discord_RunCallbacks();
    
    void Discord_Shutdown();
    
    void Discord_RegisterSteamGame(final String p0, final String p1);
    
    void Discord_UpdatePresence(@Nullable final DiscordRichPresence p0);
    
    void Discord_UpdateConnection();
    
    void Discord_Initialize(@Nonnull final String p0, @Nullable final DiscordEventHandlers p1, final boolean p2, @Nullable final String p3);
    
    void Discord_Register(final String p0, final String p1);
    
    void Discord_UpdateHandlers(@Nullable final DiscordEventHandlers p0);
    
    void Discord_ClearPresence();
}
