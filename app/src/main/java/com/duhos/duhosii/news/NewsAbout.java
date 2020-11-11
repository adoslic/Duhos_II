package com.duhos.duhosii.news;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageButton;
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
import com.duhos.duhosii.models.News;
import com.duhos.duhosii.prayers.WebViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class NewsAbout extends Fragment {

    private TextView zaglavlje;
    private BottomNavigationView bottomNavigationView;
    private News news;
    private View multimedijaView;
    private TextView naslov,sadrzaj,idiNaPoveznicu;
    private ImageButton linkButton;
    private ScrollView scrollView;


    public NewsAbout(News news) {
        this.news = news;
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
        zaglavlje.setText(getContext().getResources().getString(R.string.novostiNaslov));

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);
        bottomNavigationView.setBackground(getContext().getResources().getDrawable(R.color.white));

        multimedijaView = inflater.inflate(R.layout.fragment_multimedija_opsirno, container, false);
        naslov=multimedijaView.findViewById(R.id.naslovOpsirno);
        sadrzaj=multimedijaView.findViewById(R.id.sadrzajOpsirno);
        idiNaPoveznicu=multimedijaView.findViewById(R.id.idiNaPoveznicu);
        linkButton=multimedijaView.findViewById(R.id.linkButton);

        scrollView=multimedijaView.findViewById(R.id.multimedija_opsirno_scollView);

        naslov.setText(news.getNaslov());
        sadrzaj.setText(news.getSadrzaj());


        if(news.getMedij().toLowerCase().equals(getContext().getResources().getString(R.string.facebookString))) {
            idiNaPoveznicu.setText(getContext().getResources().getString(R.string.viseProcitajteUObjavi));
            linkButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_buttonfacebook));
        }
        else if(news.getMedij().toLowerCase().equals(getContext().getResources().getString(R.string.youtubeString))) {
            idiNaPoveznicu.setText(getContext().getResources().getString(R.string.uNastavkuPogledajte));
            linkButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_buttonyoutube));
        }
        else  if(news.getMedij().toLowerCase().equals(getContext().getResources().getString(R.string.webString))) {
            idiNaPoveznicu.setText(getContext().getResources().getString(R.string.uClankuProcitajte));
            linkButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_buttoninternet));
        }
        else  if(news.getMedij().toLowerCase().equals(getContext().getResources().getString(R.string.instagramString))) {
            idiNaPoveznicu.setText(getContext().getResources().getString(R.string.viseProcitajteUObjavi));
            linkButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_buttoninstagram));
        }
        else{
            idiNaPoveznicu.setText(getContext().getResources().getString(R.string.zaVisePritisnite));
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
        if(URLUtil.isValidUrl(news.getLink())) {
            WebViewFragment frag = new WebViewFragment(news.getLink(), "Novosti");
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


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).SetNavItemChecked(3);
    }
}