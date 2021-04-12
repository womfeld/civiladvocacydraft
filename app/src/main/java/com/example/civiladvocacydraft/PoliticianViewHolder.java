package com.example.civiladvocacydraft;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PoliticianViewHolder extends RecyclerView.ViewHolder {

    TextView pname;
    TextView ptitle;
    public PoliticianViewHolder(@NonNull View itemView) {
        super(itemView);
        pname = itemView.findViewById(R.id.name);
        ptitle = itemView.findViewById(R.id.description);


    }
}
