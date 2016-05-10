package com.conz13.d.strongpasswordcreator;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dillon on 5/5/16.
 */
public class GeneratedWordRecyclerAdapter extends RecyclerView.Adapter<GeneratedWordRecyclerAdapter.GeneratedWordViewHolder>
        {
    private ArrayList<String> mWords;

            // TODO: set up ItemTouchHelper for swipe to delete and drag on reorder icon
    public class GeneratedWordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView mDeleteButton;
        public final ImageView mReorderButton;
        public final TextView mResultantWord;

        public GeneratedWordViewHolder(View view){
            super(view);
            mDeleteButton = (ImageView)view.findViewById(R.id.delete_button);
            mReorderButton = (ImageView)view.findViewById(R.id.reorder_button);
            mResultantWord = (TextView)view.findViewById(R.id.resultant_word);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public GeneratedWordRecyclerAdapter(ArrayList<String> resultantWords){
        mWords = resultantWords;
    }

    public void setmWords(ArrayList<String> words){
        mWords = words;
    }

    public static interface GeneratedWordAdapterOnClickHandler {
        void onClick(GeneratedWordViewHolder vh);
    }

    @Override
    public GeneratedWordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resultant_word_list_item,parent,false);

        return new GeneratedWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GeneratedWordViewHolder holder, int position) {
        if(null != mWords.get(position)) {
            holder.mResultantWord.setText(mWords.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mWords.size();
    }

}
