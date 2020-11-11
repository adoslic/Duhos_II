package com.duhos.duhosii.info;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.duhos.duhosii.R;
import com.duhos.duhosii.prayers.PrayerGroupsFragment;
import com.duhos.duhosii.utils.SliderAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class ApplicationInfoFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    LinearLayout linearLayout;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    TextView iduceButton,natragButton;
    int currentPage;

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
        zaglavlje.setText(getContext().getResources().getString(R.string.oAplikacijiNaslov));

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        bottomNavigationView.setBackground(getContext().getResources().getDrawable(R.color.white));
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        View aplikacijaInfoView=inflater.inflate(R.layout.fragment_aplikacija_info, container, false);

        viewPager=aplikacijaInfoView.findViewById(R.id.info_viewPager);
        linearLayout=aplikacijaInfoView.findViewById(R.id.infoButtonLayout);

        iduceButton=aplikacijaInfoView.findViewById(R.id.iduceButton);
        natragButton=aplikacijaInfoView.findViewById(R.id.natragButton);

        iduceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage==4){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,new PrayerGroupsFragment()).addToBackStack("").commit();

                }
                viewPager.setCurrentItem(currentPage+1);
            }
        });
        natragButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(currentPage-1);
            }
        });
        sliderAdapter=new SliderAdapter(viewPager.getContext());

        viewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);
        return aplikacijaInfoView;
    }

    public void addDotsIndicator(int position){
        dots=new TextView[5];
        linearLayout.removeAllViews();
        for(int i=0;i<dots.length;i++) {
            dots[i]=new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#C4CFD7"));
            linearLayout.addView(dots[i]);
        }
        if(dots.length>0)
            dots[position].setTextColor(getResources().getColor(R.color.duhosPlava));
    }

    ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage=position;
            if(position==0){
                iduceButton.setVisibility(View.VISIBLE);
                natragButton.setVisibility(View.GONE);
                iduceButton.setText(getContext().getResources().getString(R.string.sljedeceNaslov));

            }
            else if(position==dots.length-1){
                iduceButton.setVisibility(View.VISIBLE);
                iduceButton.setText(getContext().getResources().getString(R.string.zavrsiString));
                natragButton.setVisibility(View.VISIBLE);
            }
            else {
                iduceButton.setText(getContext().getResources().getString(R.string.sljedeceNaslov));
                iduceButton.setVisibility(View.VISIBLE);
                natragButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



}
