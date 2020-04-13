package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Adapters.Normal_user_profile_QA_adapter;
import com.mandywebdesign.impromptu.Adapters.QuestionAdapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Models.PojoQuestion;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroDeleteQUEs;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.Join_us;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NormalPublishProfile extends AppCompatActivity {

    public static int RESULT_LOAD_IMAGE = 1001;
    public static int REQUEST_CAMERA = 1002;
    public static int MY_PERMISSIONS_REQUEST_CAMERA = 1255;
    public EditText username, about, age;
    public static TextView addQUES;
    public static Spinner spinner;
    String getGender;
    ImageView backonnormalpublish;
    RoundedImageView userImage;
    RadioButton male, female;
    RadioGroup radioGroup;
    EditText editText;
    SharedPreferences sharedPreferences;
    RecyclerView questionRecycler;
    public static String Question, answer;
    View view;
    static Bitmap bitmap;
    public static String Socai_user, userimage, user_id, userName_, age_, about_;
    static String getUsername = "", getUserStatus = "", getUserAge = "";
    Button publish_profle;
    public static String userToken, profileStstus;
    static Uri uri;
    String Gender;
    String[] dialogOptions = new String[]{"Camera", "Gallery", "Cancel"};
    FragmentManager manager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static MultipartBody.Part part;
    Dialog progressDialog;
    public static ArrayList<String> QUES = new ArrayList<>();
    public static ArrayList<String> QUES_Update = new ArrayList<>();
    public static ArrayList<String> ANSWER = new ArrayList<>();
    public static ArrayList<String> ANSWER_Update = new ArrayList<>();
    public static ArrayList<String> QA_id = new ArrayList<>();
    public ArrayList<String> empty_QA_id = new ArrayList<>();
    List<PojoQuestion> pojoQuestions = new ArrayList<>();
    QuestionAdapter questionAdapter;
    public static List QuesAnswer = new ArrayList<>();
    public static String[] arryString = new String[]{"Most favourite place in your city?",
            "If you had to eat at one restaurant, which would it be?", "What is your favourite concert venue?",
            "The best gig you have been to?", "Your favourite nightspot in your city?",
            "The best art gallery near you?", "Best market in your city?", "Best road trip taken from your home?"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_publish_profile);

        progressDialog = ProgressBarClass.showProgressDialog(this);

        Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(NormalPublishProfile.this, R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);

        progressDialog.show();

        preferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = preferences.getString("Socailtoken", "");
        user_id = preferences.getString("SocailuserId", "");

        Intent intent = getIntent();
        if (intent != null) {
            profileStstus = intent.getStringExtra("normal_edit");
        }

        init();
        listeners();

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        Socai_user = sharedPreferences.getString("Social_username", "");
        userimage = sharedPreferences.getString("Social_image", "");
        addQUES.setText(R.string.userProfileQuestion);
        addQUES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        if (profileStstus.equals("1")) {
            Call<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> call = WebAPI.getInstance().getApi().normalGetPRofile("Bearer " + userToken, "application/json", "");
            call.enqueue(new Callback<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile>() {
                @Override
                public void onResponse(Call<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> call, Response<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> response) {

                    if (response.body() != null) {
                        if (response.body().getStatus().equals("200")) {


                            getUsername = response.body().getData().get(0).getUsername();
                            getUserAge = response.body().getData().get(0).getAge();
                            getUserStatus = response.body().getData().get(0).getStatus().toString();
                            getGender = response.body().getData().get(0).getGender();

                            if (getGender.equals("Male")) {
                                male.setChecked(true);
                                female.setVisibility(View.GONE);
                            } else if (getGender.equals("Female")) {
                                female.setChecked(true);
                                male.setVisibility(View.GONE);
                            } else {
                                male.setChecked(false);
                                female.setChecked(false);
                            }


                            username.setText(getUsername);
                            username.setClickable(false);
                            username.setFocusable(false);

                            Glide.with(NormalPublishProfile.this).load(response.body().getData().get(0).getImage()).into(userImage);
                            new getImagefromURL(userImage).execute(NormalGetProfile.getNormalUserImage);

                            age.setText(getUserAge);
                            age.setClickable(false);
                            age.setFocusable(false);

                            about.setText(getUserStatus);
                            com.mandywebdesign.impromptu.Retrofit.NormalGetProfile normalGetProfile = response.body();
                            List<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile.Question> datum = normalGetProfile.getData().get(0).getQuestion();

                            QUES.clear();
                            ANSWER.clear();
                            QA_id.clear();
                            for (com.mandywebdesign.impromptu.Retrofit.NormalGetProfile.Question question : datum) {

                                QUES.add(question.getQuestion());
                                ANSWER.add(question.getAnswer());
                                QA_id.add(question.getQuestionId().toString());
                                Log.d("++++QuestionsiDs++++", "" + QA_id);


                                Log.d("141414", "" + QUES.toString());

                                if (QUES.size() >= 3) {
                                    addQUES.setVisibility(View.INVISIBLE);
                                } else if (QUES.size() <= 2) {
                                    addQUES.setVisibility(View.VISIBLE);

                                }
                            }

                            LinearLayoutManager layoutManager = new LinearLayoutManager(NormalPublishProfile.this, LinearLayoutManager.VERTICAL, false);
                            questionRecycler.setLayoutManager(layoutManager);

                            Normal_user_profile_QA_adapter adapter = new Normal_user_profile_QA_adapter(NormalPublishProfile.this, QUES, ANSWER, arryString, userToken,new QuesIDIF() {
                                @Override
                                public void questID(String pos, String id) {
                                    ConfirmationDialog(pos, id, userToken);

                                }
                            });
                            questionRecycler.setAdapter(adapter);
                            adapter.Normal_user_profile_QA_adapter(new QuesIDIF() {
                                @Override
                                public void questID(String pos, String id) {
                                    ConfirmationDialog(pos, id, userToken);

                                }
                            });
                        }
                    } else {
                        Intent intent = new Intent(NormalPublishProfile.this, NoInternetScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }

                @Override
                public void onFailure(Call<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> call, Throwable t) {
                    if (NoInternet.isOnline(NormalPublishProfile.this) == false) {
                        progressDialog.dismiss();

                        NoInternet.dialog(NormalPublishProfile.this);
                    }
                }
            });
        } else {
            username.setText(Socai_user);
            age.setHint("Age");
            about.setHint("Write a bit about yourself");

            Glide.with(NormalPublishProfile.this)

                    .load(userimage)
                    .placeholder(R.drawable.profile).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(userImage);
            new getImagefromURL(userImage).execute(userimage);
            progressDialog.dismiss();
        }

    }

    public void ConfirmationDialog(String position, String quesID, final String usrToken) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.confirmationdialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button yes = dialog.findViewById(R.id.yesdialog);
        Button no = dialog.findViewById(R.id.nodialog);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<RetroDeleteQUEs> call = WebAPI.getInstance().getApi().deleteQues("Bearer " + userToken, quesID);
                call.enqueue(new Callback<RetroDeleteQUEs>() {
                    @Override
                    public void onResponse(Call<RetroDeleteQUEs> call, Response<RetroDeleteQUEs> response) {
                        if (response.isSuccessful()) {

                            recreate();

                        }
                        progressDialog.dismiss();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<RetroDeleteQUEs> call, Throwable t) {
                        progressDialog.dismiss();
                        dialog.dismiss();
                        Toast.makeText(NormalPublishProfile.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

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

    private void listeners() {
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.normal_user_male:
                        Gender = "Male";
                        break;

                    case R.id.normal_user_female:
                        Gender = "Female";
                        break;

                }
            }
        });


        backonnormalpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        publish_profle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(age.getText().toString())) {
                    age.setError("Enter Age");
                } else {
                    if (Gender == (null)) {
                        Toast.makeText(NormalPublishProfile.this, "Select Gender", Toast.LENGTH_SHORT).show();
                    } else if (part == null) {
                        Toast.makeText(NormalPublishProfile.this, "Add Image", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(about.getText().toString())) {
                        Toast.makeText(NormalPublishProfile.this, "Add Description", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(username.getText().toString())) {
                        username.setError("Enter Username");
                    } else {
                        if (profileStstus.equalsIgnoreCase("0")) {
                            userName_ = username.getText().toString();
                            age_ = age.getText().toString();
                            about_ = about.getText().toString();

                            progressDialog.show();


                            Call<com.mandywebdesign.impromptu.Retrofit.NormalPublishProfile> call = WebAPI.getInstance().getApi().normalPublishProfile("Bearer " + userToken, user_id, Gender, userName_, age_, about_, QUES, ANSWER, empty_QA_id, part);
                            call.enqueue(new Callback<com.mandywebdesign.impromptu.Retrofit.NormalPublishProfile>() {
                                @Override
                                public void onResponse(Call<com.mandywebdesign.impromptu.Retrofit.NormalPublishProfile> call, Response<com.mandywebdesign.impromptu.Retrofit.NormalPublishProfile> response) {
                                    progressDialog.dismiss();
                                    if (response.body() != null) {
                                        if (response.body().getStatus().equals("200")) {
                                            Toast.makeText(NormalPublishProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                            editor = sharedPreferences.edit();
                                            editor.putString("profilegender", Gender);
                                            editor.putBoolean("profileStatus", true);
                                            editor.apply();
                                            Intent intent = new Intent(NormalPublishProfile.this, NormalGetProfile.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } else {
                                        Intent intent = new Intent(NormalPublishProfile.this, NoInternetScreen.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }

                                }

                                @Override
                                public void onFailure(Call<com.mandywebdesign.impromptu.Retrofit.NormalPublishProfile> call, Throwable t) {
                                    if (NoInternet.isOnline(NormalPublishProfile.this) == false) {
                                        progressDialog.dismiss();

                                        NoInternet.dialog(NormalPublishProfile.this);
                                    }
                                }
                            });

                            Log.d("147258369", "" + QuesAnswer);
                        } else if (profileStstus.equalsIgnoreCase("1")) {
                            userName_ = username.getText().toString();
                            age_ = age.getText().toString();
                            about_ = about.getText().toString();

                            progressDialog.show();


                            Call<com.mandywebdesign.impromptu.Retrofit.NormalPublishProfile> call = WebAPI.getInstance().getApi().normalPublishProfile("Bearer " + userToken, user_id, Gender, userName_, age_, about_, QUES, ANSWER, QA_id, part);
                            call.enqueue(new Callback<com.mandywebdesign.impromptu.Retrofit.NormalPublishProfile>() {
                                @Override
                                public void onResponse(Call<com.mandywebdesign.impromptu.Retrofit.NormalPublishProfile> call, Response<com.mandywebdesign.impromptu.Retrofit.NormalPublishProfile> response) {
                                    if (response.body() != null) {
                                        if (response.body().getStatus().equals("200")) {
                                            editor = sharedPreferences.edit();
                                            editor.putString("profilegender", Gender);
                                            editor.apply();
                                            Toast.makeText(NormalPublishProfile.this, "Profile Updated ", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(NormalPublishProfile.this, NormalGetProfile.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } else {
                                        Intent intent = new Intent(NormalPublishProfile.this, NoInternetScreen.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onFailure(Call<com.mandywebdesign.impromptu.Retrofit.NormalPublishProfile> call, Throwable t) {
                                    if (NoInternet.isOnline(NormalPublishProfile.this) == false) {
                                        progressDialog.dismiss();

                                        NoInternet.dialog(NormalPublishProfile.this);
                                    }
                                    progressDialog.dismiss();
                                    Toast.makeText(NormalPublishProfile.this, "" + t, Toast.LENGTH_SHORT).show();
                                }
                            });

                            Log.d("147258369", "" + QuesAnswer);
                        }

                    }
                }
            }
        });
    }

    private void init() {
        addQUES = findViewById(R.id.normal_user_profile_question);
        about = findViewById(R.id.normal_user_about);
        questionRecycler = findViewById(R.id.normal_user_profile_question_recycle);
        username = findViewById(R.id.normal_user_profile_username);
        userImage = findViewById(R.id.normal_user_profile_userimage);
        age = findViewById(R.id.normal_user_profile_age);
        publish_profle = findViewById(R.id.normal_published_bt);
        radioGroup = findViewById(R.id.gendergroup);
        male = findViewById(R.id.normal_user_male);
        female = findViewById(R.id.normal_user_female);
        backonnormalpublish = findViewById(R.id.backonnormalpublish);
    }

    public void dialog() {
        final Dialog dialog = new Dialog(NormalPublishProfile.this);

        dialog.setContentView(R.layout.queston_dilog_question);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCanceledOnTouchOutside(false);

        spinner = dialog.findViewById(R.id.user_profile_sppiner);
        editText = dialog.findViewById(R.id.user_profile_answer);
        ImageView close = dialog.findViewById(R.id.close_questtionDialog);

        final List<String> stringList = new ArrayList<String>(Arrays.asList(arryString));
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(NormalPublishProfile.this,
                android.R.layout.simple_spinner_item, stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        Button dialogButton = dialog.findViewById(R.id.user_profile_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Question = spinner.getSelectedItem().toString();
                answer = editText.getText().toString();

                if (TextUtils.isEmpty(answer)) {
                    editText.setError("Write your Answer");
                } else {


                    Log.d("qwerty", "" + Question);

                    QUES.add(Question);
                    ANSWER.add(answer);
                    QA_id.add("");
                    Log.d("123456789", "mnmnm " + QUES.toString() + "\n" + ANSWER.toString());
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(NormalPublishProfile.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    questionRecycler.setLayoutManager(layoutManager);
                    questionRecycler.setNestedScrollingEnabled(false);

                    if (QUES.size() >= 3) {
                        addQUES.setVisibility(View.INVISIBLE);
                    } else {
                        addQUES.setVisibility(View.VISIBLE);
                    }

//                    for (int i=0;i<)
                    final PojoQuestion pojoQuestion = new PojoQuestion(Question, answer, QA_id);
                    pojoQuestions.add(pojoQuestion);

                    // Log.d("update","141 "+Question+"   "+answer);

                    Normal_user_profile_QA_adapter adapter = new Normal_user_profile_QA_adapter(NormalPublishProfile.this, QUES, ANSWER, arryString, userToken,new QuesIDIF() {
                        @Override
                        public void questID(String pos, String id) {
                            ConfirmationDialog(pos, id, userToken);

                        }
                    });
                    questionRecycler.setAdapter(adapter);

//                    questionAdapter = new QuestionAdapter(NormalPublishProfile.this, pojoQuestions,arryString);
//                    questionRecycler.setAdapter(questionAdapter);
//                    questionAdapter.notifyDataSetChanged();

                    dialog.dismiss();
                }


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

    private int getCurrentItem() {
        return ((LinearLayoutManager) spinner.getSelectedItem())
                .findFirstVisibleItemPosition();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {


            uri = data.getData();

            try {

                CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).start(this);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                userImage.setImageBitmap(bitmap);
                part = sendImageFileToserver(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            Uri imageURI = result.getUri();
            Log.d("uri", imageURI.toString());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                userImage.setImageBitmap(bitmap);
                part = sendImageFileToserver(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, requestCode);

            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");

            //CropImage.activity(uri).setAspectRatio(1,1).start(getActivity());
            userImage.setImageBitmap(bitmap);
            encodeTobase64(bitmap);

            try {
                part = sendImageFileToserver(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //Get Image from Url
    public class getImagefromURL extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        public getImagefromURL(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... url) {

            String urlimage = url[0];

            bitmap = null;
            try {
                InputStream stream = new URL(urlimage).openStream();
                bitmap = BitmapFactory.decodeStream(stream);
                part = sendImageFileToserver(bitmap);
                progressDialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }

    public MultipartBody.Part sendImageFileToserver(Bitmap bitMap) throws IOException {

        File filesDir = getFilesDir();
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


    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NormalPublishProfile.this);
        builder.setTitle("Select Images");
        builder.setCancelable(false);

        builder.setItems(dialogOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Camera".equals(dialogOptions[which])) {

                    if (ContextCompat.checkSelfPermission(NormalPublishProfile.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(NormalPublishProfile.this, Manifest.permission.CAMERA)) {

                            ActivityCompat.requestPermissions(NormalPublishProfile.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        } else {
                            ActivityCompat.requestPermissions(NormalPublishProfile.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        }

                    } else {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }

                } else if ("Gallery".equals(dialogOptions[which])) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);

                } else if ("Cancel".equals(dialogOptions[which])) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }

}
