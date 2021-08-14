package me.gavin.notorious.friend;

import java.util.ArrayList;
import java.util.List;

public class Friends {
    public ArrayList<Friend> friends;
    public Friends(){
        friends = new ArrayList<>();
    }

    public List<Friend> getFriends(){
        return friends;
    }

    public boolean isFriend(String name){
        for(Friend f : getFriends()){
            if(f.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public ArrayList<String> getFriendByName() {
        ArrayList<String> friendsName = new ArrayList<>();
        friends.forEach(friend -> {
            friendsName.add(friend.getName());
        });
        return friendsName;
    }

    public Friend getFriendByName(String name){
        Friend fr = null;
        for(Friend f : getFriends()){
            if(f.getName().equalsIgnoreCase(name)) fr = f;
        }
        return fr;
    }

    public void addFriend(String name){
        friends.add(new Friend(name));
    }

    public void delFriend(String name){
        friends.remove(getFriendByName(name));
    }
}
