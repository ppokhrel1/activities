package com.example.user.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    List<Form> forms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
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
        final RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        forms = new ArrayList<Form>();
        try {
            FormFeed myfeed = new FormFeed();


            //myfeed.getResults();
            forms = myfeed.execute().get();
        }catch(Exception e){
            Log.d("Exception", "Error caught");
            //Log.d("poaso", MyJSON.getData(ListActivity.this));


        }
        Log.d("asdfhjlakdf", MyJSON.getData(ListActivity.this));

        if (forms.size() == 0){
            forms = getDataFromLocal();;
        }

        MyAdapter adapter = new MyAdapter(forms);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        final Intent intent = new Intent(ListActivity.this, DetailActivity.class);
        //onClickListener
        rv.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final View child = rv.findChildViewUnder(event.getX(), event.getY());
                if (child != null) {
                    child.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Log.d("  ", " " + forms.get(rv.getChildLayoutPosition(v)).name);
                            intent.putExtra("form", forms.get(rv.getChildLayoutPosition(v)));
                            startActivity(intent);
                        }
                    });
                }
                return false;
            }


        });


    }

    protected ArrayList<Form> getDataFromLocal(){
        String data = MyJSON.getData(ListActivity.this);
        //now turn the data to json
        //BufferedReader bufferedReader =
        //        new BufferedReader(data);
        ArrayList<Form> returnForms = new ArrayList<Form>();
        String[] lines = data.split(System.getProperty("line.separator"));
        Log.d("iuuasdf", lines[0]);
        for (String mydata : lines) {
            try {


                JSONObject jas = new JSONObject(mydata);
                String form_name = jas.getString("name");
                String description = jas.getString("description");
                Form recipe = new Form();
                recipe.name = form_name;
                recipe.description = description;


                JSONArray ja = jas.getJSONArray("fields");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);
                    //Log.d("adf", jo.toString());
                    //Log.d("adfas", "adfa");

                    String label = jo.getString("label");
                    //String description = jo.getString("description");
                    String type = jo.getString("type");
                    String key_value = jo.getString("key_value");
                    //String ingredients = jo.getString("ingredients");
                    recipe.fields.add(new String[]{label, type, key_value});

                }
                returnForms.add(recipe);

            } catch (JSONException e) {
                //do nothing
            }
        }
        return returnForms;


    }
    class FormFeed extends AsyncTask<String, Void, List<Form>> {

        protected ArrayList<Form> doInBackground(String... urls) {
            //Log.d("ppdasf", "auiydfui");
            return this.populate();
        }

        protected void onPostExecute(List<Form> recipes){
            super.onPostExecute(recipes);
        }

        //returns a list of recipes after querying the site.
        //parameters are obtained after querying the url.
        //try to find the forms feed from the url
        //then save the parameters into a file
        // and/else read from the file
       /** private ArrayList<Form> populate(){
            ArrayList<Form> items = new ArrayList<Form>();
            try {
                JSONObject jsonObject = new JSONObject(MyJSON.getData(ListActivity.this));
                JSONArray ja = jsonObject.getJSONArray("results");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);

                    Form form = new Form();
                    String name = jo.getString("name");
                    //String description = jo.getString("description");
                    String myUrl = jo.getString("href");
                    String description = jo.getString("description");

                    form.name = name;
                    //recipe.link = myUrl;
                    //recipe.ingredients = ingredients;
                    form.description = description;
                    items.add(form);
                }
            }catch(JSONException e){
                //do nothing
            }

            return items;
        }
        **/

       //returns a list of recipes after querying the site.
       //parameters are obtained after querying the url.
       private ArrayList<Form> populate(){
           ArrayList<Form> items = new ArrayList<Form>();
           int pageNumber = 1;
           Intent intent = getIntent();
           Bundle bundle = intent.getExtras();
           String ingredientsAPI = intent.getStringExtra("ingredients");
           ingredientsAPI.replaceAll(" ", "+");
           String myRecipe = intent.getStringExtra("recipe");
           String allStrings = "";
           //Log.d("tystdf", ingredientsAPI + myRecipe);
           //String myurl = "http://www.recipepuppy.com/api/?i="+ ingredientsAPI + "&q="+ myRecipe + "&p=" + pageNumber;
           String myurl = "http://192.168.1.10:81/ddc/json.php";
           try {
               URL url = new URL
                       (myurl);
               //Log.d("kjhdfkjas", myurl);
               HttpURLConnection urlConnection =
                       (HttpURLConnection) url.openConnection();
               urlConnection.setRequestMethod("GET");
               urlConnection.connect();
               // gets the server json data
               BufferedReader bufferedReader =
                       new BufferedReader(new InputStreamReader(
                               urlConnection.getInputStream()));
               String next;

                //allStrings = bufferedReader.toString();
               while ((next = bufferedReader.readLine()) != null) {
                   //JSONArray ja = new JSONArray(next);
                   allStrings += next + "\n";
                   JSONObject jas = new JSONObject(next);
                   //JSONArray ja = new JSONArray("results");
                   //Log.d("uweyuri", jas.toString());
                   String form_name = jas.getString("name");
                   String description = jas.getString("description");
                   Form recipe = new Form();
                   recipe.name = form_name;
                   recipe.description = description;


                   JSONArray ja = jas.getJSONArray("fields");
                   //Log.d("adfas", ja.toString());
                   for (int i = 0; i < ja.length(); i++) {
                       JSONObject jo = (JSONObject) ja.get(i);
                       //Log.d("adf", jo.toString());
                       //Log.d("adfas", "adfa");
                       //items.add(jo.getString("text"));

                       String label = jo.getString("label");
                       //String description = jo.getString("description");
                       String type = jo.getString("type");

                       String key_value = jo.getString("key_value");
                       //String ingredients = jo.getString("ingredients");
                       recipe.fields.add(new String[]{label, type, key_value});
                       //recipe.name = form_name;
                       //recipe.description = description;
                       //recipe.ingredients = ingredients;
                       //items.add(recipe);
                       //get all the jsonobjects, create different recipes from them and add to items array.

                   }
                   items.add(recipe);
               }
           } catch (MalformedURLException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           } catch (JSONException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }

           if(allStrings != null && !allStrings.isEmpty()) {
               MyJSON.saveData(ListActivity.this, allStrings);
               Log.d("makakaf", MyJSON.getData(ListActivity.this));
           }
           else{
               allStrings = MyJSON.getData(ListActivity.this);
               //Now, use the buffered reader
               //Log.d("adaf", MyJSON.getData(ListActivity.this));
               //Log.d("aodpfas", "fuck you piece of shit app");

           }
           //Log.d("padpfadaf", MyJSON.getData(ListActivity.this));
           return items;
       }


        //when we get the results, we will save it to a file
        private String getResults(){
            ArrayList<Form> items = new ArrayList<Form>();
            int pageNumber = 1;
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            String ingredientsAPI = intent.getStringExtra("ingredients");
            ingredientsAPI.replaceAll(" ", "+");
            String myRecipe = intent.getStringExtra("recipe");
            String allLines = "";
            //Log.d("tystdf", ingredientsAPI + myRecipe);
            //String myurl = "http://www.recipepuppy.com/api/?i="+ ingredientsAPI + "&q="+ myRecipe + "&p=" + pageNumber;
            String myurl = "192.168.1.10:81/ddc/json.php";
            try {
                URL url = new URL
                        (myurl);

                HttpURLConnection urlConnection =
                        (HttpURLConnection) url.openConnection();
                Log.d("kjhdfkjas", myurl);
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000); //milliseconds
                urlConnection.setConnectTimeout(15000); //millis
                Log.d("kjhdfkjas", "fuck you");
                urlConnection.connect();
                Log.d("kjhdfkjas", myurl);
                // gets the server json data
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream()));
                String next;


                //Log.d("adfa", "Pujan");
                while ((next = bufferedReader.readLine()) != null) {
                    //JSONArray ja = new JSONArray(next);
                    allLines += next;
                    JSONObject jas = new JSONObject(next);
                    //JSONArray ja = new JSONArray("results");
                    Log.d("uweyuri", next);

                    JSONArray ja = jas.getJSONArray("results");
                    //Log.d("adfas", ja.toString());

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                        //Log.d("adf", jo.toString());
                        //Log.d("adfas", "adfa");
                        //items.add(jo.getString("text"));
                        Form form = new Form();
                        //change later
                        String name = jo.getString("title");
                        //String description = jo.getString("description");
                        String myUrl = jo.getString("href");
                        String description = jo.getString("description");

                        form.name = name;
                        //recipe.link = myUrl;
                        //recipe.ingredients = ingredients;
                        form.description = description;
                        items.add(form);
                        //get all the jsonobjects, create different recipes from them and add to items array.

                    }
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //save the data to JSON file and then return the string
            MyJSON.saveData(ListActivity.this, allLines);
            return allLines;
        }
    }
}