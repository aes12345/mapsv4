package com.example.aes.mapsv4;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.aes.mapsv4.entidades.Usuario_dispositivo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mapa extends AppCompatActivity {
    Button buttoncargar;
    GoogleMap googleMap;
    MapView mapView;//es la vista del mapa
    TextView textViewcoordenada;

    Location localizacion_actual;
    List<Usuario_dispositivo> listaGps = new ArrayList<Usuario_dispositivo>();
    JSONParser1 jParser1 = new JSONParser1();

    //mapa
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    //fin map
    //normal
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        mapView = (MapView) findViewById(R.id.id_mapa);
        mapView.onCreate(savedInstanceState);//crea la mapa

        buttoncargar = (Button) findViewById(R.id.buttonCargar);

        buttoncargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ServicioGpsByAlex servicioGpsByAlex = new ServicioGpsByAlex(getApplicationContext());
                 localizacion_actual=servicioGpsByAlex.getLocation();





               new ActualizarPosicion().execute();
                new OptenerPuntosGeograficos().execute();


            }
        });
        textViewcoordenada = (TextView) findViewById(R.id.textViewCoordenadas);


    }

    public void CargarMapa() {

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(googleMap.MAP_TYPE_NORMAL);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }


                googleMap.setMyLocationEnabled(true);
                //   Location location=googleMap.getMyLocation();

              /*  LatLng Riobamba= new LatLng(-1.6636891,-78.65456929999999);

                googleMap.addMarker(new MarkerOptions().title("Dog").position(Riobamba));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Riobamba, 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                */

                // Toast.makeText(getApplicationContext(),),Toast.LENGTH_LONG).show();
                for (int i = 0; i < listaGps.size(); i++) {

                    Usuario_dispositivo item = listaGps.get(i);
                    LatLng Riobamba = new LatLng(item.getLatitud(), item.getLongitud());
                    googleMap.addMarker(new MarkerOptions()
                            .title(item.getNick())
                            .position(Riobamba)
                           .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                           // .icon(
                           //         BitmapDescriptorFactory.fromBitmap(BitmapFactory
                           //                 .decodeResource(getResources(), R.drawable.icon)))
                    );



                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Riobamba, 15));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

                }

                //gps


                ServicioGpsByAlex servicioGpsByAlex = new ServicioGpsByAlex(getApplicationContext());

                //  textViewcoordenada.setText(Double.toString(Double.toString(location.getLongitude())+"<->"+Double.toString(location.getLatitude())));
                servicioGpsByAlex.setTextViewCordenada(textViewcoordenada);
            }
        });

    }


    //AsyncTask< parametros,cuando se esta ejecuta que retorna,alfinalizar que parametro que retorna >
    public class OptenerPuntosGeograficos extends AsyncTask<Void, Integer, Boolean> {

        ProgressDialog pDialog;
        // products JSONArray
        JSONArray products = null;
        //que va hacer antes de ejecutar

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Mapa.this);
            pDialog.setMessage("Cargando Mapa");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        //que es lo que va hacer de  durante la ejecucion
        @Override
        protected Boolean doInBackground(Void... params) {


            List parametros = new ArrayList();

            JSONObject json;


            json = jParser1.makeHttpRequest("http://findme.webcindario.com/vista/usuario_dispositivo_listar.php", "POST", parametros);

            // params.add(new BasicNameValuePair("cedula", variable_global.getInstance().Get_static_cedula_usuario()));


            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray("usuario_dispositivos");

                    // looping through All Products
                    //Log.i("ramiro", "produtos.length" + products.length());
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        Usuario_dispositivo item = new Usuario_dispositivo();
                        item.setId_usuario_dispositivo(c.getInt("id_usuario_dispositivo"));
                        item.setId_usuario(c.getInt("id_usuario"));
                        item.setId_dispositivo(c.getInt("id_dispositivo"));
                        item.setLongitud(c.getDouble("longitud"));
                        item.setLatitud(c.getDouble("latitud"));
                        item.setAltura(c.getInt("altura"));
                        //item.setNick(c.getString("nick"));
                        item.setNick(c.getString("descripcion"));
                        listaGps.add(item);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }


        //cuando se termine
        @Override
        protected void onPostExecute(Boolean result) {
            if (result)// pregunta si se ejecuto bien
            {
                CargarMapa();


                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Tarea finalizada!",
                        Toast.LENGTH_SHORT).show();


            }
        }

        //cuando este en progreso
        @Override
        protected void onProgressUpdate(Integer... values) {

            // int progreso = values[0].intValue();

            //  pDialog.setProgress(progreso);
        }

        //cuando se cancelada
        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), "Tarea cancelada!",
                    Toast.LENGTH_SHORT).show();
        }
    }




    class ActualizarPosicion extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Mapa.this);
            pDialog.setMessage("Actualizado  posicion...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;


            try {
                // Building Parameters
                List params = new ArrayList();


                params.add(new BasicNameValuePair("id_usuario_dispositivo", "1"));
                params.add(new BasicNameValuePair("id_usuario", "1"));
                params.add(new BasicNameValuePair("id_dispositivo", "1"));
                params.add(new BasicNameValuePair("longitud", Double.toString(localizacion_actual.getLongitude())));
                params.add(new BasicNameValuePair("latitud", Double.toString(localizacion_actual.getLatitude())));
               // params.add(new BasicNameValuePair("longitud", "1.12"));
               // params.add(new BasicNameValuePair("latitud", "0.12"));
                params.add(new BasicNameValuePair("altura", "12"));
                Log.d("request!", "starting");
                String REGISTER_URL="http://findme.webcindario.com/union/actualizar_usuario_dispositivo.php";

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt("success");
                if (success == 1) {
                    Log.d("Correct Created!", json.toString());

                    //return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Fail Failure!", json.getString("message"));
                   // return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
              //  Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }



}
