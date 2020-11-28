// 
// Decompiled by Procyon v0.5.36
// 

package club.minnced.discord.rpc;

import com.sun.jna.Callback;
import java.util.Objects;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import com.sun.jna.Structure;

public class DiscordEventHandlers extends Structure
{
    public /* synthetic */ OnJoinRequest joinRequest;
    public /* synthetic */ OnStatus errored;
    public /* synthetic */ OnStatus disconnected;
    public /* synthetic */ OnReady ready;
    private static final /* synthetic */ List<String> FIELD_ORDER;
    public /* synthetic */ OnGameUpdate spectateGame;
    public /* synthetic */ OnGameUpdate joinGame;
    
    static {
        FIELD_ORDER = Collections.unmodifiableList((List<? extends String>)Arrays.asList("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest"));
    }
    
    protected List<String> getFieldOrder() {
        return DiscordEventHandlers.FIELD_ORDER;
    }
    
    public int hashCode() {
        return Objects.hash(this.ready, this.disconnected, this.errored, this.joinGame, this.spectateGame, this.joinRequest);
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiscordEventHandlers)) {
            return false;
        }
        final DiscordEventHandlers discordEventHandlers = (DiscordEventHandlers)o;
        return Objects.equals(this.ready, discordEventHandlers.ready) && Objects.equals(this.disconnected, discordEventHandlers.disconnected) && Objects.equals(this.errored, discordEventHandlers.errored) && Objects.equals(this.joinGame, discordEventHandlers.joinGame) && Objects.equals(this.spectateGame, discordEventHandlers.spectateGame) && Objects.equals(this.joinRequest, discordEventHandlers.joinRequest);
    }
    
    public interface OnReady extends Callback
    {
        void accept(final DiscordUser p0);
    }
    
    public interface OnJoinRequest extends Callback
    {
        void accept(final DiscordUser p0);
    }
    
    public interface OnStatus extends Callback
    {
        void accept(final int p0, final String p1);
    }
    
    public interface OnGameUpdate extends Callback
    {
        void accept(final String p0);
    }
}
