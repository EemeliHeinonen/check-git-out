package com.eemeliheinonen.gitcheck;

import android.view.View;

/**
 * Created by eemeliheinonen on 29/03/2017.
 */

//Interface for passing the name of the clicked RecyclerView item from the adapter to the fragment
interface RecyclerViewClickListener {

    void recyclerViewListClicked(View v, int position, String name);
}
