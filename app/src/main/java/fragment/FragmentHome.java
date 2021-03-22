package fragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.servicosagenda.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import UTIL.Util;

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

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ValueEventListener valueEventListener;

    private String latitude_Empresa;
    private String longitude_Empresa;


    //Metodo construtor
    public FragmentHome() {
        // Required empty public constructor
    }

    //Para chamada para o banco de dados
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("BD").child("Home").child("Dados");

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

                click_ContatoTelefonico();

                break;

            case R.id.cardView_Home_MapaLocalizacao:

                click_MapaLocalizacao();

                break;
             }
    }

    private void click_ContatoTelefonico(){

        String numero = textViewNumeroContato.getText().toString();
        if (!numero.isEmpty()){
            dialogContato(numero);

        }else{
            Toast.makeText(getContext(),"Numero de contato Indisponível!", Toast.LENGTH_LONG).show();
        }
    }

    private void click_MapaLocalizacao(){
        if (!latitude_Empresa.isEmpty() && !longitude_Empresa.isEmpty()){
            if (Util.statusInternet_MoWi(getContext())){
                abrirLocalizacaoEmpresa();
            }else{
                Toast.makeText(getContext(),"Erro de conexao com a Internet", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(getContext(),"Localização indisponível", Toast.LENGTH_LONG).show();
        }
    }
    //----------------------------------- AÇAO DE CLICK ABRIR ENDERECO ----------------------------------------
        private void abrirLocalizacaoEmpresa(){

        String url = "https://www.google.com/maps/search/?api=1&query="+latitude_Empresa+longitude_Empresa;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);

    }
    //----------------------------------- AÇAO DE DIALOG CONTATO WHATSAPP E LIGAÇÃO ----------------------------


    private void dialogContato(String numero){

        String contato = numero
                .replace(" ","")
                .replace("-","")
                .replace("(","")
                .replace(")","");

       StringBuffer numeroContato = new StringBuffer(contato);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Entrar em contato")
                .setMessage("O que gostaria de fazer?")
                .setPositiveButton("WhatsApp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        entrarEmContatoWhatsApp(numeroContato);
                    }
                }).setNegativeButton("Ligar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        entarEmContatoLigacao(numeroContato);
                    }
                });
        builder.show();
    }

    private void entrarEmContatoWhatsApp(StringBuffer numeroContato){


        try {
            numeroContato.deleteCharAt(0);
            numeroContato.deleteCharAt(2);

            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));

            intent.putExtra("jid",
                    PhoneNumberUtils.stripSeparators("55" + numeroContato)+ "@s.whatsapp.net");
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(getContext(),"Erro - Verifique se o WhatsApp esta instalado no seu Celular",Toast.LENGTH_LONG).show();
        }

    }

   private void entarEmContatoLigacao(StringBuffer numeroContato){

        Uri uri = Uri.parse("tel:" + numeroContato);
        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        startActivity(intent);
    }
//__________________________________________ OUVINTE FIREBASE ________________________________________________________________

    private void ouvinte(){

        if (valueEventListener == null){
            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String imagemUrl = dataSnapshot.child("imagemUrl").getValue(String.class);
                        String informacao = dataSnapshot.child("informacao").getValue(String.class);
                        String latitude = dataSnapshot.child("latitude").getValue(String.class);
                        String longitude = dataSnapshot.child("longitude").getValue(String.class);
                        String numeroContato = dataSnapshot.child("numeroContato").getValue(String.class);
                        String valorServico = dataSnapshot.child("valorServico").getValue(String.class);

                            if(!informacao.isEmpty() && !valorServico.isEmpty()){
                                atualizarDados(imagemUrl,informacao,latitude,longitude,valorServico,numeroContato);
                            }
                      }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            reference.addValueEventListener(valueEventListener);
        }
    }

    private void atualizarDados(String imagemUrl,String informacao,String latitude,String longitude,
                                String valorServico,String numeroContato){

        textView_Informacao.setText(informacao);
        textView_ValorServico.setText(valorServico);
        textViewNumeroContato.setText(numeroContato);

        //Dados do Google Maps
        latitude_Empresa = latitude;
        longitude_Empresa = longitude;


        Glide.with(getContext()).asBitmap().load(imagemUrl).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                Toast.makeText(getContext(),"Erro ao realizar Donwload de imagem",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(imageView);
    }

//__________________________________________ CICLO DE VIDA FRAGMENT ________________________________________________________________

    @Override
    public void onStart() {
        super.onStart();

       ouvinte();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (valueEventListener != null){
            reference.removeEventListener(valueEventListener);
            valueEventListener = null;
        }
    }
}