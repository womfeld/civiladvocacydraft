package com.example.civiladvocacydraft;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PoliticianViewHolder extends RecyclerView.ViewHolder {

    TextView ptitle;
    TextView pname;

    public PoliticianViewHolder(@NonNull View itemView) {
        super(itemView);
        ptitle = itemView.findViewById(R.id.name);
        pname = itemView.findViewById(R.id.description);


    }
}
