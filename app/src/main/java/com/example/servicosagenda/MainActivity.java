package com.example.servicosagenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fragment.FragmentCalendario;
import fragment.FragmentHome;

public class MainActivity extends AppCompatActivity {


    //declarando as variaveis

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;

    //Fragmentes
    private Fragment fragment;
    private FragmentHome fragmentHome;
    private FragmentCalendario fragmentCalendario;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ações dos textViews

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigationBottom();

        //estanciando as classes fragments
        fragmentHome = new FragmentHome();
        fragmentCalendario = new FragmentCalendario();


        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.framealayout_Fragment,fragmentHome).commit();

    }

    //Metodo contrutor para os botoes

    private void navigationBottom(){

        //metodo de verificação
        onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.item_navegacao_home:

                            fragment = fragmentHome;
                        break;
                    case R.id.item_navegacao_calendario:

                            fragment = fragmentCalendario;
                        break;
                }

                    getSupportFragmentManager().beginTransaction().replace(R.id.framealayout_Fragment, fragment).commit();


                return true;
            }
        };
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }
}