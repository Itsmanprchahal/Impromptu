package com.mandywebdesign.impromptu.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEventDetailAcitvity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AttendingTab.Upcoming;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Utils.Constants;
import com.mandywebdesign.impromptu.ui.BarcodeEncoder;
import com.mandywebdesign.impromptu.ui.BookEventActivity;

public class Normal_upcoming_events_adpater extends RecyclerView.Adapter<Normal_upcoming_events_adpater.ViewHolder> {
    Context context;
    FragmentManager manager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public Normal_upcoming_events_adpater(Context context, FragmentManager manager) {
        this.context = context;
        this.manager = manager;
    }

    @NonNull
    @Override
    public Normal_upcoming_events_adpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_barcode_events,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Normal_upcoming_events_adpater.ViewHolder viewHolder, final int i) {

        sharedPreferences = context.getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        viewHolder.eventName.setText(Upcoming.title.get(i));
        viewHolder.date.setText(Upcoming.time.get(i));


        if (Upcoming.prices.get(i).equals("0"))
        {
            viewHolder.evetPrice.setText("Free");
        } else if (!Upcoming.prices.get(i).equals("0") && !Upcoming.prices.get(i).equals("Paid"))
        {
            viewHolder.evetPrice.setText("£ "+Upcoming.prices.get(i));
        }else {
            viewHolder.evetPrice.setText(Upcoming.prices.get(i));
        }

        String s = Upcoming.addres.get(i);
        Log.e("addre",s );

        if (s.contains(" NearBy "))
        {
            String[] arrayString = s.split(" NearBy ");

            String add1 = arrayString[1];

            Log.e("add1",add1);
            viewHolder.eventAddress.setText("Landmark "+add1);

        }else {
            viewHolder.eventAddress.setText(Upcoming.addres.get(i));
        }


        viewHolder.eventAddress.setText(Upcoming.addres.get(i));
        if (Upcoming.usertype.get(i).equals("business"))
        {
                int count = Integer.parseInt(Upcoming.total_book_tickets.get(i));

                int count1 = count-1;
                if (count1==0)
                {
                    viewHolder.total_tickettext1.setVisibility(View.GONE);
                }else {
                    viewHolder.total_tickettext1.setText("+"+count1);
                }
        }

        viewHolder.category.setText(Upcoming.categois.get(i));
        Glide.with(context).load(Upcoming.images.get(i)).into(viewHolder.eventImage);

        //MAke QR code here.....................................


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode("Paid" + "\n" + Upcoming.event_id.get(i)+"\n"+Upcoming.title.get(i)+"\n"+Upcoming.userID.get(i), BarcodeFormat.QR_CODE, 700, 700);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            viewHolder.QRImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        viewHolder.QRImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.setContentView(R.layout.custom_barcode_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                ImageView QR = dialog.findViewById(R.id.QR_image_opemn);

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode("Paid" + "\n" + Upcoming.event_id.get(i)+"\n"+Upcoming.title.get(i)+"\n"+Upcoming.userID.get(i), BarcodeFormat.QR_CODE, 700, 700);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    QR.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = Upcoming.event_id.get(i);
                Intent intent = new Intent(context, BookEventActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("event_id",value);
                intent.putExtra("eventType","upcoming");
                editor.putString(Constants.itemPosition, String.valueOf(i));
                editor.commit();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Upcoming.name1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName,eventAddress,total_tickettext1;
        TextView evetPrice,category,date;
        RoundedImageView QRImage;


        public ViewHolder(final View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.event_image);
            eventName = itemView.findViewById(R.id.event_name);
            evetPrice = itemView.findViewById(R.id.event_price);
            eventAddress = itemView.findViewById(R.id.custom_text1);
            category = itemView.findViewById(R.id.custom_category_name);
            QRImage  = itemView.findViewById(R.id.QR_image);
            date = itemView.findViewById(R.id.date);
            total_tickettext1 = itemView.findViewById(R.id.total_tickettext1);
        }
    }
}
