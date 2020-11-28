// 
// Decompiled by Procyon v0.5.36
// 

package club.minnced.discord.rpc;

import java.util.Collections;
import java.util.Arrays;
import java.util.Objects;
import java.util.List;
import com.sun.jna.Structure;

public class DiscordUser extends Structure
{
    public /* synthetic */ String username;
    public /* synthetic */ String userId;
    private static final /* synthetic */ List<String> FIELD_ORDER;
    public /* synthetic */ String avatar;
    public /* synthetic */ String discriminator;
    
    public int hashCode() {
        return Objects.hash(this.userId, this.username, this.discriminator, this.avatar);
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiscordUser)) {
            return false;
        }
        final DiscordUser discordUser = (DiscordUser)o;
        return Objects.equals(this.userId, discordUser.userId) && Objects.equals(this.username, discordUser.username) && Objects.equals(this.discriminator, discordUser.discriminator) && Objects.equals(this.avatar, discordUser.avatar);
    }
    
    public DiscordUser(final String stringEncoding) {
        this.setStringEncoding(stringEncoding);
    }
    
    static {
        FIELD_ORDER = Collections.unmodifiableList((List<? extends String>)Arrays.asList("userId", "username", "discriminator", "avatar"));
    }
    
    protected List<String> getFieldOrder() {
        return DiscordUser.FIELD_ORDER;
    }
    
    public DiscordUser() {
        this("UTF-8");
    }
}
