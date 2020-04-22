package com.example.okazo.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.LoginActivity;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.Model.Posts;
import com.example.okazo.R;
import com.example.okazo.util.FeedAdapter;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    public FeedFragment() {
        // Required empty public constructor
    }
private RecyclerView recyclerView;
    private ApiInterface apiInterface;
        private String userId;
        private TextView textViewTest;
        private ArrayList<String> arrayListEventTitle=new ArrayList<>(),arrayListProfileImage=new ArrayList<>(),arrayListDetail=new ArrayList<>(),arrayListCreatedDate=new ArrayList<>();
        private FeedAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_feed, container, false);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        recyclerView=view.findViewById(R.id.feed_fragment_recyler_view);
        textViewTest=view.findViewById(R.id.feed_fragment_text_view);
        //textViewTest=view.findViewById(R.id.card_feed_text);
        //apiInterface.getFeed()
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences.getString("user_id","")!=null  && !sharedPreferences.getString("user_id","").isEmpty()){
            userId=sharedPreferences.getString("user_id","");
            apiInterface.getFeed(userId).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                   APIResponse apiResponse=response.body();
                   if(!apiResponse.getError()){
                       recyclerView.setVisibility(View.VISIBLE);
                       textViewTest.setVisibility(View.GONE);
                      // Log.d("checkHere","null "+apiResponse.getEventArray().get(0).getTitle());
                       ArrayList<Posts> eventDetails=apiResponse.getPostArray();
                       for (Posts value:eventDetails
                            ) {

                            arrayListEventTitle.add(value.getTitle());
                            arrayListCreatedDate.add(value.getCreatedDate());
                            arrayListDetail.add(value.getDetail());
                            arrayListProfileImage.add(value.getProfileImage());

                       }
                     //  Log.d("title",arrayListEventTitle.size()+"=="+apiResponse.getEventArray().size());
                       adapter=new FeedAdapter(arrayListEventTitle,arrayListProfileImage,arrayListDetail,arrayListCreatedDate,getActivity().getApplicationContext());
                       LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                       linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                       recyclerView.setLayoutManager(linearLayoutManager);
                       recyclerView.setAdapter(adapter);
                   }else if(apiResponse.getErrorMsg().equals("NO FEED")) {
                       recyclerView.setVisibility(View.GONE);
                       textViewTest.setVisibility(View.VISIBLE);
                       textViewTest.setText("No feed to show");
                   }else {
                       DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse.getErrorMsg()).show();
                   }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });
        }else {
            DynamicToast.makeError(getActivity().getApplicationContext(),"Something went wrong").show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_email","");
            editor.putString("user_id","");
            Intent intent=new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        return view;
    }
}
