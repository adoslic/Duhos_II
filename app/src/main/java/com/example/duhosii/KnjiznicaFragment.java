package com.example.duhosii;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import org.w3c.dom.Text;

import java.io.Externalizable;

public class KnjiznicaFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    ImageButton popisKnjigaButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.show();

        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View view=mActionBar.getCustomView();
        zaglavlje=view.findViewById(R.id.naslov);
        zaglavlje.setText("Knjižnica");

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        View knjiznicaView=inflater.inflate(R.layout.fragment_knjiznica, container, false);
        popisKnjigaButton=knjiznicaView.findViewById(R.id.popisKnjigaButton);

        popisKnjigaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popisKnjiga();
            }
        });

        return knjiznicaView;
    }

    public void popisKnjiga() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,new PopisKnjigaFragment()).addToBackStack("selectedFragment").commit();
    }


}
