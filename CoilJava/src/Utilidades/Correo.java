package Utilidades;

import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Clase para gestionar el envío de correos electrónicos.
 */
public class Correo {
    private static final Logger BITACORA = Logger.getLogger(VerificadorBitacora.class);
    private String remitente;
    private String contrasena;
    private String destinario;
    private String tema;
    private String contenido;
    private final Properties PROPIEDADES_CORREO;
    private Session sesion;
    private MimeMessage mimeCorreo;

    /**
     * Constructor que carga la configuración del correo desde un archivo de propiedades.
     */
    public Correo () {
        PROPIEDADES_CORREO = new Properties();
        try (FileInputStream archivoConfiguracion = new FileInputStream("src/Utilidades/configuracionCorreo.properties")) {
            PROPIEDADES_CORREO.load(archivoConfiguracion);
            remitente = PROPIEDADES_CORREO.getProperty("mail.smtp.user");
            contrasena = PROPIEDADES_CORREO.getProperty("mail.smtp.password");
        } catch (IOException error) {
            BITACORA.fatal(error.getMessage());
        }
    }

    /**
     * Crea un correo electrónico con el destinatario, tema y contenido especificados.
     *
     * @throws ErrorDAO si hay un error en la sintaxis del correo o un error de mensajería.
     */
    public void crearCorreo () throws ErrorDAO {
        sesion = Session.getDefaultInstance(PROPIEDADES_CORREO);

        mimeCorreo = new MimeMessage(sesion);
        try {
            mimeCorreo.setFrom(new InternetAddress(this.remitente));
            mimeCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(destinario));
            mimeCorreo.setSubject(this.tema);
            mimeCorreo.setText(this.contenido, "ISO-8859-1", "html");
        }
        catch (AddressException error) {
            BITACORA.error(error.getMessage());
            throw new ErrorDAO("Error en la sintaxis del correo", ErrorDAO.Tipo.CORREO);
        }
        catch (MessagingException error) {
            BITACORA.error(error.getMessage());
            throw new ErrorDAO("Error de mensajería al enviar el correo electrónico", ErrorDAO.Tipo.CORREO);

        }
    }

    /**
     * Envía el correo electrónico previamente creado.
     *
     * @throws ErrorDAO si hay un error al enviar el correo electrónico.
     */
    public void enviarCorreo () throws ErrorDAO {
        try {
            Transport transporte = sesion.getTransport("smtp");
            transporte.connect(this.remitente, this.contrasena);
            transporte.sendMessage(mimeCorreo, mimeCorreo.getRecipients(Message.RecipientType.TO));
            transporte.close();
        }
        catch (NoSuchProviderException error) {
            BITACORA.error(error.getMessage());
            throw new ErrorDAO("Proveedor de correo electrónico no encontrado", ErrorDAO.Tipo.CORREO);
        }
        catch (MessagingException error) {
            BITACORA.error(error.getMessage());
            throw new ErrorDAO("Error de mensajería al enviar el correo electrónico ", ErrorDAO.Tipo.CORREO);
        }
    }

    /**
     * Establece el destinatario del correo.
     *
     * @param destinario el destinatario del correo.
     */
    public void setDestinario (String destinario) {
        this.destinario = destinario;
    }


    /**
     * Establece el tema del correo.
     *
     * @param tema el tema del correo.
     */
    public void setTema (String tema) {
        this.tema = tema;
    }

    /**
     * Establece el contenido del correo.
     *
     * @param contenido el contenido del correo.
     */
    public void setContenido (String contenido) {
        this.contenido = contenido;
    }
}
