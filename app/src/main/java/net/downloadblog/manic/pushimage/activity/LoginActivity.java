package net.downloadblog.manic.pushimage.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.downloadblog.manic.pushimage.R;
import net.downloadblog.manic.pushimage.common.BaseActivity;
import net.downloadblog.manic.pushimage.fragment.RegisterFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.btn_login)
    Button btn_login;
    @Bind(R.id.btn_register)
    Button btn_register;
    @Bind(R.id.edt_email)
    EditText edt_email;
    @Bind(R.id.edt_password)
    EditText edt_password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static String TAG = "Tag";
    FirebaseUser user;
    String picturePath;
    Uri photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activt);

        ButterKnife.bind(this);
        //In your sign-up activity's onCreate method, get the shared instance of the FirebaseAuth object:
        mAuth = FirebaseAuth.getInstance();
        //Set up an AuthStateListener that responds to changes in the user's sign-in state:
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    //Toast.makeText(LoginActivity.this, "Signed Out", Toast.LENGTH_LONG).show();
                    // User is signed out
                    // Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        // ...
    }

    @OnClick(R.id.btn_login)
    public void setLogin() {
//        if (Integer.parseInt(edt_password.getText().toString()) < 6) {
//            Toast.makeText(LoginActivity.this, "password must be at least 6 characters", Toast.LENGTH_LONG).show();
//            return;
//        } else {
        final BaseActivity  baseActivity= new BaseActivity(this);
        baseActivity.showProgressDialog();
        try {
            Log.e("Get Pass", Integer.parseInt(edt_password.getText().toString()) + "");
            mAuth.signInWithEmailAndPassword(edt_email.getText().toString(), edt_password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            baseActivity.hideProgressDialog();
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("id", mAuth.getCurrentUser().getUid().toString());
                            startActivity(intent);

                        }
                    });
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "the username or password is incorrect! Please try again!", Toast.LENGTH_LONG).show();
//            ShowDialog showDialog= new ShowDialog(getApplicationContext());
//            showDialog.DialogPress("the username or password is incorrect! Please try again!");
        }
    }

    // }
    @OnClick(R.id.btn_register)
    public void setRegister() {

        showRegisterFrag();
    }

    public void showMainList() {
        ((MainActivity) getBaseContext()).showMainListFrag();
    }

    public void showRegisterFrag() {
        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .add(android.R.id.content, new RegisterFragment()).commit();
    }




    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
