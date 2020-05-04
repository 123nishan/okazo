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
import android.widget.EditText;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private TextView textViewClear,textViewError;
    private ApiInterface apiInterface;
    private ArrayList<String> arrayListName=new ArrayList<>(),arrayListEmail=new ArrayList<>(),arrayListImage=new ArrayList<>(),arrayListId=new ArrayList<>();
    private AddModeratorAdapter adapter;
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
        adapter=new AddModeratorAdapter(arrayListName,arrayListEmail,arrayListImage,getActivity().getApplicationContext(),arrayListId);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnButtonClickListener(new AddModeratorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int position) {

            }
        });
        textViewClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayListEmail.clear();
                arrayListName.clear();
                arrayListId.clear();
                arrayListImage.clear();
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.GONE);
                editTextSearch.clearFocus();
                editTextSearch.setText("");
                editTextSearch.setHint("Search");
                textViewError.setVisibility(View.VISIBLE);
            }
        });
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>3){
                    apiInterface.getAllUser(editable).enqueue(new Callback<APIResponse>() {
                        @Override
                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                            APIResponse apiResponse=response.body();
                            if(!apiResponse.getError()){
                                arrayListEmail.clear();
                                arrayListName.clear();
                                arrayListId.clear();
                                arrayListImage.clear();
                                ArrayList<User> users=apiResponse.getUserArray();
                                for (User val:users
                                ) {
                                    arrayListImage.add(val.getImage());
                                    arrayListName.add(val.getName());
                                    arrayListEmail.add(val.getEmail());
                                    arrayListId.add(val.getId());

                                   // Toast.makeText(getActivity().getApplicationContext(),val.getEmail() , Toast.LENGTH_SHORT).show();
                                }
                                recyclerView.setVisibility(View.VISIBLE);
                                textViewError.setVisibility(View.GONE);


                               // recyclerView.scrollToPosition(arrayListEmail.size()-1);
                                adapter.notifyDataSetChanged();
                            }else {
                                arrayListEmail.clear();
                                arrayListName.clear();
                                arrayListId.clear();
                                arrayListImage.clear();
                                adapter.notifyDataSetChanged();
                                recyclerView.setVisibility(View.GONE);
                                textViewError.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity().getApplicationContext(),"No user" , Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<APIResponse> call, Throwable t) {
                            Toast.makeText(getActivity().getApplicationContext(),t.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    arrayListEmail.clear();
                    arrayListName.clear();
                    arrayListId.clear();
                    arrayListImage.clear();
                    adapter.notifyDataSetChanged(); recyclerView.setVisibility(View.GONE);
                    textViewError.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

}
