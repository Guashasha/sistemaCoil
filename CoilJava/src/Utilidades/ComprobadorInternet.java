package Utilidades;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Clase que verifica la conexión a Internet.
 */
public class ComprobadorInternet {

    /**
     * Comprueba si hay conexión a Internet realizando una solicitud a Google.
     *
     * @return true si la conexión es exitosa (código de respuesta 200), false en caso contrario.
     * @throws IOException si ocurre un error al intentar conectarse.
     */
    public static boolean comprobarConexion () throws IOException {
            URL url = new URL("https://www.google.com");
            HttpURLConnection conexionUrl = (HttpURLConnection) url.openConnection();
            conexionUrl.connect();
            return conexionUrl.getResponseCode() == 200;
    }
}
