package Logica;

import AccesoADatos.RetroalimentacionActividadDB;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bitacora {
    private Logger logger;

    public Bitacora (String nombre) {
        logger = Logger.getLogger(nombre);

        try {
            logger.addHandler(new FileHandler("./logs/bitacoraCOIL.log"));
        }
        catch (IOException error) {
            System.err.println("Ocurrió un error al iniciar el Logger");
            throw new RuntimeException(error);
        }
    }

    public void escribirError (Throwable error) {
        logger.log(Level.SEVERE, error.getMessage(), error);
    }

    public void escribirInformacion (String mensaje) {
        logger.info(mensaje);
    }
}
