package com.sohamfit.sohamfitapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class Filters extends AppCompatActivity {

    // Views
    private Spinner mVideoTypeSpinner;
    private Button mButtonFilter;
    private Spinner mVideoLevelSpinner;

    // Filters
    private boolean mFilters = false;
    private boolean mFilterByVideoType = false;
    private String mVideoType;
    private boolean mFilterByVideoLevel = false;
    private String mVideoLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        //Receiving Intents
        if (getIntent().getExtras() != null){
            mFilters = getIntent().getExtras().getBoolean("filters");
            mFilterByVideoType = getIntent().getExtras().getBoolean("filterByVideoType");
            mVideoType = getIntent().getExtras().getString("videoType");
            mFilterByVideoLevel = getIntent().getExtras().getBoolean("filterByVideoLevel");
            mVideoLevel = getIntent().getExtras().getString("videoLevel");
        }

        // Views
        mVideoTypeSpinner = (Spinner) findViewById(R.id.spinner_type);
        mVideoLevelSpinner = (Spinner) findViewById(R.id.spinner_level);
        mButtonFilter = (Button) findViewById(R.id.button_filter);

        mButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Filters.this, VideosList.class);

                if (mVideoTypeSpinner.getSelectedItemId() != 0){
                    intent.putExtra("filterByVideoType", true);
                    intent.putExtra("videoType", String.valueOf(mVideoTypeSpinner.getSelectedItem()));
                }

                if (mVideoLevelSpinner.getSelectedItemId() != 0){
                    intent.putExtra("filterByVideoLevel", true);
                    intent.putExtra("videoLevel", String.valueOf(mVideoLevelSpinner.getSelectedItem()));

                }

                if(mVideoTypeSpinner.getSelectedItemId() != 0 || mVideoLevelSpinner.getSelectedItemId() != 0){
                    intent.putExtra("filters", true);
                }else {
                    intent.putExtra("filters", false);
                }


                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        });


        // Set spinners options
        if (mFilters){
            if (mFilterByVideoType){
                if(mVideoType.equals("Clases")) {
                    mVideoTypeSpinner.setSelection(1);
                } else if (mVideoType.equals("Secuencias cortas")){
                    mVideoTypeSpinner.setSelection(2);
                } else if (mVideoType.equals("Posturas")){
                    mVideoTypeSpinner.setSelection(3);
                } else if (mVideoType.equals("Correcciones")){
                    mVideoTypeSpinner.setSelection(4);
                }
            }

            if (mFilterByVideoLevel){
                if(mVideoLevel.equals("Básico")) {
                    mVideoLevelSpinner.setSelection(1);
                } else if (mVideoLevel.equals("Intermedio")){
                    mVideoLevelSpinner.setSelection(2);
                }
            }
        }

    }

}
