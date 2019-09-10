package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.FAQ;

import java.util.ArrayList;

public class FAQ_Adapter extends RecyclerView.Adapter<FAQ_Adapter.ViewHolder> {

    Context context;
    ArrayList<FAQ.Datum> faq = new ArrayList<>();


    public FAQ_Adapter(Context context, ArrayList<FAQ.Datum> faq) {
        this.context = context;
        this.faq = faq;
    }

    @NonNull
    @Override
    public FAQ_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_faq, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FAQ_Adapter.ViewHolder viewHolder, final int i) {

        FAQ.Datum datum = faq.get(i);
        viewHolder.faq_questions.setText(datum.getQuestion());
        viewHolder.faq_answers.setText(datum.getAnswer());


        viewHolder.openanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.openanswer.setVisibility(View.INVISIBLE);
                viewHolder.close_answer.setVisibility(View.VISIBLE);
                viewHolder.faq_answers.setVisibility(View.VISIBLE);
                viewHolder.faq_questions.setVisibility(View.VISIBLE);
                viewHolder.view.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.close_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewHolder.close_answer.setVisibility(View.INVISIBLE);
                viewHolder.openanswer.setVisibility(View.VISIBLE);
                viewHolder.faq_answers.setVisibility(View.GONE);
                viewHolder.faq_questions.setVisibility(View.VISIBLE);
                viewHolder.view.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public int getItemCount() {
        return faq.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView faq_questions, faq_answers;
        ImageView openanswer;
        ImageView close_answer;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            faq_questions = itemView.findViewById(R.id.faq_questions);
            faq_answers = itemView.findViewById(R.id.faq_answers);
            openanswer = itemView.findViewById(R.id.openanswer);
            close_answer = itemView.findViewById(R.id.close_answer);
            view = itemView.findViewById(R.id.view);
        }
    }
}
