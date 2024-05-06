package Utilidades;

import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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
    private static Correo instancia;

    private Correo () {
        PROPIEDADES_CORREO = new Properties();
        try (FileInputStream archivoConfiguracion = new FileInputStream("src/Utilidades/configuracionCorreo.properties")) {
            PROPIEDADES_CORREO.load(archivoConfiguracion);
            remitente = PROPIEDADES_CORREO.getProperty("mail.smtp.user");
            contrasena = PROPIEDADES_CORREO.getProperty("mail.smtp.password");
        } catch (IOException error) {
            BITACORA.error(error.getMessage());
        }
    }

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

    public void setDestinario (String destinario) {
        this.destinario = destinario;
    }


    public void setTema (String tema) {
        this.tema = tema;
    }

    public void setContenido (String contenido) {
        this.contenido = contenido;
    }

    public static Correo getInstancia () {
        if (instancia == null) {
            instancia = new Correo();
        }
        return instancia;
    }
}
