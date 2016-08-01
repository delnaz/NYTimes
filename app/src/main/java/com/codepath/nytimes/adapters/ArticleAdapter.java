package com.codepath.nytimes.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytimes.R;
import com.codepath.nytimes.activities.ArticleActivity;
import com.codepath.nytimes.models.Article;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleAdapter extends ArrayAdapter<Article> {

   public ArticleAdapter(Context context, List<Article> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Article article = this.getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result,parent,false);

        }
        viewHolder = new ViewHolder(convertView);
        viewHolder.imageView.setImageResource(0);

        viewHolder.textView.setText(article.getHeadline());

        String thumbnail = article.getThumbnail();
        if(!TextUtils.isEmpty(thumbnail)){
            Glide.with(getContext()).load(thumbnail).into(viewHolder.imageView);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ArticleActivity.class);
                intent.putExtra("articleDetail", Parcels.wrap(article));
                getContext().startActivity(intent);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
    static class ViewHolder {
        @BindView(R.id.ivImage) ImageView imageView;
        @BindView(R.id.tvTitle) TextView textView;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}