package com.conz13.d.strongpasswordcreator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dillon on 4/18/16.
 */
public class WordGenerationFragment extends Fragment {

    ImageView mDiceOne;
    ImageView mDiceTwo;
    ImageView mDiceThree;
    ImageView mDiceFour;
    ImageView mDiceFive;
    TextView mTextView;

    int mGeneratedNumber[];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != savedInstanceState){
            if(null != savedInstanceState.getIntArray(getString(R.string.dice_array_key))){
                mGeneratedNumber = savedInstanceState.getIntArray(getString(R.string.dice_array_key));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(null != mGeneratedNumber) {
            outState.putIntArray(getString(R.string.dice_array_key), mGeneratedNumber);
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

        spinOnClick((Button)rootView.findViewById(R.id.spin_button));

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
