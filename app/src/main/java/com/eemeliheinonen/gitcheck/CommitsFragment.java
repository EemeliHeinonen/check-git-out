package com.eemeliheinonen.gitcheck;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.eemeliheinonen.gitcheck.models.Commit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommitsFragment extends Fragment {

    private static final String USERNAME = "param1";
    private static final String REPO_NAME = "param2";

    private String username;
    private String repositoryName;
    private List<Commit> commitList;
    private RecyclerView recyclerViewCommits;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshCommits;
    private Toast toast;


    public static CommitsFragment newInstance(String param1, String param2) {
        CommitsFragment fragment = new CommitsFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, param1);
        args.putString(REPO_NAME, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mLayoutManager = new LinearLayoutManager(getActivity());

        //Get values passed from the previous fragment
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
            repositoryName = getArguments().getString(REPO_NAME);
        }

        // Retrieve list items
        if(savedInstanceState != null){
            username = savedInstanceState.getString("SAVED_USERNAME");
            repositoryName = savedInstanceState.getString("SAVED_REPOSITORY");
            if (savedInstanceState.containsKey("SAVED_LIST_COMMITS")){
                commitList = savedInstanceState.getParcelableArrayList("SAVED_LIST_COMMITS");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout myView = (FrameLayout) inflater.inflate(R.layout.fragment_commits, container, false);
        final GitHubApiInterface apiService = RestClient.getClient();
        mSwipeRefreshCommits = (SwipeRefreshLayout) myView.findViewById(R.id.fragment_commits_swipe_refresh_layout);
        recyclerViewCommits = (RecyclerView) myView.findViewById(R.id.recycler_commits);
        recyclerViewCommits.setLayoutManager(mLayoutManager);

        //Add a divider line between the items in the RecyclerView
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerViewCommits.getContext(),
                mLayoutManager.getOrientation());
        recyclerViewCommits.addItemDecoration(mDividerItemDecoration);

        //Create a toast to be shown when an API call returns no commits.
        toast = Toast.makeText(getContext(), "Couldn't get commits", Toast.LENGTH_SHORT);

        //check if returning from a savedState, if not, get commits from the GitHub API
        if (commitList == null){
            getList(apiService);
        } else {
            CommitsAdapter adapter = new CommitsAdapter(getActivity(), commitList);
            recyclerViewCommits.setAdapter(adapter);
            getActivity().setTitle(username+" - "+repositoryName);
        }

        //Reload the commits of the repository by swipe refreshing
        mSwipeRefreshCommits.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh () {
                getList(apiService);
            }
        });

        return myView;
    }


    public void getList(GitHubApiInterface service) {
        mSwipeRefreshCommits.setRefreshing(true);
        Call<List<Commit>> call = service.commitList(username, repositoryName);
        call.enqueue(new Callback<List<Commit>>() {
            @Override
            public void onResponse(Call<List<Commit>> call, Response<List<Commit>> response) {
                if (response.isSuccessful()) {
                    commitList = response.body();

                    // Create an adapter passing in the commits data from the response and populate the RecyclerView with it
                    CommitsAdapter adapter = new CommitsAdapter(getActivity(), commitList);
                    recyclerViewCommits.setAdapter(adapter);
                    getActivity().setTitle(username+" - "+repositoryName);
                    mSwipeRefreshCommits.setRefreshing(false);
                } else {
                    System.out.println("Retrofit call response unsuccessful: "+response.errorBody().toString());
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<List<Commit>> call, Throwable t) {
                System.out.println("GitHub Api call failure: " + t);
                toast.show();
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("SAVED_USERNAME", username);
        state.putString("SAVED_REPOSITORY", repositoryName);
        state.putParcelableArrayList("SAVED_LIST_COMMITS", (ArrayList<? extends Parcelable>) commitList);
    }
}
