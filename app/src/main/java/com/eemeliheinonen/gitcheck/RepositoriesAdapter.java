package com.eemeliheinonen.gitcheck;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.eemeliheinonen.gitcheck.models.Repository;

import java.util.List;


/**
 * Created by eemeliheinonen on 28/03/2017.
 */

public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.ViewHolder> {

    private List<Repository> repositoryList;
    private static RecyclerViewClickListener itemListener;
    private  Context mContext;


    public RepositoriesAdapter(Context context, List<Repository> repos, RecyclerViewClickListener listener) {
        repositoryList = repos;
        itemListener = listener;
        mContext = context;
    }

    // Easy access to the context object in the recyclerView
    private Context getContext() {
        return mContext;
    }

    @Override
    public RepositoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create and inflater to inflate all the list items with their layout
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.list_item_repositories, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    // Populate the list items through the ViewHolder
    @Override
    public void onBindViewHolder(RepositoriesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Repository repository = repositoryList.get(position);

        // Set item views based on your views and data model
        Button button = viewHolder.messageButton;
        button.setText(repository.getName());
    }

    @Override
    public int getItemCount() {
        if(repositoryList!=null){
            return repositoryList.size();
        } else {
            Toast toast = Toast.makeText(getContext(), "No repositories", Toast.LENGTH_SHORT);
            toast.show();
            return 0;
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //Contain variables for all views in a row
        Button messageButton;

        public ViewHolder(final View itemView) {
            super(itemView);

            //Init the view & add an onClickListener
            messageButton = (Button) itemView.findViewById(R.id.buttonSelectRepository);

            messageButton.setOnClickListener(new View.OnClickListener() {

                //Pass the selected item from this adapter to the RepositoriesFragment class
                public void onClick(View v) {

                    //Call the recyclerViewClickListener interfaces method to know which list item was clicked.
                    itemListener.recyclerViewListClicked(itemView, getLayoutPosition(), messageButton.getText().toString());
                }
            });
        }
    }
}
