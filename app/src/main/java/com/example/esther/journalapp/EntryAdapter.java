package com.example.esther.journalapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.esther.journalapp.data.Entry;

import java.util.ArrayList;

public class EntryAdapter extends ArrayAdapter<Entry> {
    /**
     *
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param entries A List of Earthquake objects to display in a list
     */
    public EntryAdapter(Activity context, ArrayList<Entry> entries) {
        super(context, 0, entries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.entry_list, parent, false);
        }

        Entry currentEntry = getItem(position);

        // Find the TextView in the entry_list.xml layout with the ID title_text_view
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        // Get the title from the current entry object and
        // set this text on the title TextView
        titleTextView.setText(currentEntry.getTitle());

        // Find the TextView in the entry_list.xm layout with the ID content_textview
        TextView contentTextView = (TextView) listItemView.findViewById(R.id.content_text_view);
        // Get the content from the current Entry object and
        // set this text on the location TextView
        contentTextView.setText(currentEntry.getContent());

        // Find the TextView in the entry_list.xm layout with the ID date_textview
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        // Get the date from the current Entry object and
        // set this text on the date TextView
        dateTextView.setText(currentEntry.getDate());

        // Return the whole list item layout (containing 3 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
