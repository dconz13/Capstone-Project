package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.conz13.d.strongpasswordcreator.helper.ClearDeleteButton;
import com.conz13.d.strongpasswordcreator.helper.GeneratedWordItemTouchHelperCallback;
import com.conz13.d.strongpasswordcreator.helper.OnDragListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dillon on 4/18/16.
 */
public class WordGenerationFragment extends Fragment
    implements ClearDeleteButton, OnDragListener{

    public static final int DELETE_ALL_BUTTON = 0;
    public static final int SAVE_BUTTON = 1;

    private ImageView mDiceOne;
    private ImageView mDiceTwo;
    private ImageView mDiceThree;
    private ImageView mDiceFour;
    private ImageView mDiceFive;
    private TextView mTextView;
    private ImageButton mAddButton;

    private Menu mMenu;

    private RecyclerView mRecyclerView;
    private GeneratedWordRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mResultantWords;
    private ItemTouchHelper mItemTouchHelper;
    private CoordinatorLayout mCoordLayout;

    private AlertDialog mDeleteAllDialog;

    int mGeneratedNumber[];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != savedInstanceState){
            if(null != savedInstanceState.getIntArray(getString(R.string.dice_array_key))){
                mGeneratedNumber = savedInstanceState.getIntArray(getString(R.string.dice_array_key));
            }
            if(null != savedInstanceState.getStringArrayList(getString(R.string.words_list_key))){
                mResultantWords = savedInstanceState.getStringArrayList(getString(R.string.words_list_key));
            }
        } else {
            // Only executed if the save state was not preserved
            if(mResultantWords == null)
                mResultantWords = new ArrayList<>();
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(null != mGeneratedNumber) {
            outState.putIntArray(getString(R.string.dice_array_key), mGeneratedNumber);
        }
        if(null != mResultantWords) {
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
        mAddButton = (ImageButton)rootView.findViewById(R.id.add_to_list_button);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.resultant_word_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GeneratedWordRecyclerAdapter(mResultantWords, this, this);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new GeneratedWordItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        spinOnClick((Button)rootView.findViewById(R.id.roll_button));
        addOnClick((ImageButton)rootView.findViewById(R.id.add_to_list_button));
        fabOnClick((FloatingActionButton)rootView.findViewById(R.id.save_fab));
        mCoordLayout = (CoordinatorLayout)rootView.findViewById(R.id.coord_layout);

        if(null != savedInstanceState && null != mGeneratedNumber){
            setUpDice(mGeneratedNumber);
        } else{
            initDice();
        }

        ((MainActivity)getActivity()).updateNavItemSelected(MainActivity.HOME);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        if(null != mDeleteAllDialog){
            mDeleteAllDialog.dismiss();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.generator_menu, menu);
        mMenu = menu;
        if(!mResultantWords.isEmpty()){
            enableDeleteAll();
        }
        else {
            disableDeleteAll();
        }
        if(((MainActivity)getActivity()).skippedFlag){
            menu.findItem(R.id.sign_out).setTitle(getContext().getString(R.string.sign_in));
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_delete_all: {
                // Alert Dialog to confirm before deleting the list
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.delete_all_title))
                        .setMessage(getString(R.string.delete_all_message))
                        .setPositiveButton(R.string.delete_all_positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearList(DELETE_ALL_BUTTON);
                            }
                        });
                builder.setNegativeButton(R.string.delete_all_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing!
                    }
                });
                mDeleteAllDialog = builder.create();
                mDeleteAllDialog.show();
                break;
            }
            case R.id.action_settings: {
                startActivity(new Intent(getContext(), SettingsActivity.class));
                break;
            }
            case R.id.sign_out: {
                ((MainActivity)getActivity()).signOut();
                break;
            }
            default: break;
        }
        return super.onOptionsItemSelected(item);
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
                if(!mTextView.getText().equals(""))
                    if(mResultantWords.size()<20) // Does anyone really need a password this long?
                        if(mResultantWords.add(String.valueOf(mTextView.getText()))){
                            mAdapter.notifyDataSetChanged();
                            enableDeleteAll();
                        }
            }
        });
    }

    private void fabOnClick(FloatingActionButton fab){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start dialog for saving to database
                boolean skipFlag = ((MainActivity)getActivity()).skippedFlag;
                // skipFlag = true if login was skipped
                if(!skipFlag) {
                    ((MainActivity) getActivity()).showSaveDialog(mResultantWords);
                } else {
                    Snackbar.make(mCoordLayout, getString(R.string.save_dialog_snackbar_skip_mode),
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    // Used Picasso to load large PNG files on a separate thread so less frames would be skipped.
    // Also used Picasso to avoid writing an inefficient AsyncTask.
    private void initDice(){
        Context context = getContext();
        mDiceOne.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(1)));
        mDiceTwo.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(2)));
        mDiceThree.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(3)));
        mDiceFour.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(4)));
        mDiceFive.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(5)));
//        Picasso.with(context).load(Utility.getDiceImage(1)).into(mDiceOne);
//        Picasso.with(context).load(Utility.getDiceImage(2)).into(mDiceTwo);
//        Picasso.with(context).load(Utility.getDiceImage(3)).into(mDiceThree);
//        Picasso.with(context).load(Utility.getDiceImage(4)).into(mDiceFour);
//        Picasso.with(context).load(Utility.getDiceImage(5)).into(mDiceFive);
    }

    private void setUpDice(int generatedNumber[]){
        Context context = getContext();
        mDiceOne.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(generatedNumber[0])));
        mDiceTwo.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(generatedNumber[1])));
        mDiceThree.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(generatedNumber[2])));
        mDiceFour.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(generatedNumber[3])));
        mDiceFive.setImageDrawable(getResources().getDrawable(Utility.getDiceImage(generatedNumber[4])));
//        Picasso.with(context).load(Utility.getDiceImage(generatedNumber[0])).into(mDiceOne);
//        Picasso.with(context).load(Utility.getDiceImage(generatedNumber[1])).into(mDiceTwo);
//        Picasso.with(context).load(Utility.getDiceImage(generatedNumber[2])).into(mDiceThree);
//        Picasso.with(context).load(Utility.getDiceImage(generatedNumber[3])).into(mDiceFour);
//        Picasso.with(context).load(Utility.getDiceImage(generatedNumber[4])).into(mDiceFive);

        String numberAsString = Utility.convertIntArrayToString(generatedNumber);
        String dicewareWord = Utility.getPropertyValue(getContext(), numberAsString, Utility.getLanguage(context));
        mTextView.setText(dicewareWord);
        mAddButton.setVisibility(View.VISIBLE);
    }

    public void clearList(int source){
        mResultantWords.clear();
        mAdapter.notifyDataSetChanged();
        disableDeleteAll();

        if(source == SAVE_BUTTON)
            Snackbar.make(mCoordLayout, getString(R.string.save_dialog_snackbar), Snackbar.LENGTH_LONG).show();
    }

    private void enableDeleteAll(){
        MenuItem item = mMenu.findItem(R.id.action_delete_all);
        if(!item.isEnabled()) {
            item.setEnabled(true);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    private void disableDeleteAll(){
        MenuItem item = mMenu.findItem(R.id.action_delete_all);
        if(item.isEnabled()){
            item.setEnabled(false);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }
    }

    @Override
    public void clearDeleteButton() {
        disableDeleteAll();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
