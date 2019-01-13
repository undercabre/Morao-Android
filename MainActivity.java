package com.example.adminportatil.tssapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int SOLICITUD_PERMISOS_ESCRITURA = 1;
    private Intent intentLlamada;
    private TextView delegacion, lblMensaje;
    private Button bt1;
    double x, y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creamos la carpeta que utiliza la aplicación
        new File("/storage/emulated/0/informacionTss").mkdir();

        //declaracion de un botón y de dos TextView
        bt1 = findViewById(R.id.btnEntrada);
        lblMensaje = findViewById(R.id.informacion);
        delegacion = findViewById(R.id.delegacion);

        //damos valor a las variables x e y con la latitud, longitud
        x = 43.3058391;
        y = -2.0136476;

        //enviamos la información por la extención .setText()
        delegacion.setText("Guipúzcoa");
        lblMensaje.setText("Guipúzcoa - tlf: 943 786 998 - e-mail: guipuzcoaTss@gmail.com");

        //creamos la variable intentLlamada y le damos un valor
        intentLlamada = new Intent(Intent.ACTION_APPLICATION_RESTRICTIONS_CHANGED);

        //comprovamos si la aplicacion tiene permisos aplicado y si no pedirlo al usuario
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            startActivity(intentLlamada);
        } else {
            emplicarUsoPermiso();
            solicitarPermisoEscritura();
        }

//botón para entrar a las cuatro obciones de la aplicacion y mandamos informacion al la CuatroOpcionesActivity.java
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myInten = new Intent(MainActivity.this, CuatroOpcionesActivity.class);
                myInten.putExtra("nombre", "Guipúzcoa");
                startActivityForResult(myInten, 1234);
            }
        });

        /*//obligamos a que el google maps este actualizado
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {


            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapa);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(), 10);
            dialog.show();
        }
        //*/

    }

    //metodo para cargar o generar el GoogleMaps de la aplicación
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap;
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng casa = new LatLng(x, y);

        mMap.addMarker(new MarkerOptions().position(casa).title("Tss Sede Central").icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(casa, 13));
    }


    //Le preguntamos al usuario otra vez si desea denegar los permisos al usuario otra vez
    private void emplicarUsoPermiso() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            lanzarMesaje();
        }
    }

    //solicitamos los permisos adecuados para poder trabajar
    private void solicitarPermisoEscritura() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SOLICITUD_PERMISOS_ESCRITURA);
    }

    //comprobamos si el usuario acepta o no los permisos requeridos
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SOLICITUD_PERMISOS_ESCRITURA: {
// If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    //lanzamos un mensaje por el AlertDialog si desea cancelar la solicitud de permisos requeridos
    public void lanzarMesaje() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Solicitud de Permiso");
        builder.setMessage("Sin el permiso de escritura y lectura no podemos guardar los pedidos ni leerlos");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
