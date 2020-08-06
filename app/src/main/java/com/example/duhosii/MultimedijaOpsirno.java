package com.example.duhosii;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class MultimedijaOpsirno extends Fragment {

    private TextView zaglavlje;
    private BottomNavigationView bottomNavigationView;
    private Medij medij;
    private View multimedijaView;
    private TextView naslov,sadrzaj,idiNaPoveznicu;
    private ImageButton linkButton;
    private ScrollView scrollView;


    public MultimedijaOpsirno(Medij medij) {
        this.medij = medij;
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
        zaglavlje.setText("Novosti");

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);

        multimedijaView = inflater.inflate(R.layout.fragment_multimedija_opsirno, container, false);
        naslov=multimedijaView.findViewById(R.id.naslovOpsirno);
        sadrzaj=multimedijaView.findViewById(R.id.sadrzajOpsirno);
        idiNaPoveznicu=multimedijaView.findViewById(R.id.idiNaPoveznicu);
        linkButton=multimedijaView.findViewById(R.id.linkButton);

        scrollView=multimedijaView.findViewById(R.id.multimedija_opsirno_scollView);

        naslov.setText(medij.getNaslov());
        sadrzaj.setText(medij.getSadrzaj());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sadrzaj.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        if(medij.getMedij().toString().toLowerCase().equals("facebook")) {
            idiNaPoveznicu.setText("Više pročitajte u objavi.");
            linkButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_buttonfacebook));
        }
        else if(medij.getMedij().toString().toLowerCase().equals("youtube")){
            idiNaPoveznicu.setText("U nastavku poslušajte pjesmu.");
            linkButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_buttonyoutube));
        }
        else  if(medij.getMedij().toString().toLowerCase().equals("web")){
            idiNaPoveznicu.setText("U članku pročitajte više.");
            linkButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_buttoninternet));
        }
        else  if(medij.getMedij().toString().toLowerCase().equals("instagram") ){
            idiNaPoveznicu.setText("Više pročitajte u objavi.");
            linkButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_buttoninstagram));
        }
        else{
            idiNaPoveznicu.setText("Za više pritisnite na poveznicu.");
            linkButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_buttoninternet));
        }


        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLink();
            }
        });

        return multimedijaView;
    }

    public void goToLink() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(medij.getLink()));
        startActivity(browserIntent);
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).SetNavItemChecked(3);
    }
}