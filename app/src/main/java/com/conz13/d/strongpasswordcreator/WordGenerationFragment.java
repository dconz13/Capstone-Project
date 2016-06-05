package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conz13.d.strongpasswordcreator.helper.ClearDeleteButton;
import com.conz13.d.strongpasswordcreator.helper.GeneratedWordItemTouchHelperCallback;
import com.conz13.d.strongpasswordcreator.helper.OnDragListener;

import java.util.ArrayList;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;

/**
 * Created by dillon on 4/18/16.
 */
public class WordGenerationFragment extends Fragment
    implements ClearDeleteButton, OnDragListener, SharedPreferences.OnSharedPreferenceChangeListener{

    public static final int DELETE_ALL_BUTTON = 0;
    public static final int SAVE_BUTTON = 1;

    private WheelView mDiceOne;
    private WheelView mDiceTwo;
    private WheelView mDiceThree;
    private WheelView mDiceFour;
    private WheelView mDiceFive;
    private TextView mTextView;
    private ImageButton mAddButton;
    private boolean animationsEnabled;

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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        animationsEnabled = prefs
                .getBoolean(getContext().getString(R.string.disable_dice_animation_key), true);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(null != sharedPreferences && null!= key && key.equals(getContext().getString(R.string.disable_dice_animation_key))){
            animationsEnabled = sharedPreferences.getBoolean(key, true);
        }
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
        mDiceOne = (WheelView) rootView.findViewById(R.id.slot_1);
        mDiceTwo = (WheelView) rootView.findViewById(R.id.slot_2);
        mDiceThree = (WheelView) rootView.findViewById(R.id.slot_3);
        mDiceFour = (WheelView) rootView.findViewById(R.id.slot_4);;
        mDiceFive = (WheelView) rootView.findViewById(R.id.slot_5);
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
        ((MainActivity)getActivity()).updateNavItemSelected(MainActivity.HOME);

        if(null != savedInstanceState && null != mGeneratedNumber){
            setUpDice(mGeneratedNumber);
        } else{
            Context context = getContext();
            initDice(context);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        if(((MyApplication)getActivity().getApplication()).getSKIPPED_LOGIN()){
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
                boolean skipFlag = ((MyApplication)getActivity().getApplication()).getSKIPPED_LOGIN();
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

    /**
     * The following code was referenced from https://github.com/maarek/android-wheel and heavily edited for my use case
     */

    private void initDice(Context context){
        WheelView test[] = {mDiceOne, mDiceTwo, mDiceThree, mDiceFour, mDiceFive};
        for(WheelView wheel : test) {
            wheel.setViewAdapter(new SlotMachineAdapter(context));
            wheel.setCurrentItem(0);

            wheel.addChangingListener(changedListener);
            wheel.addScrollingListener(scrolledListener);
            wheel.setCyclic(true);
            wheel.setEnabled(false);
        }
    }

    // Set up dice when a number is restored from savedInstanceState
    private void initDice(int generatedNumber[], Context context){
        WheelView test[] = {mDiceOne, mDiceTwo, mDiceThree, mDiceFour, mDiceFive};
        int index = 0;
        for(WheelView wheel : test) {
            wheel.setViewAdapter(new SlotMachineAdapter(context));
            wheel.setCurrentItem(generatedNumber[index] - 1);

            wheel.addChangingListener(changedListener);
            wheel.addScrollingListener(scrolledListener);
            wheel.setCyclic(true);
            wheel.setEnabled(false);
            ++index;
        }
    }

    // Updates the currently displayed number value
    private void updateDice() {
        int generatedNumber[] = getDiceValue();
        mGeneratedNumber = generatedNumber;

        // If the user rotates the device while the dice are still spinning this context check
        // will prevent a crash
        if (null != getContext()){
            String numberAsString = Utility.convertIntArrayToString(generatedNumber);
            String dicewareWord = Utility.getPropertyValue(getContext(), numberAsString, Utility.getLanguage(getContext()));
            mTextView.setText(dicewareWord);
            mAddButton.setVisibility(View.VISIBLE);
        }
    }

    // Converts the current dice values to an int array
    private int[] getDiceValue(){
        int value[] = new int[5];
        value[0] = mDiceOne.getCurrentItem() + 1;
        value[1] = mDiceTwo.getCurrentItem() + 1;
        value[2] = mDiceThree.getCurrentItem() + 1;
        value[3] = mDiceFour.getCurrentItem() + 1;
        value[4] = mDiceFive.getCurrentItem() + 1;
        return value;
    }

    // Sets up the dice and word on a rotation change
    private void setUpDice(int generatedNumber[]){
        Context context = getContext();
        initDice(generatedNumber, context);

        String numberAsString = Utility.convertIntArrayToString(generatedNumber);
        String dicewareWord = Utility.getPropertyValue(getContext(), numberAsString, Utility.getLanguage(context));
        mTextView.setText(dicewareWord);
        mAddButton.setVisibility(View.VISIBLE);
    }

    private void spinDice(int generatedNumber[]){
        if(animationsEnabled) {
            mDiceOne.scroll(-350 + generatedNumber[0], 2000);
            mDiceTwo.scroll(-350 + generatedNumber[1], 2050);
            mDiceThree.scroll(-350 + generatedNumber[2], 2100);
            mDiceFour.scroll(-350 + generatedNumber[3], 2150);
            mDiceFive.scroll(-350 + generatedNumber[4], 2200);
        }
        else {
            mDiceOne.setCurrentItem(generatedNumber[0], false);
            mDiceTwo.setCurrentItem(generatedNumber[1], false);
            mDiceThree.setCurrentItem(generatedNumber[2], false);
            mDiceFour.setCurrentItem(generatedNumber[3], false);
            mDiceFive.setCurrentItem(generatedNumber[4], false);
        }
    }

    private void spinOnClick(Button rollButton){
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int generatedNumber[] = Utility.getDiceRoll();
                mGeneratedNumber = generatedNumber;
                spinDice(generatedNumber);
            }
        });
    }

    // Wheel scrolled flag
    private boolean wheelScrolled = false;
    private int x = 0;

    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }
        @Override
        public void onScrollingFinished(WheelView wheel) {
            if(wheelScrolled && x>=4) {
                wheelScrolled = false;
                updateDice();
                x=0;
            }
            x++;
        }
    };

    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!wheelScrolled) {
                updateDice();
            }
        }
    };
}
