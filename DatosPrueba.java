/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class DatosPrueba {

    
    public static List<RegistroViaje> generarRegistros() {

        List<RegistroViaje> registros = new ArrayList<>();

        registros.add(new RegistroViaje(1, "Ruta Norte", "Portal Norte", true, LocalDateTime.now().minusMinutes(120)));
        registros.add(new RegistroViaje(2, "Ruta Sur", "Venecia", true, LocalDateTime.now().minusMinutes(115)));
        registros.add(new RegistroViaje(3, "Ruta Centro", "Museo Nacional", false, LocalDateTime.now().minusMinutes(110)));
        registros.add(new RegistroViaje(1, "Ruta Norte", "Calle 100", false, LocalDateTime.now().minusMinutes(105)));
        registros.add(new RegistroViaje(4, "Ruta Occidente", "Portal 80", true, LocalDateTime.now().minusMinutes(100)));
        registros.add(new RegistroViaje(5, "Ruta Sur", "Restrepo", true, LocalDateTime.now().minusMinutes(95)));
        registros.add(new RegistroViaje(2, "Ruta Sur", "Venecia", false, LocalDateTime.now().minusMinutes(90)));
        registros.add(new RegistroViaje(6, "Ruta Centro", "Las Aguas", true, LocalDateTime.now().minusMinutes(85)));
        registros.add(new RegistroViaje(3, "Ruta Centro", "Museo Nacional", true, LocalDateTime.now().minusMinutes(80)));
        registros.add(new RegistroViaje(7, "Ruta Occidente", "Quirigua", false, LocalDateTime.now().minusMinutes(75)));

        registros.add(new RegistroViaje(8, "Ruta Norte", "Calle 85", true, LocalDateTime.now().minusMinutes(70)));
        registros.add(new RegistroViaje(4, "Ruta Occidente", "Portal 80", false, LocalDateTime.now().minusMinutes(65)));
        registros.add(new RegistroViaje(9, "Ruta Sur", "General Santander", true, LocalDateTime.now().minusMinutes(60)));
        registros.add(new RegistroViaje(5, "Ruta Sur", "Restrepo", false, LocalDateTime.now().minusMinutes(55)));
        registros.add(new RegistroViaje(10, "Ruta Norte", "Toberín", true, LocalDateTime.now().minusMinutes(50)));
        registros.add(new RegistroViaje(6, "Ruta Centro", "Las Aguas", false, LocalDateTime.now().minusMinutes(45)));
        registros.add(new RegistroViaje(11, "Ruta Occidente", "Quirigua", true, LocalDateTime.now().minusMinutes(40)));
        registros.add(new RegistroViaje(7, "Ruta Occidente", "Quirigua", true, LocalDateTime.now().minusMinutes(35)));
        registros.add(new RegistroViaje(12, "Ruta Centro", "Universidades", false, LocalDateTime.now().minusMinutes(30)));
        registros.add(new RegistroViaje(8, "Ruta Norte", "Calle 85", false, LocalDateTime.now().minusMinutes(25)));

        return registros;
    }
}
