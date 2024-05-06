package Utilidades;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

import java.io.IOException;

public class VerificadorBitacora implements TriggeringEventEvaluator {
    private static final Logger BITACORA = Logger.getLogger(VerificadorBitacora.class);
    @Override
    public boolean isTriggeringEvent (LoggingEvent evento) {
        boolean hayConexionInternet = false;
        boolean esNivelFatal = evento.getLevel().isGreaterOrEqual(Level.FATAL);
        if (esNivelFatal) {
            try {
                hayConexionInternet = ComprobadorInternet.comprobarConexion();
                NotificacionErroresCOILBot botTelegram = NotificacionErroresCOILBot.getInstanciaBot();
                botTelegram.enviarMensaje(evento);
            }
            catch (IOException error) {
                BITACORA.error("Error al verificar la conexión a internet " + error.getMessage());
            }
        }
        return hayConexionInternet && esNivelFatal;
    }
}