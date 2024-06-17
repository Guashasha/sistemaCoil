package Utilidades;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

import java.io.IOException;

/**
 * Verificador de bitácora que evalúa si un evento de logging debería desencadenar una acción.
 */
public class VerificadorBitacora implements TriggeringEventEvaluator {
    private static final Logger BITACORA = Logger.getLogger(VerificadorBitacora.class);

    /**
     * Evalúa si un evento de logging debería desencadenar una acción.
     *
     * @param evento el evento de logging a evaluar.
     * @return true si el evento es de nivel FATAL y hay conexión a Internet, false en caso contrario.
     */
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