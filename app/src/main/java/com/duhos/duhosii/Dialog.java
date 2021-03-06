package com.duhos.duhosii;

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
                AplikacijaInfoFragment frag = new AplikacijaInfoFragment();
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
                OpcenitoFragment frag = new OpcenitoFragment();
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
                KapelaniFragment frag = new KapelaniFragment();
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
                KnjiznicaFragment frag = new KnjiznicaFragment();
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
                TimoviFragment frag = new TimoviFragment();
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
                UclaniSeFragment frag = new UclaniSeFragment();
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
