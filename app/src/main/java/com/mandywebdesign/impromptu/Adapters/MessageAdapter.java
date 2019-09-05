package com.mandywebdesign.impromptu.Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.messages.ChatBoxActivity;
import com.mandywebdesign.impromptu.messages.Messages;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    FragmentManager manager;
    Context context;
    ArrayList<String> eventTitle;
    int pos = -1;


    public MessageAdapter(FragmentManager manager, Context context, ArrayList<String> eventTitle) {
        this.manager = manager;
        this.context = context;
        this.eventTitle = eventTitle;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_guest_check, viewGroup, false);

        return new ViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.imageView.setVisibility(View.GONE);

        viewHolder.title.setText(Messages.eventTitle.get(i));
        if (Messages.lastMEsg.get(i).length()>13)
        {
            viewHolder.ticketType.setText(Messages.lastMEsg.get(i).substring(0,12)+"...");
        }else {
            viewHolder.ticketType.setText(Messages.lastMEsg.get(i));
        }

        if (Messages.MesgCount.get(i).equals("0"))
        {
            viewHolder.mesgCount.setVisibility(View.GONE);
        }else {
            viewHolder.mesgCount.setVisibility(View.VISIBLE);
            viewHolder.mesgCount.setText(Messages.MesgCount.get(i));
            viewHolder.ticketType.setTypeface(viewHolder.mesgCount.getTypeface(), Typeface.BOLD);
            viewHolder.ticketType.setTextColor(context.getColor(R.color.colortextBlack));
            Typeface typeface = ResourcesCompat.getFont(context, R.font.circularstdbold);
            viewHolder.ticketType.setTypeface(typeface);
        }

//        viewHolder.mesgCount.setVisibility(View.VISIBLE);
//        viewHolder.mesgCount.setText(Messages.MesgCount.get(i));

        Glide.with(context).load(Messages.eventImage.get(i)).apply(new RequestOptions().override(200,200)).into(viewHolder.eventImage);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = viewHolder.getAdapterPosition();
                Intent intent = new Intent(context, ChatBoxActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("event_title",Messages.eventTitle.get(pos));
                intent.putExtra("event_image",Messages.eventImage.get(pos));
                intent.putExtra("eventID",Messages.eventID.get(pos));
                intent.putExtra("event_host_user",Messages.hostUserID.get(pos));


//                Bundle bundle = new Bundle();
//                bundle.putString("event_title", Messages.eventTitle.get(pos));
//                bundle.putString("event_image", Messages.eventImage.get(pos));
//                bundle.putString("eventID", Messages.eventID.get(pos));
//                bundle.putString("event_host_user", Messages.hostUserID.get(pos));

                if (Messages.MesgCount.get(i).equals("0"))
                {
                    intent.putExtra("seen_status","0");
                }else {
                    intent.putExtra("seen_status","1");
                }

//                ChatBox chatBox = new ChatBox();
//                chatBox.setArguments(bundle);
//
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.replace(R.id.home_frame_layout, chatBox);
//                transaction.addToBackStack(null);
//                transaction.commit();
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return Messages.eventTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView eventImage;
        TextView title, ticketType,mesgCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.custom_guest_check);
            eventImage = itemView.findViewById(R.id.custom_guest_image);
            ticketType = itemView.findViewById(R.id.custom_guest_ticket);
            title = itemView.findViewById(R.id.custom_guest_name);
            mesgCount = itemView.findViewById(R.id.count);
        }
    }

    //This method will filter the list
    //here we are passing the filtered data
    //and assigning it to the list with notifydatasetchanged method
    public void filterList(ArrayList<String> filterdNames) {
        Messages.eventTitle = filterdNames;
        notifyDataSetChanged();
    }


}
