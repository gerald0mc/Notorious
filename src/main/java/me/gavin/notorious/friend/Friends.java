package me.gavin.notorious.friend;

import java.util.ArrayList;
import java.util.List;

public class Friends {
    public static ArrayList<Friend> friends;
    public Friends(){
        friends = new ArrayList<>();
    }

    public static List<Friend> getFriends(){
        return friends;
    }

    public static boolean isFriend(String name){
        for(Friend f : getFriends()){
            if(f.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public static ArrayList<String> getFriendByName() {
        ArrayList<String> friendsName = new ArrayList<>();
        friends.forEach(friend -> {
            friendsName.add(friend.getName());
        });
        return friendsName;
    }

    public static Friend getFriendByName(String name){
        Friend fr = null;
        for(Friend f : getFriends()){
            if(f.getName().equalsIgnoreCase(name)) fr = f;
        }
        return fr;
    }

    public static void addFriend(String name){
        friends.add(new Friend(name));
    }

    public static void delFriend(String name){
        friends.remove(getFriendByName(name));
    }
}
