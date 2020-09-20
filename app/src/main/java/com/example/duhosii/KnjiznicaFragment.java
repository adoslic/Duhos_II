package com.example.duhosii;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import org.w3c.dom.Text;

import java.io.Externalizable;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class KnjiznicaFragment extends Fragment {

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            knjiznicaTekst.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
        return knjiznicaView;
    }

    public void popisKnjiga() {
        if(URLUtil.isValidUrl(linkNaPopisKnjiga)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkNaPopisKnjiga));
            startActivity(browserIntent);
        }
        else
            Toast.makeText(getContext(),getContext().getResources().getString(R.string.neispravanLinkString),Toast.LENGTH_SHORT).show();
    }


}
