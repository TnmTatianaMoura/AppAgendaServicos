package fragment;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.servicosagenda.R;
import com.google.android.gms.common.internal.Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import UTIL.Util;
import activity.HorariosActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCalendario extends Fragment implements CalendarView.OnDateChangeListener {

    // TODO: Definindo parametros
    private CalendarView calendarView;

    private int dia_Atual;
    private int mes_Atual;
    private int ano_Atual;

    public FragmentCalendario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        calendarView = (CalendarView) view.findViewById(R.id.calendarWiew_Calendario);

        obterDataAtual();
        configurarCalendario();

        calendarView.setOnDateChangeListener(this);

        versaoLollipop();

        return view;
    }

    //__________________________________ OBTER DATA ATUAL _______________________________________________

    private void obterDataAtual() {

        long datalong = calendarView.getDate() ;

        Locale locale = new Locale("pt", "br");

        SimpleDateFormat dia = new SimpleDateFormat("dd",locale);
        SimpleDateFormat mes = new SimpleDateFormat("MM",locale);
        SimpleDateFormat ano = new SimpleDateFormat("yyyy",locale);

        dia_Atual = Integer.parseInt(dia.format(datalong));
        mes_Atual = Integer.parseInt(mes.format(datalong));
        ano_Atual = Integer.parseInt(ano.format(datalong));

        Toast.makeText(getContext(),
                "Dia: "+dia_Atual+ "\nMes: "+mes_Atual+ "\nAno: "+ano_Atual,
                Toast.LENGTH_LONG).show();
    }

    //__________________________________ CONFIGURAR CALENDARIO _______________________________________________

    private void configurarCalendario(){

        Calendar dataMinima = configurarDataMinima();
        Calendar dataMaxima = configurarDataMaxima();

        calendarView.setMinDate(dataMinima.getTimeInMillis());
        calendarView.setMaxDate(dataMaxima.getTimeInMillis());

    }

    private Calendar configurarDataMinima(){

        Calendar dataMinima = Calendar.getInstance();
        int diaInicioCalendario = 1;

        dataMinima.set(ano_Atual,mes_Atual-1,diaInicioCalendario);

        return dataMinima;
    }

    private Calendar configurarDataMaxima(){

        Calendar dataMaxima = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        int diaFinalCalendario = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (dia_Atual == diaFinalCalendario){// usuario só vai cair nele se ele estiver no ultimo dia do mes

            dataMaxima.set(ano_Atual,mes_Atual,15);

        }else{

            dataMaxima.set(ano_Atual,mes_Atual-1,diaFinalCalendario);
        }
        return  dataMaxima;
    }
    //__________________________________ CLICK CALENDARIO _______________________________________________
    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView,
                                    int anoSelecionado,
                                    int mesSelecionado,
                                    int diaSelecionado) {

        int  mes = mesSelecionado + 1;

        //Toast.makeText(getContext(),
               // "Dia: "+diaSelecionado+ "\nMes: "+mes+ "\nAno: "+anoSelecionado,
               // Toast.LENGTH_LONG).show();

        dataSelecionada(diaSelecionado,mes,anoSelecionado);
    }

    private void dataSelecionada(int diaSelecionado, int mesSelecionado, int anoSelecionado) {

        Locale locale = new Locale("pt", "br");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy",locale);

        Calendar data = Calendar.getInstance();

        try {
            data.setTime(simpleDateFormat.parse(diaSelecionado+"/"+mesSelecionado+"/"+anoSelecionado));

            boolean disponivelAgendamento;

            // realizar uma verificação

            if (mesSelecionado != mes_Atual){

                disponivelAgendamento = true;
            }else{
                disponivelAgendamento = agendaDisponivel(data,diaSelecionado);
            }


            //realizar uma verificação
            if (disponivelAgendamento){

                if (Util.statusInternet_MoWi(getContext())){

                    String dia = String.valueOf(diaSelecionado);
                    String mes = String.valueOf(mesSelecionado);
                    String ano = String.valueOf(anoSelecionado);

                    ArrayList<String> dataList = new ArrayList<String>();

                    dataList.add(dia);
                    dataList.add(mes);
                    dataList.add(ano);

                    Intent intent = new Intent(getContext(), HorariosActivity.class);
                    intent.putExtra("data", dataList);
                    startActivity(intent);//Chamada da proxima activit

                }else {

                    Toast.makeText(getContext(), "Erro - Sem conexão com a Internet", Toast.LENGTH_LONG).show();
                }

                String dia = String.valueOf(diaSelecionado);
                String mes = String.valueOf(mesSelecionado);
                String ano = String.valueOf(anoSelecionado);

                ArrayList<String> dataList = new ArrayList<String>();

                dataList.add(dia);
                dataList.add(mes);
                dataList.add(ano);

                Intent intent = new Intent(getContext(), HorariosActivity.class);
                intent.putExtra("data", dataList);
                startActivity(intent);//Chamada da proxima activit
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean agendaDisponivel(Calendar data, int diaSelecionado) {

        Calendar calendar = Calendar.getInstance();

        int diaFinalCalendario = calendar.getActualMaximum(mes_Atual-1);

        if (diaFinalCalendario == dia_Atual){
            Toast.makeText(getContext(),
                    "Agendamento disponível.",
                    Toast.LENGTH_LONG).show();

        }

        if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            Toast.makeText(getContext(),
                    "Infelismente não trabalhamos no Domingo",
                    Toast.LENGTH_LONG).show();
            return false;

        }else if (diaSelecionado <= dia_Atual){
            Toast.makeText(getContext(),
                    "Agendamento disponivel do dia "+(dia_Atual+1)+" para frente",

                    Toast.LENGTH_LONG).show();
            return false;

        }else{

            return true;

        }
    }

    //---------------------------------------- VERSOES ANTIGAS ANDROID ----------------------------------------
    private void versaoLollipop(){

        int versao = Build.VERSION.SDK_INT;

        if( versao <= Build.VERSION_CODES.LOLLIPOP){

            WindowManager windowManager = (WindowManager)getActivity().getSystemService(getActivity().WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();

            Point tamanho = new Point();
            display.getSize(tamanho);

            int width = tamanho.x;

            if (width == 480){

                calendarView.getLayoutParams().width = 730;
                calendarView.getLayoutParams().height = 500;

            }else{

                calendarView.getLayoutParams().width = 800;
                calendarView.getLayoutParams().height = 650;
            }

        }else{
            // versoes mais novas do android. Não precisa configurar manualmente o calendario.
        }
    }
}

