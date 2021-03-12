package fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicosagenda.R;

/**
 *
 */
public class FragmentHome extends Fragment implements View.OnClickListener {

    // TODO: Definindo os parametros
    private ImageView imageView;
    private ProgressBar progressBar;

    private TextView textView_Informacao;
    private TextView textView_ValorServico;
    private TextView textViewNumeroContato;

    private CardView cardView_Ligacao;
    private  CardView cardView_Localizacao;


    //Metodo construtor
    public FragmentHome() {
        // Required empty public constructor
    }

    public FragmentHome(CardView cardView_Localizacao) {
        this.cardView_Localizacao = cardView_Localizacao;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageView = (ImageView) view.findViewById(R.id.imagemView_Home);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_Home);

        textView_Informacao = (TextView) view.findViewById(R.id.textView_Home_Informacao);
        textView_ValorServico = (TextView) view.findViewById(R.id.textView_Home_ValorServico);
        textViewNumeroContato = (TextView) view.findViewById(R.id.textView_Home_NumeroContato);


        cardView_Ligacao = (CardView) view.findViewById(R.id.cardView_Home_ContatoTelefone);
        cardView_Localizacao = (CardView) view.findViewById(R.id.cardView_Home_MapaLocalizacao);

        cardView_Ligacao.setOnClickListener(this);
        cardView_Localizacao.setOnClickListener(this);

        return view;

    }
       //----------------------------------- AÇAO DE CLICK CARDVIEW-----------------------------
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.cardView_Home_ContatoTelefone:
                Toast.makeText(getContext(),"CardView Numero de Telefone", Toast.LENGTH_LONG).show();
                break;

            case R.id.cardView_Home_MapaLocalizacao:
                Toast.makeText(getContext(),"CardView Numero de Mapa de Localização", Toast.LENGTH_LONG).show();
                break;

    }
    }

}