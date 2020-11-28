// 
// Decompiled by Procyon v0.5.36
// 

package club.minnced.discord.rpc;

import java.util.Collections;
import java.util.Arrays;
import java.util.Objects;
import java.util.List;
import com.sun.jna.Structure;

public class DiscordRichPresence extends Structure
{
    public /* synthetic */ String spectateSecret;
    public /* synthetic */ String smallImageKey;
    public /* synthetic */ String largeImageKey;
    public /* synthetic */ long endTimestamp;
    public /* synthetic */ String details;
    public /* synthetic */ String smallImageText;
    public /* synthetic */ String state;
    public /* synthetic */ String largeImageText;
    public /* synthetic */ int partySize;
    public /* synthetic */ String matchSecret;
    private static final /* synthetic */ List<String> FIELD_ORDER;
    public /* synthetic */ int partyMax;
    public /* synthetic */ String partyId;
    public /* synthetic */ String joinSecret;
    public /* synthetic */ byte instance;
    public /* synthetic */ long startTimestamp;
    
    public DiscordRichPresence() {
        this("UTF-8");
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiscordRichPresence)) {
            return false;
        }
        final DiscordRichPresence discordRichPresence = (DiscordRichPresence)o;
        return this.startTimestamp == discordRichPresence.startTimestamp && this.endTimestamp == discordRichPresence.endTimestamp && this.partySize == discordRichPresence.partySize && this.partyMax == discordRichPresence.partyMax && this.instance == discordRichPresence.instance && Objects.equals(this.state, discordRichPresence.state) && Objects.equals(this.details, discordRichPresence.details) && Objects.equals(this.largeImageKey, discordRichPresence.largeImageKey) && Objects.equals(this.largeImageText, discordRichPresence.largeImageText) && Objects.equals(this.smallImageKey, discordRichPresence.smallImageKey) && Objects.equals(this.smallImageText, discordRichPresence.smallImageText) && Objects.equals(this.partyId, discordRichPresence.partyId) && Objects.equals(this.matchSecret, discordRichPresence.matchSecret) && Objects.equals(this.joinSecret, discordRichPresence.joinSecret) && Objects.equals(this.spectateSecret, discordRichPresence.spectateSecret);
    }
    
    public int hashCode() {
        return Objects.hash(this.state, this.details, this.startTimestamp, this.endTimestamp, this.largeImageKey, this.largeImageText, this.smallImageKey, this.smallImageText, this.partyId, this.partySize, this.partyMax, this.matchSecret, this.joinSecret, this.spectateSecret, this.instance);
    }
    
    static {
        FIELD_ORDER = Collections.unmodifiableList((List<? extends String>)Arrays.asList("state", "details", "startTimestamp", "endTimestamp", "largeImageKey", "largeImageText", "smallImageKey", "smallImageText", "partyId", "partySize", "partyMax", "matchSecret", "joinSecret", "spectateSecret", "instance"));
    }
    
    public DiscordRichPresence(final String stringEncoding) {
        this.setStringEncoding(stringEncoding);
    }
    
    protected List<String> getFieldOrder() {
        return DiscordRichPresence.FIELD_ORDER;
    }
}
