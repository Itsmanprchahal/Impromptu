package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.Normal_user_profile;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.UserProfileFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NormalUSerSetQues_answer extends RecyclerView.Adapter<NormalUSerSetQues_answer.ViewHolder> {

    Context context;
    ArrayList<String> question;
    ArrayList<String> answer;
    String profileStatus;

    public NormalUSerSetQues_answer(Context context, ArrayList<String> question, ArrayList<String> answer) {
        this.context = context;
        this.question = question;
        this.answer = answer;
    }

    @NonNull
    @Override
    public NormalUSerSetQues_answer.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_user_profile_quest, viewGroup, false);
        return new NormalUSerSetQues_answer.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NormalUSerSetQues_answer.ViewHolder viewHolder, final int i) {
        viewHolder.ques.setText(question.get(i));
        viewHolder.answer.setText(answer.get(i));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click "+ UserProfileFragment.QA_id.get(i), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return question.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView ques;
        TextView answer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ques = itemView.findViewById(R.id.recycler_question);
            answer = itemView.findViewById(R.id.recycler_answer);
        }
    }
}