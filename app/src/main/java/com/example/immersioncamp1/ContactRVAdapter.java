// Used for setting data to each item

package com.example.immersioncamp1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

public class ContactRVAdapter extends RecyclerView.Adapter<ContactRVAdapter.ViewHolder> {

    // creating variables for context and array list.
    private Context context;
    private ArrayList<ContactsModal> contactsModalArrayList;

    // creating a constructor
    public ContactRVAdapter(Context context, ArrayList<ContactsModal> contactsModalArrayList) {
        this.context=context;
        this.contactsModalArrayList=contactsModalArrayList;
    }

    @NonNull
    @Override
    public ContactRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ContactRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_contacts_rv_item, parent, false));
    }

    // below method is use for filtering data in our array list
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<ContactsModal> filterlist) {
        // on below line we are passing filtered
        // array list in our original array list
        contactsModalArrayList = filterlist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRVAdapter.ViewHolder holder, int position) {
        // getting data from array list in our modal.
        ContactsModal modal = contactsModalArrayList.get(position);
        // on below line we are setting data to our text view.
        holder.contactTV.setText(modal.getUserName());
        ColorGenerator generator = ColorGenerator.MATERIAL;
        // generate random color
        int color = generator.getRandomColor();

        // below text drawable is a circular.
        TextDrawable drawable2 = TextDrawable.builder().beginConfig()
                .width(100)
                .height(100)
                .endConfig()
                .buildRound(modal.getUserName().substring(0, 1), color);
        // setting image to our image view on below line.
        holder.contactIV.setImageDrawable(drawable2);
        // on below line we are adding on click listener to our item of recycler view.
        holder.itemView.setOnClickListener(v -> {
            // on below line we are opening a new activity and passing data to it.
            Intent i = new Intent(context, ContactDetailActivity.class);
            i.putExtra("name", modal.getUserName());
            i.putExtra("contact", modal.getContactNumber());
            // on below line we are starting a new activity.
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return contactsModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // on below line creating a variable
        // for our image view and text view.
        private ImageView contactIV;
        private TextView contactTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our image view and text view.
            contactIV = itemView.findViewById(R.id.idIVContact);
            contactTV = itemView.findViewById(R.id.idTVContactName);
        }
    }
}
