package com.example.okazo.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.EventActivity;
import com.example.okazo.EventDetailPreviewActivity;
import com.example.okazo.GeoFenceActivity;
import com.example.okazo.LoginActivity;
import com.example.okazo.MainActivity;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.R;
import com.example.okazo.RegisterActivity;
import com.example.okazo.TicketDetailActivity;
import com.example.okazo.eventDetail;
import com.example.okazo.util.constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.okazo.util.constants.KEY_EVENT_DETAIL;
import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;
import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;
import static com.example.okazo.util.constants.KEY_USER_ID;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
    private AppBarLayout appBarLayout;
    private  Button buttonLogOut,buttonAddEvent,buttonTicket;
    private  AppCompatImageButton imageButtonAdd;
    private  CollapsingToolbarLayout collapsingToolbarLayout;
    private CircleImageView circleImageView;
    private TextView textViewFirst,textViewSecond,textViewThird;
    private ApiInterface apiInterface;
    private String userId;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RelativeLayout relativeLayoutExtented;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        buttonLogOut=view.findViewById(R.id.home_fragment_log_out);
        appBarLayout= view.findViewById(R.id.fragment_home_app_bar);
        imageButtonAdd= view.findViewById(R.id.fragment_home_add);
        circleImageView=view.findViewById(R.id.home_fragment_circular_Image);
        textViewFirst=view.findViewById(R.id.home_fragment_first_textview);
        textViewSecond=view.findViewById(R.id.home_fragment_second_textview);
        textViewThird=view.findViewById(R.id.home_fragment_third_textview);
        relativeLayoutExtented=view.findViewById(R.id.home_fragment_extented_relative_layout);
        tabLayout=view.findViewById(R.id.home_fragment_tab_layout);
        viewPager=view.findViewById(R.id.home_fragment_view_pager);
        collapsingToolbarLayout=view.findViewById(R.id.fragment_home_collapsing_tool_bar);

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        MainActivity mainActivity= (MainActivity) this.getActivity();
//        ActionBar bar=mainActivity.getSupportActionBar();
//        bar.hide();
       // ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        if(sharedPreferences.getString("user_id","")!=null  && !sharedPreferences.getString("user_id","").isEmpty()){





            HomeFragment.ViewPagerAdapter viewPagerAdapter=new HomeFragment.ViewPagerAdapter(getChildFragmentManager());
            viewPagerAdapter.addFragment(new FeedFragment() ,"Feed");
            viewPagerAdapter.addFragment(new ProfileFragment(),"Events");
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);

            userId=sharedPreferences.getString("user_id","");

            String request="home";
            //api for user name

            apiInterface.getUserName(userId,request).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){
                                textViewSecond.setText((apiResponse.getUser().getName().toUpperCase()));
                    }else {
                       DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse.getErrorMsg()).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("user_email");
                        editor.remove("user_id");
                        editor.commit();
//                        editor.putString("user_email","");
//                        editor.putString("user_id","");
                        Intent intent=new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });
            //api for event name
            apiInterface.checkEvent(userId).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){
                        EventDetail eventDetail=apiResponse.getEvent();
                        textViewFirst.setVisibility(View.VISIBLE);
                        textViewThird.setVisibility(View.GONE);
                        textViewFirst.setText(apiResponse.getEvent().getTitle().toUpperCase());
                        String imagePath=KEY_IMAGE_ADDRESS+(apiResponse.getEvent().getImage());
                        Glide.with(getActivity().getApplicationContext())
                                .load(Uri.parse(imagePath))
                                .placeholder(R.drawable.ic_place_holder_background)
                                //.error(R.drawable.ic_image_not_found_background)
                                .centerCrop()
                                .into(circleImageView);
                        textViewFirst.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                apiInterface.primaryEventInfo(eventDetail.getId(),userId).enqueue(new Callback<APIResponse>() {
                                    @Override
                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                        APIResponse apiResponse1=response.body();
                                        if(!apiResponse1.getError()){
                                            Intent intent=new Intent(getActivity().getApplicationContext(), EventActivity.class);
                                            intent.putExtra(KEY_EVENT_DETAIL,apiResponse1.getEvent());
                                            intent.putExtra(KEY_USER_ID,userId);
                                            startActivity(intent);
                                        }else {
                                            DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse1.getErrorMsg()).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {
                                        DynamicToast.makeError(getActivity().getApplicationContext(),t.getLocalizedMessage()).show();
                                    }
                                });


                            }
                        });

                    }else {
                        textViewFirst.setVisibility(View.GONE);
                        textViewThird.setVisibility(View.VISIBLE);
                        Glide.with(getActivity().getApplicationContext())
                                .load(R.mipmap.ic_okazo_logo_round)
                                .placeholder(R.drawable.ic_place_holder_background)
                                //.error(R.drawable.ic_image_not_found_background)
                                .centerCrop()
                                .into(circleImageView);
                        if(apiResponse.getErrorMsg().equals("NO EVENT")){
                                textViewThird.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(getActivity().getApplicationContext(), eventDetail.class);
                                        //Intent intent=new Intent(getActivity().getApplicationContext(), EventDetailPreviewActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        }else {
                            DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse.getErrorMsg()).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });
        }else {
            DynamicToast.makeError(getActivity().getApplicationContext(),"Something went wrong").show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("user_email","");
//            editor.putString("user_id","");
            editor.remove("user_email");
            editor.remove("user_id");
            editor.commit();
            Intent intent=new Intent(getActivity().getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }



//        buttonTicket=view.findViewById(R.id.ticket);
//        buttonTicket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity().getApplicationContext(), TicketDetailActivity.class);
//                startActivity(intent);
//            }
//        });

        //buttonAddEvent=view.findViewById(R.id.add_event);
//        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity().getApplicationContext(), eventDetail.class);
//                //Intent intent=new Intent(getActivity().getApplicationContext(), EventDetailPreviewActivity.class);
//                startActivity(intent);
//            }
//        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange=-1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if(scrollRange==-1){
                    scrollRange=appBarLayout.getTotalScrollRange();
                }

                if(scrollRange+i==0){

                    imageButtonAdd.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle("Okazo");
                    relativeLayoutExtented.setVisibility(View.GONE);

                }else {
                    //fully expanded
                    imageButtonAdd.setVisibility(View.GONE);
                    relativeLayoutExtented.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle("");

                }
            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicToast.makeWarning(getActivity(),"Logging Out").show();
                SharedPreferences pref = getActivity().getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("user_email");
                editor.remove("user_id");
                editor.commit();

                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });
        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

         // Intent intent=new Intent(getActivity().getApplicationContext(), GeoFenceActivity.class);
          Intent intent=new Intent(getActivity().getApplicationContext(), eventDetail.class);
            startActivity(intent);
            }
        });
        return view;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter

    {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
