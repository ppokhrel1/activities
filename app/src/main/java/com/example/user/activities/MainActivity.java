package com.example.user.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final Intent intent = new Intent(MainActivity.this, ListActivity.class);

        Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredients;

                //ingredients = editText.getText().toString().trim();
                //Log.d("pokhrel", ingredients);
                //Log.d("pujan", editText.getText().toString().trim());


                String myRecipe;

                //myRecipe = recipe.getText().toString().trim();
                myRecipe = "pasta";
                ingredients = "garlic";
                if(ingredients != null) {
                    intent.putExtra("ingredients", ingredients);
                    intent.putExtra("recipe", myRecipe);
                    Log.d("ksjdkfja", ingredients);
                    Log.d("kajdkfksj", myRecipe);
                    //Global.ingredients = ingredients;
                    //Global.recipe = myRecipe;
                    startActivity(intent);
                }
            }
        });


        //Log.d("jsonString", jsonString);
        Button sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = "http://192.168.1.10:81/ddc/uploads.php";
                String data = "Data sent";
                MyTask task = new MyTask();
                task.execute(URL);
            }
        });

    }

    private class MyTask extends AsyncTask<String , Void, String > {

        private Exception exception;


        protected String doInBackground(String... urls) {
            // your code here
            try {
                //Bundle bundle = intent.getExtras();
                //final String jsonString = intent.getStringExtra("json");
                JSONArray myArray = Global.jsonArray;
                String URL = urls[0];
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("array", myArray);
                Log.d("Global value", jsonObject.toString());
                //Log.d("adfasf", jsonString);
                Log.d("padsfo", jsonObject.toString());
                //JSONObject jsonObjRecv =
                Log.d("oisjdofa", HttpClient.SendHttpPost(URL, jsonObject).toString());

                String data = "Success";
                ////now, send the json object received to toast and show it
                Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();

            }catch(JSONException e){
                //do nothing
            }catch (Exception e){
                //do nothing dear
            }
            return "";

        }

        protected void onPostExecute(Void feed) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
