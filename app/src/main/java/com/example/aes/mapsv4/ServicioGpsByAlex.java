package com.example.aes.mapsv4;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Aes on 12/8/2016.
 */
public class ServicioGpsByAlex extends Service implements LocationListener {
    private final Context ctx;

    int version=1;
    double latitud;
    double longitud;
    Location location;
    boolean gpsActivo;
    TextView textViewCordenada;
    LocationManager locationManager;//permite sacar gps cordenadas

    public ServicioGpsByAlex() {
        super();
        this.ctx = this.getApplicationContext();
    }

    public ServicioGpsByAlex(Context ctx) {
        super();
        this.ctx = ctx;
        optenerPosicion();

    }

    public void setTextViewCordenada(TextView v) {
        optenerPosicion();
        this.textViewCordenada = v;
        this.textViewCordenada.setText("Latutud:" + latitud + "Longitud" + longitud+"Version "+version);

    }

    public Location getLocation() {
        optenerPosicion();
        return location;
    }

    public void optenerPosicion() {

        try {
            version++;

            locationManager = (LocationManager) this.ctx.getSystemService(LOCATION_SERVICE);//saca los datos
            //gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);//por gps
            gpsActivo=locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);//por red


            Log.d("hola"," ya estoy aqui :)");
            if (gpsActivo) {
            //permiso
                if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                //optiene la ubicacion GPS

                locationManager.requestLocationUpdates(locationManager.PASSIVE_PROVIDER
                        , 20 * 60//cada minuto
                        , 10//cada 10 metros
                        , this//intent
                );
                location=locationManager.getLastKnownLocation(locationManager.PASSIVE_PROVIDER);//optiene todo las cosas com long lat altittud

                latitud=location.getLatitude();
                longitud=location.getLongitude();
                Log.d("gps Eror ","Latutud:" + location.getLatitude() + "Longitud" + location.getLongitude());
            }else{
                Log.d("gps Eror ","inactivo "+gpsActivo);
            }

        }catch (Exception e){
            Log.d("gp Eror ","NO se puedo saber "+e.getMessage());

        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        // cada cuanto esta cambiando
        optenerPosicion();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

        // si estas connectado o no
    }

    @Override
    public void onProviderEnabled(String s) {

        //cuando se activa el proveedor
    }

    @Override
    public void onProviderDisabled(String s) {

        //cuando se desabilita
    }
}
