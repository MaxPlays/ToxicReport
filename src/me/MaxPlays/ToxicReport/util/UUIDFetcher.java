package me.MaxPlays.ToxicReport.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Max_Plays on 12.08.2017.
 */
public class UUIDFetcher {

    private static Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(100000).expireAfterWrite(1, TimeUnit.HOURS).build();

    public static String getName(String id){
        cache.cleanUp();

        String uuid = id.replace("-", "");

        ConcurrentMap<String, String> map = cache.asMap();
        if(map.containsKey(uuid))
            return map.get(uuid);
        try {
            URLConnection conn = new URL("https://api.mojang.com/user/profiles/" + uuid + "/names").openConnection();
            conn.connect();
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            JsonArray array = (((JsonObject) new JsonParser().parse(line)).getAsJsonArray());
            String name = array.get(array.size() - 1).getAsJsonObject().get("name").getAsString();
            cache.put(uuid, name);
            return name;
        } catch (IOException e) {
            e.printStackTrace();
                    return "ERROR";
        }
    }
}
