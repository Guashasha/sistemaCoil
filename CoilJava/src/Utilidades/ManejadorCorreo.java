package Utilidades;

import InterfazGrafica.ConsultaColaboracionControlador;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ManejadorCorreo {
    private static final Logger BITACORA = Logger.getLogger(ManejadorCorreo.class);

    private ExecutorService executorService;
    private static ManejadorCorreo instancia;

    private ManejadorCorreo() {
    }

    public static synchronized ManejadorCorreo getInstancia() {
        if (instancia == null) {
            instancia = new ManejadorCorreo();
        }
        return instancia;
    }

    private synchronized void iniciarExecutorService() {
        if (executorService == null || executorService.isShutdown() || executorService.isTerminated()) {
            executorService = Executors.newSingleThreadExecutor();
        }
    }

    public void enviarCorreoHilo(String destinatario, String tema, String contenido) throws ErrorDAO {
        iniciarExecutorService();
        executorService.submit(() -> {
            try {
                Correo correo = new Correo();
                correo.setDestinario(destinatario);
                correo.setTema(tema);
                correo.setContenido(contenido);
                correo.crearCorreo();
                correo.enviarCorreo();
            } finally {
                cerrarExecutorService();
            }
        });
    }

    private void cerrarExecutorService() {
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                    if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                        BITACORA.fatal("El hilo executor no se cerró");
                    }
                }
            } catch (InterruptedException ie) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
