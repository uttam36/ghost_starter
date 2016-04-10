package com.google.engedu.ghost;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity
{
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static final String saveUserScore = "user score";
    private static final String saveCompScore = "comp score";
    private static final String saveGhostText = "ghost text";
    private static final String saveStatus = "status";

    static int USER_SCORE = 0;
    static int COMP_SCORE = 0;



    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    TextView tvGhostText;
    TextView tvStatus ;
    Button bChallenge;
    Button bReset;
    TextView tUser;
    TextView tComp;

    private SimpleDictionary simpleDictionary;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        InputStream is = null;

        try {
               is = getAssets().open("words.txt");
               simpleDictionary = new SimpleDictionary(is);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        tvGhostText = (TextView) findViewById(R.id.ghostText);
        tvStatus = (TextView) findViewById(R.id.gameStatus);
        TextView tvStatus = (TextView) findViewById(R.id.gameStatus);
        Button bChallenge = (Button) findViewById(R.id.Challenge);
        Button bReset = (Button) findViewById(R.id.Restart);
        tUser = (TextView) findViewById(R.id.userText);
        tComp = (TextView) findViewById(R.id.compText);
        onStart(null);

        if(savedInstanceState!=null)
        {
            USER_SCORE = savedInstanceState.getInt(saveUserScore);
            COMP_SCORE = savedInstanceState.getInt(saveCompScore);
            tvGhostText.setText(savedInstanceState.getString(saveGhostText));
            tvStatus.setText(savedInstanceState.getString(saveStatus));
        }

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restart();
            }
        });

        bChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Challenge();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putInt(saveUserScore,USER_SCORE);
        savedInstanceState.putInt(saveCompScore,COMP_SCORE);
        savedInstanceState.putString(saveGhostText,tvGhostText.getText().toString());
        savedInstanceState.putString(saveStatus,tvStatus.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    private void computerTurn()
    {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        String text = tvGhostText.getText().toString();

        if(text.length()>3 && simpleDictionary.isWord(text))
        {
            Toast.makeText(GhostActivity.this,"Computer Win !!",Toast.LENGTH_SHORT).show();
            label.setText("Computer Win !!");
            COMP_SCORE++;
            tComp.setText("Computer Score: "+COMP_SCORE);
            Restart();
        }
        else
        {
            String longWord = simpleDictionary.getAnyWordStartingWith(text);
            if (longWord == null)
            {
                Toast.makeText(GhostActivity.this,"Computer Win !!",Toast.LENGTH_SHORT).show();
                label.setText("Computer Win !!");
                COMP_SCORE++;
                tComp.setText("Computer Score: "+COMP_SCORE);
                Restart();
            }
            else
            {
                text = text + longWord.charAt(text.length());
                tvGhostText.setText(text);
                if(simpleDictionary.isWord(text))
                {
                    Toast.makeText(GhostActivity.this,"You Win !!",Toast.LENGTH_SHORT).show();
                    tvStatus.setText("You Win !!");
                    USER_SCORE++;
                    tUser.setText("Your Score: "+USER_SCORE);
                    Restart();
                }
            }
        }

        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        char character = (char) event.getUnicodeChar();

        if(character<97 ||  character>122)
        {
            Toast.makeText(GhostActivity.this,"Invalid letter. Enter from a-z. ",Toast.LENGTH_SHORT).show();
            return super.onKeyUp(keyCode, event);
        }
        else
        {
            tvGhostText.setText(tvGhostText.getText().toString()+character);
            tvStatus.setText(COMPUTER_TURN);
            computerTurn();
            return true;
        }
    }

    public void Challenge()
    {
        String text = tvGhostText.getText().toString();

        if(text.length()>3 && (simpleDictionary.isWord(text) || simpleDictionary.getAnyWordStartingWith(text)==null))
        {
            Toast.makeText(GhostActivity.this,"You Win !!",Toast.LENGTH_SHORT).show();
            tvStatus.setText("You Win !!");
            USER_SCORE++;
            tUser.setText("Your Score: "+USER_SCORE);
            Restart();
        }
        else
        {
            Toast.makeText(GhostActivity.this,"Computer Win !!",Toast.LENGTH_SHORT).show();
            tvStatus.setText("Computer Win !!");
            COMP_SCORE++;
            tComp.setText("Computer Score: "+COMP_SCORE);
            Restart();
        }
    }

    public void Restart()
    {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
    }
}
