package com.mandywebdesign.impromptu.Utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Util {

    Context context;

    public static long calender_time_to_timestamp(String str_date) {
        long time_stamp = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
            //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date = (Date) formatter.parse(str_date);
            time_stamp = date.getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        time_stamp = time_stamp / 1000;
        return time_stamp;
    }

    //date convert into time stamp
    public static long calender_date_to_timestamp(String str_date) {

        long time_stamp = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date = (Date) formatter.parse(str_date);
            time_stamp = date.getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        time_stamp = time_stamp / 1000;
        return time_stamp;
    }

    public static String convertTimeStampToTime(long timestamp) {
//        Calendar calendar = Calendar.getInstance();
//        TimeZone tz = TimeZone.getDefault();
//        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
////        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//        sdf.setTimeZone(tz);
//        Date currenTimeZone = new Date(timestamp * 1000);
//        return sdf.format(currenTimeZone);

        // Create a DateFormatter object for displaying date in specified format.
        DateFormat formatter = new SimpleDateFormat("hh:mm a");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return formatter.format(calendar.getTime());
    }

    public static String convertTimeStampDate(long timestamp) {

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return formatter.format(calendar.getTime());
    }

    public static String convertTimeStampDateTime(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        sdf.setTimeZone(tz);
        Date currenTimeZone = new Date(timestamp * 1000);
        return sdf.format(currenTimeZone);
    }

    public MultipartBody.Part sendImageFileToserver(Bitmap bitMap) throws IOException {

        File filesDir = context.getFilesDir();
        File file = new File(filesDir, "avatar" + ".png");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "avatar");

        return body;
    }
}
