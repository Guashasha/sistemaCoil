package Utilidades;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ComprobadorInternet {
    public static boolean comprobarConexion () throws IOException {
            URL url = new URL("https://www.google.com");
            HttpURLConnection conexionUrl = (HttpURLConnection) url.openConnection();
            conexionUrl.connect();
            return conexionUrl.getResponseCode() == 200;
    }
}
