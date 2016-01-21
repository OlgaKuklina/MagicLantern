package com.example.android.magiclantern.fragments;

import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.android.magiclantern.R;
import com.example.android.magiclantern.activities.MovieDetailsViewActivityState;
import com.example.android.magiclantern.data.MovieDataContainer;
import com.example.android.magiclantern.data.ReviewData;
import com.example.android.magiclantern.data.TrailerData;
import com.example.android.magiclantern.utils.JSONLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_BACKGROUND_PATH;
import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_DURATION;
import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_MOVIE_PLOT;
import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_NAME_MOVIE_ID;
import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_NAME_TITLE;
import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_POSTER_PATH;
import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_VOTE_AVERAGE;
import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_YEAR;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsViewUniversalActivityFragment extends Fragment {

    private static final String TAG = DetailsViewUniversalActivityFragment.class.getSimpleName();
    private static final Uri URI = Uri.parse("content://com.android.magiclantern.popularmovies.provider/favorite");
    private static final String POSTER_BASE_URI = "http://image.tmdb.org/t/p/w185";
    private static final String BACKGROUND_BASE_URI = "http://image.tmdb.org/t/p/w500";
    private MovieDetailsViewActivityState state;
    private ShareActionProvider mShareActionProvider;
    private MovieDataContainer detailDatas;
    private List<TrailerData> trailerData;
    private List<ReviewData> reviewData;
    private ImageButton deleteFromFavButton;
    private ScrollView scrollView;
    private LinearLayout trailerList;
    private LinearLayout reviewList;
    private ImageButton markAsFavButton;
    private ImageView moviePoster;
    private ImageView backgroundPoster;
    private TextView movieVoteAverage;
    private TextView movieDate;
    private TextView movieDuration;
    private TextView moviePlot;
    private TextView title;
    private LinearLayout  rating;
    private TextView textRating;
    private ImageView starRating;

    private Intent sharedIntent;
    private MenuItem item;
    private Toolbar toolbar;
    private boolean isTrailerLoaded;
    private int id;


    public DetailsViewUniversalActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_view_universal, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        moviePoster = (ImageView) view.findViewById(R.id.movie_poster);
        backgroundPoster = (ImageView) view.findViewById(R.id.background_poster);
        starRating = (ImageView) view.findViewById(R.id.star_rating);
        movieDate = (TextView) view.findViewById(R.id.movie_date);
        movieDuration = (TextView) view.findViewById(R.id.movie_duration);
        movieVoteAverage = (TextView) view.findViewById(R.id.vote_average);
        moviePlot = (TextView) view.findViewById(R.id.movie_plot);
        title = (TextView) view.findViewById(R.id.title);
        rating = (LinearLayout) view.findViewById(R.id.rating);
        textRating = (TextView) view.findViewById(R.id.text_rating);
        trailerList = (LinearLayout) view.findViewById(R.id.movie_trailers);
        reviewList = (LinearLayout) view.findViewById(R.id.movie_reviews);
        markAsFavButton = (ImageButton) view.findViewById(R.id.mark_as_fav_button);
        deleteFromFavButton = (ImageButton) view.findViewById(R.id.delete_from_fav_button);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        Intent intent = getActivity().getIntent();
        Log.v(TAG, "oncreate - intent = " + intent);
        isTrailerLoaded = false;
        item = null;
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            fetchMovieData(intent.getIntExtra(Intent.EXTRA_TEXT, -1));
        }
        Cursor cursor = getActivity().getContentResolver().query(ContentUris.withAppendedId(URI, id), new String[]{COLUMN_NAME_MOVIE_ID}, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            deleteFromFavButton.setVisibility(View.VISIBLE);
        } else {
            markAsFavButton.setVisibility(View.VISIBLE);
        }

        deleteFromFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getContentResolver().delete(URI, COLUMN_NAME_MOVIE_ID + " = ?", new String[]{Integer.toString(id)});
                markAsFavButton.setVisibility(View.VISIBLE);
                deleteFromFavButton.setVisibility(View.GONE);
            }
        });
        return view;

    }

    public void fetchMovieData(int id) {
        this.id = id;
        trailerList.removeAllViews();
        reviewList.removeAllViews();

        if (state == null) {
            FetchDetailsMovieTask task = new FetchDetailsMovieTask();
            task.execute(id);
            FetchTrailerMovieTask tTask = new FetchTrailerMovieTask();
            tTask.execute(id);
            FetchReviewMovieTask rTask = new FetchReviewMovieTask();
            rTask.execute(id);
        } else {
            Log.v(TAG, "state = " + state.getTrailerDatas());
            populateDetailsViewData(detailDatas = state.getDetailDatas());
            populateReviewList(reviewData = state.getReviewDatas());
            populateTrailerList(trailerData = state.getTrailerDatas());
        }
    }

    public void clearState() {
        state = null;
    }

    private void populateTrailerList(List<TrailerData> data) {
        Log.v(TAG, "populateTrailerList - data = " + data);
        for (final TrailerData trailer : data) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.movie_trailer_list_item, null);
            TextView tralerName = (TextView) view.findViewById(R.id.trailer_name);
            tralerName.setText(trailer.getTrailerName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, trailer.getTrailerUri()));
                }
            });
            trailerList.addView(view);
        }
        Log.v(TAG, "data " + data);
        if (data != null && !data.isEmpty()) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            Uri uri = data.get(0).getTrailerUri();
            shareIntent.putExtra(Intent.EXTRA_TEXT, uri.toString());
            if (item == null) {
                this.sharedIntent = shareIntent;
            } else {
//                mShareActionProvider.setShareIntent(shareIntent);
                sharedIntent = null;
                item.setVisible(true);
            }
        } else if (item != null) {
            item.setVisible(false);
        }
        isTrailerLoaded = true;
    }

    private void populateReviewList(List<ReviewData> data) {
        for (final ReviewData review : data) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.movie_review_list_item, null);
            TextView reviewAuthor = (TextView) view.findViewById(R.id.review_author);
            reviewAuthor.setText(review.getReviewAuthor());
            TextView reviewContent = (TextView) view.findViewById(R.id.review_content);
            reviewContent.setText(review.getReviewContent());
            reviewList.addView(view);
        }
    }

    private void populateDetailsViewData(final MovieDataContainer container) {


        final Palette.PaletteAsyncListener paletteAsyncListener = new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                if (getActivity() == null)
                    return;
                Log.v(TAG, "textSwatch.PaletteAsyncListener");

                Palette.Swatch textSwatch = palette.getMutedSwatch();
                Palette.Swatch bgSwatch = palette.getDarkVibrantSwatch();

                Log.v(TAG, "textSwatch = " + textSwatch);
                Log.v(TAG, "bgSwatch = " + bgSwatch);
                if (textSwatch != null && bgSwatch != null) {
                    title.setTextColor(textSwatch.getTitleTextColor());
                    title.setBackgroundColor(textSwatch.getRgb());
                    textRating.setTextColor(bgSwatch.getTitleTextColor());
                    rating.setBackgroundColor(bgSwatch.getRgb());
                    starRating.setBackgroundColor(bgSwatch.getTitleTextColor());

                    Log.v(TAG, "textSwatch.getTitleTextColor() = " + Integer.toHexString(textSwatch.getTitleTextColor()));
                    Log.v(TAG, "textSwatch.getRgb() = " + Integer.toHexString(textSwatch.getRgb()));
                } else if (bgSwatch != null) {
                    title.setBackgroundColor(bgSwatch.getRgb());
                    title.setTextColor(bgSwatch.getBodyTextColor());
                    rating.setBackgroundColor(bgSwatch.getBodyTextColor());
                    textRating.setTextColor(bgSwatch.getRgb());
                    starRating.setBackgroundColor(bgSwatch.getRgb());
                } else if (textSwatch != null) {
                    title.setBackgroundColor(textSwatch.getRgb());
                    title.setTextColor(textSwatch.getBodyTextColor());
                    rating.setBackgroundColor(textSwatch.getBodyTextColor());
                    textRating.setTextColor(textSwatch.getRgb());
                    starRating.setBackgroundColor(textSwatch.getRgb());
                } else {
                    title.setTextColor(getResources().getColor(R.color.textcolorPrimary, null));
                    title.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));

                    textRating.setTextColor(getResources().getColor(R.color.textcolorSec, null));
                    rating.setBackgroundColor(getResources().getColor(R.color.colorBackground, null));
                    PorterDuff.Mode mode = PorterDuff.Mode.SRC;
                }

                scrollView.setBackgroundColor(getResources().getColor(R.color.detail_view_background_color));

            }
        };

        Callback callback = new Callback() {
            @Override
            public void onSuccess() {
                if (backgroundPoster != null) {
                    Bitmap bitmapBg = ((BitmapDrawable) backgroundPoster.getDrawable()).getBitmap();
                    Palette.from(bitmapBg).generate(paletteAsyncListener);
                } else if (moviePoster != null) {
                    Bitmap bitmapBg = ((BitmapDrawable) moviePoster.getDrawable()).getBitmap();
                    Palette.from(bitmapBg).generate(paletteAsyncListener);
                }

            }

            @Override
            public void onError() {
                Log.v(TAG, "Callback error");
                Bitmap bitmapBg = ((BitmapDrawable) backgroundPoster.getDrawable()).getBitmap();
                Palette.from(bitmapBg).generate(paletteAsyncListener);
            }
        };
        Picasso pic = Picasso.with(getActivity());
        pic.load(POSTER_BASE_URI + container.getMoviePoster())
                .error(R.drawable.no_movie_poster)
                .into(moviePoster);

        pic.load(BACKGROUND_BASE_URI + container.getbackgroundPath())
                .fit()
                        //.centerCrop()
                .error(R.drawable.no_background_poster)
                .into(backgroundPoster, callback);

        if (StringUtils.isNotBlank(container.getYear())) {
            movieDate.setText(container.getYear());
        } else {
            movieDate.setText(R.string.details_view_no_release_date);
        }
        if (container.getDuration() != null) {
            movieDuration.setText(container.getDuration() + getString(R.string.details_view_text_minutes));
        } else {
            movieDuration.setVisibility(View.GONE);
        }
        if (container.getVoteaverage() != null) {
            textRating.setText(container.getVoteaverage() + getString(R.string.details_view_text_vote_average_divider));
        } else {
            movieVoteAverage.setVisibility(View.GONE);
        }
        if (StringUtils.isBlank(container.getPlot())) {
            moviePlot.setText(R.string.details_view_no_description);
        } else {
            moviePlot.setText(container.getPlot());
        }
        title.setText(container.getTitle());

        markAsFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_MOVIE_ID, id);
                values.put(COLUMN_DURATION, container.getDuration());
                values.put(COLUMN_MOVIE_PLOT, container.getPlot());
                values.put(COLUMN_NAME_TITLE, container.getTitle());
                values.put(COLUMN_POSTER_PATH, container.getMoviePoster());
                values.put(COLUMN_VOTE_AVERAGE, container.getVoteaverage());
                values.put(COLUMN_YEAR, container.getYear());
                values.put(COLUMN_BACKGROUND_PATH, container.getbackgroundPath());
                getActivity().getContentResolver().insert(URI, values);
                markAsFavButton.setVisibility(View.GONE);
                deleteFromFavButton.setVisibility(View.VISIBLE);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Locate MenuItem with ShareActionProvider
        // Fetch and store ShareActionProvider
        // Return true to display menu
        item = menu.findItem(R.id.menu_item_share);
//        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
//        if(isTrailerLoaded) {
//            if(sharedIntent != null) {
//            mShareActionProvider.setShareIntent(sharedIntent);
//            item.setVisible(true);
//             }else{
//            item.setVisible(false);
//            }
//        }
        return true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        state = new MovieDetailsViewActivityState(trailerData, reviewData, detailDatas);

    }

    private class FetchDetailsMovieTask extends AsyncTask<Integer, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Integer... params) {
            JSONObject jObj = JSONLoader.load("/movie/" + params[0]);

            return jObj;
        }

        @Override
        protected void onPostExecute(final JSONObject jObj) {
            super.onPostExecute(jObj);
            if (jObj != null) {
                try {
                    detailDatas = new MovieDataContainer(jObj.getString("poster_path"), id, jObj.getString("title"), jObj.getString("overview"), jObj.getString("release_date"), jObj.getInt("runtime"), jObj.getDouble("vote_average"), jObj.getString("backdrop_path"));
                    populateDetailsViewData(detailDatas);

                } catch (JSONException e) {
                    Log.e(TAG, "", e);
                }

            } else {

                final Cursor cursor = getActivity().getContentResolver().query(ContentUris.withAppendedId(URI, id), new String[]{COLUMN_DURATION, COLUMN_YEAR, COLUMN_MOVIE_PLOT, COLUMN_NAME_TITLE, COLUMN_POSTER_PATH, COLUMN_VOTE_AVERAGE, COLUMN_BACKGROUND_PATH}, null, null, null);
                Log.d(TAG, "Cursor = " + cursor.getCount());
                if (cursor.getCount() != 0) {

                    cursor.moveToFirst();
                    detailDatas = new MovieDataContainer(cursor.getString(4), id, cursor.getString(3), cursor.getString(2), cursor.getString(1), cursor.getInt(0), cursor.getDouble(5), cursor.getString(6));
                    populateDetailsViewData(detailDatas);
                }

            }
        }
    }

    private class FetchTrailerMovieTask extends AsyncTask<Integer, Void, JSONObject> {
        private static final String TRAILER_BASE_URI = "http://www.youtube.com/watch?v=";

        @Override
        protected JSONObject doInBackground(Integer... params) {
            JSONObject jObj = JSONLoader.load("/movie/" + params[0] + "/videos");

            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            if (jObj != null) {
                trailerData = new ArrayList<TrailerData>();
                try {
                    JSONArray array = jObj.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        trailerData.add(new TrailerData(Uri.parse(TRAILER_BASE_URI + object.getString("key")), object.getString("name")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "", e);
                }
                populateTrailerList(trailerData);
            }

        }
    }

    private class FetchReviewMovieTask extends AsyncTask<Integer, Void, JSONObject> {
        private static final String TRAILER_BASE_URI = "http://www.youtube.com/watch?v=";

        @Override
        protected JSONObject doInBackground(Integer... params) {
            JSONObject jObj = JSONLoader.load("/movie/" + params[0] + "/reviews");

            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            if (jObj != null) {
                reviewData = new ArrayList<ReviewData>();
                try {
                    JSONArray array = jObj.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        reviewData.add(new ReviewData(object.getString("author"), object.getString("content")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "", e);
                }
                populateReviewList(reviewData);
            }

        }
    }
}
