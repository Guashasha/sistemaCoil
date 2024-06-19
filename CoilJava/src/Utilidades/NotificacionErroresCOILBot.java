package Utilidades;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Bot de Telegram para notificar errores de COIL.
 *
 * @author FerRMZ
 */
@SuppressWarnings("deprecation")
public class NotificacionErroresCOILBot extends TelegramLongPollingBot {
    private static final Logger BITACORA = Logger.getLogger(NotificacionErroresCOILBot.class);
    private static NotificacionErroresCOILBot instanciaBot;

    /**
     * Constructor privado para implementar el patrón Singleton.
     */
    private NotificacionErroresCOILBot () {
    }

    /**
     * Maneja actualizaciones recibidas desde Telegram.
     *
     * @param update la actualización recibida.
     */
    @Override
    public void onUpdateReceived (Update update) {

    }

    /**
     * Maneja una lista de actualizaciones recibidas desde Telegram.
     *
     * @param updates la lista de actualizaciones recibidas.
     */
    @Override
    public void onUpdatesReceived (List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    /**
     * Obtiene el nombre de usuario del bot de Telegram.
     *
     * @return el nombre de usuario del bot.
     */
    @Override
    public String getBotUsername () {
        return "Notificacion_Errores_COIL_bot";
    }

    /**
     * Obtiene el token del bot de Telegram.
     *
     * @return el token del bot.
     */
    public String getBotToken () {
        return "6867312568:AAHr4aqzQZZHVWx0VN-F5YphCV5fybu55d4";
    }

    /**
     * Método llamado cuando el bot se registra.
     */
    @Override
    public void onRegister () {
        super.onRegister();
    }

    /**
     * Envía un mensaje a un chat de Telegram con la información de un evento de logging.
     *
     * @param evento el evento de logging a enviar.
     */
    public void enviarMensaje (LoggingEvent evento) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String fechaHora = formatoFecha.format(new Date(evento.getTimeStamp()));
        String mensajeLog = fechaHora + " " + evento.getLevel() + " " + evento.getLoggerName() + ":" + evento.getLocationInformation()
                                                                                                             .getLineNumber() + " - " + evento.getMessage()
                                                                                                                                              .toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId("7193864462");
        sendMessage.setText(mensajeLog);
        try {
            execute(sendMessage);
        }
        catch (TelegramApiException error) {
            BITACORA.error(error.getMessage());
        }
    }

    /**
     * Obtiene la instancia única del bot (Singleton).
     *
     * @return la instancia única del bot.
     */
    public static NotificacionErroresCOILBot getInstanciaBot () {
        if (instanciaBot == null) {
            instanciaBot = new NotificacionErroresCOILBot();
        }
        return instanciaBot;
    }
}
