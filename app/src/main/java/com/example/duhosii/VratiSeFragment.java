package com.example.duhosii;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

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
            veciTekst.setText("PITANJE JE POSLANO!");
            manjiTekst.setText("Kapelan će odgovoriti na pitanje te ćemo isto objaviti u aplikaciji kako bi vidio odgovor. Hvala ti na korištenju aplikacije i popunjavanju naše rubrike.");
        }
        if(flag.equals("nakana")){
            veciTekst.setText("MOLITVENA NAKANA JE POSLANA!");
            manjiTekst.setText("Vidimo se na sljedećem euharistijskom klanjanju na kojem ćemo moliti za tvoju nakanu.");
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









