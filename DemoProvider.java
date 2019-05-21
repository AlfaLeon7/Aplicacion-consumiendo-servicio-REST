package inc.alfaleon.pruebarv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

public class DemoProvider extends Activity {


    private Button btnInsertar;
    private Button btnConsultar;
    private Button btnEliminar;
    private Button btnObtener;
    private Button btnActualizar;
    private EditText txtNombre;
    private EditText txtDescripcion;
    private EditText txtDescripcion_larga;
    private TextView resultado;
    private ListView listMonstruos;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demoprovider);

        btnConsultar = (Button)findViewById(R.id.btnListar);
        btnInsertar = (Button)findViewById(R.id.btnInsertar);
        btnEliminar = (Button)findViewById(R.id.btnEliminar);
        btnObtener = (Button)findViewById(R.id.btnObtener);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        txtDescripcion_larga = (EditText) findViewById(R.id.txtDescripcion_larga);
        resultado = (TextView) findViewById(R.id.lblResultado);
        listMonstruos = (ListView) findViewById(R.id.lstClientes);


        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaWSListar tarea = new TareaWSListar();
                tarea.execute();
                }

        });

        btnObtener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaWSObtener tarea = new TareaWSObtener();
                tarea.execute(txtNombre.getText().toString());
            }
        });


        btnInsertar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                TareaWSInsertar tarea = new TareaWSInsertar();
                tarea.execute(
                        txtNombre.getText().toString(),
                        txtDescripcion.getText().toString());
                        txtDescripcion_larga.getText().toString();
            }
        });


        btnEliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                TareaWSEliminar tarea = new TareaWSEliminar();
                tarea.execute(txtNombre.getText().toString());
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaWSActualizar tarea = new TareaWSActualizar();
                tarea.execute(
                        txtNombre.getText().toString(),
                        txtDescripcion.getText().toString(),
                        txtDescripcion_larga.getText().toString());
            }

        });








    }
    //Tarea Asíncrona para llamar al WS de inserción en segundo plano
    private class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog progressDialog;
        protected Boolean doInBackground(String... params) {

            boolean resul = true;



            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("https://dam2.ieslamarisma.net/2019/crhistianperez/rest_slim_bd/yokai");
            post.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("nombre", txtNombre.getText());
                dato.put("descripcion",txtDescripcion.getText());
                dato.put("descripcion_larga",txtDescripcion_larga.getText());

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());
                Thread.sleep(2000);
                if(!respStr.equals("true"))
                    resul = false;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                progressDialog.dismiss();
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                resultado.setText("Insertado OK.");
            }
            progressDialog.dismiss();
        }
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DemoProvider.this,
                    "Realizando cambios en la base de datos",
                    "Espera durante "+3+ " segundos");
        }
    }



    //Tarea Asíncrona para llamar al WS de eliminación en segundo plano
    private class TareaWSEliminar extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog progressDialog;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String nombre = params[0];

            HttpDelete del =
                    new HttpDelete("https://dam2.ieslamarisma.net/2019/crhistianperez/rest_slim_bd/yokai/" + nombre);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                Thread.sleep(2000);

                if(!respStr.equals("true"))
                    resul = false;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                progressDialog.dismiss();
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                resultado.setText("Eliminado OK.");
            }
            progressDialog.dismiss();
        }
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DemoProvider.this,
                    "Realizando cambios en la base de datos",
                    "Espera durante "+3+ " segundos");
        }
    }


    //Tarea Asíncrona para llamar al WS de actualización en segundo plano
    private class TareaWSActualizar extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog progressDialog;
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String nombre = params[0];

            HttpPut put = new HttpPut("https://dam2.ieslamarisma.net/2019/crhistianperez/rest_slim_bd/yokai/" + nombre);
            put.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("nombre", params[0]);
                dato.put("descripcion", params[1]);
                dato.put("descripcion_larga", params[2]);


                StringEntity entity = new StringEntity(dato.toString());
                put.setEntity(entity);

                HttpResponse resp = httpClient.execute(put);
                String respStr = EntityUtils.toString(resp.getEntity());
                Thread.sleep(2000);

                if(!respStr.equals("true"))
                    resul = false;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
                progressDialog.dismiss();

            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                resultado.setText("Actualizado OK.");
            }
            progressDialog.dismiss();
        }
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DemoProvider.this,
                    "Realizando cambios en la base de datos",
                    "Espera durante "+3+ " segundos");
        }
    }



    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    private class TareaWSObtener extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog progressDialog;

        private String nombre;
        private String descripcion;
        private String descripcion_larga;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String nombree = params[0];

            HttpGet del =
                    new HttpGet("https://dam2.ieslamarisma.net/2019/crhistianperez/rest_slim_bd/yokai/" + nombree);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                Thread.sleep(2000);


                JSONObject respJSON = new JSONObject(respStr);

                nombre = respJSON.getString("nombre");
                descripcion = respJSON.getString("descripcion");
                descripcion_larga = respJSON.getString("descripcion_larga");
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
                progressDialog.dismiss();

            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                resultado.setText("" + nombre + "-" + descripcion + "-" + descripcion_larga);
            }
            progressDialog.dismiss();

        }
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DemoProvider.this,
                    "Realizando cambios en la base de datos",
                    "Espera durante "+3+ " segundos");
        }
    }



    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog progressDialog;

        private String[] monstruos;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet("https://dam2.ieslamarisma.net/2019/crhistianperez/rest_slim_bd/yokais");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                Thread.sleep(2000);

                JSONArray respJSON = new JSONArray(respStr);

                monstruos = new String[respJSON.length()];

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);

                    String nombre = obj.getString("nombre");
                    String descripcion = obj.getString("descripcion");
                    String descripcion_larga = obj.getString("descripcion_larga");

                    monstruos[i] = "" + nombre + "-" + descripcion + "-" + descripcion_larga;
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
                progressDialog.dismiss();

            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {

                ArrayAdapter<String> adaptador =
                        new ArrayAdapter<String>(DemoProvider.this,
                                android.R.layout.simple_list_item_1, monstruos);

                listMonstruos.setAdapter(adaptador);
            }
            progressDialog.dismiss();

        }
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DemoProvider.this,
                    "Realizando cambios en la base de datos",
                    "Espera durante "+3+ " segundos");
        }
    }
}
