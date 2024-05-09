package Utilidades;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManejadorCorreo {
    private ExecutorService executorService;
    private static ManejadorCorreo instancia;

    private ManejadorCorreo () {
        executorService = Executors.newSingleThreadExecutor();
    }
    public static ManejadorCorreo getInstancia () {
        if (instancia == null) {
            instancia = new ManejadorCorreo();
        }
        return instancia;
    }

    public void enviarCorreoHilo(String destinatario, String tema, String contenido) throws ErrorDAO {
        executorService.submit( () -> {
            Correo correo = new Correo();
            correo.setDestinario(destinatario);
            correo.setTema(tema);
            correo.setContenido(contenido);
            correo.crearCorreo();
            correo.enviarCorreo();
        });
    }

}
