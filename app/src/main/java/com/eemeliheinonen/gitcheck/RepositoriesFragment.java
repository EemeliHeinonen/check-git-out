package com.eemeliheinonen.gitcheck;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eemeliheinonen.gitcheck.models.Repository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RepositoriesFragment extends Fragment implements RecyclerViewClickListener {

    private String TAG = "RepositoriesFragment";
    Button btnSearch;
    EditText etSearch;
    private RecyclerView recyclerViewRepositories;
    private List<Repository> repositoryList;
    private RepositoriesAdapter repoAdapter;
    private String username;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshRepositories;
    private String nextLink;
    private PageLinks pageLinks;
    private Toast noReposToast;
    final GitHubApiInterface apiService = RestClient.getClient();

    //Pagination
    private int previousItemCount = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;


    public static RepositoriesFragment newInstance() {
        return new RepositoriesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getActivity());
        pageLinks = new PageLinks();

        // Retrieve list items
        if(savedInstanceState != null){
            username = savedInstanceState.getString("SAVED_USERNAME");
            if (savedInstanceState.containsKey("SAVED_LIST")){
                repositoryList = savedInstanceState.getParcelableArrayList("SAVED_LIST");
            }
        }
    }

    //return for easy access to the listener for creating the adapter
    public RecyclerViewClickListener getListener(){
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout myView = (LinearLayout) inflater.inflate(R.layout.fragment_repositories, container, false);
        //TODO hide arrow
        btnSearch = (Button) myView.findViewById(R.id.buttonSearch);
        etSearch = (EditText) myView.findViewById(R.id.editTextSearch);
        mSwipeRefreshRepositories = (SwipeRefreshLayout) myView.findViewById(R.id.fragment_repositories_swipe_refresh_layout);
        recyclerViewRepositories = (RecyclerView) myView.findViewById(R.id.recycler_repositories);
        recyclerViewRepositories.setLayoutManager(mLayoutManager);

        //Create a toast to be shown when an API call fails to show repositories
        noReposToast = Toast.makeText(getContext(), "Couldn't find repositories", Toast.LENGTH_SHORT);


        //Pagination
        recyclerViewRepositories.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerViewRepositories.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousItemCount) {
                        loading = false;
                        previousItemCount = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached

                    if (nextLink!=null) {
                        getNextPage(apiService);
                    }
                    loading = true;
                }
            }
        });


        //Populate the RecyclerView if list data was retrieved from a saved state
        if (repositoryList!=null){
            repoAdapter = new RepositoriesAdapter(getActivity(), repositoryList, getListener());
            recyclerViewRepositories.setAdapter(repoAdapter);
            //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(username+" - Repositories");
            getActivity().setTitle(username+" - Repositories");
        }


        //Search for repositories and hide the keyboard by clicking done on the soft keyboard
        etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnSearch.callOnClick();
                    return true;
                }
                return false;
            }
        });

        // More of the same
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (etSearch.getText().toString().equals("")) {
                    System.out.println("Search string is null");
                } else {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    username = etSearch.getText().toString().trim();
                    etSearch.getText().clear();
                    getList(apiService);

                }
            }
        });

        //Load the users repositories again when swiping to refresh
        mSwipeRefreshRepositories.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh () {
                getList(apiService);
            }
        });
        return myView;
    }

    //API call to get the user's first 30 repositories, display a toast if the call fails or there are no repos
     void getList(GitHubApiInterface service){

             mSwipeRefreshRepositories.setRefreshing(true);
             Call<List<Repository>> call = service.repositoryList(username);
             call.enqueue(new Callback<List<Repository>>() {
                 @Override
                 public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                     if (response.isSuccessful()) {
                         pageLinks.setLinks(response);
                         repositoryList = response.body();
                         nextLink = pageLinks.getNext();
                         System.out.println("api call onResponse list size: " + repositoryList.size());
                         System.out.println("next link: "+nextLink);

                         // Create adapter passing in the Repository data
                         repoAdapter = new RepositoriesAdapter(getActivity(), repositoryList, getListener());
                         // Attach the adapter to the recyclerView to populate items
                         recyclerViewRepositories.setAdapter(repoAdapter);
                         mSwipeRefreshRepositories.setRefreshing(false);
                         getActivity().setTitle(username+" - Repositories");
                         previousItemCount = 0; //reset the value for pagination to work when searching for another users repos

                     } else { noReposToast.show();}
                 }

                 @Override
                 public void onFailure(Call<List<Repository>> call, Throwable t) {
                     System.out.println("GitHub Api call failure: " + t);
                     noReposToast.show();
                 }
             });
    }


    //Use the link from the header of the previous API call response to get the next page of repositories
    private void getNextPage(GitHubApiInterface service) {
        mSwipeRefreshRepositories.setRefreshing(true);
        Call<List<Repository>> call = service.repositoryListPaginate(nextLink);
        call.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                if (response.isSuccessful()) {
                    pageLinks.setLinks(response);
                    repositoryList.addAll(response.body());

                    //Check if there's a new link for the next page
                    if (!pageLinks.getNext().equals(nextLink)) {
                        nextLink = pageLinks.getNext();
                    } else {
                        nextLink = null;
                    }

                    Log.d(TAG, "onResponse: *** nextLink is: "+nextLink);
                    System.out.println("api call onResponse list size: " + repositoryList.size());
                    repoAdapter.notifyDataSetChanged();

                } else {
                    noReposToast.show();
                }
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {
                System.out.println("GitHub Api call failure: " + t);
                noReposToast.show();
            }
        });
        mSwipeRefreshRepositories.setRefreshing(false);
    }


    //Move to the next fragment and pass the selected data to it, by clicking a button on the recyclerView
    @Override
    public void recyclerViewListClicked(View v, int position, String itemName) {
        Fragment fragment = CommitsFragment.newInstance(username, itemName);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("SAVED_USERNAME", username);
        state.putParcelableArrayList("SAVED_LIST", (ArrayList<? extends Parcelable>) repositoryList);
    }


    @Override
    public void onPause() {
        super.onPause();

        //detach the LayoutManager to prevent it from being in use when returning to this fragment
        recyclerViewRepositories.setLayoutManager(null);
    }


    @Override
    public void onResume() {
        super.onResume();

        //reattach the LayoutManager, so the list isn't empty when the app comes back to foreground
        recyclerViewRepositories.setLayoutManager(mLayoutManager);
    }
}