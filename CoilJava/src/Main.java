//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import Utilidades.Correo;
import org.apache.log4j.Logger;

public class Main {

    private static Logger bitacora = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        
        System.out.printf("Hello and welcome!");
        Correo correo = Correo.getInstancia();
        correo.setContenido("Meow");
        correo.setTema("Sniff sniff");
        correo.setDestinario("ferram200011@gmail.com");
        correo.crearCorreo();
        correo.enviarCorreo();

    }


}