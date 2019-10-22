package com.mandywebdesign.impromptu.messages;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.mandywebdesign.impromptu.Adapters.MessageAdapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroAllChats;
import com.mandywebdesign.impromptu.Retrofit.SearchMessages;
import com.mandywebdesign.impromptu.Utils.Util;
import com.mandywebdesign.impromptu.ui.Join_us;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Messages extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    View view;
    RecyclerView recyclerView;
    AutoCompleteTextView message_search;
    MessageAdapter adapter;
    Toolbar toolbar;
    SharedPreferences sharedPreferences, sharedPreferences1, profileupdatedPref;
    FragmentManager manager;
    Dialog progressDialog;
    TextView nomessages;
    public static String BToken, S_Token;
    public static ArrayList<String> eventTitle = new ArrayList<>();
    public static ArrayList<String> eventImage = new ArrayList<>();
    public static ArrayList<String> eventTicketType = new ArrayList<>();
    public static ArrayList<String> eventID = new ArrayList<>();
    public static ArrayList<String> hostUserID = new ArrayList<>();
    public static ArrayList<String> MesgCount = new ArrayList<>();
    public static ArrayList<String> lastMEsg = new ArrayList<>();
    public static ArrayList<String> bookingstatus = new ArrayList<>();
    public static ArrayList<String> event_status = new ArrayList<>();
    public static ArrayList<Integer> rating_status = new ArrayList<>();
    public static ArrayList<String> lastmesgtime = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_messages, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        sharedPreferences = getContext().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");
        sharedPreferences1 = getActivity().getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        profileupdatedPref = getContext().getSharedPreferences("profileupdated", Context.MODE_PRIVATE);

        progressDialog = ProgressBarClass.showProgressDialog(getContext());
        progressDialog.dismiss();

        manager = getFragmentManager();

        init();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                if (!BToken.equals("")) {
                    getAllChats("Bearer " + BToken);
                } else if (!S_Token.equals("")) {
                    getAllChats("Bearer " + S_Token);
                }
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorTheme));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!BToken.equals("")) {
            getAllChats("Bearer " + BToken);
        } else if (!S_Token.equals("")) {
            getAllChats("Bearer " + S_Token);
        }
    }

    private void init() {
        recyclerView = view.findViewById(R.id.message_recycler);
        nomessages = view.findViewById(R.id.nomessages);
        toolbar = view.findViewById(R.id.toolbar);
        message_search = view.findViewById(R.id.message_search);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
    }


    private void getAllChats(final String token) {
        progressDialog.show();
        Call<RetroAllChats> call = WebAPI.getInstance().getApi().allChats(token, "application/json");
        call.enqueue(new Callback<RetroAllChats>() {
            @Override
            public void onResponse(Call<RetroAllChats> call, Response<RetroAllChats> response) {
                if (response.body()!=null) {
                    if (response.body().getStatus().equals("200")) {
                        RetroAllChats data = response.body();
                        List<RetroAllChats.Datum> datumList = data.getData();

                        clear();

                        for (RetroAllChats.Datum datum : datumList) {
                            eventTitle.add(datum.getTitle());
                            eventTicketType.add(datum.getTicketType());
                            eventImage.add(datum.getFile());
                            eventID.add(datum.getEventId().toString());
                            hostUserID.add(datum.getSenderId().toString());
                            MesgCount.add(datum.getCount().toString());
                            lastMEsg.add(datum.getLastMessageShow());
                            rating_status.add(datum.getRatingStatus());
                            bookingstatus.add(datum.getBookingStatus());
                            event_status.add(datum.getEventStatus());

                            lastmesgtime.add(datum.getLastMessageDatetime().toString());
                            setData();

                            Search(token);


                        }
                        progressDialog.dismiss();
                    } else if (response.body().getStatus().equals("401")) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.clear();
                        editor1.commit();
                        Toast.makeText(getContext(), "Business Logout", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor2 = profileupdatedPref.edit();
                        editor2.clear();
                        editor2.commit();

                        progressDialog.show();

                        Intent intent = new Intent(getActivity(), Join_us.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    } else {
                        progressDialog.dismiss();
                        nomessages.setVisibility(View.VISIBLE);
                    }

                }else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RetroAllChats> call, Throwable t) {

                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                if (NoInternet.isOnline(getContext()) == false) {
                    progressDialog.dismiss();

                    NoInternet.dialog(getContext());
                }
            }
        });
    }

    private void clear() {
        lastmesgtime.clear();
        eventImage.clear();
        eventTicketType.clear();
        eventTitle.clear();
        eventID.clear();
        hostUserID.clear();
        MesgCount.clear();
        lastMEsg.clear();
    }

    @SuppressLint("WrongConstant")
    private void setData() {
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayout);
        adapter = new MessageAdapter(manager, getContext(), eventTitle,S_Token);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void Search(final String token) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, eventTitle);
        message_search.setAdapter(arrayAdapter);

        message_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String message = arrayAdapter.getItem(position);

                getChat(token,message);
            }
        });

        message_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()==0)
                {
                   getChat(token,"");
                }
            }
        });
    }

    private void getChat(String token, String message) {
        Call<SearchMessages> call = WebAPI.getInstance().getApi().search(token, message);
        call.enqueue(new Callback<SearchMessages>() {
            @Override
            public void onResponse(Call<SearchMessages> call, Response<SearchMessages> response) {

                if (response.body()!=null)
                {
                    clear();
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        eventTitle.add(response.body().getData().get(i).getTitle());
                        eventTicketType.add(response.body().getData().get(i).getTicketType());
                        eventImage.add(response.body().getData().get(i).getFile());
                        eventID.add(response.body().getData().get(i).getEventId().toString());
                        hostUserID.add(response.body().getData().get(i).getSenderId().toString());
                        lastMEsg.add(response.body().getData().get(i).getLastMessageShow());
                        MesgCount.add(response.body().getData().get(i).getCount().toString());
                        lastmesgtime.add(response.body().getData().get(i).getLastMessageDatetime().toString());
                        setData();
                    }
                }else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<SearchMessages> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
