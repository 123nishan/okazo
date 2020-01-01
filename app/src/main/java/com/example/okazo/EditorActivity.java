package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.okazo.Api.ApiInterface;

public class EditorActivity extends AppCompatActivity {
private  EditText editTextTitel,editTextNote;
ProgressDialog progressDialog;
ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
//        editTextNote=findViewById(R.id.note);
//        editTextTitel=findViewById(R.id.titel);
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("please wait....");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_editor,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                String title=editTextTitel.getText().toString().trim();
                String note=editTextNote.getText().toString().trim();
                int color=-2184710;
                if(title.isEmpty()){
                    editTextTitel.setError("please enter");
                }else if (note.isEmpty()){
                    editTextNote.setError("please enter");
                }else {
//                    saveNote(title,note,color);
                }
                return true;
                default:
                    return super.onOptionsItemSelected(item);

        }

    }
//    private void saveNote(final String title,final String note,final  int color){
//        progressDialog.show();
//        apiInterface=ApiClient.getApiClient().create(ApiInterface.class);
//        Call<Note> call =apiInterface.saveNote(title,note,color);
//        call.enqueue(new Callback<Note>() {
//            @Override
//            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
//                progressDialog.dismiss();
//                if(response.isSuccessful() && response.body() !=null){
//                    Boolean success=response.body().getSuccess();
//                    if(success){
//                        Toast.makeText(EditorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                        finish(); //back to main activity
//                    }else {
//                        //if error, stilll in this activity
//                        Toast.makeText(EditorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Note> call,@NonNull Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(EditorActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
