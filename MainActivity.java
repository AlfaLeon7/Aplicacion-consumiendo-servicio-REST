package inc.alfaleon.pruebarv;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;
import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    AdapterDatos adapter;
    Conexion conn;
    SwipeRefreshLayout swipeRefreshLayout;
      public  ArrayList<Monstruo> listaMonstruos;
        RecyclerView recyclerMonstrus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);


         conn = new Conexion(getApplicationContext(), "yokai", null, 1);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorblanco);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorblue);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refrescar();

            }


        });






        listaMonstruos = new ArrayList<>();
        recyclerMonstrus = findViewById(R.id.recyclerId);
        recyclerMonstrus.setLayoutManager(new LinearLayoutManager(this));

       consultarListaMonstruos();

         adapter = new AdapterDatos(listaMonstruos);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),  listaMonstruos.get(recyclerMonstrus.getChildAdapterPosition(v)).getNombre(), Toast.LENGTH_SHORT).show();
                Monstruo monstruo = listaMonstruos.get(recyclerMonstrus.getChildAdapterPosition(v));



                Intent intent = new Intent(MainActivity.this, Informacion.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable("Monstruo", monstruo);
                intent.putExtras(bundle);
                startActivity(intent);
                adapter.notifyDataSetChanged();

            }
        });
        recyclerMonstrus.setAdapter(adapter);





    }

    public void consultarListaMonstruos(){
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.NOMBRE_TABLA, null);

        while(cursor.moveToNext()){


            Monstruo monstruo2 = new Monstruo();

            monstruo2.setNombre(cursor.getString(0));
            monstruo2.setDescripcion(cursor.getString(1));
            monstruo2.setDescripcionLarga(cursor.getString(2));
            monstruo2.setFoto(R.drawable.kappa);





            listaMonstruos.add(monstruo2);
        }
    }

    private void refrescar() {
        swipeRefreshLayout.setRefreshing(true);
        listaMonstruos.clear();


        consultarListaMonstruos();
        swipeRefreshLayout.setRefreshing(false);
        recyclerMonstrus.setLayoutManager(new LinearLayoutManager(this));

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();


        if(id==R.id.addmonstruo){

            Intent crear = new Intent(this, CrearMonstruo.class);
            startActivity(crear);

        }

        if(id==R.id.actualizar){

            Intent provider = new Intent(this, DemoProvider.class);
            startActivity(provider);

        }

        if(id==R.id.title3){
            Toast.makeText(this, "Item3", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Bundle bundle = getIntent().getExtras();
               Monstruo monstruo = (Monstruo)bundle.getSerializable("resultado");
                listaMonstruos.add(new Monstruo(monstruo.getNombre(), monstruo.getDescripcion(), monstruo.getDescripcionLarga()));
                adapter.notifyDataSetChanged();
                recyclerMonstrus.setAdapter(adapter);



            }
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userinput = newText.toLowerCase();
        ArrayList<Monstruo> newList = new ArrayList<Monstruo>();
        for(Monstruo monstru : listaMonstruos){
            if(monstru.getNombre().toLowerCase().contains(userinput)||(monstru.getDescripcion().toLowerCase().contains(userinput))){
                newList.add(monstru);
            }
        }
        adapter.updateList(newList);

        return true;
    }


}




