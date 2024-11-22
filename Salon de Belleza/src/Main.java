//import Menus.MenuPrincipal;
import com.google.gson.Gson;
import enumeraciones.TipoManicura;

import excepciones.DNInoEncontradoException;
import gestores.*;

import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
    public static void main(String[] args) {

        //MenuPrincipal menuPrincipal = new MenuPrincipal();

       // menuPrincipal.menuPrincipal();

        GestorServicio servicios = new GestorServicio();
        servicios.agregarServicio();

      servicios.guardarServiciosEnArchivo();




    }
}

