package com.example.duhosii;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class PjesmaOpsirno extends Fragment {

    private TextView zaglavlje;
    private BottomNavigationView bottomNavigationView;
    private Pjesma pjesma;
    private View pjesmaricaView;
    private TextView naslov,tekstPjesme;
    private ImageView slika;
    private ImageButton shareButton,pdfButton;
    private ScrollView scrollView;


    public PjesmaOpsirno(Pjesma pjesma) {
        this.pjesma = pjesma;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.show();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View viewActionBar = mActionBar.getCustomView();
        zaglavlje = viewActionBar.findViewById(R.id.naslov);
        zaglavlje.setText("Pjesmarica");

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);

        pjesmaricaView = inflater.inflate(R.layout.fragment_pjesmarica_opsirno, container, false);
        naslov=pjesmaricaView.findViewById(R.id.naslovOpsirno);
        tekstPjesme=pjesmaricaView.findViewById(R.id.tekstOpsirno);
        slika=pjesmaricaView.findViewById(R.id.slikaOpsirno);
        shareButton=pjesmaricaView.findViewById(R.id.shareButton);
        pdfButton=pjesmaricaView.findViewById(R.id.pdfButton);

        scrollView=pjesmaricaView.findViewById(R.id.pjesmarica_opsirno_scollView);

        naslov.setText(pjesma.getNaslov());
        String formatedText=pjesma.getTekstPjesme();

        if(pjesma.getLink().equals("Link je nedostupan"))
            pdfButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_buttonakordimissing));
        else
            pdfButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_akordi_button));

        tekstPjesme.setText(formatedText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tekstPjesme.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pjesma.getLink().equals("Link je nedostupan")){
                    goToAkordi();
                }
                else
                    Toast.makeText(getContext(),"Link je trenutačno nedostupan",Toast.LENGTH_SHORT).show();
            }
        });

        return pjesmaricaView;
    }

    private void goToAkordi() {
        if(URLUtil.isValidUrl(pjesma.getLink())) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pjesma.getLink()));
            startActivity(browserIntent);
        }
        else
            Toast.makeText(getContext(),"Link je neispravan, kontaktirajte nadležnu osobu!",Toast.LENGTH_SHORT).show();
    }

    public void share() {
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT,naslov.getText().toString()+" - "+pjesma.getBend() + "\n\n" + tekstPjesme.getText().toString());
        share.putExtra(Intent.EXTRA_SUBJECT, naslov.getText().toString()+" - "+pjesma.getBend());
        share.setType("text/plain");
        getContext().startActivity(share.createChooser(share, "Share using"));
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).SetNavItemChecked(0);
    }
}