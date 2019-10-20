package com.example.parkwayz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//Goals: Register user on the database
//       then go back to Login Page for login



public class RegistrationActivity extends AppCompatActivity {

    //declare class variables

    private EditText userEmail, userPassword, userPassword2, userName;
    private ProgressBar loadingProgress;
    private Button regButton;

    private FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //assign  variables to button ids

        userEmail = findViewById(R.id.regEmail);
        userName = findViewById(R.id.regName);
        userPassword = findViewById(R.id.regPassword);
        userPassword2 = findViewById(R.id.regPasswordConfirm);
        loadingProgress = findViewById(R.id.RegProgressBar);
        regButton = findViewById(R.id.RegBtn);
        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();


        regButton.setOnClickListener(new View.OnClickListener() {


            @Override

            public void onClick(View view){

                regButton.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);

                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 =  userPassword2.getText().toString();
                final String name = userName.getText().toString();

                //checks for valid reasons to continue
                if(email.isEmpty() || name.isEmpty() || password.isEmpty() || password2.isEmpty() || !password.equals(password2)){

                    //all fields must be filled
                    //show error message

                    showMessage("Please Enter All Fields Accurately");
                    regButton.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);


                }
                else{

                    //everything looks good and ready to create user account

                    createUserAccount(email,name,password);

                    goToLoginPage();



                }



            }




        });



        setContentView(R.layout.activity_registration);
    }

    //Display Message
    private void showMessage(String message){

        //Toast lets you display
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();


    }

    //go to login page

    public  void goToLoginPage(){

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);

    }






    //create user account method

    private void createUserAccount(String email, final String name, String password){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){


            @Override
            public  void onComplete(@NonNull Task<AuthResult> task){

                if(task.isSuccessful()) {

                    //account created failed
                    showMessage("Account Created");

                    //update name
//                    updateUserInfo(name,mAuth.getCurrentUser());


                }
                else{

                    //account created failed
                    showMessage("Account Creation Failed" + task.getException().getMessage());
                    regButton.setVisibility(View.INVISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);


                }

            }



        });


    }

//    private void updateUserInfo(String name, FirebaseUser currentUser){
//
//      StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_name");
//      StorageReference nameFilePath = mStorage.child(name.getLastPathSegment());
//      nameFilePath.putFile()
//
//
//        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest().Builder()
//                    .setDisplayName(name)
//                    .build();
//
//
//
//
//
//
//    }

















}
