package com.conz13.d.strongpasswordcreator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dillon on 4/18/16.
 */
public class WordGenerationFragment extends Fragment {

    private ImageView mDiceOne;
    private ImageView mDiceTwo;
    private ImageView mDiceThree;
    private ImageView mDiceFour;
    private ImageView mDiceFive;
    private TextView mTextView;

    private RecyclerView mRecyclerView;
    private GeneratedWordRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mResultantWords;

    int mGeneratedNumber[];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != savedInstanceState){
            if(null != savedInstanceState.getIntArray(getString(R.string.dice_array_key))){
                mGeneratedNumber = savedInstanceState.getIntArray(getString(R.string.dice_array_key));
                mResultantWords = savedInstanceState.getStringArrayList(getString(R.string.words_list_key));
            }
        } else {
            // Only executed if the save state was not preserved
            mResultantWords = new ArrayList<>();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(null != mGeneratedNumber) {
            outState.putIntArray(getString(R.string.dice_array_key), mGeneratedNumber);
            outState.putStringArrayList(getString(R.string.words_list_key), mResultantWords);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_generation_fragment_layout, container, false);
        mDiceOne = (ImageView)rootView.findViewById(R.id.dice_holder_one);
        mDiceTwo = (ImageView)rootView.findViewById(R.id.dice_holder_two);
        mDiceThree = (ImageView)rootView.findViewById(R.id.dice_holder_three);
        mDiceFour = (ImageView)rootView.findViewById(R.id.dice_holder_four);
        mDiceFive = (ImageView)rootView.findViewById(R.id.dice_holder_five);
        mTextView = (TextView)rootView.findViewById(R.id.temp_word_textview);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.resultant_word_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GeneratedWordRecyclerAdapter(mResultantWords);
        mRecyclerView.setAdapter(mAdapter);

        spinOnClick((Button)rootView.findViewById(R.id.roll_button));
        addOnClick((ImageButton)rootView.findViewById(R.id.add_to_list_button));

        if(null != savedInstanceState && null != mGeneratedNumber){
            setUpDice(mGeneratedNumber);
        }
        return rootView;
    }

    private void spinOnClick(Button rollButton){
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int generatedNumber[] = Utility.getDiceRoll();
                mGeneratedNumber = generatedNumber;
                setUpDice(generatedNumber);
            }
        });
    }

    private void addOnClick(ImageButton addButton){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTextView.getText() != null)
                if(mResultantWords.add(String.valueOf(mTextView.getText()))){
                    mAdapter.setmWords(mResultantWords);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setUpDice(int generatedNumber[]){
        mDiceOne.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(generatedNumber[0])));
        mDiceTwo.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(generatedNumber[1])));
        mDiceThree.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(generatedNumber[2])));
        mDiceFour.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(generatedNumber[3])));
        mDiceFive.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(generatedNumber[4])));

        // TODO: make a separate set up list text entries so that on rotate the list view is preserved too

        String numberAsString = Utility.convertIntArrayToString(generatedNumber);
        String dicewareWord = Utility.getPropertyValue(getContext(), numberAsString);
        mTextView.setText(dicewareWord);
    }
}
