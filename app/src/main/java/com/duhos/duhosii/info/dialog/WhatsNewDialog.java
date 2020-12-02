package com.duhos.duhosii.info.dialog;

import android.bluetooth.le.ScanSettings;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

/**
 * Created by DUHOS AppTeam on 02,December,2020
 */
public class WhatsNewDialog extends AppCompatDialogFragment {
    AlertDialog whatsNewDialog;
    ImageView whatsNewImage;
    ImageButton odustani, nastavi;
    TextView whatsNewText;
    int flag=1;
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.whats_new_layout,null);

        whatsNewImage=view.findViewById(R.id.whatsNewImage);
        odustani=view.findViewById(R.id.buttonOdustani);
        nastavi=view.findViewById(R.id.buttonNastavi);
        whatsNewText=view.findViewById(R.id.whatsNewText);

        nastavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag++;
                switch (flag) {
                    case 1:
                        whatsNewImage.setImageDrawable(getResources().getDrawable(R.drawable.sto_je_novo_prva));
                        whatsNewText.setText(getResources().getString(R.string.whatsNewText1));
                        break;
                    case 2:
                        whatsNewImage.setImageDrawable(getResources().getDrawable(R.drawable.sto_je_novo_druga));
                        whatsNewText.setText(getResources().getString(R.string.whatsNewText2));
                        break;
                    case 3:
                        whatsNewImage.setImageDrawable(getResources().getDrawable(R.drawable.sto_je_novo_treca));
                        whatsNewText.setText(getResources().getString(R.string.whatsNewText3));
                        break;
                    case 4:
                        whatsNewDialog.dismiss();
                        break;
                }
            }
        });
        odustani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsNewDialog.dismiss();
            }
        });
        builder.setView(view);
        whatsNewDialog=builder.create();
        whatsNewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return whatsNewDialog;
    }
}
