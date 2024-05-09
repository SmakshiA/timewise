package com.example.timewise;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;



public class medicineadaptor extends RecyclerView.Adapter<medicineVH> {

     List<String> items;
    private SQLiteDatabase db;
    public medicineadaptor(List<String> items, SQLiteDatabase db) {
        this.items = items;
        this.db=db;
    }

    @NonNull
    @Override
    public medicineVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new medicineVH(view, this,db);
    }

    @Override
    public void onBindViewHolder(@NonNull medicineVH holder, int position) {
        holder.tv.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class medicineVH extends RecyclerView.ViewHolder {
    TextView tv;
    private medicineadaptor adapter;
    private SQLiteDatabase db;

    public medicineVH(@NonNull View itemView, medicineadaptor adapter, SQLiteDatabase db) {
        super(itemView);
        tv = itemView.findViewById(R.id.text1);
        this.adapter = adapter;
        this.db = db;

        itemView.findViewById(R.id.cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                Log.e("Database","yes "+ position);
                if (position != RecyclerView.NO_POSITION) {
                    String medicineWithTime = adapter.items.get(position);
                    String[] parts = medicineWithTime.split(" "); // Split the string by space
                    String removedMedicine = parts[0];

                    // Remove the item from the database
                    removeItemFromDatabase(removedMedicine);

                    // Remove the item from the adapter's data source
                    adapter.items.remove(position);

                    // Notify the adapter that an item has been removed
                    adapter.notifyItemRemoved(position);
                }
            }
        });
    }

    private void removeItemFromDatabase(String canceledItem) {

        String selection = DatabaseHelperMed.COLUMN_MEDICINE_NAME + " = ?";
        String[] selectionArgs = { canceledItem };

        try {
            int deletedRows = db.delete(DatabaseHelperMed.TABLE_ACTIVE_MEDICINE, selection, selectionArgs);

            if (deletedRows > 0) {
                // Item removed from the database
                // You can handle this event if needed, e.g., showing a message
                Log.d("Database", "Deleted " + deletedRows + " rows.");
            } else {
                Log.e("Database", "No rows deleted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Database", "Error deleting item: " + e.getMessage());
        }
    }


}

