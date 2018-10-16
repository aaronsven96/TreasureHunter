package com.dartmouth.treasurehunter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    Activity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtonClicks();
    }
    private void setButtonClicks(){
        final Button mHost=findViewById(R.id.button_host);
        Button mJoin=findViewById(R.id.button_join);
        mHost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(HostServerActivity.startServerIntent(activity));
            }
        });
        mJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FindServerActivity.openFindIntent(activity));
            }
        });





    }



    public static Intent openMainIntent(Activity activity){
        return new Intent(activity,MainActivity.class);
    }
}
