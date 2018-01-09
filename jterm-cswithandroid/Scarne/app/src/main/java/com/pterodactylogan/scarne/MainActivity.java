package com.pterodactylogan.scarne;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Random;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    public int userOverallScore = 0;
    public int userTurnScore = 0;
    public int compOverallScore = 0;
    public int compTurnScore = 0;
    public int currRoll1 = 0;
    public int currRoll2 = 0;
    Random rand = new Random();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void Reset(View view) {
        Reset();
    }

    public void Reset() {
        userTurnScore = 0;
        userOverallScore = 0;
        compOverallScore = 0;
        compTurnScore = 0;
        currRoll1 = 0;
        currRoll2 = 0;
        comp=false;
        handler.removeCallbacks(compTurn);
        scoreDisplay("your");
    }

    public void scoreDisplay(String player) {
        String display = "Your score: " + userOverallScore + " Computer score: " + compOverallScore;
        display += "\n\nIt is " + player + " turn!";
        if (userTurnScore != 0) {
            display += " Turn score: " + userTurnScore;
        } else if (compTurnScore != 0) {
            display += " Turn score: " + compTurnScore;
        }
        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setText(display);
    }

    public void Roll(View veiw) {
        //do nothing if its the computers turn
        if(comp==true) return;

        //Log.d("roll test", "rolled");
        RollDisplay();
        /*if (currRoll == 1) {
            userTurnScore = 0;
            scoreDisplay("the computer's");
            computerTurn();
        } else {
            userTurnScore += currRoll;
            scoreDisplay("your");
        }*/
    }

    public void Hold(View view) {
        //do nothing if its the computers turn
        if(comp==true) return;

        userOverallScore += userTurnScore;
        //if user has won
        if(userOverallScore>=100){
            String display = "Your score: " + userOverallScore + " Computer score: " + compOverallScore;
            display+="\n\nYou Win! Press Reset to play again";
            TextView scoreText = (TextView) findViewById(R.id.scoreText);
            scoreText.setText(display);
            comp=true;
            return;
        }
        userTurnScore = 0;
        scoreDisplay("the computer's");
        computerTurn();
    }

    //this function sets currRoll to a num btwn 1-6 and changes the image accordingly
    public void RollDisplay() {
        currRoll1 = rand.nextInt(6) + 1;
        currRoll2 = rand.nextInt(6)+1;
        if (currRoll1 == 1) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage1);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice1));
            dieImage.setContentDescription("one");
        } else if (currRoll1 == 2) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage1);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice2));
            dieImage.setContentDescription("two");
        } else if (currRoll1 == 3) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage1);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice3));
            dieImage.setContentDescription("three");
        } else if (currRoll1 == 4) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage1);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice4));
            dieImage.setContentDescription("four");
        } else if (currRoll1 == 5) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage1);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice5));
            dieImage.setContentDescription("five");
        } else {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage1);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice6));
            dieImage.setContentDescription("six");
        }
        if (currRoll2 == 1) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage2);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice1));
            dieImage.setContentDescription("one");
        } else if (currRoll2 == 2) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage2);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice2));
            dieImage.setContentDescription("two");
        } else if (currRoll2 == 3) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage2);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice3));
            dieImage.setContentDescription("three");
        } else if (currRoll2 == 4) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage2);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice4));
            dieImage.setContentDescription("four");
        } else if (currRoll2 == 5) {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage2);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice5));
            dieImage.setContentDescription("five");
        } else {
            ImageView dieImage = (ImageView) findViewById(R.id.dieImage2);
            dieImage.setImageDrawable(getResources().getDrawable(R.drawable.dice6));
            dieImage.setContentDescription("six");
        }
    }

    private final Handler handler = new Handler();
    boolean comp = false;

    public void computerTurn() {
        //it is the computers turn
        comp = true;

        //delays call of compTurn.run() by 1 second (to see 1 on screen so user knows what happened)
        handler.postDelayed(compTurn, 1000);

        comp=false;
        return;

    }

    final Runnable compTurn = new Runnable() {
        @Override
        public void run() {
            //hold if score is 20 or over
            if (compTurnScore >= 20) {
                compOverallScore += compTurnScore;
                //if computer has won
                if(compOverallScore>=100){
                    String display = "Your score: " + userOverallScore + " Computer score: " + compOverallScore;
                    display+="\n\nThe computer wins :( Press Reset to play again";
                    TextView scoreText = (TextView) findViewById(R.id.scoreText);
                    scoreText.setText(display);
                    comp=true;
                    return;
                }
                compTurnScore = 0;
                scoreDisplay("your");
                return;
            } else {
                //roll the die
                RollDisplay();
                /*if (currRoll == 1) {
                    compTurnScore = 0;
                    //players turn
                    scoreDisplay("your");
                    return;
                } else {
                    compTurnScore += currRoll;
                    scoreDisplay("the computer's");
                    //keep rolling
                    handler.postDelayed(this, 1000);
                }*/
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
