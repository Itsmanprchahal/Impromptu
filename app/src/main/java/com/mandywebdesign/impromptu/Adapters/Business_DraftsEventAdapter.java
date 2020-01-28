package com.mandywebdesign.impromptu.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEventDetailAcitvity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.Drafts;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Models.DeleteDraftRespose;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroDelete;
import com.mandywebdesign.impromptu.Retrofit.RetroDraftsEvents;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.HelpOptionsActivity;
import com.mandywebdesign.impromptu.Utils.Constants;
import com.mandywebdesign.impromptu.ui.Join_us;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Business_DraftsEventAdapter extends RecyclerView.Adapter<Business_DraftsEventAdapter.ViewHolder> {

    Context context;
    FragmentManager manager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CardAdapterHelper cardAdapterHelper = new CardAdapterHelper();
    deletedraftIF deletedraftIF;
    ArrayList<RetroDraftsEvents.Datum> data = new ArrayList<>();
    Dialog dialog;

    public void DeleteDraft(deletedraftIF deletedraftIF1) {
        deletedraftIF = deletedraftIF1;
    }

    public Business_DraftsEventAdapter(Context context, FragmentManager manager, ArrayList<RetroDraftsEvents.Datum> data1) {
        this.context = context;
        this.manager = manager;
        this.data = data1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_events, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

//        cardAdapterHelper.onBindViewHolder(viewHolder.itemView,i,getItemCount());
        sharedPreferences = context.getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        RetroDraftsEvents.Datum datum = data.get(i);

        viewHolder.eventName.setText(datum.getTitle());
        viewHolder.delete_draft_bt.setVisibility(View.VISIBLE);
        if (datum.getPrice().equals("0")) {
            viewHolder.evetPrice.setText("Free");
        } else if (!datum.getPrice().equals("0") && !datum.getPrice().equals("Paid")) {
            viewHolder.evetPrice.setText("£ " + Drafts.prices.get(i));
        } else {
            viewHolder.evetPrice.setText(Drafts.prices.get(i));
        }

        String s = datum.getAddressline1();
        Log.e("addre", s);

        if (s.contains(" NearBy ")) {
            String[] arrayString = s.split(" NearBy ");

            String add1 = arrayString[1];

            Log.e("add1", add1);
            viewHolder.eventAddress.setText("Landmark " + add1);

        } else {
            viewHolder.eventAddress.setText(datum.getAddressline1());
        }

        viewHolder.category.setText(datum.getCategory());
        viewHolder.date.setText(Drafts.eventTIme.get(i));
        Glide.with(context).load(datum.getFile().toString()).into(viewHolder.eventImage);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = datum.getEventId().toString();
                Intent intent = new Intent(context, BusinessEventDetailAcitvity.class);
                intent.putExtra("event_id", value);
                intent.putExtra("eventType", "draft");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                editor.putString(Constants.itemPosition, String.valueOf(i));
                editor.commit();
                context.startActivity(intent);

            }
        });

        viewHolder.delete_draft_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfirmationDialog(datum.getEventId(),i);

            }
        });
    }


    @Override
    public int getItemCount() {
        return Drafts.title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView eventImage;
        TextView eventName, eventAddress;
        TextView evetPrice, category, date;
        Button delete_draft_bt;

        public ViewHolder(final View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.event_image);
            eventName = itemView.findViewById(R.id.event_name);
            evetPrice = itemView.findViewById(R.id.event_price);
            eventAddress = itemView.findViewById(R.id.custom_text1);
            category = itemView.findViewById(R.id.custom_category_name);
            date = itemView.findViewById(R.id.date);
            delete_draft_bt = itemView.findViewById(R.id.delete_draft_bt);
        }
    }

    public void ConfirmationDialog(Integer eventId, int i) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.deletedraft);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button yes = dialog.findViewById(R.id.yesdialog);
        Button no = dialog.findViewById(R.id.nodialog);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deletedraftIF.onSucess(String.valueOf(eventId), i);
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
