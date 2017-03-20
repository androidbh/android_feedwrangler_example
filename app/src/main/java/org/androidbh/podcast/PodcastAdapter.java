package org.androidbh.podcast;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.androidbh.podcast.entity.Podcast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by felipearimateia on 20/03/17.
 */

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.ViewHolder>{

    public List<Podcast> items;

    public PodcastAdapter(List<Podcast> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_podcast, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Podcast podcast =  items.get(position);
        holder.textView.setText(podcast.getTitle());

        Picasso.with(holder.itemView.getContext())
                .load(podcast.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.podcast_image)
        AppCompatImageView imageView;

        @BindView(R.id.podcast_title)
        AppCompatTextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
