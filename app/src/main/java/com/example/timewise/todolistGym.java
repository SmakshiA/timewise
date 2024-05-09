package com.example.timewise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
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



public class todolistGym extends AppCompatActivity {

    private ArrayList<ListItem> items;
    private CustomListAdapter itemsAdapter;
    private ListView listView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolistgym);
        listView = findViewById(R.id.listview);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        items = new ArrayList<>();
        itemsAdapter = new CustomListAdapter(this, items);
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Item removed", Toast.LENGTH_LONG).show();
                ListItem item = items.get(i);
                itemsAdapter.remove(item);
                return true;
            }
        });
    }

    private void addItem(View view) {
        EditText input = findViewById(R.id.editTextText);
        String itemText = input.getText().toString();

        if (!itemText.isEmpty()) {
            ListItem item = new ListItem(itemText, false);
            itemsAdapter.add(item);
            input.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Please enter text..", Toast.LENGTH_LONG).show();
        }
    }

    private class ListItem {
        String text;
        boolean checked;

        ListItem(String text, boolean checked) {
            this.text = text;
            this.checked = checked;
        }
    }



    private class CustomListAdapter extends ArrayAdapter<ListItem> {
        public CustomListAdapter(Context context, ArrayList<ListItem> items) {
            super(context, 0, items);
        }

        @SuppressLint("ViewHolder")

        // CustomListAdapter
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

            // Set the checkbox color to black when checked
            int checkboxColor = ContextCompat.getColor(getContext(), R.color.gray); // Replace with your color resource
            checkBox.setButtonTintList(ColorStateList.valueOf(checkboxColor));

            checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getItem(position).checked = isChecked;

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
                    // Handle item deletion
                    ListItem item = getItem(position);
                    remove(item);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

    }

}