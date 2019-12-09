package com.mandywebdesign.impromptu.Adapters;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AttendingTab.Past;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.Rating;
import com.mandywebdesign.impromptu.Utils.Util;
import com.mandywebdesign.impromptu.messages.ChatBoxActivity;
import com.mandywebdesign.impromptu.messages.Messages;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    FragmentManager manager;
    Context context;
    ArrayList<String> eventTitle;
    String S_Token;
    int pos = -1;
    Dialog progressDialog;


    public MessageAdapter(FragmentManager manager, Context context, ArrayList<String> eventTitle, String S_Token) {
        this.manager = manager;
        this.context = context;
        this.eventTitle = eventTitle;
        this.S_Token = S_Token;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_guest_check, viewGroup, false);
        progressDialog = ProgressBarClass.showProgressDialog(context);
        progressDialog.dismiss();
        return new ViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.imageView.setVisibility(View.GONE);


        viewHolder.title.setText(Messages.eventTitle.get(i));
        if (Messages.lastMEsg.get(i).length() > 13) {
            viewHolder.ticketType.setText(Messages.lastMEsg.get(i).substring(0, 12) + "...");
        } else {
            viewHolder.ticketType.setText(Messages.lastMEsg.get(i));
        }

        if (Messages.MesgCount.get(i).equals("0")) {
            viewHolder.mesgCount.setVisibility(View.GONE);
        } else {
            viewHolder.mesgCount.setVisibility(View.VISIBLE);
            viewHolder.mesgCount.setText(Messages.MesgCount.get(i));
            viewHolder.ticketType.setTypeface(viewHolder.mesgCount.getTypeface(), Typeface.BOLD);
//            viewHolder.ticketType.setTextColor(context.getColor(R.color.colortextBlack));
            Typeface typeface = ResourcesCompat.getFont(context, R.font.circularstdbold);
            viewHolder.ticketType.setTypeface(typeface);
        }


        if (!S_Token.equals("")) {
            if (Messages.rating_status.get(i).equals(1))
            {
                viewHolder.leavefeedback.setVisibility(View.GONE);
            }
            if (Messages.eventTitle.get(i).equals("")) {
                viewHolder.mesgCount.setVisibility(View.GONE);
                viewHolder.linearLayout1.setVisibility(View.VISIBLE);
                viewHolder.linearLayout.setVisibility(View.GONE);
                viewHolder.bookedeventname.setText("'" + Messages.bookedeventname.get(i) + "'");
                viewHolder.attendeename.setText(Messages.attendeename.get(i));
            } else {
                setMessagedata(viewHolder,i);

            }

        } else {
            if (Messages.rating_status.get(i).equals(1))
            {
                viewHolder.leavefeedback.setVisibility(View.GONE);
            }
            viewHolder.leavefeedback.setVisibility(View.GONE);
            viewHolder.time.setVisibility(View.VISIBLE);

            if (Messages.bookingstatus.get(i).equals("No")) {
                viewHolder.leavefeedback.setVisibility(View.GONE);
                viewHolder.time.setVisibility(View.VISIBLE);
                getTime(viewHolder, i);
            } else {
                if (Messages.event_status.get(i).equals("live")) {
                    viewHolder.leavefeedback.setVisibility(View.GONE);
                    viewHolder.time.setVisibility(View.VISIBLE);
                    getTime(viewHolder, i);
                } else {
                    if (Messages.rating_status.get(i).equals(0)) {
                        viewHolder.linearLayout.setVisibility(View.GONE);
                        viewHolder.linearLayout1.setVisibility(View.VISIBLE);
                        viewHolder.leavefeedback.setVisibility(View.GONE);
                        viewHolder.time.setVisibility(View.VISIBLE);
                        viewHolder.mesgCount.setVisibility(View.GONE);
                        viewHolder.attendeename.setText(Messages.attendeename.get(i));
                        viewHolder.bookedeventname.setText("'" + Messages.bookedeventname.get(i) + "'");

                    }else if (Messages.rating_status.get(i).equals(1))
                    {
                        viewHolder.leavefeedback.setVisibility(View.GONE);
                    }
                }
            }
        }

        Glide.with(context).load(Messages.eventImage.get(i)).apply(new RequestOptions().override(200, 200)).into(viewHolder.eventImage);

        if (!Messages.eventTitle.get(i).equals(""))
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = viewHolder.getAdapterPosition();
                    Intent intent = new Intent(context, ChatBoxActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("event_title", Messages.eventTitle.get(pos));
                    intent.putExtra("event_image", Messages.eventImage.get(pos));
                    intent.putExtra("eventID", Messages.eventID.get(pos));
                    intent.putExtra("event_host_user", Messages.hostUserID.get(pos));


                    if (Messages.MesgCount.get(i).equals("0")) {
                        intent.putExtra("seen_status", "0");
                    } else {
                        intent.putExtra("seen_status", "1");
                    }

                    context.startActivity(intent);

                }
            });
        }

    }

    private void setMessagedata(final ViewHolder viewHolder, final int i) {
        if (Messages.bookingstatus.get(i).equals("No")) {
            viewHolder.leavefeedback.setVisibility(View.GONE);
            viewHolder.time.setVisibility(View.VISIBLE);
            getTime(viewHolder, i);
        } else {
            if (Messages.event_status.get(i).equals("live")) {
                viewHolder.leavefeedback.setVisibility(View.GONE);
                viewHolder.time.setVisibility(View.VISIBLE);
                getTime(viewHolder, i);
            } else {
                if (Messages.rating_status.get(i).equals(0)) {
                    viewHolder.leavefeedback.setVisibility(View.VISIBLE);
                    viewHolder.leavefeedback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.custom_rating_box);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCancelable(true);

                            final RatingBar ratingBar = dialog.findViewById(R.id.rating_bar);
                            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                            final EditText feedback = dialog.findViewById(R.id.feedback);
                            Button dialogratingshare_button = dialog.findViewById(R.id.dialogratingshare_button);
                            dialog.show();

                            dialogratingshare_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String rating = String.valueOf(ratingBar.getRating());
                                    String feedbck = feedback.getText().toString();
                                    if (rating.equals("") | feedbck.equals("")) {
                                        Toast.makeText(context, "Add Rating  and reviews", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressDialog.show();
                                        Call<Rating> call = WebAPI.getInstance().getApi().rating("Bearer " + S_Token, Messages.eventID.get(i), rating, feedbck);
                                        call.enqueue(new Callback<Rating>() {
                                            @Override
                                            public void onResponse(Call<Rating> call, Response<Rating> response) {
                                                if (response.body() != null) {
                                                    progressDialog.dismiss();
                                                    dialog.dismiss();
                                                    if (response.body().getStatus().equals("200")) {
                                                        viewHolder.leavefeedback.setVisibility(View.GONE);
                                                    } else {
                                                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Rating> call, Throwable t) {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                } else {
                    viewHolder.leavefeedback.setVisibility(View.GONE);
                    viewHolder.time.setVisibility(View.VISIBLE);

                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time ==> " + c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                    String formattedDate = df.format(c.getTime());
                    c.add(Calendar.DATE, 1);

                    String yesterday = df.format(c.getTime());

                    if (formattedDate.matches(Util.convertTimeStampDate(Long.parseLong(Messages.lastmesgtime.get(i))))) {
                        viewHolder.time.setText("Today at " + Util.convertTimeStampToTime(Long.parseLong(Messages.lastmesgtime.get(i))));
                    } else if (yesterday.matches(Util.convertTimeStampToTime(Long.parseLong(Messages.lastmesgtime.get(i))))) {
                        viewHolder.time.setText("Tomorrow at " + Util.convertTimeStampToTime(Long.parseLong(Messages.lastmesgtime.get(i))));
                    } else {
                        String date = Util.convertTimeStampDate(Long.parseLong(Messages.lastmesgtime.get(i)));
                        //to change server date formate
                        String s1 = date;
                        String[] str = s1.split("/");
                        String str1 = str[0];
                        String str2 = str[1];
                        String str3 = str[2];
                        viewHolder.time.setText(str2 + "/" + str1 + "/" + str3 + " at " + Util.convertTimeStampToTime(Long.parseLong(Messages.lastmesgtime.get(i))));
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return Messages.eventTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView eventImage;
        TextView title, ticketType, mesgCount, leavefeedback, time;
        LinearLayout linearLayout1;
        RelativeLayout linearLayout;
        TextView attendeename, bookedeventname;
        View view, view1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.custom_guest_check);
            eventImage = itemView.findViewById(R.id.custom_guest_image);
            ticketType = itemView.findViewById(R.id.custom_guest_ticket);
            title = itemView.findViewById(R.id.custom_guest_name);
            mesgCount = itemView.findViewById(R.id.count);
            leavefeedback = itemView.findViewById(R.id.leavefeedback);
            time = itemView.findViewById(R.id.time);
            linearLayout = itemView.findViewById(R.id.layout);
            linearLayout1 = itemView.findViewById(R.id.layout2);
            attendeename = itemView.findViewById(R.id.attendeename);
            bookedeventname = itemView.findViewById(R.id.bookedeventname);
            view1 = itemView.findViewById(R.id.view1);
        }
    }

    //This method will filter the list
    //here we are passing the filtered data
    //and assigning it to the list with notifydatasetchanged method
    public void filterList(ArrayList<String> filterdNames) {
        Messages.eventTitle = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public void getTime(ViewHolder viewHolder, int i) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time ==> " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String formattedDate = df.format(c.getTime());
        c.add(Calendar.DATE, 1);

        String yesterday = df.format(c.getTime());


        if (formattedDate.matches(Util.convertTimeStampDate(Long.parseLong(Messages.lastmesgtime.get(i))))) {
            viewHolder.time.setText("Today at " + Util.convertTimeStampToTime(Long.parseLong(Messages.lastmesgtime.get(i))));
        } else if (yesterday.matches(Util.convertTimeStampToTime(Long.parseLong(Messages.lastmesgtime.get(i))))) {
            viewHolder.time.setText("Tomorrow at " + Util.convertTimeStampToTime(Long.parseLong(Messages.lastmesgtime.get(i))));
        } else {
            String date = Util.convertTimeStampDate(Long.parseLong(Messages.lastmesgtime.get(i)));
            /*to change server date formate*/
            String s1 = date;
            String[] str = s1.split("/");
            String str1 = str[0];
            String str2 = str[1];
            String str3 = str[2];
            viewHolder.time.setText(str2 + "/" + str1 + "/" + str3 + " at " + Util.convertTimeStampToTime(Long.parseLong(Messages.lastmesgtime.get(i))));
        }
    }

}
