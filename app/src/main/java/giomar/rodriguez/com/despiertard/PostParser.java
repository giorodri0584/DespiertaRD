package giomar.rodriguez.com.despiertard;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import giomar.rodriguez.com.despiertard.model.Post;


/**
 * Created by giorod on 10/28/2016.
 */

public class PostParser {
    public static List<Post> parseFeed(String content){
        List<Post> posts = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                Post post = new Post();
                post.setPostId(obj.getInt("id"));
                post.setTitle(getNested(obj, "title"));
                post.setContent(getNested(obj, "content"));
                post.setMediaId(obj.getInt("featured_media"));
                post.setPostUrl(obj.getString("link"));
                posts.add(post);
            }
           return posts;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getNested(JSONObject obj, String property){
        String rendered = null;
        try {
            JSONObject object = obj.getJSONObject(property);
            rendered = object.getString("rendered");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return rendered;
    }
}
