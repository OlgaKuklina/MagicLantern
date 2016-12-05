package com.ok.lab.magiclantern.fragments;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ok.lab.magiclantern.R;
import com.ok.lab.magiclantern.data.MovieDataContainer;
import com.ok.lab.magiclantern.data.PersonalProfileDatas;
import com.ok.lab.magiclantern.utils.JSONLoader;
import com.ok.lab.magiclantern.utils.JsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

import static android.R.attr.id;
import static android.provider.CalendarContract.CalendarCache.URI;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_BACKGROUND_PATH;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_DURATION;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_GENRES;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_IMDB_ID;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_MOVIE_PLOT;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_NAME_TITLE;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_ORIGINAL_COUNTRIES;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_ORIGINAL_LANGUAGE;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_POSTER_PATH;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_PROD_COMPANIES;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_STATUS;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_VOTE_AVERAGE;
import static com.ok.lab.magiclantern.data.FavoriteMoviesContract.FavoriteMovieColumn.COLUMN_YEAR;
import static com.ok.lab.magiclantern.utils.JsonUtils.getStringValue;

/**
 * A placeholder fragment containing a simple view.
 */
public class PersonalProfileActivityFragment extends Fragment {
    private static final String TAG = PersonalProfileActivityFragment.class.getSimpleName();
    private static final String POSTER_PERSONAL_IMAGE_BASE_URI = "http://image.tmdb.org/t/p/w92";
    private PersonalProfileDatas personalDatas;
    private RecyclerView recyclerView;
    private ImageView personalProfImage;
    private TextView placeOfBirth;
    private TextView biography;
    private int id;

    public PersonalProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personal_profile, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.pager);
        personalProfImage = (ImageView) v.findViewById(R.id.personal_image);
        placeOfBirth = (TextView) v.findViewById(R.id.place_of_birth);
        biography = (TextView) v.findViewById(R.id.biography);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new PersonalProfileMoviesAdapter(getContext()));

        Intent intent = getActivity().getIntent();
        Log.v(TAG, "oncreate - intent = " + intent);
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            fetchPersonalData(intent.getIntExtra(Intent.EXTRA_TEXT, -1));
        }
    }

    public void fetchPersonalData(int id) {
        this.id = id;
        FetchPersonalProfileTask cTask = new FetchPersonalProfileTask(getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN), POSTER_PERSONAL_IMAGE_BASE_URI);
        cTask.execute(id);
    }


    public static class PersonalProfileMoviesViewHolder extends RecyclerView.ViewHolder {

        public PersonalProfileMoviesViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class PersonalProfileMoviesAdapter extends RecyclerView.Adapter<PersonalProfileMoviesViewHolder> {

        private final Context context;

        public PersonalProfileMoviesAdapter(Context context) {
            this.context = context;
        }

        @Override
        public PersonalProfileMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.fragment_screen_slide_page, parent, false);

            return new PersonalProfileMoviesViewHolder(v);
        }

        @Override
        public void onBindViewHolder(PersonalProfileMoviesViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    private void populatePersonalViewData(PersonalProfileDatas personalDatas) {
        placeOfBirth.setText(personalDatas.getPlaceOfBirth());
        biography.setText(personalDatas.getBiography());
        Picasso pic = Picasso.with(getActivity());
        Log.v(TAG, "path" + personalDatas.getProfileImagePath());
        if (personalDatas.getProfileImagePath() == null) {
            pic.load(R.drawable.no_movie_poster)
                    .fit().centerCrop()
                    .into(personalProfImage);
        } else {
            pic.load(personalDatas.getProfileImagePath())
                    .fit().centerCrop()
                    .error(R.drawable.no_movie_poster)
                    .into(personalProfImage);
        }
    }


    public class FetchPersonalProfileTask extends AsyncTask<Integer, Void, JSONObject> {
        private final String baseUri;
        private final String apiKey;
        private PersonalProfileDatas personalDatas;

        public FetchPersonalProfileTask(String apiKey, String baseUri) {
            this.baseUri = baseUri;
            this.apiKey = apiKey;
        }

        @Override
        protected JSONObject doInBackground(Integer... params) {

            JSONObject jObj = JSONLoader.load("/person/" + params[0], apiKey);
            return jObj;
        }

        @Override
        protected void onPostExecute(final JSONObject jObj) {
            super.onPostExecute(jObj);
            if (jObj != null) {
                try {

                    Log.v(TAG, "jObj = " + jObj);
                    personalDatas = new PersonalProfileDatas(getStringValue(jObj, "name"), baseUri + JsonUtils.getStringValue(jObj, "profile_path"), JsonUtils.getStringValue(jObj, "place_of_birth"), JsonUtils.getStringValue(jObj, "biography"), id);
                    populatePersonalViewData(personalDatas);

                } catch (JSONException e) {
                    Log.e(TAG, "", e);
                }

            } else {

                final Cursor cursor = getActivity().getContentResolver().query(ContentUris.withAppendedId(URI, id), new String[]{COLUMN_DURATION, COLUMN_YEAR, COLUMN_MOVIE_PLOT, COLUMN_NAME_TITLE, COLUMN_POSTER_PATH, COLUMN_VOTE_AVERAGE, COLUMN_BACKGROUND_PATH, COLUMN_ORIGINAL_LANGUAGE, COLUMN_ORIGINAL_COUNTRIES, COLUMN_GENRES, COLUMN_STATUS, COLUMN_IMDB_ID, COLUMN_PROD_COMPANIES}, null, null, null);
                Log.d(TAG, "Cursor = " + cursor.getCount());
                if (cursor.getCount() != 0) {

                    cursor.moveToFirst();
                    //personalDatas = new MovieDataContainer(cursor.getString(4), id, cursor.getString(3), cursor.getString(2), cursor.getString(1), cursor.getInt(0), cursor.getDouble(5), cursor.getString(6), cursor.getString(7), Collections.<String>emptyList(), Collections.<String>emptyList(), cursor.getString(10), cursor.getString(11), Collections.<String>emptyList());
                    populatePersonalViewData(personalDatas);
                }

            }
        }
    }
}