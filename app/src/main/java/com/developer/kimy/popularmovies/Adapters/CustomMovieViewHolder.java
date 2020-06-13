package com.developer.kimy.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.kimy.popularmovies.Models.Movie;
import com.developer.kimy.popularmovies.R;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

class CustomMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private Context mContext;

    @BindView(R.id.tvMovieTitle)
    TextView movieTitle;

    @BindView(R.id.ivMovieImage)
    ImageView coverImage;

    @BindView(R.id.tvReleaseDate)
    TextView releaseDate;

    private RecyclerViewClickListener clickListener;

    CustomMovieViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
        this.clickListener = listener;
    }

    void bindMovie(Movie movie) {
        StringBuilder releaseText = new StringBuilder().append(mContext.getResources().getString(R.string.Release_Date_Title));
        releaseText.append(movie.getReleaseDate());

        movieTitle.setText(movie.getOriginalTitle());
        releaseDate.setText(releaseText);
        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.downloader(new OkHttp3Downloader(mContext));
        builder.build().load(mContext.getResources().getString(R.string.IMAGE_BASE_URL) + movie.getPosterPath())
                .placeholder((R.drawable.gradient_background))
                .error(R.drawable.ic_launcher_background)
                .into(coverImage);
    }

    @Override
    public void onClick(View v) {
        clickListener.onClick(v, getAdapterPosition());
    }
}
