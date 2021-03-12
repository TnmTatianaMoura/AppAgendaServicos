package UTIL;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class Util {


// Classe de coneção com a Internet
public static boolean statusInternet_MoWi(Context context) {

    boolean status = false;
    ConnectivityManager conexao = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    if (conexao != null){

        // PARA DISPOSTIVOS NOVOS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            NetworkCapabilities recursosRede = conexao.getNetworkCapabilities(conexao.getActiveNetwork());

            if (recursosRede != null) {//VERIFICAMOS SE RECUPERAMOS ALGO

                if (recursosRede.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                    //VERIFICAMOS SE DISPOSITIVO TEM 3G
                    return true;

                }
                else if (recursosRede.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                    //VERIFICAMOS SE DISPOSITIVO TEM WIFFI
                    return true;

                }

                //NÃO POSSUI UMA CONEXAO DE REDE VÁLIDA

                return false;

            }

        } else {//COMECO DO ELSE

            // PARA DISPOSTIVOS ANTIGOS  (PRECAUÇÃO)         MESMO CODIGO
            NetworkInfo informacao = conexao.getActiveNetworkInfo();


            if (informacao != null && informacao.isConnected()) {
                status = true;
            } else
                status = false;

            return status;

        }//FIM DO ELSE
    }
    return false;
    }
}
