package Utilidades;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class NotificacionErroresCOILBot extends TelegramLongPollingBot {
    private static final Logger BITACORA = Logger.getLogger(NotificacionErroresCOILBot.class);
    private static final NotificacionErroresCOILBot INSTANCIA_BOT = new NotificacionErroresCOILBot();
    @Override
    public void onUpdateReceived (Update update) {

    }
    @Override
    public void onUpdatesReceived (List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername () {
        return "Notificacion_Errores_COIL_bot";
    }

    public String getBotToken () {
        return "6867312568:AAHr4aqzQZZHVWx0VN-F5YphCV5fybu55d4";
    }
    @Override
    public void onRegister () {
        super.onRegister();
    }

    public void enviarMensaje (String mensaje) {
        SendMessage message = new SendMessage();
        message.setChatId("7193864462");
        message.setText(mensaje);
        try {
            execute(message);
        }
        catch (TelegramApiException error) {
            BITACORA.error(error.getMessage());
        }
    }
    public static NotificacionErroresCOILBot getInstanciaBot() {
        return INSTANCIA_BOT;
    }
}
