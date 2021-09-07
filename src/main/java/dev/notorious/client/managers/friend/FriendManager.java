package dev.notorious.client.managers.friend;

import com.google.gson.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FriendManager {
    private final ArrayList<Friend> friends;

    public FriendManager(){
        this.friends = new ArrayList<>();
    }

    public ArrayList<Friend> getFriends(){
        return friends;
    }

    public Friend getFriend(final String name){
        return friends.stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean isFriend(final String name){
        for (Friend friend : friends){
            if (friend.getName().equals(name)) return true;
        }

        return false;
    }

    public void addFriend(final String name){
        friends.add(new Friend(name));
    }

    public void removeFriend(final String name){
        if (getFriend(name) != null){
            friends.remove(getFriend(name));
        }
    }

    public void clearFriends(){
        friends.clear();
    }

    public void loadConfig() throws IOException {
        if (!Files.exists(Paths.get("Notorious/Client/Friends.json"))) return;

        InputStream stream = Files.newInputStream(Paths.get("Notorious/Client/Friends.json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
        if (mainObject.get("Friends") == null) return;

        JsonArray friendArray = mainObject.get("Friends").getAsJsonArray();
        friendArray.forEach(friend -> addFriend(friend.getAsString()));

        stream.close();
    }

    public void saveConfig() throws IOException {
        if (Files.exists(Paths.get("Notorious/Client/Friends.json")))
            new File("Notorious/Client/Friends.json").delete();
        Files.createFile(Paths.get("Notorious/Client/Friends.json"));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream("Notorious/Client/Friends.json"), StandardCharsets.UTF_8);
        JsonObject mainObject = new JsonObject();
        JsonArray friendArray = new JsonArray();

        for (Friend friend : friends){
            friendArray.add(friend.getName());
        }

        mainObject.add("Friends", friendArray);
        stream.write(gson.toJson(new JsonParser().parse(mainObject.toString())));
        stream.close();
    }
}
