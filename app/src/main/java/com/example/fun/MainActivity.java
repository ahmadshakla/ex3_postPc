package com.example.fun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /* data structures to store the todoItems*/
    private ArrayList<Todo> todos;
    private ArrayList<String> messages;
    private ArrayList<Boolean> states;

    /*Magic numbers*/
    private final static String SHARED_PREFERENCE = "shared preference";
    private final static String STRING_ARR = "StringArray";
    private final static String BOOL_ARR = "BooleanArray";
    private final static String ROTATION_STRING = "rotationString";
    private final static String ROTATION_BOOL = "rotationBoolean";
    private final static String EMPTY_STRING_ERR = "You must write something!";
    private final static String TAG = "TODOBOOM";

    /*variables*/
    private TextView insertion;
    private RecyclerView recyclerView;
    private Context context;
    private TodoAdapter todoAdapter;
    Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messages = new ArrayList<>();
        states = new ArrayList<>();
        todos = new ArrayList<>();
        SavedTodos savedTodos = ((SavedTodos) getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        recyclerView = findViewById(R.id.recycle_view);
        context = this;
        insertion = findViewById(R.id.editText2);
        final Button button = findViewById(R.id.button);
        todoAdapter = new TodoAdapter(context);
        if (savedInstanceState != null) {
            ArrayList<String> tasks = savedInstanceState.getStringArrayList(ROTATION_STRING);
            boolean[] clicked = savedInstanceState.getBooleanArray(ROTATION_BOOL);
            for (int i = 0; i < tasks.size(); i++) {
                addToRecyclerView(new Todo(tasks.get(i), clicked[i]));
            }
        } else {
            todos = savedTodos.getTodoArrayList();;
            Log.i(TAG,todos.size() + " is the current number of todos");
            messages = savedTodos.getMessages();
            states = savedTodos.getStates();
            update();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = insertion.getText().toString();
                if (message.length() != 0 && !message.matches(" *")) {
                    insertion.setText("");
                    addToRecyclerView(new Todo(message, false));
                    editor.apply();
                } else {
                    Toast.makeText(context, EMPTY_STRING_ERR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        ArrayList<String> tasks = new ArrayList<>();
        boolean[] clicked = new boolean[todos.size()];
        int j = 0;
        for (Todo todo : todos) {
            tasks.add(todo.getTodoText());
            clicked[j] = todos.get(j).isClicked();
            j++;
        }
        outState.putStringArrayList(ROTATION_STRING, tasks);
        outState.putBooleanArray(ROTATION_BOOL, clicked);
        super.onSaveInstanceState(outState);
    }

    /**
     * adds a new todoItem to the recyclerView and updates it
     * @param todo the new todo we want to add
     */
    private void addToRecyclerView(Todo todo) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        messages.add(todo.getTodoText());
        states.add(todo.isClicked());
        todos.add(todo);
        update();
        String todosStrings = gson.toJson(messages);
        String todosStates = gson.toJson(states);
        editor.putString(STRING_ARR, todosStrings);
        editor.putString(BOOL_ARR, todosStates);
        editor.apply();
    }

    /**
     * updates the recyclerView on the screen
     */
    public void update() {
        todoAdapter.setTodo(todos);
        recyclerView.setAdapter(todoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }


    /**
     * deletes a todoItem from the recyclerView and updates it
     *
     * @param index the index of the todoItem that we want to delete
     */
    public void deleteFromRecyclerView(int index) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        deleteTodo(index);
        String todosStrings = gson.toJson(messages);
        String todosStates = gson.toJson(states);
        editor.putString(STRING_ARR, todosStrings);
        editor.putString(BOOL_ARR, todosStates);
        update();
        editor.apply();
    }

    /**
     * deletes the todo item from all the relevant data structures
     *
     * @param index the index of the todoItem
     */
    public void deleteTodo(int index) {
        if (index >= 0 || index < todos.size()) {
            todos.remove(index);
            messages.remove(index);
            states.remove(index);
        }
    }


    /**
     * sets the states array
     * @param states the new arrayList containing the new states of the todos
     */
    public void setStates(ArrayList<Boolean> states) {
        this.states.clear();
        this.states.addAll(states);
    }
}
