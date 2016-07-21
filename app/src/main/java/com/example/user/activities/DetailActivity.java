package com.example.user.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DetailActivity extends BaseActivity {

    Form form;
    //DBHelper helper;
    String jsonObject = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //helper = new DBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TableLayout tl = (TableLayout) findViewById(R.id.myTableLayout);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /**FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
         fab.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show();
        }
        });
         **/

        //LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout2);

        //get the intent to get how the linearlayout is to be populated
        Intent i = this.getIntent();
        form = (Form) i.getSerializableExtra("form");

        //get the form array from the form class and populate linearlayout


        for (int a = 0; a < form.fields.size(); a++) {
            if (form.fields.get(a)[1].equals("edittext")) { //get the type
                TableRow row = new TableRow(getApplicationContext());
                TextView tv = new TextView(this);
                tv.setText(form.fields.get(a)[0]);
                tv.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                EditText et = new EditText(this);
                et.setText("");
                et.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                row.addView(tv);
                row.addView(et);
                tl.addView(row); //add the view
                //tl.addView(row,i,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
            if (form.fields.get(a)[1].equals("spinner")){
                TableRow row = new TableRow(getApplicationContext());
                TextView tv = new TextView(this);
                tv.setText(form.fields.get(a)[0]);
                tv.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                Spinner spinner = new Spinner(getApplicationContext());
                String[] list = form.fields.get(a)[2].split(";;");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, list);



                dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                spinner.setAdapter(dataAdapter);
                spinner.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                row.addView(tv);
                row.addView(spinner);
                tl.addView(row);

            }
            //ll.addView(et);
        }

        Button myButton = new Button(this);
        myButton.setText("Save");
        myButton.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        myButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailActivity.this.saveAnswers();
                    }
                }
        );
        //LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tl.addView(myButton);

        //add text view

    }

    public void saveAnswers(){
        TableLayout root = (TableLayout) findViewById(R.id.myTableLayout);
        loopQuestions(root);
        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
        intent.putExtra("json", jsonObject);
        startActivity(intent);

    }

    private void loopQuestions(ViewGroup parent){
        //So, the id will start from 1
        //get it?????
        int entry_id = helper.getMax(form.name) + 1;
        String form_name = form.name;
        String name = "Pujan";
        //helper = new DBHelper(this.getApplicationContext());

        for(int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i) instanceof TableRow){
                TableRow child = (TableRow)parent.getChildAt(i);
                Log.d("Pujan", "ahdfakdfj" + i);
                if (child.getChildAt(1) instanceof EditText) {
                    //Support for EditText

                    EditText et = (EditText) child.getChildAt(1);
                    Log.d("ANDROID DYNAMIC VIEWS:", "EdiText: " + et.getText());
                    String field_value = et.getText().toString();

                    TextView my_field_name = (TextView) child.getChildAt(0);
                    String field_name = my_field_name.getText().toString();
                    Log.d("kajdfka", "inside row");
                    helper.insertForm(name, entry_id, form_name, field_name, field_value);

                }

            }
        }
        //jsonObject = helper.getResults().toString();
        Global.jsonArray = helper.getResults();
        Log.d("database", helper.getResults().toString());
        Log.d("column_count", ""+helper.getMax(form.name));
        helper.close();
    }




}


