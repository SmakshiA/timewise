package com.example.timewise;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class todolist extends AppCompatActivity {

    private ArrayList<ListItem> items;
    private CustomListAdapter itemsAdapter;
    private ListView listView;
    private Button button;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        listView = findViewById(R.id.listview);
        button = findViewById(R.id.button);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        // Load items from the database
        items = loadItemsFromDatabase();

        itemsAdapter = new CustomListAdapter(this, items);
        listView.setAdapter(itemsAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        setUpListViewListener();
    }

    private ArrayList<ListItem> loadItemsFromDatabase() {
        ArrayList<ListItem> itemList = new ArrayList<>();

        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                @SuppressLint("Range") String text = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEXT));
                @SuppressLint("Range") int checked = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CHECKED));

                ListItem item = new ListItem(text, checked == 1); // 1 for checked
                item.setId(id);

                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return itemList;
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Item removed", Toast.LENGTH_LONG).show();
                ListItem item = items.get(i);
                removeItemFromDatabase(item);
                itemsAdapter.remove(item);
                return true;
            }
        });
    }

    private void addItem(View view) {
        EditText input = findViewById(R.id.editTextText);
        String itemText = input.getText().toString();

        if (!itemText.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TEXT, itemText);
            values.put(DatabaseHelper.COLUMN_CHECKED, 0); // 0 for unchecked

            long itemId = db.insert(DatabaseHelper.TABLE_NAME, null, values);

            if (itemId != -1) {
                ListItem item = new ListItem(itemText, false);
                item.setId(itemId);
                itemsAdapter.add(item);
                input.setText("");
            } else {
                Toast.makeText(getApplicationContext(), "Failed to add item to the database.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enter text..", Toast.LENGTH_LONG).show();
        }
    }

    private void removeItemFromDatabase(ListItem item) {
        db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(item.getId())});
    }

    private class ListItem {
        long id;
        String text;
        boolean checked;

        ListItem(String text, boolean checked) {
            this.text = text;
            this.checked = checked;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }
    }

    private class CustomListAdapter extends ArrayAdapter<ListItem> {
        public CustomListAdapter(Context context, ArrayList<ListItem> items) {
            super(context, 0, items);
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ListItem item = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }

            TextView textView = convertView.findViewById(R.id.textView);
            final CheckBox checkBox = convertView.findViewById(R.id.checkBox);
            ImageView deleteIcon = convertView.findViewById(R.id.deleteIcon);

            textView.setText(item.text);
            checkBox.setChecked(item.checked);

            // Check if the item is checked and set the appropriate text decoration
            if (item.checked) {
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getItem(position).checked = isChecked;
                    updateItemInDatabase(getItem(position));
                    // Set or remove the strike-through text decoration
                    if (isChecked) {
                        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    }

                    notifyDataSetChanged();
                }
            });

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListItem item = getItem(position);
                    removeItemFromDatabase(item);
                    remove(item);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    private void updateItemInDatabase(ListItem item)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CHECKED, item.checked ? 1 : 0); // 1 for checked, 0 for unchecked
        db.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(item.getId())});


    }


}