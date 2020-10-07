package com.duhos.duhosii;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class PosaljiSvjedocanstvoFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    private View nakanaFragmentView;
    String text="Prilikom slanja svjedočanstva unos imena i prezimena nije nužan ukoliko želiš ostati anoniman. Svjedočanstvo će uskoro nakon slanja biti vidljivo u aplikaciji i na web stranici DUHOS-a.";
    private String marioMail="m.zigman6@gmail.com";
    //private String marioMail="kresimirtomic1998@gmail.com";
    private EditText imeEditText,nakanaEditText;
    private ImageButton sendNakanaButton;
    private int padding;
    private float density;

    public PosaljiSvjedocanstvoFragment() {
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
        zaglavlje=viewActionBar.findViewById(R.id.naslov);
        zaglavlje.setText("Svjedočanstvo");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);

        nakanaFragmentView = inflater.inflate(R.layout.fragment_nakana,container,false);
        otvoriDialog(nakanaFragmentView);
        density= getContext().getResources().getDisplayMetrics().density;
        padding= (int) (15*density);

        sendNakanaButton=nakanaFragmentView.findViewById(R.id.sendNakanaButton);
        imeEditText=nakanaFragmentView.findViewById(R.id.editTextIme);
        nakanaEditText=nakanaFragmentView.findViewById(R.id.editTextNakana);

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

        nakanaEditText.setHint("Svjedočanstvo");

        nakanaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0) {
                    nakanaEditText.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_shape));
                    nakanaEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pitanjepitanjaactive, 0, 0, 0);
                }
                else {
                    nakanaEditText.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_shape_shadow_small_radius));
                    nakanaEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pitanjepitanja, 0, 0, 0);
                    nakanaEditText.setPadding(padding,padding,padding,padding);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        sendNakanaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nakanaEditText.getText().length()==0){
                    Toast.makeText(getContext(),getEmojiByUnicode(0x1F446)+"Unesi svjedočanstvo!",Toast.LENGTH_SHORT).show();
                }
                else{
                    sendMail();
                    //back.performClick();
                    //Toast.makeText(getContext(),("\u2714")+" E-mail je uspješno poslan",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return nakanaFragmentView;
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    private void sendMail() {
        String subject;
        if(imeEditText.length()>0)
            subject="Svjedočanstvo od: "+imeEditText.getText().toString();
        else
            subject="Svjedočanstvo od anonimnog pošiljatelja";

        String message=nakanaEditText.getText().toString();
        JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(), marioMail,subject , message);
        javaMailAPI.execute();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,new VratiSeFragment("svjedočanstvo")).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).SetNavItemChecked(2);
    }

    public void otvoriDialog(View view) {
        final DialogDisclaimer dialog=new DialogDisclaimer(text);
        dialog.show(getActivity().getSupportFragmentManager(),"dialog");
    }


}
