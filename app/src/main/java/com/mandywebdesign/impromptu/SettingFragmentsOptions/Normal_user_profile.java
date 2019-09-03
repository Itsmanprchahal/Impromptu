package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Adapters.Normal_user_profile_QA_adapter;
import com.mandywebdesign.impromptu.Adapters.QuestionAdapter;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Home;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Models.PojoQuestion;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.NormalGetProfile;
import com.mandywebdesign.impromptu.Retrofit.NormalPublishProfile;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
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

import static android.app.Activity.RESULT_OK;


public class Normal_user_profile extends Fragment {

    public static int RESULT_LOAD_IMAGE = 1001;
    public static int REQUEST_CAMERA = 1002;
    public static int MY_PERMISSIONS_REQUEST_CAMERA = 1255;
    public  EditText username, about, age;
    public static TextView addQUES;
    TextView normal_user_gender;
    public static Spinner spinner;
    String getGender;
    RoundedImageView userImage;
    RadioButton male,female;
    RadioGroup radioGroup;
    EditText editText;
    SharedPreferences sharedPreferences;
    RecyclerView questionRecycler;
    public static String Question, answer;
    View view;
    static Bitmap bitmap;
    public  static   String Socai_user, userimage, user_id, userName_, age_, about_;
    static String getUsername="",getUserStatus="",getUserAge="";
    Button publish_profle;
    public static String userToken,profileStstus;
    static Uri uri;
    String Gender;
    String[] dialogOptions = new String[]{"Camera", "Gallery", "Cancel"};
    FragmentManager manager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static MultipartBody.Part part;
    ProgressDialog progressDialog;
    public static   ArrayList<String> QUES = new ArrayList<>();
    public static   ArrayList<String> QUES_Update = new ArrayList<>();
    public static ArrayList<String> ANSWER = new ArrayList<>();
    public static ArrayList<String> ANSWER_Update = new ArrayList<>();
    public static ArrayList<String> QA_id = new ArrayList<>();
    public  ArrayList<String> empty_QA_id = new ArrayList<>();
    List<PojoQuestion> pojoQuestions = new ArrayList<>();
    QuestionAdapter questionAdapter;
    private Normal_user_profile_QA_adapter.OnItemClickListener itemClickListener;
    public static List QuesAnswer = new ArrayList<>();
    public static String[] arryString = new String[]{"Most favourite place in your city?",
            "If you had to eat at one restaurant, which would it be?", "What is your favourite concert venue?",
            "The best gig you have been to?", "Your favourite nightspot in your city?",
            "The best art gallery near you?", "Best market in your city?", "Best road trip taken from your home?"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_normal_user_profile, container, false);
        manager = getFragmentManager();

        progressDialog = new ProgressDialog(getContext());
        Drawable drawable = new ProgressBar(getContext()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(drawable);
        progressDialog.show();

        preferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = preferences.getString("Socailtoken", "");
        user_id = preferences.getString("SocailuserId", "");

        Toolbar toolbar = view.findViewById(R.id.normal_user_profile_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.getString("backongendercheck", "");
                if (bundle!=null)
                {
                    FragmentTransaction transaction2 = manager.beginTransaction();
                    transaction2.replace(R.id.home_frame_layout, new Home());
                    transaction2.commit();
                }else {
                    getActivity().onBackPressed();
                }
            }
        });

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            profileStstus = bundle.getString("normal_edit");
        }

        init();
        listeners();

        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        Socai_user = sharedPreferences.getString("Social_username", "");
        userimage = sharedPreferences.getString("Social_image", "");
        addQUES.setText(R.string.userProfileQuestion);
        addQUES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        if (profileStstus.equals("1"))
        {
            Call<NormalGetProfile> call = WebAPI.getInstance().getApi().normalGetPRofile("Bearer " + userToken, "application/json");
            call.enqueue(new Callback<NormalGetProfile>() {
                @Override
                public void onResponse(Call<NormalGetProfile> call, Response<NormalGetProfile> response) {

                    if (response.body()!=null)
                    {
                        if (response.body().getStatus().equals("200")) {

                            progressDialog.dismiss();

                            getUsername = response.body().getData().get(0).getUsername();
                            getUserAge = response.body().getData().get(0).getAge();
                            getUserStatus = response.body().getData().get(0).getStatus().toString();
                            getGender = response.body().getData().get(0).getGender();

                            if (getGender.equals("Male"))
                            {
                                male.setChecked(true);
                            }else if (getGender.equals("Female"))
                            {
                                female.setChecked(true);
                            }else {
                                male.setChecked(false);
                                female.setChecked(false);
                            }

                            username.setText(getUsername);
                            Glide.with(getContext()).load(response.body().getData().get(0).getImage()).into(userImage);
                            new getImagefromURL(userImage).execute(UserProfileFragment.getNormalUserImage);
                            age.setText(getUserAge);
                            about.setText(getUserStatus);
                            NormalGetProfile normalGetProfile = response.body();
                            List<NormalGetProfile.Question> datum = normalGetProfile.getData().get(0).getQuestion();

                            QUES.clear();
                            ANSWER.clear();
                            QA_id.clear();
                            for (NormalGetProfile.Question question : datum) {

                                QUES.add(question.getQuestion());
                                ANSWER.add(question.getAnswer());
                                QA_id.add(question.getQuestionId().toString());



                                Log.d("141414",""+QUES.toString());

                                if (QUES.size()>=3)
                                {
                                    addQUES.setVisibility(View.INVISIBLE);
                                }else if (QUES.size()<=2)
                                {
                                    addQUES.setVisibility(View.VISIBLE);

                                }
                            }

                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            questionRecycler.setLayoutManager(layoutManager);

                            Normal_user_profile_QA_adapter adapter = new Normal_user_profile_QA_adapter(getContext(),QUES,ANSWER,arryString,userToken);
                            questionRecycler.setAdapter(adapter);

                        }
                    }else {
                        Intent intent = new Intent(getContext(), NoInternetScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }
                @Override
                public void onFailure(Call<NormalGetProfile> call, Throwable t) {
                    if (NoInternet.isOnline(getContext())==false)
                    {
                        progressDialog.dismiss();

                        NoInternet.dialog(getContext());
                    }
                }
            });
        }else {
            username.setText(Socai_user);
            Glide.with(getContext()).load(userimage).into(userImage);
            new getImagefromURL(userImage).execute(userimage);
            progressDialog.dismiss();
        }

        return view;
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
                switch (i)
                {
                    case R.id.normal_user_male:
                        Gender = "Male";
                        break;

                    case R.id.normal_user_female:
                        Gender = "Female";
                        break;

                }
            }
        });

        publish_profle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(age.getText().toString()))
                {
                    age.setError("Enter Age");
                }else
                {
                    if (Gender==(null))
                    {
                        Toast.makeText(getContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                    }else if (part==null)
                    {
                        Toast.makeText(getContext(), "Add Image", Toast.LENGTH_SHORT).show();
                    }else if (TextUtils.isEmpty(about.getText().toString()))
                    {
                        Toast.makeText(getContext(), "Add Description", Toast.LENGTH_SHORT).show();
                    }else if (TextUtils.isEmpty(username.getText().toString()))
                    {
                        username.setError("Enter Username");
                    }
                    else
                    {
                            if (profileStstus.equalsIgnoreCase("0"))
                            {
                                userName_ = username.getText().toString();
                                age_ = age.getText().toString();
                                about_ = about.getText().toString();

                                progressDialog.setMessage("Please wait until we publish your profile");
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();


                                Call<NormalPublishProfile> call = WebAPI.getInstance().getApi().normalPublishProfile("Bearer " + userToken, user_id,Gender, userName_, age_, about_, QUES, ANSWER, empty_QA_id, part);
                                call.enqueue(new Callback<NormalPublishProfile>() {
                                    @Override
                                    public void onResponse(Call<NormalPublishProfile> call, Response<NormalPublishProfile> response) {
                                        progressDialog.dismiss();
                                        if (response.body()!=null)
                                        {
                                            if (response.body().getStatus().equals("200"))
                                            {
                                                Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                                editor =  sharedPreferences.edit();
                                                editor.putString("profilegender",Gender);
                                                editor.apply();
                                                manager.beginTransaction().replace(R.id.home_frame_layout,new UserProfileFragment()).addToBackStack(null).commit();
                                            }

                                        }else {
                                            Intent intent = new Intent(getContext(), NoInternetScreen.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<NormalPublishProfile> call, Throwable t) {
                                        if (NoInternet.isOnline(getContext())==false)
                                        {
                                            progressDialog.dismiss();

                                            NoInternet.dialog(getContext());
                                        }
                                    }
                                });

                                Log.d("147258369", "" + QuesAnswer);
                            }else if (profileStstus.equalsIgnoreCase("1"))
                            {
                                userName_ = username.getText().toString();
                                age_ = age.getText().toString();
                                about_ = about.getText().toString();

                                progressDialog.setMessage("Please wait until we publish your profile");
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();


                                Call<NormalPublishProfile> call = WebAPI.getInstance().getApi().normalPublishProfile("Bearer " + userToken, user_id,Gender, userName_, age_, about_, QUES, ANSWER, QA_id, part);
                                call.enqueue(new Callback<NormalPublishProfile>() {
                                    @Override
                                    public void onResponse(Call<NormalPublishProfile> call, Response<NormalPublishProfile> response) {
                                        if (response.body()!=null)
                                        {progressDialog.dismiss();
                                            if (response.body().getStatus().equals("200"))
                                            {
                                                editor =  sharedPreferences.edit();
                                                editor.putString("profilegender",Gender);
                                                editor.apply();
                                                Toast.makeText(getContext(), "Profile Updated ", Toast.LENGTH_SHORT).show();
                                                manager.beginTransaction().replace(R.id.home_frame_layout,new UserProfileFragment()).addToBackStack(null).commit();
                                            }


                                        }else {
                                            Intent intent = new Intent(getContext(), NoInternetScreen.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<NormalPublishProfile> call, Throwable t) {
                                        if (NoInternet.isOnline(getContext())==false)
                                        {
                                            progressDialog.dismiss();

                                            NoInternet.dialog(getContext());
                                        }
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), ""+t, Toast.LENGTH_SHORT).show();
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

        addQUES = (TextView) view.findViewById(R.id.normal_user_profile_question);
        about = (EditText) view.findViewById(R.id.normal_user_about);
        questionRecycler = (RecyclerView) view.findViewById(R.id.normal_user_profile_question_recycle);
        username = (EditText) view.findViewById(R.id.normal_user_profile_username);
        userImage = (RoundedImageView) view.findViewById(R.id.normal_user_profile_userimage);
        age = (EditText) view.findViewById(R.id.normal_user_profile_age);
        publish_profle = (Button) view.findViewById(R.id.normal_published_bt);
        radioGroup = (RadioGroup)view.findViewById(R.id.gendergroup);
        male = (RadioButton)view.findViewById(R.id.normal_user_male);
        female = (RadioButton)view.findViewById(R.id.normal_user_female);
    }

    public void dialog() {

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.queston_dilog_question);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCanceledOnTouchOutside(false);


        spinner = (Spinner) dialog.findViewById(R.id.user_profile_sppiner);
        editText = (EditText) dialog.findViewById(R.id.user_profile_answer);
        ImageView close =  dialog.findViewById(R.id.close_questtionDialog);

        final List<String> stringList = new ArrayList<String>(Arrays.asList(arryString));
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        Button dialogButton = (Button) dialog.findViewById(R.id.user_profile_button);
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
                    Log.d("123456789", "mnmnm " + QUES.toString() + "\n" + ANSWER.toString());
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    questionRecycler.setLayoutManager(layoutManager);
                    questionRecycler.setNestedScrollingEnabled(false);

                    if (QUES.size() >= 3) {
                        addQUES.setVisibility(View.INVISIBLE);
                    } else {
                        addQUES.setVisibility(View.VISIBLE);
                    }
                    final PojoQuestion pojoQuestion = new PojoQuestion(Question, answer);
                    pojoQuestions.add(pojoQuestion);

                    // Log.d("update","141 "+Question+"   "+answer);

                    questionAdapter = new QuestionAdapter(getActivity(), pojoQuestions,arryString);
                    questionRecycler.setAdapter(questionAdapter);
                    questionAdapter.notifyDataSetChanged();

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

                CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).start(getContext(), this);
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
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
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageURI);
                userImage.setImageBitmap(bitmap);
                part = sendImageFileToserver(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, requestCode);

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

        File filesDir = getContext().getFilesDir();
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


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Images");
        builder.setCancelable(false);

        builder.setItems(dialogOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Camera".equals(dialogOptions[which])) {

                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
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