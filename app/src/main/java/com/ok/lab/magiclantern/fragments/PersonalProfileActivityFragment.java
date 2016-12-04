package com.ok.lab.magiclantern.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ok.lab.magiclantern.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PersonalProfileActivityFragment extends Fragment {

    private RecyclerView recyclerView;
    public PersonalProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personal_profile, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.pager);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new PersonalProfileMoviesAdapter());
    }

    private class PersonalProfileMoviesViewHolder extends RecyclerView.ViewHolder {

        public PersonalProfileMoviesViewHolder(View itemView) {
            super(itemView);
        }
    }


    private class PersonalProfileMoviesAdapter extends RecyclerView.Adapter<PersonalProfileMoviesViewHolder> {

        @Override
        public PersonalProfileMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_screen_slide_page, parent, false);

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
}
