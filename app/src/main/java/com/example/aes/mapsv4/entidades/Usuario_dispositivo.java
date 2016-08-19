package com.example.aes.mapsv4.entidades;

/**
 * Created by Aes on 16/8/2016.
 */
public class Usuario_dispositivo {

    int id_usuario_dispositivo;
    int id_usuario;
    int id_dispositivo ;
    Double longitud ;
    Double latitud ;
    int altura ;
    String nick ;

    public Usuario_dispositivo(int id_usuario, int id_usuario_dispositivo, int id_dispositivo, Double longitud, Double latitud, int altura, String nick) {
        this.id_usuario = id_usuario;
        this.id_usuario_dispositivo = id_usuario_dispositivo;
        this.id_dispositivo = id_dispositivo;
        this.longitud = longitud;
        this.latitud = latitud;
        this.altura = altura;
        this.nick = nick;
    }

    public Usuario_dispositivo() {
    }

    public int getId_usuario_dispositivo() {
        return id_usuario_dispositivo;
    }

    public void setId_usuario_dispositivo(int id_usuario_dispositivo) {
        this.id_usuario_dispositivo = id_usuario_dispositivo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_dispositivo() {
        return id_dispositivo;
    }

    public void setId_dispositivo(int id_dispositivo) {
        this.id_dispositivo = id_dispositivo;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
