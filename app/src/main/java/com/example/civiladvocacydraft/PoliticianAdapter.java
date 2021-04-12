package com.example.civiladvocacydraft;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PoliticianAdapter extends RecyclerView.Adapter<PoliticianViewHolder> {

    private ArrayList<Politician> politicians;
    private final MainActivity mainAct;



    public PoliticianAdapter(ArrayList<Politician> politicians, MainActivity mainAct) {
        this.politicians = politicians;
        this.mainAct = mainAct;
    }


    @NonNull
    @Override
    public PoliticianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.politician_entry, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new PoliticianViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PoliticianViewHolder holder, int position) {
        Politician p = politicians.get(position);
        holder.pname.setText(p.getName());
        holder.ptitle.setText(p.getTitle());

    }



    @Override
    public int getItemCount() {
        return politicians.size();
    }
}
