package giomar.rodriguez.com.despiertard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import giomar.rodriguez.com.despiertard.model.Post;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ListView lvPosts;
    List<Post> postList = new ArrayList<>();
    ProgressBar progressBar;
    boolean getMore;
    boolean complete;
    int currentPage;
    LayoutInflater layoutInflater;
    Button btnLoadMore;
    ViewGroup footer;
    PostAdapter arrayAdapter;
    private String mediaUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.toolbar_icon);


        progressBar = (ProgressBar) findViewById(R.id.pbMain);

        currentPage = 1;
        getData();

        layoutInflater = getLayoutInflater();
        footer = (ViewGroup) layoutInflater.inflate(R.layout.listview_footer, lvPosts, false);
        btnLoadMore = (Button) footer.findViewById(R.id.btnLoadMore);

        lvPosts = (ListView)findViewById(R.id.lvPosts);

        lvPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = postList.get(position);
                Intent intent = new Intent(getApplicationContext(), PostDetailActivity.class);
                intent.putExtra("title", post.getTitle());
                intent.putExtra("content", post.getContent());
                intent.putExtra("url", post.getPostUrl());
                intent.putExtra("imageUrl", post.getImageUrl());
                startActivity(intent);
            }
        });
    }

    public void getData(){
        String uri = "http://despiertadominicano.com/wp-json/wp/v2/posts?page=" + currentPage;

        StringRequest request = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //postList = PostParser.parseFeed(response);
                List<Post> tempPosts = PostParser.parseFeed(response);
                for(final Post post : tempPosts){
                    String uri = "http://despiertadominicano.com/wp-json/wp/v2/media/" + post.getMediaId();
                    StringRequest mediaRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                post.setImageUrl(object.getString("source_url"));
                                arrayAdapter.notifyDataSetChanged();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    Volley.newRequestQueue(getApplicationContext()).add(mediaRequest);
                }
                if(currentPage == 1) {
                    postList.addAll(tempPosts);
                    updateListView();
                }
                else{
                    postList.addAll(tempPosts);
                    arrayAdapter.notifyDataSetChanged();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error Getting Data:", error.toString());
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    public void onCLickLoadMore(View view){
        Log.i("load more", "Loading more posts");
        currentPage++;
        getData();
    }
    public void updateListView(){
        arrayAdapter = new PostAdapter(this, postList);
        if(currentPage == 1) {

            lvPosts.addFooterView(footer, null, false);
        }
        lvPosts.setAdapter(arrayAdapter);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
