/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.time.LocalDateTime;

public class RegistroViaje {
    int IdUsuario;
    String ruta;
    
    String estacion;
    boolean accion;
    LocalDateTime timeStamp;

    //Constructor
    public RegistroViaje(int idUsuario, String ruta, String estacion, boolean accion, LocalDateTime timeStamp) {
        this.IdUsuario = idUsuario;   // corregido: antes se asignaba el campo a sí mismo
        this.ruta = ruta;
        this.estacion = estacion;     // corregido: antes se asignaba ruta
        this.accion = accion;
        this.timeStamp = timeStamp;
    }
    //GETTERS:
    
    public String getEstacion() {
        return this.estacion;
    }

    public int getIdUsuario() {
        return this.IdUsuario;
    }

    public String getRuta() {
        return this.ruta;
    }

    //True es ingreso y false es salida
    public boolean getAccion() {
        return this.accion;
    }

    public LocalDateTime getTimeStamp() {
        return this.timeStamp;
    }

    //SETTERS:
    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public void setAccion(boolean accion) {
        this.accion = accion;
    }

    
    
    
    public void setTimestamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}