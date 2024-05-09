package com.example.timewise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.YourViewHolder> {
    private List<ListItem> itemList;

    public MyAdapter(List<ListItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public YourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new YourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourViewHolder holder, int position) {
        ListItem item = itemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class YourViewHolder extends RecyclerView.ViewHolder {
        private TextView category;
        private TextView exp;
        private TextView date;

        public YourViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.categoryEditText);
            exp = itemView.findViewById(R.id.exp);
            date = itemView.findViewById(R.id.dateTextView);
        }

        public void bind(ListItem item) {
            String currentDate = getCurrentDate();
            date.setText(currentDate);
            category.setText(item.getCategory());
            exp.setText("Rs. "+item.getExpense());
        }
    }
    private String getCurrentDate() {
        // Create a date formatter
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Get the current date
        Date date = new Date(System.currentTimeMillis());

        // Format the date and return as a string
        return sdf.format(date);
    }
}

