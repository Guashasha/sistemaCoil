import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class TriggerLogEvent implements TriggeringEventEvaluator {

    @Override
    public boolean isTriggeringEvent (LoggingEvent event) {
        boolean nivelEvento = event.getLevel()
                                   .isGreaterOrEqual(Level.FATAL);
        if (nivelEvento) {
            NotificacionErroresCOILBot botTelegram = NotificacionErroresCOILBot.getInstanciaBot();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String fechaHora = sdf.format(new Date(event.getTimeStamp()));
            String mensajeLog = fechaHora + " " + event.getLevel() + " " + event.getLoggerName() + ":" + event.getLocationInformation()
                                                                                                                                     .getLineNumber() + " - " + event.getMessage()
                                                                                                                                                                     .toString();
            botTelegram.enviarMensaje(mensajeLog);
        }

        return nivelEvento;
    }

}