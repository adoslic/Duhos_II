package com.duhos.duhosii;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
    private boolean cancelKalendarFlag=false;
    private boolean cancelKalendarFlagAlarmLayout=false;
    private boolean cancelAlarmFlagAlarmLayout=false;
    int alarmIDDoSad;
    String vrijemeAlarmaDoSad="";
    String datumAlarmaDoSad="";
    int positionToDelete;
    String danToAdd;
    String mjesecToAdd;
    String godinaToAdd;
    String minToAdd;
    String hToAdd;
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
                        if(konacnaListaAlarma.get(i).getNaslov().toString().equals(itemList.get(j).getNaslov().toString())) {
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
            Date date=new Date();
            String todayDate=new SimpleDateFormat("dd/MM/yyyy").format(date);
            holder.alarmLayout.setVisibility(View.VISIBLE);
            for(int i=0;i<konacnaListaAlarma.size();i++) {
                    if (konacnaListaAlarma.get(i).getNaslov().toString().equals(itemList.get(position).getNaslov().toString())) {
                        String[] dateParts = konacnaListaAlarma.get(i).getDatum().toString().split("/");
                        if(konacnaListaAlarma.get(i).getDatum().toString().equals(todayDate.toString())){
                            holder.alarmTime.setText(konacnaListaAlarma.get(i).getVrijeme().toString());
                            holder.dateDate.setText(dateParts[0].toString()+"/"+dateParts[1].toString() );
                            holder.dateDate.setVisibility(View.GONE);
                            holder.alarmTime.setVisibility(View.VISIBLE);
                        }
                        else {
                            holder.alarmTime.setText(konacnaListaAlarma.get(i).getVrijeme().toString());
                            holder.dateDate.setText(dateParts[0].toString()+"/"+dateParts[1].toString());
                            holder.dateDate.setVisibility(View.VISIBLE);
                            holder.alarmTime.setVisibility(View.GONE);
                        }
                }
            }
            holder.alarm = true;
            holder.obavijest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_deletenotification));
        }
        else {
            holder.alarmLayout.setVisibility(View.GONE);
            holder.alarmTime.setText("");
            holder.dateDate.setText("");
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
            final String mjesec = parts[1];

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

                Shader textShader=new LinearGradient(0, 0,0, holder.opis.getPaint().getTextSize()*7,
                        new int[]{Color.parseColor("#143F61"),Color.parseColor("#143F61")},
                        new float[]{0, 1}, Shader.TileMode.CLAMP);
                holder.opis.getPaint().setShader(textShader);
                holder.lokacijaText.setVisibility(View.VISIBLE);
                holder.lokacija.setVisibility(View.VISIBLE);
                holder.obavijest.setVisibility(View.VISIBLE);
            } else {

                holder.isExpanded = false;
                holder.opis.setMaxLines(5);
                Shader textShader=new LinearGradient(0, 0,0, holder.opis.getPaint().getTextSize()*7,
                        new int[]{Color.parseColor("#143F61"),Color.TRANSPARENT},
                        new float[]{0, 1}, Shader.TileMode.CLAMP);
                holder.opis.getPaint().setShader(textShader);
                holder.lokacijaText.setVisibility(View.GONE);
                holder.lokacija.setVisibility(View.GONE);
                holder.obavijest.setVisibility(View.GONE);
            }


            holder.alarmLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for(int i=0;i<konacnaListaAlarma.size();i++) {
                        if (konacnaListaAlarma.get(i).getNaslov().toString().equals(itemList.get(position).getNaslov().toString())) {
                            vrijemeAlarmaDoSad=konacnaListaAlarma.get(i).getVrijeme().toString();
                            datumAlarmaDoSad=konacnaListaAlarma.get(i).getDatum().toString();
                            alarmIDDoSad=konacnaListaAlarma.get(i).alarmID;
                            positionToDelete=i;
                        }
                    }
                    cancelKalendarFlagAlarmLayout=false;
                    Calendar mcurrentTime = Calendar.getInstance();
                    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    final int minute = mcurrentTime.get(Calendar.MINUTE);
                    int my_day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                    int my_month = mcurrentTime.get(Calendar.MONTH);
                    int my_year = mcurrentTime.get(Calendar.YEAR);
                    final DatePickerDialog mDatePicker;
                    mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                             danToAdd = String.valueOf(dayOfMonth);
                             mjesecToAdd = String.valueOf(month+1);
                             godinaToAdd = String.valueOf(year);

                            if (dayOfMonth < 10)
                                danToAdd = "0" + danToAdd;
                            else
                                danToAdd = danToAdd;
                            if (month+1 < 10)
                                mjesecToAdd= "0" + mjesecToAdd;
                            else
                                mjesecToAdd = mjesecToAdd;
                            godinaToAdd = godinaToAdd;

                        }
                    }, my_year, my_month, my_day);
                    mDatePicker.show();

                    mDatePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if(!cancelKalendarFlagAlarmLayout){
                                final TimePickerDialog mTimePicker;
                                //otvori dialog za odabir vremena alarma
                                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                        minToAdd = String.valueOf(selectedMinute);
                                        hToAdd = String.valueOf(selectedHour);
                                        if (selectedHour < 10)
                                            hToAdd = "0" + hToAdd;
                                        else
                                            hToAdd = hToAdd;

                                        if (selectedMinute < 10)
                                            minToAdd = "0" + minToAdd;
                                        else
                                            minToAdd = minToAdd;
                                    }
                                }, hour, minute, true);

                                mTimePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        if(!cancelAlarmFlagAlarmLayout){
                                            unsetNotificationAlarm(alarmIDDoSad);
                                            final RealmResults<AlarmDate> results = realm.where(AlarmDate.class).equalTo("datum",konacnaListaAlarma.get(positionToDelete).getDatum().toString()).equalTo("vrijeme",konacnaListaAlarma.get(positionToDelete).getVrijeme().toString()).equalTo("naslov",konacnaListaAlarma.get(positionToDelete).naslov).findAll();
                                            realm.executeTransaction(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm realm) {
                                                    results.deleteAllFromRealm();
                                                }
                                            });
                                            konacnaListaAlarma.remove(positionToDelete);

                                            AlarmDate alarmDate = new AlarmDate();
                                            //*******************************************ovo sam dodao
                                            alarmDate.setDatum(danToAdd+"/"+mjesecToAdd+"/"+godinaToAdd);
                                            alarmDate.setVrijeme(hToAdd + ":" + minToAdd);
                                            alarmDate.setNaslov(itemList.get(position).naslov);

                                            Date date=new Date();
                                            String todayDate=new SimpleDateFormat("dd/MM/yyyy").format(date);
                                            if(todayDate.equals(danToAdd+"/"+mjesecToAdd+"/"+godinaToAdd)){
                                                holder.alarmTime.setText(hToAdd + ":" + minToAdd);
                                                holder.dateDate.setText(danToAdd+"/"+mjesecToAdd);
                                                holder.dateDate.setVisibility(View.GONE);
                                                holder.alarmTime.setVisibility(View.VISIBLE);
                                            }
                                            else{
                                                holder.alarmTime.setText(hToAdd + ":" + minToAdd);
                                                holder.dateDate.setText(danToAdd+"/"+mjesecToAdd);
                                                holder.dateDate.setVisibility(View.VISIBLE);
                                                holder.alarmTime.setVisibility(View.GONE);
                                            }

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
                                                konacnaListaAlarma.add(alarmDate);
                                                realm.beginTransaction();
                                                realm.copyToRealm(alarmDate);
                                                realm.commitTransaction();
                                                realm.close();
                                            }
                                            createNotificationChannel();
//*******************************************dodao prvi parametar novi datum  i zadnja dva da se ispise u notifikaciji
                                            setNotificationAlarm(danToAdd+"/"+mjesecToAdd+"/"+godinaToAdd + " " + hToAdd + ":" + minToAdd, randomID, holder.naslov.getText().toString(), holder.lokacija.getText().toString(),itemList.get(position).getDatum().toString(),itemList.get(position).getVrijeme().toString());




                                            //Toast.makeText(context,"OK",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                mTimePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        cancelAlarmFlagAlarmLayout=true;
                                        //Toast.makeText(context,"CANCEL",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                mTimePicker.show();
                                //Toast.makeText(context,"OK",Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                    mDatePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //Toast.makeText(context,"CANCEL",Toast.LENGTH_SHORT).show();
                            cancelKalendarFlagAlarmLayout=true;
                        }
                    });
                }
            });


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
                        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        final int minute = mcurrentTime.get(Calendar.MINUTE);

                        int my_day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                        int my_month = mcurrentTime.get(Calendar.MONTH);
                        int my_year = mcurrentTime.get(Calendar.YEAR);
//*******************************************ovo sam dodao i ugnijezdio timePicker u njega i postavio neke flagove za cancel i dismiss
                        final DatePickerDialog mDatePicker;
                        mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String dan = String.valueOf(dayOfMonth);
                                String mjesec = String.valueOf(month+1);
                                String godina = String.valueOf(year);

                                if (dayOfMonth < 10)
                                    holder.danMjeseca = "0" + dan;
                                else
                                    holder.danMjeseca = dan;
                                if (month+1 < 10)
                                    holder.mjesec = "0" + mjesec;
                                else
                                    holder.mjesec = mjesec;
                                holder.godina = godina;

                            }
                        }, my_year, my_month, my_day);
                        mDatePicker.show();

                            mDatePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    if(!cancelKalendarFlag){
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
                                    //Toast.makeText(context,context.getResources().getString(R.string.kadObavijestStize),Toast.LENGTH_SHORT).show();
                                    //kad se stisne uredu onda povisibleani taj alarm i postavi zastavicu na true, cancel flag je zbog toga sto se i onDismiss i onCancel flag
                                    // pozivaju kada se stisne uredu, pa se rusila apk, ugl.. nebitno, to je pomocna zastavica
                                    mTimePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            if (cancelFlag == false) {
//*******************************************ovdje sam stavio novi datum
                                                String vrijemeSdatumom = holder.danMjeseca+"/"+holder.mjesec+"/"+holder.godina + " " + holder.sati + ":" + holder.minute + ":00";


                                                Date vrijemeAlarma = null;
                                                try {
                                                    vrijemeAlarma = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(vrijemeSdatumom);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                if (vrijemeAlarma.before(new Date())) {
                                                    Toast.makeText(context, context.getResources().getString(R.string.odabranoJeProsloVrijeme), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    alarmVisibility.set(position, true);
                                                    final boolean visible = alarmVisibility.get(position);
                                                    if (visible == true) {
                                                        holder.alarmLayout.setVisibility(View.VISIBLE);
                                                        Date date=new Date();
                                                        String todayDate=new SimpleDateFormat("dd/MM/yyyy").format(date);
                                                        if(todayDate.equals(holder.danMjeseca+"/"+holder.mjesec+"/"+holder.godina)){
                                                            holder.alarmTime.setText(holder.sati + ":" + holder.minute);
                                                            holder.dateDate.setText(holder.danMjeseca+"/"+holder.mjesec);
                                                            holder.dateDate.setVisibility(View.GONE);
                                                            holder.alarmTime.setVisibility(View.VISIBLE);
                                                        }
                                                        else{
                                                            holder.alarmTime.setText(holder.sati + ":" + holder.minute);
                                                            holder.dateDate.setText(holder.danMjeseca+"/"+holder.mjesec);
                                                            holder.dateDate.setVisibility(View.VISIBLE);
                                                            holder.alarmTime.setVisibility(View.GONE);
                                                        }
                                                        holder.alarm = true;
                                                        holder.obavijest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_deletenotification));
                                                    } else {
                                                        holder.dateDate.setVisibility(View.VISIBLE);
                                                        holder.alarmTime.setVisibility(View.VISIBLE);
                                                        holder.alarmLayout.setVisibility(View.GONE);
                                                        holder.dateDate.setText("");
                                                        holder.alarmTime.setText("");
                                                        holder.alarm = false;
                                                        holder.obavijest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_addnotification));
                                                    }
                                                    AlarmDate alarmDate = new AlarmDate();


  //*******************************************ovo sam dodao
                                                    alarmDate.setDatum(holder.danMjeseca+"/"+holder.mjesec+"/"+holder.godina);


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
                                                        konacnaListaAlarma.add(alarmDate);
                                                        realm.beginTransaction();
                                                        realm.copyToRealm(alarmDate);
                                                        realm.commitTransaction();
                                                        realm.close();
                                                    }
                                                    createNotificationChannel();
//*******************************************dodao prvi parametar novi datum  i zadnja dva da se ispise u notifikaciji
                                                    setNotificationAlarm(holder.danMjeseca+"/"+holder.mjesec+"/"+holder.godina + " " + holder.alarmTime.getText().toString(), randomID, holder.naslov.getText().toString(), holder.lokacija.getText().toString(),itemList.get(position).getDatum().toString(),itemList.get(position).getVrijeme().toString());
                                                }
                                            } else {
                                                cancelFlag = false;
                                            }

                                        }
                                    });
                                    mTimePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            final boolean visible = alarmVisibility.get(position);
                                            holder.alarmLayout.setVisibility(View.GONE);
                                            holder.dateDate.setText("");
                                            holder.alarmTime.setText("");
                                            holder.alarm = false;
                                            holder.obavijest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_addnotification));
                                            alarmVisibility.set(position, false);
                                            cancelFlag = true;
                                            mTimePicker.dismiss();
                                        }
                                    });
                                    mTimePicker.show();
                                }
                                    else{
                                        cancelKalendarFlag=false;
                                    }
                            }
                            });
                            mDatePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    final boolean visible = alarmVisibility.get(position);
                                    holder.alarmLayout.setVisibility(View.GONE);
                                    holder.alarmTime.setText("");
                                    holder.dateDate.setText("");
                                    holder.alarm = false;
                                    holder.obavijest.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_addnotification));
                                    alarmVisibility.set(position, false);
                                    cancelFlag = true;
                                    mDatePicker.dismiss();
                                    cancelKalendarFlag = true;
                                }
                            });


                    }
                    //ako je postojao alarm, mozes ga samo obrisat
                    else{
                        //brisanje alarma i notifikacija za odabrano vrijeme
                        String datumZaBrisanje="";
                        for(int i=0;i<konacnaListaAlarma.size();i++){
                            if( konacnaListaAlarma.get(i).getNaslov().equals(itemList.get(position).naslov) && konacnaListaAlarma.get(i).getVrijeme().equals(holder.alarmTime.getText().toString())){
                                unsetNotificationAlarm(konacnaListaAlarma.get(i).alarmID);
                                datumZaBrisanje=konacnaListaAlarma.get(i).getDatum().toString();
                                konacnaListaAlarma.remove(i);
                                i=0;
                            }
                        }
                        final RealmResults<AlarmDate> results = realm.where(AlarmDate.class).equalTo("datum",datumZaBrisanje).equalTo("vrijeme",holder.alarmTime.getText().toString()).equalTo("naslov",itemList.get(position).naslov).findAll();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                results.deleteAllFromRealm();
                            }
                        });

                        holder.alarmLayout.setVisibility(View.GONE);
                        holder.alarmTime.setText("");
                        holder.dateDate.setText("");
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setNotificationAlarm(String vrijemeAlarmaString, int randomID, String naslov, String lokacija,String datum,String vrijeme) {
        Intent intent=new Intent(context, ReminderBroadcast.class);
        intent.putExtra("naslov",naslov);
//******************dodao da ispise u notifikaciji
        intent.putExtra("datum",datum);
        intent.putExtra("vrijeme",vrijeme);

        intent.putExtra("lokacija",lokacija);
        intent.putExtra("randomID",randomID);

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

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,vrijemeAlarmaMillis,pendingIntent);


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

        TextView naslov,opis,lokacija,datum,dan,lokacijaText,alarmTime,timeTime,dateDate;
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
            dateDate=itemView.findViewById(R.id.dateDate);

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
        String datum = itemList.get(position).datum.toString();

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, naslov+"\n"+"Datum: "+datum+"\n"+"Vrijeme: "+vrijeme+"\n"+"Lokacija: "+lokacija+"\n\n"+"Opis: "+opis);
        share.putExtra(Intent.EXTRA_SUBJECT, naslov);
        share.setType("text/plain");
        context.startActivity(share.createChooser(share, "Share using"));
    }

    private void undoDelete() {
        itemList.add(sharedItemPosition, sharedItem);
        notifyItemInserted(sharedItemPosition);
    }
}