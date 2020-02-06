package com.mandywebdesign.impromptu.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Button;

import com.mandywebdesign.impromptu.R;

public class NoInternet {

    Context context;

    public static boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        //Toast.makeText(BusinessCharityRegister.this, "No Internet connection!,Unable to Register", Toast.LENGTH_LONG).show();
        return netInfo != null && netInfo.isConnected() && netInfo.isAvailable();
    }

    public static Dialog dialog(final Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.nointernetdialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button done = dialog.findViewById(R.id.done_bt_on_no_net_dilog);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
//                System.exit(0);
//                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//                homeIntent.addCategory(Intent.CATEGORY_HOME);
//                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(homeIntent);

                dialog.dismiss();
            }
        });

        dialog.show();

        return dialog;
    }

}
