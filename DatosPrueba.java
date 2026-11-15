import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class DatosPrueba {
    
    public static List<RegistroViaje> generarRegistros() {

    List<RegistroViaje> registros = new ArrayList<>();

    String[] rutas = {"Ruta Norte", "Ruta Sur", "Ruta Centro"};

    String[] estaciones = {
        "Portal Norte", "Calle 100",
        "Calle 72", "Portal Sur",
        "Centro", "Universidades"
    };

    Random random = new Random();

    for (int i = 0; i < 200; i++) {

        int usuario = random.nextInt(30) + 1;

        String ruta = rutas[random.nextInt(rutas.length)];

        String estacion = estaciones[random.nextInt(estaciones.length)];

        boolean accion = random.nextBoolean();

        LocalDateTime fecha = LocalDateTime.now()
                .minusMinutes(random.nextInt(1440));

        registros.add(new RegistroViaje(
                usuario, ruta, estacion, accion, fecha
        ));
    }

    return registros;
    }
}
