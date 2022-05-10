/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    /**
     * DO NOT ALTER ANY VARIABLE OR VALUE NAMES OR THEIR INITIAL VALUES.
     *
     * Anything labeled var instead of val is expected to be changed in the functions but DO NOT
     * alter their initial values declared here, this could cause the app to not function properly.
     */
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has be drunk and the glass is empty
    private val RESTART = "restart"
    // Default the state to select
    private var lemonadeState = "select"
    // Default lemonSize to -1
    private var lemonSize = -1
    // Default the squeezeCount to -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null


    // making a tag for the Debugging
    private  val TAG = "Main Activity"





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // === DO NOT ALTER THE CODE IN THE FOLLOWING IF STATEMENT ===
        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        // === END IF STATEMENT ===

        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()
        lemonImage!!.setOnClickListener {

            // calling the methods
            clickLemonImage()
        }
        lemonImage!!.setOnLongClickListener {

            showSnackbar()
        }
    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     *
     * This method saves the state of the app if it is put in the background.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    /**
     * Clicking will elicit a different response depending on the state.
     * This method determines the state and proceeds with the correct action.
     */
    private fun clickLemonImage() {

        Log.d(TAG, "No in the clicklemonImage function")
        // making the if statement that will choose the state
        when (lemonadeState){
            SELECT -> {
                // advancing the state to the squeeze
                lemonadeState = SQUEEZE
                lemonSize = LemonTree.pick()
                squeezeCount = 0

            }

            SQUEEZE -> {
                squeezeCount++
                lemonSize--
                // checking to see if the lemonSize is 0
                if (lemonSize == 0){
                    // need to change the state
                    lemonadeState = DRINK
                    lemonSize = -1
                    // changing the squeezeCount
                    squeezeCount = -1
                }
            }

            DRINK -> {
                lemonadeState = RESTART
            }

            else -> {
                lemonadeState = this.SELECT
            }


        }
        // this is the function that will be called to make change the ui
        setViewElements()

    }

    /**
     * Set up the view elements according to the state.
     */
    private fun setViewElements() {
        Log.d(TAG, "Now in the setViewElements function")
        val textAction: TextView = findViewById(R.id.text_action)

        // making the values that will be set within the when
        // block
        var draw_int : Int = 0
        var string_int : Int = 0

        when (lemonadeState){

            this.SELECT -> {
                draw_int = R.drawable.lemon_tree
                string_int = R.string.lemon_select
            }

            this.SQUEEZE -> {
                draw_int = R.drawable.lemon_squeeze
                string_int = R.string.lemon_squeeze
            }
            this.DRINK -> {
                draw_int = R.drawable.lemon_drink
                string_int = R.string.lemon_drink
            }
            this.RESTART -> {
                draw_int = R.drawable.lemon_restart
                string_int = R.string.lemon_empty_glass
            }
        }
        // setting the text and the drawable
        textAction.text = getText(string_int)
        lemonImage!!.setImageResource(draw_int)


    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     *
     * Long clicking the lemon image will show how many times the lemon has been squeezed.
     */
    private fun showSnackbar(): Boolean {
        Log.d(TAG, "Within the showSnackbar function")
        if (lemonadeState != SQUEEZE) {
            makeMessage()
            return true
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
    /**
    This is the function that will be used to make the messages that are in the snackBar
     */
    private  fun makeMessage(){
        // using when
        val theText = when (this.lemonadeState){
            // will put messages in according to the state
            this.RESTART -> "You need to press the empty glass to start over!"
            this.DRINK -> "Come on the Lemonade is good!"
            else -> "You need to pick some lemons to make lemonade"

        }
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            theText,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}

/**
 * A Lemon tree class with a method to "pick" a lemon. The "size" of the lemon is randomized
 * and determines how many times a lemon needs to be squeezed before you get lemonade.
 */
class LemonTree {

    companion object{
        fun pick(): Int {
            return (2..4).random()
        }
    }

}
