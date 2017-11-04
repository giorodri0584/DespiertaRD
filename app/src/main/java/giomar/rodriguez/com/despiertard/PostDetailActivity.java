package giomar.rodriguez.com.despiertard;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PostDetailActivity extends AppCompatActivity {

    private String url;
    private String title;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        collapsingToolbarLayout =(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        ivImage = (ImageView) findViewById(R.id.ivImage);

        Intent intent = getIntent();
        String title = "<h3>" + intent.getStringExtra("title") + "</h3>";
        String content = intent.getStringExtra("content");
        url = intent.getStringExtra("url");
        this.title = intent.getStringExtra("title");
        Picasso
                .with(this)
                .load(intent.getStringExtra("imageUrl"))
                .fit() // will explain later
                .into((ImageView) ivImage);

        HtmlParser.ParseHtml(content);

        String head = "<html><head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" />" +
                "<style>img {\n" +
                "        width: 100%;\n" +
                "        height: auto;\n" +
                "    }\n" +
                "    iframe {\n" +
                "        width: 100%;\n" +
                "        height: auto;\n" +
                "    }</style>" +
                "</head><body>";
        String closedTag = "</body></html>";

        String fullContent = head + title + content + closedTag;

//        ActionBar actionBar = getSupportActionBar();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent videoIntent = new Intent(getApplicationContext(), VideoActivity.class);
        startActivity(videoIntent);

        WebView browser = (WebView)findViewById(R.id.wvPost);

        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        browser.loadData(fullContent, "text/html; charset=UTF-8", null);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                this.finish();
                return true;
            }
            case R.id.action_shared: {
                sharePost();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shared, menu);
        return true;
    }

    public void sharePost(){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, url);

        startActivity(Intent.createChooser(share, "Comparte Entrada"));
    }
}
