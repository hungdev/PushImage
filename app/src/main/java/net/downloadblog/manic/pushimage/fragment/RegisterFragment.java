package net.downloadblog.manic.pushimage.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.downloadblog.manic.pushimage.R;
import net.downloadblog.manic.pushimage.activity.MainActivity;
import net.downloadblog.manic.pushimage.common.BaseActivity;
import net.downloadblog.manic.pushimage.object.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    //
//    EditText edt_email, e
//    @Bind(R.id.edt_email)
//    EditText edt_email;
//    @Bind(R.id.edt_password)
//    EditText edt_password;
//    @Bind(R.id.btn_register)
//    Button btn_register;
//    @Bind(R.id.edt_name)
//    EditText edt_name;
    FirebaseUser user;
    //  String email, password;
    //=====================
    EditText edt_email;
    EditText edt_password;
    Button btn_register;
    EditText edt_name;
    TextView TvBackLogin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private ProgressDialog mProgressDialog;
    public static final String TAG = RegisterFragment.class.getSimpleName();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // ButterKnife.bind(this, view);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_password = (EditText) view.findViewById(R.id.edt_password);
        btn_register = (Button) view.findViewById(R.id.btn_register);
        edt_name=(EditText)view.findViewById(R.id.edt_name);
        TvBackLogin=(TextView)view.findViewById(R.id.tv_back_login);

        TvBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //super.onBackPressed();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BaseActivity baseActivity= new BaseActivity(getContext());


                String email = edt_email.getText().toString().trim();
                String password = edt_password.getText().toString().trim();
                //Toast.makeText(getActivity(),""+email1+password1,Toast.LENGTH_LONG).show();

                if (isvalidate(email, password) == true) {
                    try {
                        isvalidate(email, password);
                        baseActivity.showProgressDialog();
                        showProgressDialog();
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                        hideProgressDialog();
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                        setInfo();
                                        writeNewUser(mAuth.getCurrentUser().getUid(), edt_name.getText().toString(), mAuth.getCurrentUser().getEmail());
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        startActivity(intent);
//                                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
//                                        .replace(R.id.ll_register, new MainListFragment()).commit();
                                    }
                                });
                    } catch (Exception e) {

                    }

                }
            }
        });


        return view;
    }



    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    //get set info
    private void setInfo() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(edt_name.getText().toString())
                //.setPhotoUri(Uri.parse("https://www.cloudflare.com/media/cloudflare-logo.png"))
                // .setPhotoUri(Uri.parse(picturePath))
                .setPhotoUri(Uri.parse(""))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    public boolean isvalidate(String email, String password) {
        //check null
        if (email.length() < 0 || password.length() < 0) {
            Toast.makeText(getContext(), "Email or Password is not null!", Toast.LENGTH_SHORT).show();
//            return  true;
        } else {
            //check password
            if (password.length() < 6) {
                Toast.makeText(getContext(), "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
//            ShowDialog showDialog= new ShowDialog(getContext());
//            showDialog.DialogPress("Password must be at least 6 characters! ");
                return false;
            }
            //check email
            if (email.matches(emailPattern)) {
                //Toast.makeText(getContext(), "valid email address", Toast.LENGTH_SHORT).show();
//                return  true;
            } else {
                Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                return  false;
            }
        }
        return  true;

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
}
