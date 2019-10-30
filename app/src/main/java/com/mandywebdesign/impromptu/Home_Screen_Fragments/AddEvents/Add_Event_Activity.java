package com.mandywebdesign.impromptu.Home_Screen_Fragments.AddEvents;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mandywebdesign.impromptu.Adapters.AddImageAdapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroEventCategory;
import com.mandywebdesign.impromptu.Retrofit.RetroGetEventData;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_Event_Activity extends AppCompatActivity implements IPickResult {

    public static int RESULT_LOAD_IMAGE = 121;
    ProgressBar addEvent_progress;
    Button nextButton;
    ImageView back, add_event_close, valid_image, invalid_image, valid_image1, valid_image2, invalid_image1, invalid_image2, deleteimage1, deleteimage2, deleteimage3;
    Spinner spinner;
    EditText your_event_title, your_event_description, hyperlinkone, hyperlinktwo, hyperlinkthree;
    TextView mAddPhoto, createvent_addlink, createvent_addlink1, createvent_addlink2;
    RecyclerView recyclerView;
    AddImageAdapter adapter;
    ArrayList<String> cate = new ArrayList<>();
    ArrayList<String> cate_id = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String userToken, BToken, S_Token;
    String categ;
    Dialog progressDialog;
    public static ArrayList<MultipartBody.Part> part = new ArrayList<>();
    static int count = 0;
    public static Bitmap bitmap;
    RelativeLayout link_layoutone, link_layouttwo, link_layoutthree;
    String edit, value, edittitle, editdesc, editcate, republish;
    public static ArrayList<String> image_uris = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event_);
        part.clear();
        image_uris.clear();

        TimeZone tz = TimeZone.getDefault();
        Log.d("timezone", tz.getDisplayName() + "  " + tz.getID());

        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = "Bearer " + sharedPreferences.getString("Usertoken", "");
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");


        Intent intent = getIntent();
        if (intent != null) {
            edit = intent.getStringExtra("editevent");
            value = intent.getStringExtra("value");


        }
        republish = intent.getStringExtra("republish");
        init();

        addEvent_progress.setProgress(35);
        addEvent_progress.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorTheme), PorterDuff.Mode.SRC_ATOP);


        getCategories(userToken);


        listeners();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);


        if (edit!=null)
        {
            if (edit.equalsIgnoreCase("edit")) {
                if (!S_Token.equalsIgnoreCase("")) {
                    editEVnet("Bearer " + S_Token, value);

                } else if (!BToken.equalsIgnoreCase("")) {
                    editEVnet("Bearer " + BToken, value);
                }
            } else if (edit.equalsIgnoreCase("republish")) {
                if (!S_Token.equalsIgnoreCase("")) {
                    editEVnet("Bearer " + S_Token, value);

                } else if (!BToken.equalsIgnoreCase("")) {
                    editEVnet("Bearer " + BToken, value);
                }
            }
        }

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT,
                ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                moveItem(dragged.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                deleteItem(viewHolder.getAdapterPosition());
            }
        });

        helper.attachToRecyclerView(recyclerView);
    }

    private void getCategories(String userToken) {
        Call<RetroEventCategory> categoryCall = WebAPI.getInstance().getApi().Category(userToken);
        categoryCall.enqueue(new Callback<RetroEventCategory>() {
            @Override
            public void onResponse(Call<RetroEventCategory> call, Response<RetroEventCategory> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {
                        RetroEventCategory category = response.body();
                        List<RetroEventCategory.Datum> datumList = category.data;
                        for (RetroEventCategory.Datum datum : datumList) {
                            Log.d("name", "" + datum.getName());
                            String categ = datum.getName();
                            cate.add(categ);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RetroEventCategory> call, Throwable t) {
                Log.d("name1", "" + t);
            }
        });


        cate.add("Select Category");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.layout_spinner, cate);
        adapter.setDropDownViewResource(R.layout.custom_text);
        spinner.setAdapter(adapter);

    }

    private static final int LIMIT = 5;

    private void listeners() {
        mAddPhoto.setVisibility(View.VISIBLE);
        mAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PickImageDialog.build(new PickSetup()
                        .setButtonOrientation(LinearLayout.HORIZONTAL)).show(Add_Event_Activity.this);
            }
        });

        if (!S_Token.equalsIgnoreCase("")) {
            createvent_addlink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    link_layoutone.setVisibility(View.VISIBLE);
                    hyperlinkone.setVisibility(View.VISIBLE);
                    createvent_addlink.setVisibility(View.VISIBLE);
                    deleteimage1.setVisibility(View.VISIBLE);
                }
            });

        } else {
            createvent_addlink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    link_layoutone.setVisibility(View.VISIBLE);
                    hyperlinkone.setVisibility(View.VISIBLE);
                    createvent_addlink.setVisibility(View.VISIBLE);
                    deleteimage1.setVisibility(View.VISIBLE);
                }
            });

            createvent_addlink1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    link_layouttwo.setVisibility(View.VISIBLE);
                    hyperlinktwo.setVisibility(View.VISIBLE);
                    createvent_addlink1.setVisibility(View.GONE);
                    deleteimage1.setVisibility(View.GONE);
                    deleteimage2.setVisibility(View.VISIBLE);
                }
            });

            createvent_addlink2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    link_layoutthree.setVisibility(View.VISIBLE);
                    hyperlinkthree.setVisibility(View.VISIBLE);
                    createvent_addlink2.setVisibility(View.GONE);
                    deleteimage1.setVisibility(View.GONE);
                    deleteimage2.setVisibility(View.GONE);
                    deleteimage3.setVisibility(View.VISIBLE);
                }
            });
        }


        hyperlinkone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Patterns.WEB_URL.matcher(hyperlinkone.getText().toString()).matches()) {
                    valid_image.setVisibility(View.VISIBLE);
                    invalid_image.setVisibility(View.GONE);
                } else {
                    valid_image.setVisibility(View.GONE);
                    invalid_image.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!S_Token.equalsIgnoreCase("")) {
                    if (editable.length() == 0) {
                        valid_image.setVisibility(View.GONE);
                        invalid_image.setVisibility(View.GONE);
                    }
                } else {
                    if (editable.length() == 0) {
                        valid_image.setVisibility(View.GONE);
                        invalid_image.setVisibility(View.GONE);
                    } else {
                        createvent_addlink1.setVisibility(View.VISIBLE);
                    }
                }


            }
        });

        hyperlinktwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Patterns.WEB_URL.matcher(hyperlinktwo.getText().toString()).matches()) {
                    valid_image1.setVisibility(View.VISIBLE);
                    invalid_image1.setVisibility(View.GONE);

                } else {
                    valid_image1.setVisibility(View.GONE);
                    invalid_image1.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    valid_image1.setVisibility(View.GONE);
                    invalid_image1.setVisibility(View.GONE);
                } else {
                    createvent_addlink2.setVisibility(View.VISIBLE);
                }
            }
        });

        hyperlinkthree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Patterns.WEB_URL.matcher(hyperlinkthree.getText().toString()).matches()) {
                    valid_image2.setVisibility(View.VISIBLE);
                    invalid_image2.setVisibility(View.GONE);


                } else {
                    valid_image2.setVisibility(View.GONE);
                    invalid_image2.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    valid_image2.setVisibility(View.GONE);
                    invalid_image2.setVisibility(View.GONE);
                }
            }
        });

        deleteimage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createvent_addlink.setVisibility(View.VISIBLE);
                createvent_addlink1.setVisibility(View.GONE);
                createvent_addlink2.setVisibility(View.GONE);
                link_layoutone.setVisibility(View.GONE);
                hyperlinkone.setText("");
            }
        });

        deleteimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createvent_addlink2.setVisibility(View.GONE);
                createvent_addlink1.setVisibility(View.VISIBLE);
                link_layouttwo.setVisibility(View.GONE);
                hyperlinktwo.setText("");
                deleteimage1.setVisibility(View.VISIBLE);
            }
        });

        deleteimage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createvent_addlink2.setVisibility(View.VISIBLE);
                link_layoutthree.setVisibility(View.GONE);
                hyperlinkthree.setText("");
                deleteimage2.setVisibility(View.VISIBLE);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                categ = parent.getSelectedItem().toString();
                count = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
                String eventTitle = your_event_title.getText().toString();
                String eventDesc = your_event_description.getText().toString();
                intent.putExtra("eventTitle", eventTitle);
                intent.putExtra("eventDesc", eventDesc);
                intent.putExtra("eventCate", categ);
                intent.putExtra("editevent", edit);
                intent.putExtra("value", value);
                intent.putExtra("link1", hyperlinkone.getText().toString());
                intent.putExtra("link2", hyperlinktwo.getText().toString());
                intent.putExtra("link3", hyperlinkthree.getText().toString());


                if (TextUtils.isEmpty(eventTitle) && TextUtils.isEmpty(eventDesc) && categ.equals("Select Category")) {
                    your_event_title.setError("Add Title");
                    your_event_description.setError("Add Description");
                    Toast.makeText(Add_Event_Activity.this, "Select Category to Create Event", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(eventTitle)) {
                    your_event_title.setError("Add Title");
                } else if (TextUtils.isEmpty(eventDesc)) {
                    your_event_description.setError("Add Description");
                } else if (categ.equals("Select Category")) {
                    Toast.makeText(Add_Event_Activity.this, "Select Category to Create Event", Toast.LENGTH_SHORT).show();
                } else if (image_uris.size() == 0) {
                    Toast.makeText(Add_Event_Activity.this, "Add Images to create event", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(intent);
                }

            }
        });

        add_event_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_uris.clear();
                Intent intent = new Intent(Add_Event_Activity.this, Home_Screen.class);
                startActivity(intent);
                finish();
            }
        });

        //custom camera initlize
        Fresco.initialize(getApplicationContext());


    }


    private void init() {
        addEvent_progress = (ProgressBar) findViewById(R.id.add_event_progress_bar);
        nextButton = (Button) findViewById(R.id.add_event_next);
        back = (ImageView) findViewById(R.id.add_event_back);
        spinner = (Spinner) findViewById(R.id.add_event_category);
        mAddPhoto = (TextView) findViewById(R.id.your_event_add_picture);
        recyclerView = (RecyclerView) findViewById(R.id.add_event_recyclerView);
        your_event_title = (EditText) findViewById(R.id.your_event_title);

        your_event_description = (EditText) findViewById(R.id.your_event_description);
        add_event_close = (ImageView) findViewById(R.id.add_event_close);
        createvent_addlink = (TextView) findViewById(R.id.createvent_addlink);
        createvent_addlink1 = (TextView) findViewById(R.id.createvent_addlink1);
        createvent_addlink2 = (TextView) findViewById(R.id.createvent_addlink2);
        hyperlinkone = (EditText) findViewById(R.id.hyperlinkone);
        hyperlinktwo = (EditText) findViewById(R.id.hyperlinktwo);
        hyperlinkthree = (EditText) findViewById(R.id.hyperlinkthree);
        valid_image = (ImageView) findViewById(R.id.valid_image);
        valid_image1 = (ImageView) findViewById(R.id.valid_image1);
        valid_image2 = (ImageView) findViewById(R.id.valid_image2);
        invalid_image = (ImageView) findViewById(R.id.invalid_image);
        invalid_image1 = (ImageView) findViewById(R.id.invalid_image1);
        invalid_image2 = (ImageView) findViewById(R.id.invalid_image2);
        deleteimage1 = (ImageView) findViewById(R.id.delete_hyperlinkone);
        deleteimage2 = (ImageView) findViewById(R.id.delete_hyperlinktwo);
        deleteimage3 = (ImageView) findViewById(R.id.delete_hyperlinkthree);
        link_layoutone = (RelativeLayout) findViewById(R.id.link_layoutone);
        link_layouttwo = (RelativeLayout) findViewById(R.id.link_layouttwo);
        link_layoutthree = (RelativeLayout) findViewById(R.id.link_layoutthree);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Log.d("++++++++++", "++++ data log +++" + imageBitmap);

        }
    }

    private void SetImage() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AddImageAdapter(this, image_uris);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    void moveItem(int oldPos, int newPos) {
        String image = image_uris.get(oldPos);

        image_uris.remove(oldPos);
        image_uris.add(newPos, image);
        adapter.notifyItemMoved(oldPos, newPos);

    }

    void deleteItem(final int position) {
        image_uris.remove(position);
        adapter.notifyItemRemoved(position);
    }


    private void retrun() {
        Log.d("12345678890", "" + sum(10, 20));
    }

    public int sum(int a, int b) {
        int c = a + b;
        return c;
    }


    public void editEVnet(String userToken, String eventId) {

        progressDialog.show();
        Call<RetroGetEventData> call = WebAPI.getInstance().getApi().getEvents(userToken, "application/json", eventId);
        call.enqueue(new Callback<RetroGetEventData>() {
            @Override
            public void onResponse(Call<RetroGetEventData> call, Response<RetroGetEventData> response) {

                progressDialog.dismiss();
                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {
                        edittitle = response.body().getData().get(0).getTitle();
                        editdesc = response.body().getData().get(0).getDescription();
                        editcate = response.body().getData().get(0).getCategory();


                        if (!response.body().getData().get(0).getLink1().equals("") && response.body().getData().get(0).getLink2().equals("") && response.body().getData().get(0).getLink3().equals("")) {
                            hyperlinkone.setText(response.body().getData().get(0).getLink1());
                            hyperlinkone.setVisibility(View.VISIBLE);
                            createvent_addlink.setVisibility(View.GONE);
                            createvent_addlink1.setVisibility(View.VISIBLE);
                            deleteimage1.setVisibility(View.VISIBLE);
                        } else if (!response.body().getData().get(0).getLink1().equals("") && !response.body().getData().get(0).getLink2().equals("") && response.body().getData().get(0).getLink3().equals("")) {
                            hyperlinkone.setText(response.body().getData().get(0).getLink1());
                            hyperlinktwo.setText(response.body().getData().get(0).getLink2());
                            hyperlinkone.setVisibility(View.VISIBLE);
                            hyperlinktwo.setVisibility(View.VISIBLE);
                            createvent_addlink.setVisibility(View.GONE);
                            createvent_addlink1.setVisibility(View.GONE);
                            createvent_addlink2.setVisibility(View.VISIBLE);
                            deleteimage2.setVisibility(View.VISIBLE);
                        } else if (!response.body().getData().get(0).getLink1().equals("") && !response.body().getData().get(0).getLink2().equals("") && !response.body().getData().get(0).getLink3().equals("")) {
                            hyperlinkone.setText(response.body().getData().get(0).getLink1());
                            hyperlinktwo.setText(response.body().getData().get(0).getLink2());
                            hyperlinkthree.setText(response.body().getData().get(0).getLink3());
                            hyperlinkone.setVisibility(View.VISIBLE);
                            hyperlinktwo.setVisibility(View.VISIBLE);
                            hyperlinkthree.setVisibility(View.VISIBLE);
                            createvent_addlink.setVisibility(View.GONE);
                            createvent_addlink1.setVisibility(View.GONE);
                            createvent_addlink2.setVisibility(View.GONE);
                            deleteimage3.setVisibility(View.VISIBLE);
                        }

                        your_event_title.setText(edittitle);
                        your_event_description.setText(editdesc);
                        spinner.setSelection(response.body().getData().get(0).getCategoryId());

                        for (int i = 0; i < response.body().getData().get(0).getFile().get(0).getImg().size(); i++) {
                            image_uris.add(response.body().getData().get(0).getFile().get(0).getImg().get(i));
                            SetImage();
                            new getImagefromURL().execute(response.body().getData().get(0).getFile().get(0).getImg().get(i));

                            Log.d("IMAGE", image_uris.get(0));
                        }
                    }

                } else {
                    Intent intent = new Intent(Add_Event_Activity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RetroGetEventData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Add_Event_Activity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public File saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 50;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            try {

                part.add(sendImageFileToserver(r.getBitmap()));
                image_uris.add(r.getPath());
                SetImage();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            //Handle possible errors
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public MultipartBody.Part sendImageFileToserver(Bitmap bitMap) throws IOException {

        File filesDir = getApplicationContext().getFilesDir();
        File file = new File(filesDir, "image" + ".png" + System.currentTimeMillis());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.PNG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();


        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), saveBitmapToFile(file));
        MultipartBody.Part body = MultipartBody.Part.createFormData("pictures[]", saveBitmapToFile(file).getName(), requestFile);

        return body;
    }


    //Get Image from Url
    public class getImagefromURL extends AsyncTask<String, Void, Bitmap> {

        public getImagefromURL() {
        }

        @Override
        protected Bitmap doInBackground(String... url) {

            String urlimage = url[0];

            bitmap = null;
            try {
                InputStream stream = new URL(urlimage).openStream();
                bitmap = BitmapFactory.decodeStream(stream);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            try {
                part.add(sendImageFileToserver(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /*###################Code to Close the keyboard when your touch anywhere############*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }
}
