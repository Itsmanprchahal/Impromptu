package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mandywebdesign.impromptu.R;

import java.util.ArrayList;

public class NormalUser_updateProfileAdpeter extends RecyclerView.Adapter<NormalUser_updateProfileAdpeter.ViewHolder> {
    Context context;
    ArrayList<String> question;
    ArrayList<String> answer;

    public NormalUser_updateProfileAdpeter(Context context, ArrayList<String> question, ArrayList<String> answer) {
        this.context = context;
        this.question = question;
        this.answer = answer;
    }

    @NonNull
    @Override
    public NormalUser_updateProfileAdpeter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_user_profile_quest, viewGroup, false);

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NormalUser_updateProfileAdpeter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
