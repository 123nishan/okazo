package com.example.okazo.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.EventActivity;
import com.example.okazo.EventDetailPreviewActivity;
import com.example.okazo.LoginActivity;
import com.example.okazo.Model.Note;
import com.example.okazo.Model.User;
import com.example.okazo.ModeratorActivity;
import com.example.okazo.ModeratorListActivity;
import com.example.okazo.MyTicketActivity;
import com.example.okazo.R;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;
import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;
import static com.example.okazo.util.constants.KEY_USER_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    private TextView textViewUserName,textViewTMoney,textViewFollowing,textViewModerator,textViewEmail,textViewPhone,textViewTicket,textViewLogout;
    private ImageView imageViewProfile;
    private LinearLayout linearLayoutModerator,linearLayoutFollowing;
    private ApiInterface apiInterface;
    private String userId,imagePath;
    private static final int CHOOSE_IMAGE = 505;
    private Uri uriProfileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_profile, container, false);
        textViewUserName=v.findViewById(R.id.profile_fragment_user_name);
        textViewTMoney=v.findViewById(R.id.profile_fragment_t_money);
        textViewFollowing=v.findViewById(R.id.profile_fragment_following);
        textViewModerator=v.findViewById(R.id.profile_fragment_moderator);
        textViewEmail=v.findViewById(R.id.profile_fragment_user_email);
        textViewPhone=v.findViewById(R.id.profile_fragment_user_phone);
        textViewTicket=v.findViewById(R.id.profile_fragment_ticket_detail);
        imageViewProfile=v.findViewById(R.id.profile_fragment_image);
        linearLayoutFollowing=v.findViewById(R.id.profile_fragment_following_list);
        linearLayoutModerator=v.findViewById(R.id.profile_fragment_moderator_list);
        textViewLogout=v.findViewById(R.id.profile_fragment_logout);

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences.getString("user_id","")!=null  && !sharedPreferences.getString("user_id","").isEmpty()){
            userId=sharedPreferences.getString("user_id","");
            apiInterface=ApiClient.getApiClient().create(ApiInterface.class);
            apiInterface.getProfileInfo(userId).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){
                        User user=apiResponse.getUser();
                        textViewEmail.setText(user.getEmail());
                        textViewUserName.setText(user.getName().toUpperCase());
                        textViewPhone.setText(user.getPhone());
                        textViewFollowing.setText(user.getFollowingCount());
                        textViewModerator.setText(user.getModeratorCount());

                         imagePath=KEY_IMAGE_ADDRESS+(user.getImage());

                        Glide.with(getActivity().getApplicationContext())
                                .load(Uri.parse(imagePath))
                                .placeholder(R.drawable.ic_place_holder_background)
                                //.error(R.drawable.ic_image_not_found_background)
                                .centerCrop()
                                .into(imageViewProfile);

                        imageViewProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showImageChooser();
                            }
                        });

                        linearLayoutModerator.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int count=Integer.valueOf(user.getModeratorCount());
                                if(count==0){
                                    DynamicToast.makeError(getActivity().getApplicationContext(),"you are not moderator in any group").show();
                                }else {
                                    Intent intent=new Intent(getActivity().getApplicationContext(), ModeratorListActivity.class);
                                    intent.putExtra(KEY_USER_ID,userId);
                                    startActivity(intent);
                                }
                            }
                        });

                        textViewTicket.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(getActivity().getApplicationContext(), MyTicketActivity.class);
                                startActivity(intent);
                            }
                        });
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
            Intent intent=new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        return v;
    }
    private void showImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_IMAGE);
        //startActivityForResult(Intent.createChooser(intent, "Select image"), CHOOSE_IMAGE);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            uriProfileImage = data.getData();

            Glide.with(getActivity().getApplicationContext())
                    .load(uriProfileImage)
                    .placeholder(R.drawable.ic_place_holder_background)
                    //.error(R.drawable.ic_image_not_found_background)
                    .centerCrop()
                    .into(imageViewProfile);
            // Bitmap bitmap = null;
            String[] filePathColumn = {MediaStore.Images.Media.DATA};


            Cursor cursor=getActivity().getContentResolver().query(uriProfileImage,filePathColumn,null,null,null);
            assert cursor!=null;
            cursor.moveToFirst();

            int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
            imagePath=cursor.getString(columnIndex);
            cursor.close();
            File file = new File(imagePath);

            String imageName=(userId)+"_"+"profile"+".png";
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), imageName);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            apiInterface.uploadEventProfileImage(fileToUpload,fileName).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){

                        apiInterface.updateUserImage(userId,"okazo/upload/"+imageName).enqueue(new Callback<APIResponse>() {
                            @Override
                            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                APIResponse apiResponse1=response.body();
                                if(!apiResponse.getError()){
                                    DynamicToast.makeSuccess(getActivity().getApplicationContext(),"Image changed").show();
                                }else {
                                    DynamicToast.makeSuccess(getActivity().getApplicationContext(),apiResponse1.getErrorMsg()).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponse> call, Throwable t) {

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });


        }
    }


}
