package giomar.rodriguez.com.despiertard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import giomar.rodriguez.com.despiertard.model.Post;

/**
 * Created by giorod on 12/9/2016.
 */

public class PostAdapter extends ArrayAdapter {
    private LayoutInflater inflater;
    private List<Post> postList;
    private Context context;
    private Target target;

    public PostAdapter(Context context, List<Post> postList) {
        super(context, R.layout.post_item, postList);
        this.postList = postList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.post_item, parent, false);
        }
        TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
        //TextView tvCategory = (TextView)convertView.findViewById(R.id.tvCategory);
        ImageView ivFoto = (ImageView)convertView.findViewById(R.id.ivFoto);

        Post post = postList.get(position);
        tvTitle.setText(Html.fromHtml(post.getTitle()));
        //loadImage(post, ivFoto);

        //tvCategory.setText(post.getCategory());
        //Log.i("ImageUrl:", post.getImageUrl());
        Picasso
                .with(context)
                .load(post.getImageUrl())
                .fit() // will explain later
                .into((ImageView) ivFoto);
        //ivFoto.setImageBitmap(post.getBitmap());

        return convertView;
    }

    void loadImage(final Post post, final ImageView ivFoto) {

        //final ImageView imageView = (ImageView) findViewById(R.id.image);

        target = new Target() {
            @Override
            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                //Do something
                post.setBitmap(bitmap);
                //ivFoto.setImageBitmap(post.getBitmap());
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context)
                .load(post.getImageUrl())
                .into(target);
    }
}
