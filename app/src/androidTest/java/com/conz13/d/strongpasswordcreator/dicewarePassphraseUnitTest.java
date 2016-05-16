package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class dicewarePassphraseUnitTest {
    Context context;
    // Uncomment fr, pl and tr when lists are updated
    String languages[] = {"_ca",
    "_da",
    "_nl",
    "_en",
    "_eo",
    "_fi",
    //"_fr",
    "_de",
    "_it",
    "_jp",
    "_mi",
    "_no",
   //"_pl",
    "_ru",
    "_es",
    "_sv"
   //"_tr"
    };

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);


    // Tests to make sure there are no null values in the diceware_en.properties file
    @Test
    public void testAllPossibleValuesTest() {
        int num = 11111;
        String word = "";
        int counter = 1;
        int listLength = 7776;
        context = mActivityTestRule.getActivity().getApplicationContext();

        for(String language : languages) {
            Log.v("testAllPossibleValues", language);
            while (num < 66666) {
                word = Utility.getPropertyValue(context, Integer.toString(num), language);
                if (word == null) {
                    break;
                }
                counter++;
                num = handleNumber(num);
            }
        }
        assertEquals(listLength,counter);
        assertNotNull(word);
    }

    // I created a mediocre base 6 number system that can only go between 1 and 6

    private int handleNumber(int num){
        int[] intArray = disassembleNum(num);

        intArray[4]++; // ones
        if(intArray[4] > 6){
            intArray[4] = 1;
            intArray[3]++; // tens
            if(intArray[3] > 6){
                intArray[3] = 1;
                intArray[2] ++; //hundreds
                if(intArray[2] > 6){
                    intArray[2] = 1;
                    intArray[1] ++; //thousands
                    if(intArray[1] > 6){
                        intArray[1] = 1;
                        intArray[0] ++; //ten-thousands
                        if(intArray[0] > 6){
                            //reset the numbers to something greater than 66666
                            //so that the while loop can end
                            intArray[0] = 7;
                            intArray[1] = 7;
                            intArray[2] = 7;
                            intArray[3] = 7;
                            intArray[4] = 7;
                        }
                    }
                }
            }
        }

        return assembleNum(intArray);
    }

    private int assembleNum(int[] intArray){

        intArray[0] = intArray[0]*10000;
        intArray[1] = intArray[1]*1000;
        intArray[2] = intArray[2]*100;
        intArray[3] = intArray[3]*10;

        return intArray[0] + intArray[1] + intArray[2] + intArray[3] + intArray[4];
    }

    private int[] disassembleNum(int num){
        int[] intArray = new int[5];

        intArray[4] = num % 10; // ones
        num = num / 10;
        intArray[3] = num % 10; // tens
        num = num / 10;
        intArray[2] = num % 10; // hundreds
        num = num / 10;
        intArray[1] = num % 10; // thousands
        num = num /10;
        intArray[0] = num; // ten-thousands

        return intArray;
    }

    // Randomly test x values for null
    @Test
    public void testRandomDicewareIsNotNull() {
        int MAX_INT = 6;
        int x = 500;
        boolean nullFlag = false;
        context = mActivityTestRule.getActivity().getApplicationContext();

        Random rndNumber = new Random();
        for(String language: languages) {
            for (int i = 0; i < x; i++) {
                String temp = "";
                while (temp.length() < 5) {
                    temp = temp.concat(Integer.toString(rndNumber.nextInt(MAX_INT) + 1));
                }
                String word = Utility.getPropertyValue(context, temp, language);
                if (word == null) {
                    nullFlag = true;
                    Log.e("randomDicewareIsNotNull", "value was: " + temp);
                    break;
                }
            }
        }

        assertFalse(nullFlag);
    }

    @Test
    public void testGetDiceRoll(){
        int arrayToTest[];
        int timesToTest = 1000;
        boolean flag = true;

        for(int i =0; i < timesToTest; i++){
            arrayToTest = Utility.getDiceRoll();
            for(int y : arrayToTest){
                if(y < 1 || y > 6){
                    flag = false;
                    break;
                }
            }
        }
        assertTrue(flag);
    }

    @Test
    public void testConvertArrayToString(){
        int arrayToTest[] = new int[5];
        arrayToTest[0] = 1;
        arrayToTest[1] = 2;
        arrayToTest[2] = 3;
        arrayToTest[3] = 4;
        arrayToTest[4] = 5;
        String stringToTest = Utility.convertIntArrayToString(arrayToTest);

        assertEquals("12345", stringToTest);
    }

}