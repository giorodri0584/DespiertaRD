package giomar.rodriguez.com.despiertard;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by giorod on 7/10/2017.
 */

public class HtmlParser {
    public static String ParseHtml(String content){
        Document document = Jsoup.parse(content);
        Elements elements = document.getElementsByTag("p");
        StringBuilder html = new StringBuilder();
        Elements videos = document.getElementsByTag("iframe");
        Element video = document.select("iframe").first();
        String videoUrl = video.attr("src");
        for(Element element : elements){
            Log.i("Tag", element.toString());
        }
        /*for(Element video : videos){
            Log.i("Video", video.toString());
        }*/
        Log.i("Video", videoUrl);

        return videoUrl;

    }
}
