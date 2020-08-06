package com.example.duhosii;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.shimmer.ShimmerFrameLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import io.realm.Realm;
import io.realm.RealmResults;
import static android.content.Context.ALARM_SERVICE;
import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class KalendarItemAdapter extends RecyclerView.Adapter<KalendarItemAdapter.ViewHolder> {

    private List<Dogadjaj> itemList;
    private Dogadjaj sharedItem;
    private int sharedItemPosition;
    private AppCompatActivity activity;
    private Context context;
    boolean showShimmer = true;
    private int SHIMMER_ITEM_NUMBER = 6;
    private int pitanjePosition;
    private boolean pitanjeShow = false;
    private boolean doAnimation=true;
    private List<AlarmDate> konacnaListaAlarma=new ArrayList<>();
    private boolean cancelFlag=false;

    private ArrayList<Boolean> alarmVisibility = new ArrayList<>();


    public KalendarItemAdapter(List<Dogadjaj> itemList, List<AlarmDate> konacnaListaAlarma,ArrayList<Boolean> alarmVisibility) {
        this.itemList = itemList;
        this.konacnaListaAlarma=konacnaListaAlarma;
        this.alarmVisibility = alarmVisibility;

    }

    @NonNull
    @Override
    public KalendarItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_kalendar_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KalendarItemAdapter.ViewHolder holder, final int position) {

        context = holder.itemLayout.getContext();


        //pokupi iz baze sve spremljene datume za alarm
        final ArrayList<AlarmDate> list = new ArrayList<>();
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm1) {
                List<AlarmDate> resultList = realm.where(AlarmDate.class).findAll();
                list.addAll(resultList);
            }
        });

        //kopiraj alarme u dodatnu listu s kojom ćese baratati
        if (!list.isEmpty()) {
            konacnaListaAlarma=list;

            for(int i=0;i<konacnaListaAlarma.size();i++){
                String vrijemeSdatumom=konacnaListaAlarma.get(i).getDatum() + " " + konacnaListaAlarma.get(i).getVrijeme()+":00";
                Date vrijemeAlarma=null;
                try {
                    vrijemeAlarma=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(vrijemeSdatumom);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(vrijemeAlarma.before(new Date())){
                    for(int j=0;j<itemList.size();j++) {
                        if (konacnaListaAlarma.get(i).getDatum().equals(itemList.get(j).getDatum()) && konacnaListaAlarma.get(i).getNaslov().equals(itemList.get(j).getNaslov())) {
                            alarmVisibility.set(j,false);
                        }
                    }
                    final RealmResults<AlarmDate> results = realm.where(AlarmDate.class).equalTo("datum",konacnaListaAlarma.get(i).datum.toString()).equalTo("vrijeme",konacnaListaAlarma.get(i).vrijeme.toString()).equalTo("naslov",konacnaListaAlarma.get(i).naslov.toString()).findAll();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            results.deleteAllFromRealm();
                        }
                    });
                    konacnaListaAlarma.remove(i);
                    i=0;
                }
            }
        }


        final boolean visible = alarmVisibility.get(position);
        if(visible==true){
            holder.alarmLayout.setVisibility(View.VISIBLE);
            for(int i=0;i<konacnaListaAlarma.size();i++){
                if(konacnaListaAlarma.get(i).getNaslov().equals(itemList.get(position).getNaslov().toString()) && konacnaListaAlarma.get(i).getDatum().equals(itemList.get(position).getDatum().toString())){
                    holder.alarmTime.setText(konacnaListaAlarma.get(i).getVrijeme().toString());
                }
            }
            holder.alarm = true;
            holder.obavijest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_deletenotification));
        }
        else {
            holder.alarmLayout.setVisibility(View.GONE);
            holder.alarmTime.setText("");
            holder.alarm = false;
            holder.obavijest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_addnotification));
        }


        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            holder.naslov.setBackground(null);
            holder.opis.setBackground(null);
            holder.lokacija.setBackground(null);
            holder.datum.setBackground(null);
            holder.dan.setBackground(null);

            String[] parts = itemList.get(position).datum.split("/");
            String dan = parts[0];
            String mjesec = parts[1];

            String dateString=itemList.get(position).datum;
            Date date=null;
            try {
                date =new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            holder.naslov.setText(itemList.get(position).naslov);
            holder.opis.setText(itemList.get(position).opis);
            holder.lokacija.setText(itemList.get(position).lokacija);
            holder.datum.setText(dan+"/"+mjesec);
            holder.timeTime.setText(itemList.get(position).vrijeme);



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                holder.opis.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }

            //provjeravaj je li u listi alarma postoji trenuacni item i ako postoji postavi sliku alarma - tu je vjerojatno greška


            switch (dayOfWeek){
                case (2):
                    holder.dan.setText("Pon");
                    break;
                case (3):
                    holder.dan.setText("Uto");
                    break;
                case (4):
                    holder.dan.setText("Sri");
                    break;
                case (5):
                    holder.dan.setText("Čet");
                    break;
                case (6):
                    holder.dan.setText("Pet");
                    break;
                case (7):
                    holder.dan.setText("Sub");
                    break;
                case (1):
                    holder.dan.setText("Ned");
                    break;
            }

            TextPaint paint = holder.opis.getPaint();
            float width = paint.measureText(holder.opis.getText().toString());

            if (doAnimation)
                holder.itemLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));

            if (pitanjePosition == position && pitanjeShow) {

                holder.isExpanded = true;
                holder.opis.setMaxLines(100000);

                Shader textShader=new LinearGradient(0, 0,0, holder.opis.getPaint().getTextSize()*5,
                        new int[]{Color.parseColor("#143F61"),Color.parseColor("#143F61")},
                        new float[]{0, 1}, Shader.TileMode.CLAMP);
                holder.opis.getPaint().setShader(textShader);
                holder.lokacijaText.setVisibility(View.VISIBLE);
                holder.lokacija.setVisibility(View.VISIBLE);
                holder.obavijest.setVisibility(View.VISIBLE);
            } else {

                holder.isExpanded = false;
                holder.opis.setMaxLines(4);
                Shader textShader=new LinearGradient(0, 0,0, holder.opis.getPaint().getTextSize()*5,
                        new int[]{Color.parseColor("#143F61"),Color.TRANSPARENT},
                        new float[]{0, 1}, Shader.TileMode.CLAMP);
                holder.opis.getPaint().setShader(textShader);
                holder.lokacijaText.setVisibility(View.GONE);
                holder.lokacija.setVisibility(View.GONE);
                holder.obavijest.setVisibility(View.GONE);
            }


            //klik na gumb za dodavanje ili brisanje obavijesti
            holder.obavijest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<AlarmDate> list = new ArrayList<>();
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm1) {
                            List<AlarmDate> resultList = realm.where(AlarmDate.class).findAll();
                            list.addAll(resultList);
                        }
                    });

                    //kopiraj alarme u dodatnu listu s kojom ćese baratati
                    if (!list.isEmpty()) {
                        konacnaListaAlarma=list;
                    }
                    //ako nema alarma => dodaj alarm
                    if (holder.alarm == false) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        final TimePickerDialog mTimePicker;
                        //otvori dialog za odabir vremena alarma
                        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                String min = String.valueOf(selectedMinute);
                                String h = String.valueOf(selectedHour);
                                if (selectedHour < 10)
                                    holder.sati = "0" + h;
                                else
                                    holder.sati = h;

                                if (selectedMinute < 10)
                                    holder.minute = "0" + min;
                                else
                                    holder.minute = min;
                            }
                        }, hour, minute, true);
                        Toast.makeText(context,"Obavijest će stići na dan događaja u odabrano vrijeme!",Toast.LENGTH_SHORT).show();
                        //kad se stisne uredu onda povisibleani taj alarm i postavi zastavicu na true, cancel flag je zbog toga sto se i onDismiss i onCancel flag
                        // pozivaju kada se stisne uredu, pa se rusila apk, ugl.. nebitno, to je pomocna zastavica
                        mTimePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if(cancelFlag==false) {
                                    alarmVisibility.set(position, true);
                                    final boolean visible = alarmVisibility.get(position);
                                    if(visible==true){
                                        holder.alarmLayout.setVisibility(View.VISIBLE);
                                        holder.alarmTime.setText(holder.sati + ":" + holder.minute);
                                        holder.alarm = true;
                                        holder.obavijest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_deletenotification));
                                    }
                                    else {
                                        holder.alarmLayout.setVisibility(View.GONE);
                                        holder.alarmTime.setText("");
                                        holder.alarm = false;
                                        holder.obavijest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_addnotification));
                                    }
                                    AlarmDate alarmDate = new AlarmDate();
                                    alarmDate.setDatum(itemList.get(position).datum);
                                    alarmDate.setVrijeme(holder.sati + ":" + holder.minute);
                                    alarmDate.setNaslov(itemList.get(position).naslov);

                                    Random random = new Random();
                                    int randomID = random.nextInt(100000000);
                                    alarmDate.setAlarmID(randomID);

                                    //provjera postoji li vec takav alarm u bazi - mozda sada nebitno jer sam onemogucio da budu u bazi vise događaja istih parametara
                                    boolean dataExist = false;
                                    for (int i = 0; i < konacnaListaAlarma.size(); i++) {
                                        if (konacnaListaAlarma.get(i).getDatum().equals(alarmDate.getDatum()) && konacnaListaAlarma.get(i).getVrijeme().equals(alarmDate.getVrijeme()) && konacnaListaAlarma.get(i).getNaslov().equals(alarmDate.getNaslov()) && konacnaListaAlarma.get(i).getAlarmID() == alarmDate.getAlarmID())
                                            dataExist = true;
                                    }
                                    if (dataExist == false) {
                                        realm.beginTransaction();
                                        realm.copyToRealm(alarmDate);
                                        realm.commitTransaction();
                                        realm.close();
                                    }
                                    createNotificationChannel();
                                    setNotificationAlarm(itemList.get(position).datum.toString() + " " + holder.alarmTime.getText().toString(), randomID, holder.naslov.getText().toString(),holder.timeTime.getText().toString(),holder.lokacija.getText().toString());
                                }
                                else {
                                    cancelFlag=false;
                                }
                            }
                        });
                        mTimePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                final boolean visible = alarmVisibility.get(position);
                                holder.alarmLayout.setVisibility(View.GONE);
                                holder.alarmTime.setText("");
                                holder.alarm = false;
                                holder.obavijest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_addnotification));
                                alarmVisibility.set(position, false);
                                cancelFlag=true;
                                mTimePicker.dismiss();
                            }
                        });
                        mTimePicker.show();
                    }
                    //ako je postojao alarm, mozes ga samo obrisat
                    else{
                        //brisanje alarma i notifikacija za odabrano vrijeme
                        for(int i=0;i<konacnaListaAlarma.size();i++){
                            if(konacnaListaAlarma.get(i).getDatum().equals(itemList.get(position).datum) && konacnaListaAlarma.get(i).getNaslov().equals(itemList.get(position).naslov) && konacnaListaAlarma.get(i).getVrijeme().equals(holder.alarmTime.getText().toString())){
                                unsetNotificationAlarm(konacnaListaAlarma.get(i).alarmID);
                                konacnaListaAlarma.remove(i);
                                i=0;
                            }
                        }
                        final RealmResults<AlarmDate> results = realm.where(AlarmDate.class).equalTo("datum",itemList.get(position).datum).equalTo("vrijeme",holder.alarmTime.getText().toString()).equalTo("naslov",itemList.get(position).naslov).findAll();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                results.deleteAllFromRealm();
                            }
                        });

                        holder.alarmLayout.setVisibility(View.GONE);
                        holder.alarmTime.setText("");
                        holder.alarm = false;
                        holder.obavijest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_addnotification));
                        alarmVisibility.set(position, false);

                        realm.close();
                    }
                }
            });

            //klik na jedan cijeli item
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.isExpanded == false) {
                        pitanjePosition = position;
                        pitanjeShow = true;
                        doAnimation = false;
                        notifyDataSetChanged();
                    } else if (holder.isExpanded == true) {

                        pitanjeShow = false;
                        doAnimation = false;
                        notifyDataSetChanged();
                    }


                }
            });

        }

    }

    private void unsetNotificationAlarm(int randomID) {
        Intent intent=new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context, randomID,intent,0);
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void setNotificationAlarm(String vrijemeAlarmaString,int randomID,String naslov,String vrijeme,String lokacija) {
        Intent intent=new Intent(context, ReminderBroadcast.class);
        intent.putExtra("naslov",naslov);
        intent.putExtra("vrijeme",vrijeme);
        intent.putExtra("lokacija",lokacija);

        vrijemeAlarmaString=vrijemeAlarmaString + ":00";

        PendingIntent pendingIntent=PendingIntent.getBroadcast(context, randomID,intent,0);

        AlarmManager alarmManager=(AlarmManager)context.getSystemService(ALARM_SERVICE);
        Date vrijemeAlarma=null;
        try {
            vrijemeAlarma=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(vrijemeAlarmaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long vrijemeAlarmaMillis = vrijemeAlarma.getTime();

        alarmManager.set(AlarmManager.RTC_WAKEUP,vrijemeAlarmaMillis,pendingIntent);


    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Duhos Channel";
            String description = "Channel for Duhos Reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notify",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public int getItemCount() {
        return showShimmer ? SHIMMER_ITEM_NUMBER : itemList.size();
    }

    public Dogadjaj getDatum(int position){
        return itemList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView naslov,opis,lokacija,datum,dan,lokacijaText,alarmTime,timeTime;
        RelativeLayout itemLayout,datumLayout,contentLayout,alarmLayout;
        ImageButton obavijest;
        ImageView alarmIcon;
        ShimmerFrameLayout shimmerFrameLayout;
        private boolean isExpanded = false;
        private boolean alarm = false;

        String danMjeseca,mjesec,godina,sati,minute;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            naslov = itemView.findViewById(R.id.naslov);
            opis = itemView.findViewById(R.id.opis);
            lokacija = itemView.findViewById(R.id.lokacija);
            datum = itemView.findViewById(R.id.datum);
            dan = itemView.findViewById(R.id.dan);
            lokacijaText = itemView.findViewById(R.id.lokacija_tekst);
            obavijest=itemView.findViewById(R.id.obavijest);
            timeTime=itemView.findViewById(R.id.timeTime);

            alarmLayout=itemView.findViewById(R.id.alarmLayout);
            alarmTime=itemView.findViewById(R.id.alarmTime);
            alarmIcon=itemView.findViewById(R.id.alarmIcon);

            itemLayout=itemView.findViewById(R.id.itemLayout);
            datumLayout=itemView.findViewById(R.id.datumLayout);
            contentLayout=itemView.findViewById(R.id.contentLayout);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout_kalendar);
        }

    }
    public void shareItem(int position) {
        sharedItem = itemList.get(position);
        sharedItemPosition = position;
        itemList.remove(position);
        notifyItemRemoved(position);
        undoDelete();

        String opis = itemList.get(position).opis.toString();
        String naslov = itemList.get(position).naslov.toString();
        String vrijeme = itemList.get(position).vrijeme.toString();
        String lokacija = itemList.get(position).lokacija.toString();

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, naslov+"\n"+"Vrijeme: "+vrijeme+"\n"+"Lokacija: "+lokacija+"\n\n"+opis);
        share.putExtra(Intent.EXTRA_SUBJECT, naslov);
        share.setType("text/plain");
        context.startActivity(share.createChooser(share, "Share using"));

    }

    private void undoDelete() {
        itemList.add(sharedItemPosition, sharedItem);
        notifyItemInserted(sharedItemPosition);
    }
}