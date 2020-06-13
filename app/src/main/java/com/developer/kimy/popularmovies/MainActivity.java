package com.developer.kimy.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.developer.kimy.popularmovies.Adapters.CustomMoviesAdapter;
import com.developer.kimy.popularmovies.Interface.MovieInterface;
import com.developer.kimy.popularmovies.Models.Movie;
import com.developer.kimy.popularmovies.Models.MovieResponse;
import com.developer.kimy.popularmovies.Network.APIClient;
import com.developer.kimy.popularmovies.Utils.MovieUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.kimy.popularmovies.Utils.Constants.ACTIVITY_TITLE;
import static com.developer.kimy.popularmovies.Utils.Constants.MOST_POPULAR_OPTION_CHECKED;
import static com.developer.kimy.popularmovies.Utils.Constants.MOVIES_LIST;
import static com.developer.kimy.popularmovies.Utils.Constants.RECYCLER_VIEW_LAYOUT_MANAGER_STATE;
import static com.developer.kimy.popularmovies.Utils.Constants.TOP_RATED_OPTION_CHECKED;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    @BindView(R.id.rv_main)
    public RecyclerView rvMain;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    private static String API_KEY;
    public ArrayList<Movie> movies;
    private int currentPage = 1;
    private MainActivityViewModel viewModel;
    private CustomMoviesAdapter adapter;
    private MovieInterface movieService;
    private static String actionBarTitle;
    private boolean mostPopularOptionChecked = true;
    private boolean topRatedOptionChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        API_KEY = getResources().getString(R.string.API_KEY);

        ButterKnife.bind(this);
        movies = new ArrayList<>();

        adapter = new CustomMoviesAdapter(this, movies, rvMain);
        rvMain.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        viewModel.getFavoriteMovies().observe(this, favorites -> {
            if (favorites != null && !mostPopularOptionChecked && !topRatedOptionChecked) {
                if (movies == null) {
                    movies = new ArrayList<>();
                } else {
                    adapter.clear();
                }
                adapter.addAll(favorites);

                if (movies.size() == 0) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.FavoritesNotFound, Toast.LENGTH_SHORT).show();
                }
            }
        });


        movieService = APIClient.getRetrofitInstance().create(MovieInterface.class);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIES_LIST)) {
                progressBar.setVisibility(View.GONE);
                setTitle(savedInstanceState.getString(ACTIVITY_TITLE));
                movies = savedInstanceState.getParcelableArrayList(MOVIES_LIST);
                adapter.clear();
                adapter.setData(movies);
            }

            if (savedInstanceState.containsKey(MOST_POPULAR_OPTION_CHECKED)) {
                mostPopularOptionChecked = savedInstanceState.getBoolean(MOST_POPULAR_OPTION_CHECKED);
            }
            if (savedInstanceState.containsKey(TOP_RATED_OPTION_CHECKED)) {
                topRatedOptionChecked = savedInstanceState.getBoolean(TOP_RATED_OPTION_CHECKED);
            }
        }

        if (savedInstanceState == null) {
            if (mostPopularOptionChecked) {
                progressBar.setVisibility(View.VISIBLE);
                getPopularMovies();
            } else if (topRatedOptionChecked) {
                progressBar.setVisibility(View.VISIBLE);
                getTopRatedMovies();
            }
        }
    }

    private void getPopularMovies() {
        if (MovieUtils.getInstance().isNetworkAvailable(this)) {

            Call<MovieResponse> call = movieService.getPopularMovies(API_KEY, getResources().getString(R.string.LANGUAGE), currentPage);
            Log.i("Popular movies api", movieService.getPopularMovies(API_KEY, getResources().getString(R.string.LANGUAGE), 1).request().url().toString());
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response.body().getMovies() != null) {
                            Log.d(TAG, "Number of popular movies received: " + response.body().getMovies().size());
                            adapter.addAll(response.body().getMovies());
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.Something_wrong_text), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, getResources().getString(R.string.Network_Status_Not_Available), Toast.LENGTH_SHORT).show();
        }
    }

    private void getTopRatedMovies() {
        if (MovieUtils.getInstance().isNetworkAvailable(this)) {

            Call<MovieResponse> call = movieService.getTopRatedMovies(API_KEY, getResources().getString(R.string.LANGUAGE), currentPage);
            Log.i("Top movies api", movieService.getTopRatedMovies(API_KEY, getResources().getString(R.string.LANGUAGE), 1).request().url().toString());
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response.body().getMovies() != null) {
                            Log.d(TAG, "Number of top rated movies received: " + response.body().getMovies().size());
                            adapter.addAll(response.body().getMovies());
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.Something_wrong_text), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.i("Network Connection Status", "Not available");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.topRated:
                progressBar.setVisibility(View.VISIBLE);
                mostPopularOptionChecked = false;
                topRatedOptionChecked = true;
                adapter.clear();
                getTopRatedMovies();
                setTitle(R.string.toprated_movies);
                actionBarTitle = getResources().getString(R.string.toprated_movies);
                break;
            case R.id.popular:
                mostPopularOptionChecked = true;
                topRatedOptionChecked = false;
                progressBar.setVisibility(View.VISIBLE);
                adapter.clear();
                getPopularMovies();
                setTitle(R.string.popular_movies);
                actionBarTitle = getResources().getString(R.string.popular_movies);
                break;

            case R.id.favorite:
                mostPopularOptionChecked = false;
                topRatedOptionChecked = false;
                progressBar.setVisibility(View.VISIBLE);
                adapter.clear();
                setTitle(R.string.favorite_movies);
                actionBarTitle = getResources().getString(R.string.favorite_movies);
                List<Movie> favoriteMovies = viewModel.getFavoriteMovies().getValue();
                if (favoriteMovies != null && favoriteMovies.size() > 0) {
                    progressBar.setVisibility(View.INVISIBLE);
                    adapter.addAll(favoriteMovies);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.FavoritesNotFound, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(MOST_POPULAR_OPTION_CHECKED, mostPopularOptionChecked);
        outState.putBoolean(TOP_RATED_OPTION_CHECKED, topRatedOptionChecked);
        outState.putParcelableArrayList(MOVIES_LIST, movies);
        outState.putString(ACTIVITY_TITLE, actionBarTitle);
        outState.putParcelable(RECYCLER_VIEW_LAYOUT_MANAGER_STATE, rvMain.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras() != null && movies.size() == 0) {
            if (getIntent().getExtras().getBoolean(FAV_MOVIE_KEY)) {
                adapter.clear();
                setTitle(R.string.favorite_movies);
                getFavoriteMovies();
            }
        }
    }*/

}
