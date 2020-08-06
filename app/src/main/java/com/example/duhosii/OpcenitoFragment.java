package com.example.duhosii;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class OpcenitoFragment extends Fragment {

    TextView zaglavlje,opcenitoText;
    BottomNavigationView bottomNavigationView;

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
        zaglavlje.setText("Općenito informacije");

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);

        View opcenitoFragmentView= inflater.inflate(R.layout.fragment_opcenito, container, false);
        opcenitoText=opcenitoFragmentView.findViewById(R.id.opcenitoText);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            opcenitoText.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        return opcenitoFragmentView;
    }

}
