/* Copyright 2016 Google Inc.
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

package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 3;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;

    Stack<LetterTile> placedTiles = new Stack<LetterTile>();

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.containsKey("msg")){
            TextView msg = (TextView) findViewById(R.id.message_box);
            msg.setText(savedInstanceState.getString("msg"));
        }
        if(savedInstanceState.containsKey("word1")){
            word1=savedInstanceState.getString("word1");
        }
        if(savedInstanceState.containsKey("word2")){
            word2=savedInstanceState.getString("word2");
        }
        if(savedInstanceState.containsKey("letters1")){
            String letters1= savedInstanceState.getString("letters1");
            LinearLayout box1 = (LinearLayout) findViewById(R.id.word1);
            for(int i=0; i<letters1.length(); i++){
                LetterTile letter = new LetterTile(this, letters1.charAt(i));
                box1.addView(letter);
            }
        }
        if(savedInstanceState.containsKey("letters2")){
            String letters2= savedInstanceState.getString("letters2");
            LinearLayout box2 = (LinearLayout) findViewById(R.id.word2);
            for(int i=0; i<letters2.length(); i++){
                LetterTile letter = new LetterTile(this, letters2.charAt(i));
                box2.addView(letter);
            }
        }
        if(savedInstanceState.containsKey("stack")){
            String stack = savedInstanceState.getString("stack");
            for(int i=stack.length()-1; i>=0; i--){
                LetterTile tile = new LetterTile(this,stack.charAt(i));
                //Log.d("tile", result.substring(i, i+1));
                stackedLayout.push(tile);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LinearLayout box1 = (LinearLayout) findViewById(R.id.word1);
        LinearLayout box2 = (LinearLayout) findViewById(R.id.word2);
        String letters1 = "";
        String letters2 = "";
        for(int i=0; i<box1.getChildCount(); i++){
            if (box1.getChildAt(i) instanceof LetterTile) {
                LetterTile tile = (LetterTile) box1.getChildAt(i);
                letters1+=tile.getChar();
            }
        }
        for(int i=0; i<box2.getChildCount(); i++){
            if (box2.getChildAt(i) instanceof LetterTile) {
                LetterTile tile = (LetterTile) box2.getChildAt(i);
                letters2+=tile.getChar();
            }
        }

        String remaining = "";
        Stack<View> stack = stackedLayout.getTiles();
        while(!stack.isEmpty()){
            LetterTile tile = (LetterTile)stack.pop();
            remaining+=tile.getChar();
        }
        Log.i("letters1", letters1);
        Log.i("letters2", letters2);
        Log.i("stack", remaining);
        TextView messageBox = (TextView) findViewById(R.id.message_box);
        outState.putString("stack", remaining);
        outState.putString("letters1", letters1);
        outState.putString("letters2", letters2);
        outState.putString("word1", word1);
        outState.putString("word2", word2);
        outState.putString("msg", messageBox.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if(word.length()==WORD_LENGTH) words.add(word);
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        View word1LinearLayout = findViewById(R.id.word1);
        //word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
        //word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                placedTiles.push(tile);
                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    ViewGroup group = (ViewGroup) v;
                    tile.moveToViewGroup(group);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    placedTiles.push(tile);
                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {
        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");
        stackedLayout.clear();
        LinearLayout box1 = (LinearLayout) findViewById(R.id.word1);
        LinearLayout box2 = (LinearLayout) findViewById(R.id.word2);
        box1.removeAllViews();
        box2.removeAllViews();

        int num1 = random.nextInt(words.size());
        int num2 = random.nextInt(words.size());
        word1 = words.get(num1);
        word2 = words.get(num2);
        Log.i("word1", word1);
        Log.i("word2", word2);
        String result="";

        int counter1=0, counter2 = 0;
        while(counter1<WORD_LENGTH || counter2<WORD_LENGTH){
            if(counter1>=WORD_LENGTH){
                result+=word2.substring(counter2);
                counter2=WORD_LENGTH;
            }else if(counter2>=WORD_LENGTH){
                result+=word1.substring(counter1);
                counter1=WORD_LENGTH;
            }else {
                int rand = random.nextInt(2) + 1;
                if (rand == 1) {
                    result = result + word1.charAt(counter1);
                    counter1++;
                } else {
                    result = result + word2.charAt(counter2);
                    counter2++;
                }
            }
        }
        messageBox.setText(result);
        for(int i=result.length()-1; i>=0; i--){
            LetterTile tile = new LetterTile(this,result.charAt(i));
            //Log.d("tile", result.substring(i, i+1));
            stackedLayout.push(tile);
        }
        return true;
    }

    public boolean onUndo(View view) {
        if(!placedTiles.empty()) {
            LetterTile tile = placedTiles.pop();
            tile.moveToViewGroup(stackedLayout);
            return true;
        }
        return false;
    }

    public void checkForDataAndRestoreGame(Bundle savedInstanceState){

    }
}
