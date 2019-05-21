package inc.alfaleon.pruebarv;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CrearMonstruo extends AppCompatActivity {
    EditText nombre;
    EditText significado;
    EditText descripcion;
    TextView vernombre;
    TextView versignificado;
    TextView verdescripcion;
    Button añadirimagen;
    Button añadirObjeto;
    Conexion conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_monstruo);


        nombre = (EditText) findViewById(R.id.crearMonstruoNombre);
        significado = (EditText) findViewById(R.id.crearMonstruoDescripcion);
        descripcion = (EditText) findViewById(R.id.crearMonstruoDescripcionLarga);
        vernombre = (TextView) findViewById(R.id.campoNombre);
        versignificado = (TextView) findViewById(R.id.campoSignificado);
        verdescripcion = (TextView) findViewById(R.id.campoDescripcion);
        añadirObjeto = (Button) findViewById(R.id.botonCrear);
        conn=new Conexion(this,"yokai",null,1);
        final AsyncTaskRunner runner = new AsyncTaskRunner();



        añadirObjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             //   runner.execute("5");
                registrarMonstruo();

            }
        });


    }
    private void registrarMonstruo() {


        SQLiteDatabase db=conn.getWritableDatabase();

        String insert="INSERT INTO "+Utilidades.NOMBRE_TABLA
                +"("+Utilidades.CAMPO_NOMBRE+","+Utilidades.CAMPO_DESCRIPCION+","+Utilidades.CAMPO_DESCRIPCION_LARGA+")" +
                "VALUES ('"+nombre.getText().toString()+"', '"+significado.getText().toString()+"','"
                +descripcion.getText().toString()+"')";



        db.execSQL(insert);


        Toast.makeText(getApplicationContext(),"Nombre: "+nombre.getText(),Toast.LENGTH_SHORT).show();
        db.close();

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Esperando..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0])*1000;

                Thread.sleep(time);
                resp = "Espera durante " + params[0] + " segundos";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            añadirObjeto.setText(result);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CrearMonstruo.this,
                    "Operando...",
                    "Espera durante "+5+ " segundos");
        }


        @Override
        protected void onProgressUpdate(String... text) {
            añadirimagen.setText(text[0]);


        }
    }




}