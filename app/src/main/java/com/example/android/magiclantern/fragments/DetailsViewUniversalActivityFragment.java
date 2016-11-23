package com.example.android.magiclantern.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.android.magiclantern.R;
import com.example.android.magiclantern.activities.MovieDetailsViewActivityState;
import com.example.android.magiclantern.data.CastData;
import com.example.android.magiclantern.data.MovieDataContainer;
import com.example.android.magiclantern.data.ReviewData;
import com.example.android.magiclantern.data.TrailerData;
import com.example.android.magiclantern.utils.JSONLoader;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
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
import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_ORIGINAL_LANGUAGE;
import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_POSTER_PATH;
import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_VOTE_AVERAGE;
import static com.example.android.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_YEAR;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsViewUniversalActivityFragment extends Fragment
        implements YouTubePlayer.OnInitializedListener {

    private static final String TAG = DetailsViewUniversalActivityFragment.class.getSimpleName();
    private static final Uri URI = Uri.parse("content://com.android.magiclantern.provider/favorite");
    private static final String POSTER_BASE_URI = "http://image.tmdb.org/t/p/w185";
    private static final String POSTER_CAST_BASE_URI = "http://image.tmdb.org/t/p/w92";
    private static final String BACKGROUND_BASE_URI = "http://image.tmdb.org/t/p/w500";
    private static final String SHORT_TEXT_PREVIEW = " \n <font color=#cc0029>... show more</font>";
    private static final String LONG_TEXT_PREVIEW = " \n<font color=#cc0029>...show less</font>";
    private static final String END_TEXT_PREVIEW = "\n<font color=#cc0029> the end!</font>";
    private MovieDetailsViewActivityState state;
    private MovieDataContainer detailDatas;
    private List<TrailerData> trailerData;
    private List<ReviewData> reviewData;
    private List<CastData> castData;
    private ImageButton deleteFromFavButton;
    private ScrollView scrollView;
    private LinearLayout trailerList;
    private LinearLayout reviewList;
    private LinearLayout castList;
    private ImageButton markAsFavButton;
    private ImageView moviePoster;
    private ImageView backgroundPoster;
    private TextView movieVoteAverage;
    private TextView movieDate;
    private TextView movieDuration;
    private TextView moviePlot;
    private TextView moviePlotTitle;
    private LinearLayout moviePlotlayout;
    private TextView title;
    private LinearLayout rating;
    private TextView textRating;
    private TextView textLanguage;
    private ImageView starRating;
    private Intent sharedIntent;
    private MenuItem item;
    private Toolbar toolbar;
    private boolean isTrailerLoaded;
    private int id;
    private boolean isSeeMore;
    private boolean isReviewShown;
    private YouTubePlayerFragment youTubePlayerFragment;
    private YouTubePlayer youTubePlayer;

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
        youTubePlayerFragment = YouTubePlayerFragment.newInstance();
        getChildFragmentManager().beginTransaction().add(R.id.youtube_fragment, youTubePlayerFragment).commit();
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        moviePoster = (ImageView) view.findViewById(R.id.movie_poster);
        backgroundPoster = (ImageView) view.findViewById(R.id.background_poster);
        starRating = (ImageView) view.findViewById(R.id.star_rating);
        movieDate = (TextView) view.findViewById(R.id.movie_date);
        movieDuration = (TextView) view.findViewById(R.id.movie_duration);
        moviePlot = (TextView) view.findViewById(R.id.movie_plot);
        moviePlotTitle = (TextView) view.findViewById(R.id.movie_plot_title);
        moviePlotlayout = (LinearLayout) view.findViewById(R.id.movie_plot_layout);
        trailerList = (LinearLayout) view.findViewById(R.id.movie_trailers);
        title = (TextView) view.findViewById(R.id.title);
        rating = (LinearLayout) view.findViewById(R.id.rating);
        textRating = (TextView) view.findViewById(R.id.text_rating);
        textLanguage = (TextView) view.findViewById(R.id.text_language);
        reviewList = (LinearLayout) view.findViewById(R.id.movie_reviews);
        castList = (LinearLayout) view.findViewById(R.id.cast_data);
        markAsFavButton = (ImageButton) view.findViewById(R.id.mark_as_fav_button);
        deleteFromFavButton = (ImageButton) view.findViewById(R.id.delete_from_fav_button);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);

        isTrailerLoaded = false;
        item = null;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        Log.v(TAG, "oncreate - intent = " + intent);
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            fetchMovieData(intent.getIntExtra(Intent.EXTRA_TEXT, -1));
        }
        Cursor cursor = getActivity().getContentResolver().query(ContentUris.withAppendedId(URI, id), new String[]{COLUMN_NAME_MOVIE_ID}, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            deleteFromFavButton.setVisibility(View.VISIBLE);
        } else {
            markAsFavButton.setVisibility(View.VISIBLE);
        }
    }

    public void fetchMovieData(int id) {
        this.id = id;
        trailerList.removeAllViews();
        reviewList.removeAllViews();
        castList.removeAllViews();

        if (state == null) {
            FetchDetailsMovieTask task = new FetchDetailsMovieTask();
            task.execute(id);
            FetchTrailerMovieTask tTask = new FetchTrailerMovieTask();
            tTask.execute(id);
            FetchReviewMovieTask rTask = new FetchReviewMovieTask();
            rTask.execute(id);
            FetchCastMovieTask cTask = new FetchCastMovieTask();
            cTask.execute(id);
        } else {
            Log.v(TAG, "state = " + state.getTrailerDatas());
            populateDetailsViewData(detailDatas = state.getDetailDatas());
            populateReviewList(reviewData = state.getReviewDatas(), isReviewShown);
            populateTrailerList(trailerData = state.getTrailerDatas());
            populateCastList(castData = state.getCastDatas());
        }
    }


    public void clearState() {
        state = null;
    }

    private void populateTrailerList(List<TrailerData> data) {
        Log.v(TAG, "populateTrailerList - data = " + data);

        for (final TrailerData trailer : data) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.movie_trailer_list_item, null);
            view.setTag(trailer);
            TextView tralerName = (TextView) view.findViewById(R.id.trailer_name);
            tralerName.setText(trailer.getTrailerName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TrailerData data = (TrailerData) v.getTag();
                    youTubePlayer.loadVideo(data.getTrailerUri().getQueryParameter("v"));

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
                sharedIntent = null;
                item.setVisible(true);
            }
        } else if (item != null) {
            item.setVisible(false);
        }
        isTrailerLoaded = true;

        if (data != null && !data.isEmpty()) {
            youTubePlayerFragment.initialize(getString(R.string.YOUTUBE_DATA_API_V3), this);
        } else {
            getView().findViewById(R.id.youtube_fragment).setVisibility(View.GONE);
            getView().findViewById(R.id.movie_trailers).setVisibility(View.GONE);

        }

    }

    private void populateReviewList(final List<ReviewData> data, boolean showReview) {
        if (data == null && data.isEmpty()) {

            reviewList.setVisibility(View.GONE);
        }

        for (final ReviewData review : data) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.movie_review_list_item, null);
            TextView reviewAuthor = (TextView) view.findViewById(R.id.review_author);
            reviewAuthor.setText(review.getReviewAuthor());
            TextView reviewContentStart = (TextView) view.findViewById(R.id.review_content_start);
            TextView reviewContentEnd = (TextView) view.findViewById(R.id.review_content_end);
            StringBuilder buildStart = new StringBuilder();
            buildStart.append(review.getReviewContent());

            if (buildStart.length() > 72) {
                reviewContentStart.setText(Html.fromHtml(buildStart.substring(0, 72) + SHORT_TEXT_PREVIEW));
                reviewContentEnd.setText(Html.fromHtml(buildStart.substring(0, buildStart.length()) + LONG_TEXT_PREVIEW));
                Log.v(TAG, "reviewContentstart" + reviewContentStart.getText());
                Log.v(TAG, "reviewContentEnd" + reviewContentEnd.getText());

            } else {
                reviewContentStart.setText(buildStart);
                reviewContentEnd.setText(Html.fromHtml(buildStart + END_TEXT_PREVIEW));

                Log.v(TAG, "reviewContentstart" + reviewContentStart.getText());
            }
            Log.v(TAG, "reviewContentEnd" + reviewContentEnd.getText());

            reviewContentStart.setVisibility(View.VISIBLE);
            reviewContentEnd.setVisibility(View.GONE);

            reviewList.addView(view);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TextView contentStart = (TextView) v.findViewById(R.id.review_content_start);
                    TextView contentEnd = (TextView) v.findViewById(R.id.review_content_end);

                    Log.v(TAG, "reviewContentstart ON" + contentStart.getText());
                    Log.v(TAG, "reviewContentEnd ON" + contentEnd.getText());
                    if (!isReviewShown) {
                        contentEnd.setVisibility(View.VISIBLE);
                        contentStart.setVisibility(View.GONE);
                    } else {
                        contentStart.setVisibility(View.VISIBLE);
                        contentEnd.setVisibility(View.GONE);
                    }
                    isReviewShown = !isReviewShown;
                }
            });
        }
        reviewList.setVisibility(View.VISIBLE);

    }

    private void populateCastList(final List<CastData> data) {
        if (data == null && data.isEmpty()) {

            castList.setVisibility(View.GONE);
        }

        for (final CastData cast : data) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.movie_cast_list_item, null);
            TextView castName = (TextView) view.findViewById(R.id.cast_name);
            Log.v(TAG, "cast.getCastName() " + cast.getCastName());

            ImageView castImage = (ImageView) view.findViewById(R.id.cast_image);

            TextView castCharacter = (TextView) view.findViewById(R.id.cast_charecter);


            if (cast.getCastName() == null) {
                castImage.setVisibility(View.GONE);
                castCharacter.setVisibility(View.GONE);
                castName.setVisibility(View.GONE);
            } else {
                castName.setText(cast.getCastName());
            }
            if (cast.getCharacter() == null) {
                castCharacter.setVisibility(View.GONE);
            } else {
                castCharacter.setText(cast.getCharacter());
            }
            if (cast.getCastImagePath() == null) {
                castImage.setVisibility(View.GONE);
            } else {
                Picasso pic = Picasso.with(getActivity());
                pic.load(POSTER_CAST_BASE_URI + cast.getCastImagePath())
                        .fit().centerCrop()
                        .error(R.drawable.no_movie_poster)
                        .into(castImage);

            }
            castList.addView(view);
            if (cast.getCastOrder() == 7) {
                break;

            }

        }
        castList.setVisibility(View.VISIBLE);
    }

    private void populateDetailsViewData(final MovieDataContainer container) {


        final Palette.PaletteAsyncListener paletteAsyncListener = new Palette.PaletteAsyncListener() {
            @SuppressLint("NewApi")
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
        if (container.getMoviePoster() == null) {
            pic.load(R.drawable.no_movie_poster)
                    .fit().centerCrop()
                    .into(moviePoster);
        } else {
            pic.load(POSTER_BASE_URI + container.getMoviePoster())
                    .fit().centerCrop()
                    .error(R.drawable.no_movie_poster)
                    .into(moviePoster);

        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            if (container.getbackgroundPath() == null) {
                pic.load(R.drawable.no_background_poster)
                        .fit().centerCrop()
                        .into(backgroundPoster, callback);
            } else {
                pic.load(BACKGROUND_BASE_URI + container.getbackgroundPath())
                        .fit().centerCrop()
                        .error(R.drawable.no_background_poster)
                        .into(backgroundPoster, callback);
            }
        } else {
            if (container.getbackgroundPath() == null) {
                pic.load(R.drawable.no_background_poster)
                        .fit()
                        .into(backgroundPoster, callback);
            } else {
                pic.load(BACKGROUND_BASE_URI + container.getbackgroundPath())
                        .fit()
                        .error(R.drawable.no_background_poster)
                        .into(backgroundPoster, callback);
            }
        }

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
        if (container.getOriginalLanguage() != null) {
            textLanguage.setText(container.getOriginalLanguage());
        }
        Log.v(TAG, " overview '" + container.getPlot() + "'");
        if (StringUtils.isBlank(container.getPlot())) {
            moviePlot.setText(R.string.details_view_no_description);
        } else {
            if (container.getPlot().length() > 80) {
                moviePlot.setText(Html.fromHtml(container.getPlot().substring(0, 80) + SHORT_TEXT_PREVIEW));
                isSeeMore = true;
            } else {
                String string = container.getPlot();
                moviePlot.setText(Html.fromHtml(string + END_TEXT_PREVIEW));
                isSeeMore = false;
            }
        }

        moviePlotlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isBlank(container.getPlot())) {
                    moviePlot.setText(R.string.details_view_no_description);
                }
                Log.v(TAG, "moviePlot.getText().length() " + moviePlot.getText().length());
                if (isSeeMore) {
                    String string = container.getPlot();
                    moviePlot.setText(Html.fromHtml(string + LONG_TEXT_PREVIEW));
                } else {
                    if (container.getPlot().length() > 80) {
                        moviePlot.setText(Html.fromHtml(container.getPlot().substring(0, 80) + SHORT_TEXT_PREVIEW));
                    } else {
                        String string = container.getPlot();
                        moviePlot.setText(Html.fromHtml(string + END_TEXT_PREVIEW));
                    }
                }

                isSeeMore = !isSeeMore;
            }
        });

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
                values.put(COLUMN_ORIGINAL_LANGUAGE, container.getOriginalLanguage());
                getActivity().getContentResolver().insert(URI, values);
                markAsFavButton.setVisibility(View.GONE);
                deleteFromFavButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        state = new MovieDetailsViewActivityState(trailerData, reviewData, castData, detailDatas);
        if (youTubePlayer != null) {
            youTubePlayer.release();
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            Log.v(TAG, "trailerData " + trailerData);
            if (trailerData != null && !trailerData.isEmpty()) {
                Uri uri = trailerData.get(0).getTrailerUri();

                Log.v(TAG, "trailerData " + trailerData.size());
                String trailerCode = uri.getQueryParameter("v");
                if (trailerCode != null) {
                    this.youTubePlayer = youTubePlayer;
                    youTubePlayer.cueVideo(trailerCode);
                }
            } else {
                getChildFragmentManager().beginTransaction().remove(youTubePlayerFragment).commit();
            }

        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    private String getString(JSONObject jobj, String name) throws JSONException {
        if (jobj.isNull(name)) {
            return null;
        } else {
            return jobj.getString(name);
        }
    }

    private double getDouble(JSONObject jobj, String name, double defaultValue) throws JSONException {
        if (jobj.isNull(name)) {
            return defaultValue;
        } else {
            return jobj.getDouble(name);
        }
    }

    private int getInt(JSONObject jobj, String name, int defaultValue) throws JSONException {
        if (jobj.isNull(name)) {
            return defaultValue;
        } else {
            return jobj.getInt(name);
        }
    }

    public List<TrailerData> getTrailerData() {
        return trailerData;
    }

    private class FetchDetailsMovieTask extends AsyncTask<Integer, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Integer... params) {
            JSONObject jObj = JSONLoader.load("/movie/" + params[0], getString(R.string.THE_MOVIE_DB_API_TOKEN));

            return jObj;
        }

        @Override
        protected void onPostExecute(final JSONObject jObj) {
            super.onPostExecute(jObj);
            if (jObj != null) {
                try {

                    detailDatas = new MovieDataContainer(getString(jObj, "poster_path"), id, getString(jObj, "title"), getString(jObj, "overview"), getString(jObj, "release_date"), getInt(jObj, "runtime", 0), getDouble(jObj, "vote_average", 0.0), getString(jObj, "backdrop_path"), getString(jObj, "original_language"));
                    populateDetailsViewData(detailDatas);

                } catch (JSONException e) {
                    Log.e(TAG, "", e);
                }

            } else {

                final Cursor cursor = getActivity().getContentResolver().query(ContentUris.withAppendedId(URI, id), new String[]{COLUMN_DURATION, COLUMN_YEAR, COLUMN_MOVIE_PLOT, COLUMN_NAME_TITLE, COLUMN_POSTER_PATH, COLUMN_VOTE_AVERAGE, COLUMN_BACKGROUND_PATH, COLUMN_ORIGINAL_LANGUAGE}, null, null, null);
                Log.d(TAG, "Cursor = " + cursor.getCount());
                if (cursor.getCount() != 0) {

                    cursor.moveToFirst();
                    detailDatas = new MovieDataContainer(cursor.getString(4), id, cursor.getString(3), cursor.getString(2), cursor.getString(1), cursor.getInt(0), cursor.getDouble(5), cursor.getString(6), cursor.getString(7));
                    populateDetailsViewData(detailDatas);
                }

            }
        }
    }

    private class FetchTrailerMovieTask extends AsyncTask<Integer, Void, JSONObject> {
        private static final String TRAILER_BASE_URI = "http://www.youtube.com/watch?v=";

        @Override
        protected JSONObject doInBackground(Integer... params) {
            JSONObject jObj = JSONLoader.load("/movie/" + params[0] + "/videos", getString(R.string.THE_MOVIE_DB_API_TOKEN));

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

        @Override
        protected JSONObject doInBackground(Integer... params) {
            JSONObject jObj = JSONLoader.load("/movie/" + params[0] + "/reviews", getString(R.string.THE_MOVIE_DB_API_TOKEN));

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
                populateReviewList(reviewData, isReviewShown);
            }

        }
    }

    private class FetchCastMovieTask extends AsyncTask<Integer, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Integer... params) {
            JSONObject jObj = JSONLoader.load("/movie/" + params[0] + "/credits", getString(R.string.THE_MOVIE_DB_API_TOKEN));

            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            if (jObj != null) {
                castData = new ArrayList<CastData>();
                try {
                    JSONArray array = jObj.getJSONArray("cast");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);


                        castData.add(new CastData(object.getString("name"), object.getString("profile_path"), object.getString("character"), object.getInt("id"), object.getInt("order")));

                    }
                } catch (JSONException e) {
                    Log.e(TAG, "", e);
                }
                populateCastList(castData);
            }

        }
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            Log.v(TAG, " ScreenSlidePagerAdapter constructed");
        }

        @Override
        public Fragment getItem(int position) {
            Log.v(TAG, " ScreenSlidePagerAdapter YouTubePlayerFragment");
            YouTubePlayerFragment trailersFragment = YouTubePlayerFragment.newInstance();
            trailersFragment.initialize(getString(R.string.YOUTUBE_DATA_API_V3), DetailsViewUniversalActivityFragment.this);
            return trailersFragment;
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}
