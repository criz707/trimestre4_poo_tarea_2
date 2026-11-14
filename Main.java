/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */



import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    // Umbral de ocupación SIMULADO: una ruta con más de N ingresos es "crítica".
    private static final long UMBRAL_CRITICO = 5;

    // Resultado inmutable de un trayecto entre dos estaciones consecutivas.
    private record Tramo(String origen, String destino, Duration duracion) {}

    public static void main(String[] args) {

        // AJUSTAR: aquí se obtiene la lista de DatosPrueba.java.
        // Inmutabilidad: List.copyOf crea una copia que no se puede modificar,
        // así ninguna función puede alterar los datos originales.
        List<RegistroViaje> registros = List.copyOf(DatosPrueba.generarRegistros());

        // a) Afluencia por estación
        System.out.println("=== a) Afluencia por estación (ingresos por día) ===");
        afluenciaPorEstacion(registros).forEach((dia, porEstacion) -> {
            System.out.println(dia);
            porEstacion.forEach((est, n) -> System.out.println("  " + est + ": " + n));
        });

        // b) Horas pico
        System.out.println("\n=== b) Flujo por hora y horas pico ===");
        Map<Integer, Long> porHora = flujoPorHora(registros);
        porHora.forEach((h, n) -> System.out.printf("  %02d:00 -> %d registros%n", h, n));
        System.out.println("  Horas pico: " + horasPico(porHora));

        // c) Rutas más utilizadas
        System.out.println("\n=== c) Rutas más utilizadas ===");
        Map<String, Long> usoRutas = usoPorRuta(registros);
        ordenarDescendente(usoRutas)
                .forEach(e -> System.out.println("  " + e.getKey() + ": " + e.getValue()));

        // d) Patrón de viaje por usuario
        System.out.println("\n=== d) Patrón de viaje por usuario ===");
        patronesPorUsuario(registros)
                .forEach((id, estaciones) -> System.out.println("  Usuario " + id + ": " + estaciones));

        // e) Tiempo promedio entre estaciones
        System.out.println("\n=== e) Tiempo promedio entre estaciones consecutivas ===");
        tiempoPromedioEntreEstaciones(registros)
                .forEach((tramo, min) -> System.out.printf("  %s: %.1f min%n", tramo, min));

        // f) Detección de sobrecarga
        System.out.println("\n=== f) Estado de rutas (umbral = " + UMBRAL_CRITICO + ") ===");
        estadoRutas(usoRutas, UMBRAL_CRITICO)
                .forEach((ruta, estado) -> System.out.println("  " + ruta + ": " + estado));
    }

    // ------------------------------------------------------------------
    // a) Afluencia por estación
    // Paradigma funcional + Streams + Lambda + Enfoque declarativo:
    // se describe QUÉ se quiere (filtrar ingresos, agrupar y contar), no
    // CÓMO recorrer la lista con ciclos. Función pura: no modifica la
    // entrada ni usa estado externo. Colecciones: Map anidado agrupado.
    // ------------------------------------------------------------------
    static Map<LocalDate, Map<String, Long>> afluenciaPorEstacion(List<RegistroViaje> registros) {
        return registros.stream()
                .filter(RegistroViaje::getAccion) // true = ingreso
                .collect(Collectors.groupingBy(
                        r -> r.getTimeStamp().toLocalDate(),
                        TreeMap::new,
                        Collectors.groupingBy(RegistroViaje::getEstacion, TreeMap::new, Collectors.counting())));
    }

    // ------------------------------------------------------------------
    // b) Horas pico
    // Pipeline de operaciones: stream -> agrupación por hora -> conteo.
    // Segundo pipeline (declarativo) para hallar el máximo y filtrar las
    // horas que lo igualan. Funciones puras, sin variables mutables.
    // ------------------------------------------------------------------
    static Map<Integer, Long> flujoPorHora(List<RegistroViaje> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getTimeStamp().getHour(),
                        TreeMap::new,
                        Collectors.counting()));
    }

    static List<Integer> horasPico(Map<Integer, Long> porHora) {
        long max = porHora.isEmpty() ? 0 : Collections.max(porHora.values());
        return porHora.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .map(Map.Entry::getKey)
                .toList(); // lista inmutable
    }

    // ------------------------------------------------------------------
    
    // c) Rutas más utilizadas
    // PARALELIZACIÓN: parallelStream() reparte el trabajo entre varios
    // hilos. Es seguro porque la función es pura (sin estado compartido
    // mutable) y el collector combina resultados parciales por sí mismo.
    // Se cuentan los ingresos como "uso" de la ruta.
    // ------------------------------------------------------------------
    static Map<String, Long> usoPorRuta(List<RegistroViaje> registros) {
        return registros.parallelStream()
                .filter(RegistroViaje::getAccion)
                .collect(Collectors.groupingBy(RegistroViaje::getRuta, Collectors.counting()));
    }

    // Ordenar de mayor a menor (función pura, devuelve lista nueva inmutable).
    static <K> List<Map.Entry<K, Long>> ordenarDescendente(Map<K, Long> mapa) {
        return mapa.entrySet().stream()
                .sorted(Map.Entry.<K, Long>comparingByValue().reversed())
                .toList();
    }

    // ------------------------------------------------------------------
    // d) Patrón de viaje por usuario
    // Colecciones + groupingBy con collector downstream (mapping + toList).
    // Se usa stream SECUENCIAL a propósito: el orden de aparición importa
    // y la paralelización podría perderlo en este caso de uso.
    // ------------------------------------------------------------------
    static Map<Integer, List<String>> patronesPorUsuario(List<RegistroViaje> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(
                        RegistroViaje::getIdUsuario,
                        TreeMap::new,
                        Collectors.mapping(RegistroViaje::getEstacion, Collectors.toList())));
    }

    // ------------------------------------------------------------------
    // e) Tiempo promedio entre estaciones consecutivas
    // Operaciones funcionales encadenadas: groupingBy -> sorted -> flatMap
    // sobre pares consecutivos (IntStream.range) -> filter -> map ->
    // averagingDouble. Pipeline totalmente declarativo; el estado
    // intermedio es inmutable (record Tramo) y no hay bucles for.
    // ------------------------------------------------------------------
    static Map<String, Double> tiempoPromedioEntreEstaciones(List<RegistroViaje> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(RegistroViaje::getIdUsuario))
                .values().stream()
                .flatMap(Main::tramosDeUsuario)
                .collect(Collectors.groupingBy(
                        t -> t.origen() + " -> " + t.destino(),
                        TreeMap::new,
                        Collectors.averagingDouble(t -> t.duracion().toSeconds() / 60.0)));
    }

    // Función pura: convierte los registros de UN usuario en sus tramos.
    private static java.util.stream.Stream<Tramo> tramosDeUsuario(List<RegistroViaje> deUsuario) {
        List<RegistroViaje> ordenados = deUsuario.stream()
                .sorted(java.util.Comparator.comparing(RegistroViaje::getTimeStamp))
                .toList();
        return IntStream.range(0, ordenados.size() - 1)
                .mapToObj(i -> new Tramo(
                        ordenados.get(i).getEstacion(),
                        ordenados.get(i + 1).getEstacion(),
                        Duration.between(ordenados.get(i).getTimeStamp(), ordenados.get(i + 1).getTimeStamp())))
                .filter(t -> !t.origen().equals(t.destino())); // solo cambios de estación
    }

    // ------------------------------------------------------------------
    // f) Detección de sobrecarga en rutas
    // Lambda como predicado + map/collect: se transforma cada (ruta, uso)
    // en una etiqueta. Función pura parametrizada por el umbral (no usa
    // variables globales), por lo que es fácil de probar y reutilizar.
    // ------------------------------------------------------------------
    static Map<String, String> estadoRutas(Map<String, Long> usoRutas, long umbral) {
        return usoRutas.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        e -> e.getValue() > umbral ? "CRÍTICA" : "normal"));
    }
}