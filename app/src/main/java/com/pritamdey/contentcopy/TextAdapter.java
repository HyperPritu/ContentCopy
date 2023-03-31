package com.pritamdey.contentcopy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pritamdey.contentcopy.databinding.LayoutCardCategoryBinding;

import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextViewHolder> {

    private Context context;
    private List<Text> messageList;

    public TextAdapter(Context context, List<Text> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCardCategoryBinding binding = LayoutCardCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new TextViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        holder.binding.text.setText(messageList.get(position).getText());
        holder.binding.appName.setText(messageList.get(position).getAppName());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {

        LayoutCardCategoryBinding binding;

        public TextViewHolder(@NonNull LayoutCardCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
