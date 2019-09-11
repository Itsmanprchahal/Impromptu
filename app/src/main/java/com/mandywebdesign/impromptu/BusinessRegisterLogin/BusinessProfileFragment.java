package com.mandywebdesign.impromptu.BusinessRegisterLogin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;


public class BusinessProfileFragment extends Fragment {

    public static int RESULT_LOAD_IMAGE = 101;
    public static int GALLERY_REQ = 1010;
    public static int REQUEST_CAMERA = 102;
    public static int MY_PERMISSIONS_REQUEST_CAMERA = 1242;
    Button mNext;
    View view;
    public static EditText business_profile_Name_ET, addressline1ET, addressline2ET, postcode_business_ET, city_business_ET, aboutyouroragnisation_ET;
    RoundedImageView business_user_Image;
    String[] dialogOptions = new String[]{"Camera", "Gallery", "Cancel"};
    Bitmap bitmap;
    String name, address1, address2, postcode, city, aboutOrga, image1;
    String galleyImagePath;
    ImageView close;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FragmentManager manager;
    Dialog progressDialog;
    public static MultipartBody.Part part;
    Uri uri;
    File filesDir;

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_business_profile, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //Glide.with(getContext()).load(BusinessUserProfile.avatar).into(business_user_Image);
        progressDialog = ProgressBarClass.showProgressDialog(getContext());
        progressDialog.show();
        manager = getFragmentManager();
        filesDir = getActivity().getFilesDir();

        init();
        listeners();

        Bundle bundle = getArguments();

        if (bundle != null) {
            String value = bundle.getString("value");

            if (value.equals("1")) {
                business_profile_Name_ET.setText(BusinessUserProfile.userName);
                addressline1ET.setText(BusinessUserProfile.address1);
                addressline2ET.setText(BusinessUserProfile.address2);
                postcode_business_ET.setText(BusinessUserProfile.postcode);
                city_business_ET.setText(BusinessUserProfile.city);
                aboutyouroragnisation_ET.setText(BusinessUserProfile.desc);
                Glide.with(getContext()).load(BusinessUserProfile.avatar).into(business_user_Image);
                new getImagefromURL(business_user_Image).execute(BusinessUserProfile.avatar);

                close.setVisibility(View.VISIBLE);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.home_frame_layout, new BusinessUserProfile());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });


            } else if (value.equals("back")) {
                business_profile_Name_ET.setText(BusinessUserProfile.userName);
                addressline1ET.setText(BusinessUserProfile.address1);
                addressline2ET.setText(BusinessUserProfile.address2);
                postcode_business_ET.setText(BusinessUserProfile.postcode);
                city_business_ET.setText(BusinessUserProfile.city);
                aboutyouroragnisation_ET.setText(BusinessUserProfile.desc);
                new getImagefromURL(business_user_Image).execute(BusinessUserProfile.avatar);
                Glide.with(getContext()).load(BusinessUserProfile.avatar).into(business_user_Image);
                close.setVisibility(View.VISIBLE);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.home_frame_layout, new BusinessUserProfile());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            }

        }

        return view;
    }

    private void listeners() {

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = business_profile_Name_ET.getText().toString();
                address1 = addressline1ET.getText().toString();
                address2 = addressline2ET.getText().toString();
                postcode = postcode_business_ET.getText().toString();
                city = city_business_ET.getText().toString();
                aboutOrga = aboutyouroragnisation_ET.getText().toString();

                if (name.isEmpty() && address1.isEmpty() && address2.isEmpty() && postcode.isEmpty()
                        && city.isEmpty() && aboutOrga.isEmpty()) {
                    business_profile_Name_ET.setError("Enter Name");
                    addressline1ET.setError("Enter Addressline 1");
                    addressline2ET.setError("Enter Addressline 2");
                    postcode_business_ET.setError("Enter Postcode");
                    city_business_ET.setError("Enter City");
                    aboutyouroragnisation_ET.setError("Enter About Organisation");
                } else if (name.isEmpty()) {
                    business_profile_Name_ET.setError("Enter Name");
                } else if (address1.isEmpty()) {
                    addressline1ET.setError("Enter Addressline 1");
                } else if (address2.isEmpty()) {
                    addressline2ET.setError("Enter Addressline 2");
                } else if (postcode.isEmpty()) {
                    postcode_business_ET.setError("Enter Postcode");
                } else if (city.isEmpty()) {
                    city_business_ET.setError("Enter City");
                } else if (aboutOrga.isEmpty()) {
                    aboutyouroragnisation_ET.setError("Enter About Organisation");
                } else {
                    sharedPreferences = getActivity().getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor = sharedPreferences.edit();
                    editor.putString("name", name);
                    editor.putString("address1", address1);
                    editor.putString("address2", address2);
                    editor.putString("postcode", postcode);
                    editor.putString("city", city);
                    editor.putString("about", aboutOrga);
                    if (bitmap == null) {
                        Toast.makeText(getContext(), "Add Image to Continue", Toast.LENGTH_SHORT).show();
                    } else {
                        editor.putString("image", encodeTobase64(bitmap));
                        editor.apply();

                        Bundle bundle = new Bundle();
                        String value = "2";
                        bundle.putString("value", value);

                        BusinessProfileFragment1 businessProfileFragment = new BusinessProfileFragment1();
                        businessProfileFragment.setArguments(bundle);

                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.home_frame_layout, businessProfileFragment);
                        transaction.commit();

                    }
                }
            }
        });

        business_user_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialg();
            }
        });


    }


    private void init() {

        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        mNext = view.findViewById(R.id.nextBT_onbusinessprofile);
        business_profile_Name_ET = view.findViewById(R.id.business_profile_Name);
        business_user_Image = view.findViewById(R.id.business_user_Image);
        addressline1ET = view.findViewById(R.id.addressline1ET);
        addressline2ET = view.findViewById(R.id.addressline2ET);
        postcode_business_ET = view.findViewById(R.id.postcode_business);
        city_business_ET = view.findViewById(R.id.city_business);
        close = view.findViewById(R.id.close_on_business_profile);
        aboutyouroragnisation_ET = view.findViewById(R.id.aboutyouroragnisation_ET);

        name = business_profile_Name_ET.getText().toString();
        address1 = addressline1ET.getText().toString();
        address2 = addressline2ET.getText().toString();
        postcode = postcode_business_ET.getText().toString();
        city = city_business_ET.getText().toString();
        aboutOrga = aboutyouroragnisation_ET.getText().toString();
    }

    private void showDialg() {

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

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
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

    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(getActivity());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {


            uri = data.getData();

            galleyImagePath = String.valueOf(uri);
            try {
                CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).start(getContext(), this);
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                business_user_Image.setImageBitmap(bitmap);
                part = sendImageFileToserver(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageURI = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageURI);
                    Glide.with(getContext()).load(imageURI).into(business_user_Image);
                    encodeTobase64(bitmap);
                    part = sendImageFileToserver(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, requestCode);

            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");

            business_user_Image.setImageBitmap(bitmap);
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
            Log.d("part", String.valueOf(bitmap));


            try {
                InputStream stream = new URL(urlimage).openStream();
                bitmap = BitmapFactory.decodeStream(stream);
                part = sendImageFileToserver(bitmap);
                Log.d("part1", String.valueOf(bitmap));

                    progressDialog.dismiss();



            } catch (IOException e) {
                e.printStackTrace();
            }


            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            imageView.setImageBitmap(bitmap);
        }
    }


    //convert image to multipart
    public MultipartBody.Part sendImageFileToserver(Bitmap bitMap) throws IOException {


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
}
