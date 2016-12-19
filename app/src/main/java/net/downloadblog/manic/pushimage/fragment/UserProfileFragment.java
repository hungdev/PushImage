package net.downloadblog.manic.pushimage.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.downloadblog.manic.pushimage.R;


public class UserProfileFragment extends Fragment {
    TextView tvName;
    TextView tvEmail;
    Button btnChangePass;

    FirebaseUser user;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user_profile, container, false);
        tvName=(TextView)view.findViewById(R.id.tv_name);
        tvEmail=(TextView)view.findViewById(R.id.tv_email);
        btnChangePass=(Button)view.findViewById(R.id.btn_changepass);

         user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
            tvName.setText(name);
            tvEmail.setText(email);
        }
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDialog();
            }
        });

        return view;
    }
    public void displayAlertDialog() {
        LayoutInflater inflater = getLayoutInflater(null);
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        final EditText etPassword = (EditText) alertLayout.findViewById(R.id.et_Password);
        final EditText etRetype = (EditText) alertLayout.findViewById(R.id.et_Retype);
        final CheckBox cbShowPassword = (CheckBox) alertLayout.findViewById(R.id.cb_ShowPassword);

        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    etPassword.setTransformationMethod(null);
                    etRetype.setTransformationMethod(null);
                }

                else{
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etRetype.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Change Password");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // code for matching password
                String pass = etPassword.getText().toString();
                String retype = etRetype.getText().toString();
                if (pass.equals(retype)) {
                    user.updatePassword(pass)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User password updated.");
                                        Toast.makeText(getContext(), "User password updated.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();

    }

}
