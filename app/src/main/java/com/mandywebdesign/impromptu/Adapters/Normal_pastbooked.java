package com.mandywebdesign.impromptu.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEventDetailAcitvity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AttendingTab.Past;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AttendingTab.Upcoming;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Utils.Constants;
import com.mandywebdesign.impromptu.ui.BarcodeEncoder;

public class Normal_pastbooked extends RecyclerView.Adapter<Normal_pastbooked.ViewHolder> {

    Context context;
    FragmentManager manager;
    CardAdapterHelper cardAdapterHelper= new CardAdapterHelper();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Normal_pastbooked(Context context, FragmentManager manager) {
        this.context = context;
        this.manager = manager;
    }

    @NonNull
    @Override
    public Normal_pastbooked.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_barcode_events,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Normal_pastbooked.ViewHolder viewHolder, final int i) {
        //cardAdapterHelper.onBindViewHolder(viewHolder.itemView,i,getItemCount());

        sharedPreferences = context.getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        viewHolder.eventName.setText(Past.title.get(i));
        viewHolder.eventAddress.setText(Past.addres.get(i));
        if (Past.prices.get(i).equals("0"))
        {
            viewHolder.evetPrice.setText("Free");
        }else
        { viewHolder.evetPrice.setText("£ "+Past.prices.get(i));
        }

        String s = Past.addres.get(i);
        Log.e("addre",s );

        if (s.contains(" NearBy "))
        {
            String[] arrayString = s.split(" NearBy ");

            String add1 = arrayString[1];

            Log.e("add1",add1);
            viewHolder.eventAddress.setText("Landmark "+add1);

        }else {
            viewHolder.eventAddress.setText(Past.addres.get(i));
        }


        viewHolder.eventAddress.setText(Past.addres.get(i));
        viewHolder.date.setText(Past.time.get(i));

        viewHolder.category.setText(Past.categois.get(i));
        Glide.with(context).load(Past.images.get(i)).into(viewHolder.eventImage);

        //MAke QR code here.....................................


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode("Paid" + "\n" + Past.event_id.get(i)+"\n"+Past.title.get(i), BarcodeFormat.QR_CODE, 700, 700);
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
                    BitMatrix bitMatrix = multiFormatWriter.encode("Paid" + "\n" + Upcoming.event_id.get(i)+"\n", BarcodeFormat.QR_CODE, 700, 700);
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
                String value = Past.event_id.get(i);
                Intent intent = new Intent(context, BusinessEventDetailAcitvity.class);
                intent.putExtra("event_id",value);
                intent.putExtra("eventType","past");
                editor.putString(Constants.itemPosition, String.valueOf(i));
                editor.commit();
                context.startActivity(intent);

//                Bundle bundle = new Bundle();
//                String value = Past.event_id.get(i);
//                bundle.putString("event_id", value);
//                bundle.putString("eventType","past");
//
//                editor.putString(Constants.itemPosition, String.valueOf(i));
//                editor.commit();
//
//                BusinessEvent_detailsFragment businessEvent_detailsFragment = new BusinessEvent_detailsFragment();
//                businessEvent_detailsFragment.setArguments(bundle);
//
//                manager.beginTransaction().replace(R.id.home_frame_layout,businessEvent_detailsFragment).addToBackStack(null).commit();
//                Toast.makeText(context, ""+Live.event_id.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Past.name1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage,QRImage;
        TextView eventName,eventAddress,date;
        TextView evetPrice,category;

        public ViewHolder(final View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.event_image);
            eventName = itemView.findViewById(R.id.event_name);
            evetPrice = itemView.findViewById(R.id.event_price);
            eventAddress = itemView.findViewById(R.id.custom_text1);
            category = itemView.findViewById(R.id.custom_category_name);
            QRImage  = itemView.findViewById(R.id.QR_image);
            date = itemView.findViewById(R.id.date);

        }
    }
}
