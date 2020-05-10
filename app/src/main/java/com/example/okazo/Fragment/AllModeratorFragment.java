package com.example.okazo.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.LoginActivity;
import com.example.okazo.Model.User;
import com.example.okazo.R;
import com.example.okazo.util.AllModeratorAdapter;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.okazo.util.constants.KEY_EVENT_ID;
import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;
import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;
import static com.example.okazo.util.constants.KEY_USER_ROLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllModeratorFragment extends Fragment {

    public AllModeratorFragment() {
        // Required empty public constructor
    }
    private CircleImageView circleImageView;
    private TextView textViewName,textViewEmail,textViewEditorError,textViewModeratorError;
    private RecyclerView recyclerViewEditor, recyclerViewModerator;
    private ApiInterface apiInterface;
    private String eventId,userId,userName,moderatorType;
    private ArrayList<String> arrayListEditorName=new ArrayList<>(),arrayListEditorEmail=new ArrayList<>(),
            arrayListEditorModeratorId=new ArrayList<>(),arrayListEditorImage=new ArrayList<>(),arrayListEditorStatus=new ArrayList<>();
    private ArrayList<String> arrayListModeratorName=new ArrayList<>(),arrayListModeratorEmail=new ArrayList<>()
            ,arrayListModeratorModeratorId=new ArrayList<>(),arrayListModeratorImage=new ArrayList<>(),arrayListModeratorStatus=new ArrayList<>();
    private AllModeratorAdapter adapterEditor,adapterModerator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_all_moderator, container, false);
    //

        textViewEmail=v.findViewById(R.id.all_moderator_fragment_admin_email);
        textViewName=v.findViewById(R.id.all_moderator_fragment_admin_name);
        circleImageView=v.findViewById(R.id.all_moderator_fragment_admin_image);
        recyclerViewEditor=v.findViewById(R.id.all_moderator_fragment_editor_recycler_view);
        recyclerViewModerator=v.findViewById(R.id.all_moderator_fragment_moderator_recycler_view);
        textViewEditorError=v.findViewById(R.id.all_moderator_fragment_editor_error);
        textViewModeratorError=v.findViewById(R.id.all_moderator_fragment_moderator_error);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        Bundle bundle=getActivity().getIntent().getExtras();
        eventId=bundle.getString(KEY_EVENT_ID);
        moderatorType=bundle.getString(KEY_USER_ROLE);
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences.getString("user_id","")!=null  && !sharedPreferences.getString("user_id","").isEmpty()){
            userId=sharedPreferences.getString("user_id","");
            apiInterface.currentUserInfo(userId).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){
                        User user=apiResponse.getUser();
                        userName=user.getName();

                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });
        }else {
            DynamicToast.makeError(getActivity().getApplicationContext(),"Something went wrong").show();
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.remove("user_email");
            editor.remove("user_id");
            editor.commit();
            Intent intent=new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        adapterEditor=new AllModeratorAdapter(arrayListEditorName,arrayListEditorEmail,arrayListEditorImage,arrayListEditorModeratorId,getActivity().getApplicationContext(),moderatorType,arrayListEditorStatus);
        adapterModerator=new AllModeratorAdapter(arrayListModeratorName,arrayListModeratorEmail,arrayListModeratorImage,arrayListModeratorModeratorId,getActivity().getApplicationContext(),moderatorType,arrayListModeratorStatus);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewEditor.setLayoutManager(linearLayoutManager);
        recyclerViewModerator.setLayoutManager(linearLayoutManager1);
        adapterEditor.setOnRemoveClickListener(new AllModeratorAdapter.OnRemoveClickListener() {
            @Override
            public void onRemoveClick(int position) {
                //Toast.makeText(getActivity().getApplicationContext(), position, Toast.LENGTH_SHORT).show();
                apiInterface.removeModerator(eventId,arrayListEditorModeratorId.get(position)).enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        APIResponse apiResponse=response.body();
                        if(!apiResponse.getError()){
                                DynamicToast.makeSuccess(getActivity().getApplicationContext(),"Removed"+" "+arrayListEditorName.get(position)).show();
                                arrayListEditorName.remove(position);
                                arrayListEditorStatus.remove(position);
                                arrayListEditorModeratorId.remove(position);
                                arrayListEditorImage.remove(position);
                                arrayListEditorEmail.remove(position);
                               adapterEditor.notifyDataSetChanged();
                               if(arrayListEditorEmail.size()>0){

                               }else {
                                   recyclerViewEditor.setVisibility(View.GONE);
                                   textViewEditorError.setVisibility(View.VISIBLE);
                               }
                        }else {
                            DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse.getErrorMsg()).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        DynamicToast.makeError(getActivity().getApplicationContext(),t.getLocalizedMessage()).show();
                    }
                });
            }
        });
        adapterModerator.setOnRemoveClickListener(new AllModeratorAdapter.OnRemoveClickListener() {
            @Override
            public void onRemoveClick(int position) {
                String name=arrayListModeratorName.get(position);
                apiInterface.removeModerator(eventId,arrayListModeratorModeratorId.get(position)).enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        APIResponse apiResponse=response.body();
                        if(!apiResponse.getError()){
                            DynamicToast.makeSuccess(getActivity().getApplicationContext(),"Removed"+" "+name).show();
                            arrayListModeratorStatus.remove(position);
                            arrayListModeratorName.remove(position);
                            arrayListModeratorEmail.remove(position);
                            arrayListModeratorImage.remove(position);
                            arrayListModeratorModeratorId.remove(position);
                            adapterModerator.notifyDataSetChanged();
                            if(arrayListModeratorName.size()>0){

                            }else {
                                recyclerViewModerator.setVisibility(View.GONE);
                                textViewModeratorError.setVisibility(View.VISIBLE);
                            }
                        }else {
                            DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse.getErrorMsg()).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        DynamicToast.makeError(getActivity().getApplicationContext(),t.getLocalizedMessage()).show();
                    }
                });
              //  Toast.makeText(getActivity().getApplicationContext(), position, Toast.LENGTH_SHORT).show();
            }
        });

        //editor
        recyclerViewEditor.setAdapter(adapterEditor);

        getAllModerator("2");
        //moderator
        recyclerViewModerator.setAdapter(adapterModerator);
        getAllModerator("3");


      //  Toast.makeText(getActivity().getApplicationContext(), moderatorType, Toast.LENGTH_SHORT).show();

        apiInterface.getAllModerator(eventId,"1").enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    ArrayList<User> users=apiResponse.getUserArray();
                    for (User user:users
                         ) {
                        if(userName.equals(user.getName())){
                            textViewName.setText("You");
                        }else {
                            textViewName.setText(user.getName());
                        }
                        textViewEmail.setText(user.getEmail());

                        String imagePath=KEY_IMAGE_ADDRESS+(user.getImage());
                        Glide.with(getActivity().getApplicationContext())
                                .load(Uri.parse(imagePath))
                                .placeholder(R.drawable.ic_place_holder_background)
                                //.error(R.drawable.ic_image_not_found_background)
                                .centerCrop()
                                .into(circleImageView);

                    }


                }else {
                    DynamicToast.makeError(getActivity().getApplicationContext(),"ERROR GETTING DATA").show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                DynamicToast.makeError(getActivity().getApplicationContext(),t.getLocalizedMessage()).show();
            }
        });

        return v;
    }
    private void getAllModerator(String modType){
        apiInterface.getAllModerator(eventId,modType).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    ArrayList<User> users=apiResponse.getUserArray();
                    for (User user:users
                         ) {
                        if(modType.equals("2")){
                            arrayListEditorEmail.add(user.getEmail());
                            arrayListEditorImage.add(user.getImage());
                            arrayListEditorModeratorId.add(user.getModId());
                            arrayListEditorName.add(user.getName());
                            arrayListEditorStatus.add(user.getStatus());
                        }else {
                            arrayListModeratorEmail.add(user.getEmail());
                            arrayListModeratorImage.add(user.getImage());
                            arrayListModeratorModeratorId.add(user.getModId());
                            arrayListModeratorName.add(user.getName());
                            arrayListModeratorStatus.add(user.getStatus());
                        }
                    }
                    adapterEditor.notifyDataSetChanged();
                    adapterModerator.notifyDataSetChanged();

                }else {
                    if(modType.equals("2")){
                        recyclerViewEditor.setVisibility(View.GONE);
                        textViewEditorError.setVisibility(View.VISIBLE);
                    }else {
                        recyclerViewModerator.setVisibility(View.GONE);
                        textViewModeratorError.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible){
            getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
           // Toast.makeText(getActivity().getApplicationContext(), menuVisible+"", Toast.LENGTH_SHORT).show();
        }else {
           // Toast.makeText(getActivity().getApplicationContext(), menuVisible+"", Toast.LENGTH_SHORT).show();
        }
    }
}
