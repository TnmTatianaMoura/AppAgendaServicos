package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.servicosagenda.R;

import java.util.ArrayList;
import java.util.List;

import adapter.AdapterListView;

public class HorariosActivity extends AppCompatActivity implements AdapterListView.ClickItemListView {

    private ListView listView;
    private AdapterListView adapterListView;
    private List<String> horarios = new ArrayList<String>();



    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        horarios.add("7:00 Hrs");
        horarios.add("8:00 Hrs");
        horarios.add("9:00 Hrs");
        horarios.add("10:00 Hrs");
        horarios.add("11:00 Hrs");
        horarios.add("13:00 Hrs");
        horarios.add("14:00 Hrs");
        horarios.add("15:00 Hrs");
        horarios.add("16:00 Hrs");


        listView = (ListView)findViewById(R.id.ListView);

        adapterListView = new AdapterListView(this,horarios,this);

        listView.setAdapter(adapterListView);

        //Recuperar os dados da activit calendario
        data = getIntent().getStringArrayListExtra("data");

        Toast.makeText(this,
                "Informações da nova Activit\n\nDia: "+data.get(0)+"\nMes: "+data.get(1)+"\nAno: "+data.get(2),Toast.LENGTH_LONG).show();

    }

    @Override
    public void clickItem(String horario, int posicao) {

        Toast.makeText(getBaseContext(),horario,Toast.LENGTH_LONG).show();
    }
}