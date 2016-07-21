package com.example.user.activities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 7/19/16.
 */
@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Form implements Serializable {
    public ArrayList<String[]> fields;
    public String name;
    public String description;

    public Form(){
        fields = new ArrayList<String[]>();
        name = "";
        description = "";
    }

}
