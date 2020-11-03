package com.duhos.duhosii.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.duhos.duhosii.R;
import com.duhos.duhosii.prayers.WebViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class LibraryFragment extends Fragment {

    TextView zaglavlje,knjiznicaTekst;
    BottomNavigationView bottomNavigationView;
    ImageButton popisKnjigaButton;
    String linkNaPopisKnjiga="https://drive.google.com/file/d/11ssoe11OqW4b6JtyPU-CPQaghVKCn1fC/view?usp=sharing";

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
        zaglavlje.setText(getContext().getResources().getString(R.string.knjiznicaString));

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

        knjiznicaTekst=knjiznicaView.findViewById(R.id.knjiznicaTekst);

        return knjiznicaView;
    }

    public void popisKnjiga() {
        if(URLUtil.isValidUrl(linkNaPopisKnjiga)) {
            WebViewFragment frag = new WebViewFragment(linkNaPopisKnjiga, "Knjižnica");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_containter, frag);
            int count = getFragmentManager().getBackStackEntryCount();
            if(getFragmentManager().getBackStackEntryAt(count-1).getName() == "dialogBox"){
                getFragmentManager().popBackStack();
            }
            ft.addToBackStack("dialogBox");
            ft.commit();
        }
        else
            Toast.makeText(getContext(),getContext().getResources().getString(R.string.neispravanLinkString),Toast.LENGTH_SHORT).show();
    }


}
