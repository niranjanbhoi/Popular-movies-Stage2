package com.developer.kimy.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kimy.popularmovies.Models.Trailer;
import com.developer.kimy.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomTrailerAdapter extends RecyclerView.Adapter<CustomTrailerAdapter.TrailerViewHolder> {

    private Context context;
    private List<Trailer> trailers;

    public CustomTrailerAdapter(Context context, List<Trailer> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrailerViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_trailer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        if (trailer.getSite().equalsIgnoreCase("youtube")) {
            Uri uri = Uri.parse(context.getResources().getString(R.string.YOUTUBE_BASE_IMAGE_URL) + trailer.getKey() + context.getResources().getString(R.string.YOUTUBE_IMAGE_EXTENSION));
            holder.name.setText(trailer.getTitle());
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.build()
                    .load(uri)
                    .placeholder((R.drawable.gradient_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.imageView);
        }
    }


    @Override
    public int getItemCount() {
        return trailers != null ? trailers.size() : 0;
    }

    //ViewHolder for review item view to help reduce findViewById calls
    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.thumbnail)
        ImageView imageView;
        @BindView(R.id.tv_trailer_name)
        TextView name;
        @BindView(R.id.play_button)
        ImageButton trailerPlayBtn;

        TrailerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            trailerPlayBtn.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Trailer video = trailers.get(getAdapterPosition());
            if (video != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(context.getResources().getString(R.string.YOUTUBE_BASE_VIDEO_URL) + video.getKey()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Error playing the video", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Trailer video = trailers.get(getAdapterPosition());
            if (video != null) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                Uri uri = Uri.parse(context.getResources().getString(R.string.YOUTUBE_BASE_VIDEO_URL) + video.getKey());
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                context.startActivity(Intent.createChooser(sharingIntent, "Share Video url using"));
            }
            return true;
        }
    }
}
