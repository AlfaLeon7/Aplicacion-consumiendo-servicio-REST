package inc.alfaleon.pruebarv;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Informacion extends AppCompatActivity {
TextView image_name;
    EditText description;
    EditText descripcion_large;
ImageView imagen;
TextView mostrarPos;
Button borrar;
Conexion conn;
Button actualizar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        image_name = (TextView) findViewById(R.id.imageInfo);
        description = (EditText)  findViewById(R.id.descripcion);
        descripcion_large = (EditText) findViewById(R.id.descripcionExtensa);
        imagen = (ImageView) findViewById(R.id.imagenGaleria);
        mostrarPos = (TextView) findViewById(R.id.mostrarPos);
        borrar = (Button) findViewById(R.id.botonBorrar);
        actualizar = (Button) findViewById(R.id.botonActualizar);

        conn = new Conexion(getApplicationContext(), "yokai", null, 1);
        final AsyncTaskRunner runner = new AsyncTaskRunner();


        Bundle bundle = getIntent().getExtras();


        if(bundle!=null){
            Monstruo monster = (Monstruo)bundle.getSerializable("Monstruo");
            image_name.setText(monster.getNombre().toString());
            description.setText(monster.getDescripcion().toString());
            descripcion_large.setText(monster.getDescripcionLarga().toString());
            imagen.setImageResource(monster.getFoto());
            
        }

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=conn.getWritableDatabase();
                String[] parametros={image_name.getText().toString()};
                runner.execute("5");
                db.delete(Utilidades.NOMBRE_TABLA,Utilidades.CAMPO_NOMBRE+"=?",parametros);
                Toast.makeText(getApplicationContext(),"Se ha borrado el elemento",Toast.LENGTH_LONG).show();
                db.close();

            }

        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                SQLiteDatabase db = conn.getWritableDatabase();

                String[] parametros = {image_name.getText().toString()};
                ContentValues values = new ContentValues();
                values.put(Utilidades.CAMPO_NOMBRE, image_name.getText().toString());
                values.put(Utilidades.CAMPO_DESCRIPCION, description.getText().toString());
                values.put(Utilidades.CAMPO_DESCRIPCION_LARGA, descripcion_large.getText().toString());

                runner.execute("5");
                db.update(Utilidades.NOMBRE_TABLA, values, Utilidades.CAMPO_NOMBRE+"=?", parametros);

                Toast.makeText(getApplicationContext(), "Monstruo actualizado", Toast.LENGTH_SHORT).show();
                db.close();


            }
        });




    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Esperando...");
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

        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(Informacion.this,
                    "Realizando cambios en la base de datos",
                    "Espera durante "+5+ " segundos");
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();
            //Buscar en internet
        if(id==R.id.buscar){
            Bundle wikipedia = getIntent().getExtras();
            Monstruo monstruos = (Monstruo)wikipedia.getSerializable("Monstruo");
            String url = "https://wikipedia.org/wiki/" + monstruos.getNombre();
            Intent buscar = new Intent(Intent.ACTION_VIEW);
            buscar.setData(Uri.parse(url));
            startActivity(buscar);
        }
        //Compartir
        if(id==R.id.compartir){
            Bundle share = getIntent().getExtras();
            Monstruo monstruos = (Monstruo)share.getSerializable("Monstruo");
            Intent compartir = new Intent(Intent.ACTION_SEND);
            compartir.setType("text/plain");
            compartir.putExtra(Intent.EXTRA_TEXT, monstruos.getNombre() + "\n" + monstruos.getDescripcion() + "\n"  + monstruos.getDescripcionLarga() + "\n\nAprende más sobre folklore japonés con la App de Christian!");
            startActivity(Intent.createChooser(compartir, "Compartir con"));


        }
        //Enviar por correo
        if(id==R.id.correo){
            Bundle mail = getIntent().getExtras();
            Monstruo monstruos = (Monstruo)mail.getSerializable("Monstruo");
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setData(Uri.parse("mailto"));
            email.setType("text/plain");
            email.putExtra(Intent.EXTRA_EMAIL, "");
            email.putExtra(Intent.EXTRA_SUBJECT, monstruos.getNombre());
            email.putExtra(Intent.EXTRA_TEXT, monstruos.getDescripcionLarga());

            try{
                startActivity(Intent.createChooser(email, "Enviar mail"));
                finish();
            }catch(android.content.ActivityNotFoundException ex){
                Toast.makeText(Informacion.this, "No tienes aplicaciones de email instaladas", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }



}


