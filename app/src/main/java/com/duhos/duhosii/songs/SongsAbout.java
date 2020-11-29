package com.duhos.duhosii.songs;

import android.content.Intent;
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
import androidx.fragment.app.FragmentTransaction;

import com.duhos.duhosii.MainActivity;
import com.duhos.duhosii.R;
import com.duhos.duhosii.models.Song;
import com.duhos.duhosii.prayers.WebViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.Objects;

public class SongsAbout extends Fragment {

    private TextView zaglavlje;
    private BottomNavigationView bottomNavigationView;
    private Song song;
    private View pjesmaricaView;
    private TextView naslov, tekstPjesme, izvodjac;
    private ImageView slika;
    private ImageButton shareButton, pdfButton, youtubeButton;
    private ScrollView scrollView;


    public SongsAbout(Song song) {
        this.song = song;
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
        zaglavlje.setText(getContext().getResources().getString(R.string.pjesmaricaNaslov));

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);
        bottomNavigationView.setBackground(getContext().getResources().getDrawable(R.color.white));

        ((MainActivity) Objects.requireNonNull(getActivity())).setSongAbout(true);

        pjesmaricaView = inflater.inflate(R.layout.fragment_pjesmarica_opsirno, container, false);
        naslov = pjesmaricaView.findViewById(R.id.naslovOpsirno);
        tekstPjesme = pjesmaricaView.findViewById(R.id.tekstOpsirno);
        slika = pjesmaricaView.findViewById(R.id.slikaOpsirno);
        shareButton = pjesmaricaView.findViewById(R.id.shareButton);
        pdfButton = pjesmaricaView.findViewById(R.id.pdfButton);
        youtubeButton = pjesmaricaView.findViewById(R.id.youtubeButton);

        izvodjac = pjesmaricaView.findViewById(R.id.izvodjacOpsirno);

        scrollView = pjesmaricaView.findViewById(R.id.pjesmarica_opsirno_scollView);

        naslov.setText(song.getNaslov());
        izvodjac.setText(song.getBend());
        String formatedText = song.getTekstPjesme();

        if (song.getLink().equals("Link je nedostupan"))
            pdfButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_buttonakordimissing));
        else
            pdfButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_akordi_button));

        if (song.getYoutubeLink().equals("YouTube link je nedostupan"))
            youtubeButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_buttonyoutubeinmissing));
        else
            youtubeButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_buttonyoutube));

        tekstPjesme.setText(formatedText);


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!song.getLink().equals("Link je nedostupan")) {
                    goToAkordi();
                } else
                    Toast.makeText(getContext(), "Link je trenutačno nedostupan", Toast.LENGTH_SHORT).show();
            }
        });

        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!song.getYoutubeLink().equals("YouTube link je nedostupan")) {
                    goToYouTube();
                } else
                    Toast.makeText(getContext(), "Link je trenutačno nedostupan", Toast.LENGTH_SHORT).show();
            }
        });

        return pjesmaricaView;
    }

    private void goToAkordi() {
        if (URLUtil.isValidUrl(song.getLink())) {
            WebViewFragment frag = new WebViewFragment(song.getLink().toString(), "Pjesmarica");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_containter, frag);
            int count = getFragmentManager().getBackStackEntryCount();
            if (getFragmentManager().getBackStackEntryAt(count - 1).getName() == "dialogBox") {
                getFragmentManager().popBackStack();
            }
            ft.addToBackStack("dialogBox");
            ft.commit();
        } else
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.neispravanLinkString), Toast.LENGTH_SHORT).show();
    }

    private void goToYouTube() {
        if (URLUtil.isValidUrl(song.getYoutubeLink())) {
            WebViewFragment frag = new WebViewFragment(song.getYoutubeLink().toString(), "Pjesmarica");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_containter, frag);
            int count = getFragmentManager().getBackStackEntryCount();
            if (getFragmentManager().getBackStackEntryAt(count - 1).getName() == "dialogBox") {
                getFragmentManager().popBackStack();
            }
            ft.addToBackStack("dialogBox");
            ft.commit();
        } else
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.neispravanLinkString), Toast.LENGTH_SHORT).show();
    }

    public void share() {
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, naslov.getText().toString() + " - " + song.getBend() + "\n\n" + tekstPjesme.getText().toString());
        share.putExtra(Intent.EXTRA_SUBJECT, naslov.getText().toString() + " - " + song.getBend());
        share.setType("text/plain");
        getContext().startActivity(share.createChooser(share, "Share using"));
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).SetNavItemChecked(0);
    }
}