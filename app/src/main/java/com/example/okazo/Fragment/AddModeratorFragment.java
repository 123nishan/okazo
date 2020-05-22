package com.example.okazo.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.MessageActivity;
import com.example.okazo.Model.User;
import com.example.okazo.R;
import com.example.okazo.util.AddModeratorAdapter;

import com.example.okazo.util.MessageAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.IOException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_ID;
import static com.example.okazo.util.constants.KEY_USER_ROLE;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class AddModeratorFragment extends Fragment {

    public AddModeratorFragment() {
        // Required empty public constructor
    }

    private EditText editTextSearch;
    private RecyclerView recyclerView;
    private TextView textViewClear;
    private ImageView textViewError;
    private ApiInterface apiInterface;
    private ArrayList<String> arrayListName=new ArrayList<>(),arrayListEmail=new ArrayList<>(),arrayListImage=new ArrayList<>(),arrayListId=new ArrayList<>(),arrayListStatus=new ArrayList<>();
    private AddModeratorAdapter adapter;
    private String role;
    private String eventId,moderatorType;
    private GifDrawable gifChecking;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_moderator, container, false);
        editTextSearch=view.findViewById(R.id.add_moderator_fragment_search_edit_text);
        recyclerView=view.findViewById(R.id.add_moderator_recycler_view);
        textViewClear=view.findViewById(R.id.add_moderator_fragment_clear);
        textViewError=view.findViewById(R.id.add_moderator_fragment_error);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        recyclerView.setVisibility(View.VISIBLE);
        Bundle bundle=getActivity().getIntent().getExtras();
        eventId=bundle.getString(KEY_EVENT_ID);
        moderatorType=bundle.getString(KEY_USER_ROLE);
        try {
            gifChecking = new GifDrawable( getResources(), R.drawable.not_found );

        } catch (IOException e) {
            e.printStackTrace();
        }
        textViewError.setBackground(gifChecking);
        adapter=new AddModeratorAdapter(arrayListName,arrayListEmail,arrayListImage,getActivity().getApplicationContext(),arrayListId,arrayListStatus);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnButtonClickListener(new AddModeratorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int position) {

                View dialog=getLayoutInflater().inflate(R.layout.bottom_sheet_moderator_type,null);
                BottomSheetDialog sheetDialog=new BottomSheetDialog(getContext());
                sheetDialog.setContentView(dialog);
                sheetDialog.show();
                Button buttonEditor=dialog.findViewById(R.id.bottom_sheet_add_moderator_editor);
                Button buttonModerator=dialog.findViewById(R.id.bottom_sheet_add_moderator_moderator);
                buttonEditor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(dialog.getContext(), "Editor", Toast.LENGTH_SHORT).show();
                        role="Editor";
                        sheetDialog.hide();
                        addModerator(position);

                    }
                });
                buttonModerator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        role="Moderator";
                        Toast.makeText(dialog.getContext(), "Moderator", Toast.LENGTH_SHORT).show();
                        sheetDialog.hide();
                        addModerator(position);
                    }
                });
            }
        });
        textViewClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayListEmail.clear();
                arrayListName.clear();
                arrayListId.clear();
                arrayListImage.clear();
                arrayListStatus.clear();
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.GONE);
                editTextSearch.clearFocus();
                editTextSearch.setText("");
                editTextSearch.setHint("Search");
                textViewError.setVisibility(View.VISIBLE);
            }
        });
        if(moderatorType.equals("Admin") || moderatorType.equals("Editor")) {
            editTextSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length() > 3) {
                        apiInterface.getAllUser(editable, eventId).enqueue(new Callback<APIResponse>() {
                            @Override
                            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                APIResponse apiResponse = response.body();
                                if (!apiResponse.getError()) {
                                    arrayListEmail.clear();
                                    arrayListName.clear();
                                    arrayListId.clear();
                                    arrayListImage.clear();
                                    ArrayList<User> users = apiResponse.getUserArray();
                                    for (User val : users
                                    ) {
                                        arrayListImage.add(val.getImage());
                                        arrayListName.add(val.getName());
                                        arrayListEmail.add(val.getEmail());
                                        arrayListId.add(val.getId());
                                        arrayListStatus.add(val.getModStatus());

                                        // Toast.makeText(getActivity().getApplicationContext(),val.getEmail() , Toast.LENGTH_SHORT).show();
                                    }
                                    recyclerView.setVisibility(View.VISIBLE);
                                    textViewError.setVisibility(View.GONE);


                                    // recyclerView.scrollToPosition(arrayListEmail.size()-1);
                                    adapter.notifyDataSetChanged();
                                } else {

                                    arrayListEmail.clear();
                                    arrayListName.clear();
                                    arrayListId.clear();
                                    arrayListImage.clear();
                                    arrayListStatus.clear();
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setVisibility(View.GONE);
                                    textViewError.setVisibility(View.VISIBLE);
                                    // Toast.makeText(getActivity().getApplicationContext(),"No user" , Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<APIResponse> call, Throwable t) {
                                Toast.makeText(getActivity().getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        arrayListEmail.clear();
                        arrayListName.clear();
                        arrayListId.clear();
                        arrayListImage.clear();
                        arrayListStatus.clear();
                        adapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.GONE);
                        textViewError.setVisibility(View.VISIBLE);
                    }
                }
            });
        }else {
            editTextSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DynamicToast.makeError(getActivity().getApplicationContext(),"You dont have permission to add").show();
                }
            });
        }
        return view;
    }
    private void addModerator(int position){
        apiInterface.requestModerator(arrayListId.get(position),eventId,role).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    arrayListEmail.clear();
                    arrayListName.clear();
                    arrayListId.clear();
                    arrayListImage.clear();
                    arrayListStatus.clear();
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.GONE);
                    editTextSearch.clearFocus();
                    editTextSearch.setText("");
                    editTextSearch.setHint("Search");
                    textViewError.setVisibility(View.VISIBLE);
                    DynamicToast.makeSuccess(getActivity().getApplicationContext(), "Request Sent", Toast.LENGTH_SHORT).show();
                }else {
                    DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse.getErrorMsg()).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }

}
