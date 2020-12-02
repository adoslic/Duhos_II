package com.duhos.duhosii.info.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.duhos.duhosii.R;
import com.duhos.duhosii.info.AboutFragment;
import com.duhos.duhosii.info.ApplicationInfoFragment;
import com.duhos.duhosii.info.ChaplainsFragment;
import com.duhos.duhosii.info.LibraryFragment;
import com.duhos.duhosii.info.TeamsFragment;
import com.duhos.duhosii.prayers.WebViewFragment;

public class Dialog extends AppCompatDialogFragment {

    AlertDialog alertDialog;
    RelativeLayout xButtonLayout;
    ImageButton xButton;
    ImageButton uclaniSe;
    RelativeLayout infoDuhos,infoKapelani,infoTimovi,infoKnjižnica,infoApk;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog,null);

        infoDuhos=view.findViewById(R.id.infoDuhosLayout);
        infoKapelani=view.findViewById(R.id.infoKapelaniLayout);
        infoTimovi=view.findViewById(R.id.infoTimoviLayout);
        infoKnjižnica=view.findViewById(R.id.infoKnjiznicaLayout);
        infoApk=view.findViewById(R.id.infoApk);

        infoApk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                ApplicationInfoFragment frag = new ApplicationInfoFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_containter, frag);
                int count = getFragmentManager().getBackStackEntryCount();
                if(getFragmentManager().getBackStackEntryAt(count-1).getName() == "dialogBox"){
                    getFragmentManager().popBackStack();
                }
                ft.addToBackStack("dialogBox");
                ft.commit();
            }
        });

        infoDuhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                AboutFragment frag = new AboutFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_containter, frag);
                int count = getFragmentManager().getBackStackEntryCount();
                if(getFragmentManager().getBackStackEntryAt(count-1).getName() == "dialogBox"){
                    getFragmentManager().popBackStack();
                }
                ft.addToBackStack("dialogBox");
                ft.commit();
            }
        });

        infoKapelani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                ChaplainsFragment frag = new ChaplainsFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_containter, frag);
                int count = getFragmentManager().getBackStackEntryCount();
                if(getFragmentManager().getBackStackEntryAt(count-1).getName() == "dialogBox"){
                    getFragmentManager().popBackStack();
                }
                ft.addToBackStack("dialogBox");
                ft.commit();
            }
        });

        infoKnjižnica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                LibraryFragment frag = new LibraryFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_containter, frag);
                int count = getFragmentManager().getBackStackEntryCount();
                if(getFragmentManager().getBackStackEntryAt(count-1).getName() == "dialogBox"){
                    getFragmentManager().popBackStack();
                }
                ft.addToBackStack("dialogBox");
                ft.commit();
            }
        });

        infoTimovi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                TeamsFragment frag = new TeamsFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_containter, frag);
                int count = getFragmentManager().getBackStackEntryCount();
                if(getFragmentManager().getBackStackEntryAt(count-1).getName() == "dialogBox"){
                    getFragmentManager().popBackStack();
                }
                ft.addToBackStack("dialogBox");
                ft.commit();
            }
        });

        xButton=view.findViewById(R.id.xButton);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        xButtonLayout=view.findViewById(R.id.xButtonLayout);
        xButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        uclaniSe=view.findViewById(R.id.uclaniSeButton);
        uclaniSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                WebViewFragment frag = new WebViewFragment("https://docs.google.com/forms/d/e/1FAIpQLSexlM7YCFchzTa-wF865I37NLCyKL9voPy0c0rcqnPjD1qV1A/viewform", "DUHOS timovi");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_containter, frag);
                int count = getFragmentManager().getBackStackEntryCount();
                if(getFragmentManager().getBackStackEntryAt(count-1).getName() == "dialogBox"){
                    getFragmentManager().popBackStack();
                }
                ft.addToBackStack("dialogBox");
                ft.commit();
            }
        });

        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return alertDialog;
    }

}
