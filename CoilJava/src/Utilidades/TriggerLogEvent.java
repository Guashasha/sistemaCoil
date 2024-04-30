package Utilidades;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class TriggerLogEvent implements TriggeringEventEvaluator {

    @Override
    public boolean isTriggeringEvent (LoggingEvent event) {
        boolean nivelEvento = event.getLevel()
                                   .isGreaterOrEqual(Level.FATAL);

        if (nivelEvento) {
            NotificacionErroresCOILBot botTelegram = NotificacionErroresCOILBot.getInstanciaBot();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String fechaHora = formatoFecha.format(new Date(event.getTimeStamp()));
            String mensajeLog = fechaHora + " " + event.getLevel() + " " + event.getLoggerName() + ":" + event.getLocationInformation()
                                                                                                              .getLineNumber() + " - " + event.getMessage()
                                                                                                                                              .toString();
            botTelegram.enviarMensaje(mensajeLog);
        }

        return nivelEvento;
    }

}