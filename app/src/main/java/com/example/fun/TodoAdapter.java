package com.example.fun;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private Context context;
    private SharedPreferences sharedPreferences;
    private final static String SHARED_PREFERENCE = "shared preference";
    private final static String BOOL_ARR = "BooleanArray";
    private final static String TITLE = "Information";
    private final static String MESSAGE = "Are you sure that you want to delete this TODO?";
    private ArrayList<Todo> todoItems = new ArrayList<>();
    private ArrayList<Boolean> todoStates = new ArrayList<>();
    public TodoAdapter(Context context) {
        this.todoItems = new ArrayList<>();
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);


    }

    /**
     * sets the contents of the todoArray located in this class
     * @param old the old todoArray
     */
    public void setTodo(ArrayList<Todo> old) {
        todoItems.clear();
        todoStates.clear();
        for (Todo todo : old) {
            todoItems.add(todo);
            todoStates.add(todo.isClicked());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_layout,
                parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Todo todoItem = todoItems.get(position);
        final int taskNum = position + 1;
        if (todoItem.textView == null) {
            todoItem.textView = new TextView(context);
            todoItem.textView.setText(holder.textView.getText());
            todoItem.textView.setAlpha(holder.textView.getAlpha());
        }
        holder.textView.setText(taskNum + "- " + todoItem.getTodoText());
        holder.textView.setAlpha(todoItem.textView.getAlpha());
        if (todoItem.isClicked()) {
            holder.textView.setAlpha(0.4f);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.apply();
        } else {
            holder.textView.setAlpha(1f);
        }
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!todoItem.isClicked()) {
                    todoItem.setClicked();
                    Toast.makeText(context, "TODO " + todoItem.getTodoText() + " is now DONE. BOOM!",
                            Toast.LENGTH_SHORT).show();
                    normalClickHandler(holder, position, todoItem);
                }
            }
        });
        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                longClickHandler(position);
                return false;
            }
        });

    }

    /**
     * handled what to do when the textview is clicked
     * @param holder the view holder
     * @param position the position of the textView
     * @param todoItem the todoItem in that position
     */
    private void normalClickHandler(final ViewHolder holder, final int position, final Todo todoItem) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        todoStates.set(position, true);
        ((MainActivity) context).setStates(todoStates);
        String json = gson.toJson(todoStates);
        editor.putString(BOOL_ARR, json);
        editor.apply();
        holder.textView.setAlpha(0.4f);
        todoItem.textView.setAlpha(holder.textView.getAlpha());
    }

    /**
     * handled what to do when we we long click on a textView
     * @param position its position in the array
     */
    private void longClickHandler(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(TITLE)
                .setMessage(MESSAGE).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((MainActivity) context).deleteFromRecyclerView(position);
                notifyItemRemoved(position);

            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
