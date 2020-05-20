package com.example.fun;

import android.widget.TextView;

public class Todo {
    private String todoText;
    private boolean clicked;


    public TextView textView;

    public String getTodoText() {
        return todoText;
    }

    public boolean isClicked() {
        return clicked;
    }

    public Todo(String todoText,boolean clicked) {
        this.todoText = todoText;
        textView=null;
        this.clicked = clicked;
    }
    public void setClicked(){
        clicked = true;
    }

}
