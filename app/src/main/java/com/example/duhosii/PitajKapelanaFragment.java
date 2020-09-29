package com.example.duhosii;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class PitajKapelanaFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    private View pitajKapelanaFragmentView;
    private static final String TAG ="TAG";
    private boolean connectionFlag=false;
    private View connectionFragmentView;
    private ImageButton osvjeziButton,sendMessageButton;
    private ImageView marioImage,davorImage,marioImageActive,davorImageActive;
    private TextView marioText,davorText;
    private EditText imeEditText,pitanjeEditText;
    private String mailTo;
    private float density;
    private int padding;
    private ImageButton back;

    public PitajKapelanaFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.show();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar_without_menu);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View viewActionBar=mActionBar.getCustomView();
        back=viewActionBar.findViewById(R.id.idiNatrag);
        zaglavlje=viewActionBar.findViewById(R.id.naslov);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        zaglavlje.setText(getContext().getResources().getString(R.string.pitajKapelanaNaslov));

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);
        checkInternetConnection();

        if(connectionFlag==true) {
            pitajKapelanaFragmentView = inflater.inflate(R.layout.fragment_pitaj_kapelana, container, false);
            otvoriDialog(pitajKapelanaFragmentView);
            density= getContext().getResources().getDisplayMetrics().density;
            padding= (int) (15*density);
            marioImage=pitajKapelanaFragmentView.findViewById(R.id.marioImage);
            davorImage=pitajKapelanaFragmentView.findViewById(R.id.davorImage);
            marioImageActive=pitajKapelanaFragmentView.findViewById(R.id.marioImageActive);
            davorImageActive=pitajKapelanaFragmentView.findViewById(R.id.davorImageActive);
            marioText=pitajKapelanaFragmentView.findViewById(R.id.marioText);
            davorText=pitajKapelanaFragmentView.findViewById(R.id.davorText);
            sendMessageButton=pitajKapelanaFragmentView.findViewById(R.id.sendMessageButton);
            imeEditText=pitajKapelanaFragmentView.findViewById(R.id.editTextIme);
            pitanjeEditText=pitajKapelanaFragmentView.findViewById(R.id.editTextMessage);



            imeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()!=0) {
                        imeEditText.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_shape));
                        imeEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_imepitanjaactive, 0, 0, 0);
                    }
                    else {
                        imeEditText.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_shape_shadow_small_radius));
                        imeEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_imepitanja, 0, 0, 0);
                        imeEditText.setPadding(padding,padding,padding,padding);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) { }
            });

            pitanjeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()!=0) {
                        pitanjeEditText.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_shape));
                        pitanjeEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pitanjepitanjaactive, 0, 0, 0);
                    }
                    else {
                        pitanjeEditText.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_shape_shadow_small_radius));
                        pitanjeEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pitanjepitanja, 0, 0, 0);
                        pitanjeEditText.setPadding(padding,padding,padding,padding);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) { }
            });

            sendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*private String marioMail="m.zigman6@gmail.com";
                    private String davorMail="dav.vuk@gmail.com";*/
                    String marioMail=getContext().getResources().getString(R.string.mailZaPitanjaKapelan2);
                    String davorMail=getContext().getResources().getString(R.string.mailZaPitanjaKapelan1);
                    if(marioImageActive.getVisibility()==View.GONE && davorImageActive.getVisibility()==View.GONE){
                        Toast.makeText(getContext(),getEmojiByUnicode(0x1F446)+"Odaberi kapelana!",Toast.LENGTH_SHORT).show();
                    }
                    else if(pitanjeEditText.getText().length()==0){
                        Toast.makeText(getContext(),getEmojiByUnicode(0x1F446)+"Unesi pitanje!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(marioImageActive.getVisibility()==View.VISIBLE)
                            mailTo=marioMail;
                        if(davorImageActive.getVisibility()==View.VISIBLE)
                            mailTo=davorMail;
                        sendMail();
                        //back.performClick();
                        //Toast.makeText(getContext(),("\u2714")+" E-mail je uspješno poslan",Toast.LENGTH_SHORT).show();

                    }
                }
            });

            marioImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(marioImageActive.getVisibility()==View.GONE){
                        marioImageActive.setVisibility(View.VISIBLE);
                        davorImageActive.setVisibility(View.GONE);
                        marioText.setTextColor(ContextCompat.getColor(getContext(), R.color.duhosPlava));
                        marioText.setTypeface(null, Typeface.BOLD);
                        davorText.setTextColor(ContextCompat.getColor(getContext(), R.color.greyText));
                        davorText.setTypeface(null, Typeface.NORMAL);
                    }
                    else {
                        marioImageActive.setVisibility(View.GONE);
                        marioText.setTypeface(null, Typeface.NORMAL);
                        marioText.setTextColor(ContextCompat.getColor(getContext(), R.color.greyText));

                    }
                }
            });
            davorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(davorImageActive.getVisibility()==View.GONE){
                        davorImageActive.setVisibility(View.VISIBLE);
                        marioImageActive.setVisibility(View.GONE);
                        davorText.setTextColor(ContextCompat.getColor(getContext(), R.color.duhosPlava));
                        davorText.setTypeface(null, Typeface.BOLD);
                        marioText.setTextColor(ContextCompat.getColor(getContext(), R.color.greyText));
                        marioText.setTypeface(null, Typeface.NORMAL);

                    }
                    else {
                        davorImageActive.setVisibility(View.GONE);
                        davorText.setTextColor(ContextCompat.getColor(getContext(), R.color.greyText));
                        davorText.setTypeface(null, Typeface.NORMAL);

                    }
                }
            });
            return pitajKapelanaFragmentView;
        }
        else {
            connectionFragmentView = inflater.inflate(R.layout.no_internet_connection_fragment, container, false);
            osvjeziButton=connectionFragmentView.findViewById(R.id.osvjeziButton);
            osvjeziButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomNavigationView.findViewById(R.id.navigacija_molitva).performClick();
                }
            });
            return connectionFragmentView;
        }
    }

    public void otvoriDialog(View view) {
        String text=getContext().getResources().getString(R.string.pitanjeDisclaimer);
        final DialogDisclaimer dialog=new DialogDisclaimer(text);
        dialog.show(getActivity().getSupportFragmentManager(),"dialog");
    }

    private void sendMail() {
        String subject="";
        if(imeEditText.length()==0)
            subject="Pitanje za kapelana od anonimnog pošiljatelja";
        else
            subject="Pitanje za kapelana od: "+imeEditText.getText().toString();

        String message=pitanjeEditText.getText().toString();
        JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(), mailTo,subject , message);
        javaMailAPI.execute();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,new VratiSeFragment("pitanje")).commit();

    }

    private void checkInternetConnection() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=connectivityManager.getActiveNetworkInfo();
        if(null!=activeNetwork){
            if(activeNetwork.getType()==ConnectivityManager.TYPE_WIFI){
                connectionFlag=true;
            }
            else if(activeNetwork.getType()==ConnectivityManager.TYPE_MOBILE){
                connectionFlag=true;
            }

        }
        else
        {
            connectionFlag=false;
        }
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).SetNavItemChecked(4);
    }
}
