package com.example.duhosii;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class DialogDisclaimer extends AppCompatDialogFragment {

    AlertDialog alertDialog;
    ImageButton xButton;
    TextView disclaimer;
    Button nastavi;
    String text;

    public DialogDisclaimer(String text) {
        this.text = text;
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_disclaimer,null);

        xButton=view.findViewById(R.id.xButton);
        disclaimer=view.findViewById(R.id.disclaimer);
        disclaimer.setText(text);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        nastavi=view.findViewById(R.id.nastaviButton);
        nastavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return alertDialog;
    }

}
