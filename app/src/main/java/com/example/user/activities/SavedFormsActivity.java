package com.example.user.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SavedFormsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_forms);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /**FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         **/

        TableLayout tl = (TableLayout) findViewById(R.id.myTableLayoutForm);

        Intent intent = this.getIntent();
        Form form = (Form) intent.getSerializableExtra("form");

        int id=0;

        //Now, use the form to generate instances of the saved forms.
        //Hint:: Use max to get the maximum number of instances and display them.
        try{
            id = helper.getMax(form.name);
        }catch(Exception e){
            //make toast to show that no data is available.
        }
        //keep all this in a try catch block in case id returns null
        //we will use 0 as a placeholder, if no data, then id == 0.
        for (int i=0; i < id ; i++ ){
            TableRow row = new TableRow(getApplicationContext());
            TextView tv = new TextView(this);
            tv.setText("Field"+i);
            tv.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            //Now, two buttons for delete and edit
            Button button = new Button(this);
            button.setText("Edit");
            button.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            Button button2 = new Button(this);
            button2.setText("Delete");
            button2.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            row.addView(tv);
            row.addView(button);
            tl.addView(button2);

        }
    }




}
