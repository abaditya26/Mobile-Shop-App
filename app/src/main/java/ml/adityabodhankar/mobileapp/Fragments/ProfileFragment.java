package ml.adityabodhankar.mobileapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import ml.adityabodhankar.mobileapp.Models.UserModel;
import ml.adityabodhankar.mobileapp.R;

public class ProfileFragment extends Fragment {

    private TextView emailView;
    private EditText nameInput, phoneInput;
    private String gender = "";
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TextView updateProfileBtn;
    private ProgressBar progress;
    private UserModel user;
    private boolean enabled = false;
    private CircleImageView userIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        emailView = view.findViewById(R.id.profile_email);
        nameInput = view.findViewById(R.id.profile_name);
        phoneInput = view.findViewById(R.id.profile_phone);
        updateProfileBtn = view.findViewById(R.id.profile_btn);
        progress = view.findViewById(R.id.update_user_progress);
        userIcon = view.findViewById(R.id.profile_user_icon);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        progress.setVisibility(View.VISIBLE);
        updateProfileBtn.setVisibility(View.GONE);

        RadioButton maleBtn = view.findViewById(R.id.profile_male_btn);
        RadioButton femaleBtn = view.findViewById(R.id.profile_female_btn);
        maleBtn.setOnClickListener(view1 -> {
            if(enabled){
                gender = "male";
            }else{
                if(gender.equalsIgnoreCase("female")){
                    femaleBtn.setChecked(true);
                }
            }
        });
        femaleBtn.setOnClickListener(view1 -> {
            if(enabled){
                gender = "female";
            }else{
                if(gender.equalsIgnoreCase("male")){
                    maleBtn.setChecked(true);
                }
            }
        });
        updateProfileBtn.setOnClickListener(view2 -> {
            if(enabled){
                if(!validateInput()){
                    return;
                }
                user.setName(nameInput.getText().toString());
                user.setPhone(phoneInput.getText().toString());
                user.setGender(gender);
                progress.setVisibility(View.VISIBLE);
                updateProfileBtn.setVisibility(View.GONE);
                db.collection("users").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                        .update(user.toMap())
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            disableInput();
                            progress.setVisibility(View.GONE);
                            updateProfileBtn.setVisibility(View.VISIBLE);
                        }).addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Profile Not Updated", Toast.LENGTH_SHORT).show();
                            disableInput();
                            progress.setVisibility(View.GONE);
                            updateProfileBtn.setVisibility(View.VISIBLE);
                        });
            }else{
                enableInput();
            }
        });
        db.collection("users")
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .addSnapshotListener((value, error) -> {
                    if (value != null && value.exists()){
                        //snapshot exists
                        if(value.getData() == null){
                            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                        }else {
                            user = new UserModel(value.getData());
                            emailView.setText(user.getEmail());
                            nameInput.setText(user.getName());
                            phoneInput.setText(user.getPhone());
                            gender = user.getGender();
                            if(!user.getProfile().equalsIgnoreCase("default")){
                                Glide.with(requireContext()).load(user.getProfile()).into(userIcon);
                            }
                            if(gender.equalsIgnoreCase("male")){
                                maleBtn.setChecked(true);
                            }else{
                                femaleBtn.setChecked(false);
                            }
                            disableInput();
                        }
                    }else{
                        Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                    progress.setVisibility(View.GONE);
                    updateProfileBtn.setVisibility(View.VISIBLE);
                });
        return view;
    }

    private boolean validateInput() {
        if (nameInput.getText().toString().equalsIgnoreCase("")){
            nameInput.setError("Required Field");
            return false;
        }
        if (phoneInput.getText().toString().equalsIgnoreCase("")){
            phoneInput.setError("Required Field");
            return false;
        }
        if (gender.equalsIgnoreCase("")){
            Toast.makeText(getContext(), "Please Select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void enableInput(){
        enabled = true;
        nameInput.setEnabled(true);
        phoneInput.setEnabled(true);
        updateProfileBtn.setText("Update Profile");
    }

    private void disableInput(){
        enabled = false;
        nameInput.setEnabled(false);
        phoneInput.setEnabled(false);
        updateProfileBtn.setText("Edit Profile");
    }
}