package com.eemeliheinonen.gitcheck;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eemeliheinonen.gitcheck.models.Commit;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by eemeliheinonen on 29/03/2017.
 */

public class CommitsAdapter extends RecyclerView.Adapter<CommitsAdapter.ViewHolder> {

    private List<Commit> commitList;
    private  Context mContext;

    public CommitsAdapter(Context context, List<Commit> commits) {
        commitList = commits;
        mContext = context;
    }

    // Easy access to the context object in the recyclerView
    private Context getContext() {
        return mContext;
    }

    @Override
    public CommitsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_item_commits, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(CommitsAdapter.ViewHolder viewHolder, int position) {
        TextView textViewCommitMessage = viewHolder.tvCommitMessage;
        TextView textViewCommitAuthor = viewHolder.tvCommitAuthor;
        TextView textViewCommitTime = viewHolder.tvCommitTime;
        ImageView imageViewCommitAuthorAvatar = viewHolder.ivCommitAuthorAvatar;

        //Populate the commitList items
        Commit commit = commitList.get(position);
        textViewCommitMessage.setText(commit.getCommitData().getMessage());
        textViewCommitAuthor.setText("-"+commit.getCommitData().getAuthor().getName());

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm dd.MM.yy");
        String commitTime = formatter.format(commit.getCommitData().getAuthor().getDate());
        textViewCommitTime.setText(commitTime);

        //Get the author's image, if available
        if (commit.getAuthor()!=null) {
            Picasso.with(getContext())
                    .load(commit.getAuthor().getAvatarUrl())
                    .resize(150,150)
                    .into(imageViewCommitAuthorAvatar);
        }
    }

    @Override
    public int getItemCount() {
        if ( commitList!=null){
            return commitList.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //Contain variables for all views in a row
        TextView tvCommitMessage;
        TextView tvCommitAuthor;
        TextView tvCommitTime;
        ImageView ivCommitAuthorAvatar;

        public ViewHolder(final View itemView) {
            super(itemView);

            //Init the views
            tvCommitMessage = (TextView) itemView.findViewById(R.id.textViewCommitMessage);
            tvCommitAuthor  = (TextView) itemView.findViewById(R.id.textViewCommitAuthor);
            tvCommitTime  = (TextView) itemView.findViewById(R.id.textViewCommitTime);
            ivCommitAuthorAvatar  = (ImageView) itemView.findViewById(R.id.imageViewCommitAuthor);
        }
    }
}
