package com.duhos.duhosii;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VratiSeFragment extends Fragment {
    ImageButton vratiSeButton;
    private View vratiSeFragmentView;
    TextView veciTekst,manjiTekst;
    String flag="";
    BottomNavigationView bottomNavigationView;

    public VratiSeFragment(String flag) {
        this.flag=flag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.show();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.vrati_se_toolbar);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.duhosPlava));


        vratiSeFragmentView = inflater.inflate(R.layout.fragment_vrati_se, container, false);
        vratiSeButton = vratiSeFragmentView.findViewById(R.id.butonVrati);

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);


        veciTekst=vratiSeFragmentView.findViewById(R.id.veciTekst);
        manjiTekst=vratiSeFragmentView.findViewById(R.id.manjiTekst);

        if(flag.equals("pitanje")){
            veciTekst.setText(getContext().getResources().getString(R.string.pitanjeJePoslano));
            manjiTekst.setText(getContext().getResources().getString(R.string.kapelanCeOdgovoritiNaPitanjeTekst));
        }
        if(flag.equals("nakana")){
            veciTekst.setText(getContext().getResources().getString(R.string.nakanaJePoslana));
            manjiTekst.setText(getContext().getResources().getString(R.string.vidimoSeNaKlanjaju));
        }
        if(flag.equals("svjedočanstvo")){
            veciTekst.setText(getContext().getResources().getString(R.string.svjedocanstvoJePoslano));
            manjiTekst.setText(getContext().getResources().getString(R.string.objavaSvjedocanstva));
        }


        vratiSeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().popBackStack();

                }
            });
        return vratiSeFragmentView;
    }

}









