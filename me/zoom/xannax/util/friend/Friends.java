// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util.friend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Friends
{
    public static /* synthetic */ List<Friend> friends;
    
    public void addFriend(final String s) {
        Friends.friends.add(new Friend(s));
    }
    
    public static boolean isFriend(final String anotherString) {
        boolean b = false;
        final Iterator<Friend> iterator = getFriends().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equalsIgnoreCase(anotherString)) {
                b = true;
            }
        }
        return b;
    }
    
    public Friend getFriendByName(final String anotherString) {
        Friend friend = null;
        for (final Friend friend2 : getFriends()) {
            if (friend2.getName().equalsIgnoreCase(anotherString)) {
                friend = friend2;
            }
        }
        return friend;
    }
    
    public void delFriend(final String s) {
        Friends.friends.remove(this.getFriendByName(s));
    }
    
    public Friends() {
        Friends.friends = new ArrayList<Friend>();
    }
    
    public static List<Friend> getFriends() {
        return Friends.friends;
    }
}
