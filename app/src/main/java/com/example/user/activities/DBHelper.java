package com.example.user.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 7/19/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String FORMS_TABLE_NAME = "forms";
    public static final String FORMS_COLUMN_ID = "id";
    public static final String FORMS_COLUMN_NAME = "name";
    public static final String FORMS_COLUMN_ENTRY_IDL = "entry_id";
    public static final String FORMS_COLUMN_FORM_ID = "form_name";
    public static final String FORMS_COLUMN_FIELD_NAME = "field_name";
    public static final String FORMS_COLUMN_FIELD_VALUE = "field_value";
    private HashMap hp;
    private Context context;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table forms " +
                        "(id integer primary key, name text,entry_id integer,form_name text, field_name text,field_value text)"
        );  //primary key will auto_increment
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS forms");
        onCreate(db);
    }

    public boolean insertForm  (String name, Integer entry_id, String form_name, String field_name, String field_value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("entry_id", entry_id);
        contentValues.put("form_name", form_name);
        contentValues.put("field_name", field_name);
        contentValues.put("field_value", field_value);
        db.insert("forms", null, contentValues);
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from forms where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, FORMS_TABLE_NAME);
        return numRows;
    }

    public boolean updateForm (Integer id, String name, Integer entry_id, String form_name, String field_name, String field_value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("entry_id", entry_id);
        contentValues.put("form_name", form_name);
        contentValues.put("field_name", field_name);
        contentValues.put("field_value", field_value);
        db.update("forms", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteForm (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("forms",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllForms()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from forms", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(res.getString(res.getColumnIndex(FORMS_COLUMN_NAME)));

            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getFormInstance(String form_name, int id){
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from forms where entry_id=" + id+ " and form_name='"+form_name + "'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(res.getString(res.getColumnIndex(FORMS_COLUMN_NAME)));

            res.moveToNext();
        }


        return array_list;

    }

    public int getMax(String form_name){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select max(entry_id) from forms where form_name='"+form_name+"'", null );
        res.moveToFirst();
        /**if(cursor.getCount() > 0){

            minId= cursor.getInt(0);

        } **/

        return res.getInt(0);
    }
    //gets all data from the table as cursor

    public Cursor getAllData() {
        String selectQuery = "Select * from "+FORMS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;


    }
    public JSONArray getResults(){
        Cursor cursor = this.getAllData();
        JSONArray resultSet 	= new JSONArray();
        JSONObject returnObj 	= new JSONObject();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {

                    try
                    {

                        if( cursor.getString(i) != null )
                        {
                            Log.d("TAG_NAME", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d("TAG_NAME", e.getMessage()  );
                    }
                }

            }

            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        Log.d("TAG_NAME", resultSet.toString() );
        return resultSet;


    }

    /**
    //http://stackoverflow.com/questions/25722585/convert-sqlite-to-json
    public JSONArray getResults(){

        String DB_NAME = DATABASE_NAME;
        String DB_PATH = context.getDatabasePath(DB_NAME).getPath();
        String myPath = DB_PATH + DB_NAME;// Set path to your database

        String myTable = FORMS_TABLE_NAME;//Set name of your table

        //or you can use `context.getDatabasePath("my_db_test.db")`

        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        String searchQuery = "SELECT  * FROM " + myTable;
        Cursor cursor = myDataBase.rawQuery(searchQuery, null );

        JSONArray resultSet     = new JSONArray();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                            Log.d("TAG_NAME", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d("TAG_NAME", e.getMessage()  );
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("TAG_NAME", resultSet.toString());
        return resultSet;



    }
     **/

    /**
    public String getAllData(){
        Cursor cursor = getAllData();  //cursor hold all your data
        JSONObject jobj = new JSONObject();
        JSONArray arr = new JSONArray();
        cursor.moveToFirst();
        try{
            while(cursor.moveToNext()) {




                jobj.put("entry_id", cursor.getInt(0);
                jobj.put("name", cursor.getString(1));
                jobj.put("form_id", cursor.getInt(2));
                jobj.put("field_name", cursor.getString(3));
                jobj.put("field_value", cursor.getString(4));
                arr.put(jobj);

            }


            jobj = new JSONObject();
            jobj.put("data", arr);
        }catch(JSONException e){
        //
    }

        String st = jobj.toString();
        return st;

    }
     **/



}