package com.example.androidexample;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.ViewHolder> {

    private final List<String> messageList;
    private final Context context;
    private final boolean isGroup;

    public messageAdapter(Context context, List<String> messageList, boolean isGroup) {
        this.context = context;
        this.messageList = messageList;
        this.isGroup = isGroup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = messageList.get(position);
        holder.textView.setText(name);

        // Click listener to open individual or group chat
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, chatActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("isGroup", isGroup);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}

