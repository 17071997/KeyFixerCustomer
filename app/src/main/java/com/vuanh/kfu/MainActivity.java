package com.vuanh.kfu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.firebase.FirebaseApp;
import com.vuanh.kfu.Model.Customer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout welcomeLayout;
    Button btnSignIn, btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //before set content view
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_main);

        //Init Firebase
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Customers");

        //Init view
        GetButtonControl();
    }

    void GetButtonControl(){
        btnSignIn = (Button) findViewById(R.id.btn_signin);
        btnRegister = (Button) findViewById(R.id.btn_register);
        welcomeLayout = (RelativeLayout) findViewById(R.id.welcome_Layout);
        btnSignIn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_signin:
                ShowLoginDialog();
                break;
            case R.id.btn_register:
                ShowRegisterDialog();
                break;
        }
    }

    private void ShowRegisterDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Đăng kí tài khoản ");
        dialog.setMessage("Làm ơn dùng email mà đăng ký giùm mình nhé !");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);

        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edt_Email);
        final MaterialEditText edtPass = register_layout.findViewById(R.id.edt_Password);
        final MaterialEditText edtName = register_layout.findViewById(R.id.edt_Name);
        final MaterialEditText edtPhone = register_layout.findViewById(R.id.edt_Phone);

        dialog.setView(register_layout);

        //Set for the agree button :v ... ofcourse
        dialog.setPositiveButton("Đăng ký", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
                //validation
                if (TextUtils.isEmpty(edtEmail.getText().toString())){
                    Snackbar.make(welcomeLayout,"Làm ơn nhập email giùm",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtPass.getText().toString())){
                    Snackbar.make(welcomeLayout,"Làm ơn nhập mật khẩu giùm",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (edtPass.getText().toString().length() < 6){
                    Snackbar.make(welcomeLayout,"Mật khẩu ngắn quá!!!",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtPhone.getText().toString())){
                    Snackbar.make(welcomeLayout,"Làm ơn cung cấp số điện thoại nhé",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtName.getText().toString())){
                    Snackbar.make(welcomeLayout,"Thiếu cái tên nữa",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //Done ... now, let register new user
                auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //save user to db
                        Customer customer = new Customer();
                        customer.setEmail(edtEmail.getText().toString());
                        customer.setPassword(edtPass.getText().toString());
                        customer.setName(edtName.getText().toString());
                        customer.setPhone(edtPhone.getText().toString());

                        //use email as key
                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar.make(welcomeLayout,"Đăng ký thành công! <3",Snackbar.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(welcomeLayout,"Đăng ký thất bại " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(welcomeLayout,"" + e.getMessage(),Snackbar.LENGTH_SHORT).show();
                        System.out.print(e.getMessage());
                    }
                });
            }
        });
        dialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }
    private void ShowLoginDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Đăng nhập");
        dialog.setMessage("Đăng nhập để sử dụng nhé !");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_login, null);

        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edt_Email);
        final MaterialEditText edtPass = register_layout.findViewById(R.id.edt_Password);

        dialog.setView(register_layout);

        //Set for the agree button :v ... ofcourse
        dialog.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
                //set disable button sign in if is processing
                btnSignIn.setEnabled(false);
                //validation
                if (TextUtils.isEmpty(edtEmail.getText().toString())){
                    Snackbar.make(welcomeLayout,"Làm ơn nhập email giùm",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtPass.getText().toString())){
                    Snackbar.make(welcomeLayout,"Làm ơn nhập mật khẩu giùm",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                final SpotsDialog waiting_dialog = new SpotsDialog(MainActivity.this);
                waiting_dialog.show();
                //Login
                auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                waiting_dialog.dismiss();
                                startActivity(new Intent(MainActivity.this,menu_navigation.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waiting_dialog.dismiss();
                        Snackbar.make(welcomeLayout,"Đăng nhập thất bại!",Snackbar.LENGTH_SHORT).show();
                        //set enable button sign in if it failed
                        btnSignIn.setEnabled(true);
                    }
                });
            }
        });
        dialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }
}