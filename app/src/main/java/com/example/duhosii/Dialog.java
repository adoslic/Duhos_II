package com.example.duhosii;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentTransaction;
public class Dialog extends AppCompatDialogFragment {

    AlertDialog alertDialog;
    ImageButton xButton;
    Button uclaniSe;
    RelativeLayout infoDuhos,infoKapelani,infoTimovi,infoKnjižnica;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog,null);

        infoDuhos=view.findViewById(R.id.infoDuhosLayout);
        infoKapelani=view.findViewById(R.id.infoKapelaniLayout);
        infoTimovi=view.findViewById(R.id.infoTimoviLayout);
        infoKnjižnica=view.findViewById(R.id.infoKnjiznicaLayout);

        infoDuhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                OpcenitoFragment frag = new OpcenitoFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_containter, frag);
                ft.addToBackStack(null);
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
                ft.addToBackStack(null);
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
                ft.addToBackStack(null);
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
                ft.addToBackStack(null);
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

        uclaniSe=view.findViewById(R.id.uclaniSeButton);
        uclaniSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://docs.google.com/forms/d/e/1FAIpQLSfe8xETG3lyWJkftnyf4N7gcPJ0-UNoB2TjALKaGGv6NY0GAg/viewform?fbclid=IwAR2ppsjJ9aMN3MmjjQCqDIm69SDqKUJuTBL0VFgrgUV3IKBb3sWfTeCEceo";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return alertDialog;
    }

}
