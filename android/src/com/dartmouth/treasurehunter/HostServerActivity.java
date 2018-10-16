package com.dartmouth.treasurehunter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HostServerActivity extends Activity {

    //really bad i know
    public boolean isInteger( String input )
    {
        try
        {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e)
        {
            return false;
        }
    }

    private boolean checkText(String den, String freq){

        if(den == null || freq == null)return false;
        if(isInteger(den)&&isInteger(freq))return true;

        return false;
    }


    private void saveText(String den, String freq){
        SharedPreferences savedata = HostServerActivity.this.getSharedPreferences("AppPrefs",HostServerActivity.this.MODE_PRIVATE);
        SharedPreferences.Editor editor = savedata.edit();

        //editor.putString(IMG_DATA,data.imgData);
        editor.putInt("den",Integer.parseInt(den));
        editor.putInt("freq",Integer.parseInt(freq));



        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_server);



        Button mStartGame=findViewById(R.id.start_game);
        final EditText mDensity = findViewById(R.id.endens);
        final EditText mFrequency = findViewById(R.id.enfreq);


        mStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkText(mDensity.getText().toString(),mFrequency.getText().toString())){

                    saveText(mDensity.getText().toString(),mFrequency.getText().toString());

                    startActivity(FindPlayersActivity.openFindPlayersIntent(HostServerActivity.this));

                }
            }
        });



    }
    public static Intent startServerIntent(Activity activity){
        return new Intent(activity,HostServerActivity.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


}
