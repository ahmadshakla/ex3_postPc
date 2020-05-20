package com.example.fun;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SavedTodos extends Application {
    private final static String SHARED_PREFERENCE = "shared preference";
    private final static String BOOL_ARR = "BooleanArray";
    private final static String TODO_BOOL = "todobool";
    private final static String STRING_ARR = "StringArray";


    private ArrayList<Todo> todoArrayList = new ArrayList<>();
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<Boolean> states = new ArrayList<>();
    SharedPreferences sharedPreferences;
    Gson gson = new Gson();

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        initArrays();
    }


    /**
     * initializes the ArrayLists that hold the todos
     */
    private void initArrays(){
        String todosStrings = sharedPreferences.getString(STRING_ARR,"[]");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        messages = gson.fromJson(todosStrings, type);
        String todosStates = sharedPreferences.getString(BOOL_ARR,"[]");
        Type type2 = new TypeToken<ArrayList<Boolean>>(){}.getType();
        states =  gson.fromJson(todosStates, type2);
        for (int i=0;i<states.size();i++){
            Todo todo = new Todo(messages.get(i), states.get(i));
            todoArrayList.add(todo);
        }

    }

    public ArrayList<Todo> getTodoArrayList() {
        return todoArrayList;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public ArrayList<Boolean> getStates() {
        return states;
    }


}
