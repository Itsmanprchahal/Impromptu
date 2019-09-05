package com.mandywebdesign.impromptu.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mandywebdesign.impromptu.Models.PojoQuestion;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.NormalPublishProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    Context context;
    List<PojoQuestion> pojoQuestions;
    String[] strings;
    Spinner spinner;
    EditText editText;
    private QuestionAdapter.OnItemClickListener itemClickListener;



    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {

        itemClickListener = onItemClickListener;
    }

    public QuestionAdapter(Context context, List<PojoQuestion> pojoQuestions,String[] strings) {
        this.context = context;
        this.pojoQuestions = pojoQuestions;
        this.strings=strings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_recyler_question, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        PojoQuestion pojoQuestion = pojoQuestions.get(i);


        viewHolder.questionText.setText(pojoQuestion.getQuestion());
        viewHolder.answerText.setText(pojoQuestion.getAnswer());

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                pojoQuestions.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, pojoQuestions.size());
                notifyDataSetChanged();
            }
        });

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int poistion = viewHolder.getAdapterPosition();
                final Dialog dialog = new Dialog(context);

                dialog.setContentView(R.layout.queston_dilog_question);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.setCanceledOnTouchOutside(false);

                String answer = viewHolder.answerText.getText().toString();
                editText = (EditText) dialog.findViewById(R.id.user_profile_answer);
                spinner = dialog.findViewById(R.id.user_profile_sppiner);
                ImageView close = dialog.findViewById(R.id.close_questtionDialog);
                editText.setText(answer);

                final List<String> stringList = new ArrayList<String>(Arrays.asList(strings));
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item, stringList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                stringList.add(pojoQuestions.get(i).getQuestion());

                Button done = dialog.findViewById(R.id.user_profile_button);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String ques = spinner.getSelectedItem().toString();
                        String answer = editText.getText().toString();
                        viewHolder.answerText.setText(answer);
                        viewHolder.questionText.setText(ques);
                        NormalPublishProfile.QUES.remove(i);
                        NormalPublishProfile.ANSWER.remove(i);
                        NormalPublishProfile.QUES.add(i,ques);
                        NormalPublishProfile.ANSWER.add(i,answer);


                        dialog.dismiss();
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        if (viewHolder.getAdapterPosition() == 0 && viewHolder.getAdapterPosition() <= 1) {
            NormalPublishProfile.addQUES.setText("+ Add another question and answer");
           // viewHolder.answerText.setBackgroundResource(R.drawable.custom_edittext);
            NormalPublishProfile.addQUES.setVisibility(View.VISIBLE);
        }

        if (viewHolder.getAdapterPosition() == 2) {
            NormalPublishProfile.addQUES.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return pojoQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionText, answerText;
        ImageView imageView,edit;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questionText = itemView.findViewById(R.id.recycler_question);
            answerText = itemView.findViewById(R.id.recycler_answer);
            edit = itemView.findViewById(R.id.edit_answer);
            imageView = itemView.findViewById(R.id.recyler_question_close);
        }
    }
}

