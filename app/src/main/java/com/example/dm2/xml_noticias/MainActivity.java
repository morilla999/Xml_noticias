package com.example.dm2.xml_noticias;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	
	ListView nots;
	List<Noticia> noticias;
	AdaptadorItemNoticia adaptador;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		nots = (ListView)findViewById(R.id.nots);
		
        CargarXmlTask tarea = new CargarXmlTask();
        tarea.execute("http://www.europapress.es/rss/rss.aspx");
		
		adaptador = new AdaptadorItemNoticia(this, noticias);

		nots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String URL_destino = ((TextView) view.findViewById(R.id.link)).getText().toString();
				
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_destino));

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

	class AdaptadorItemNoticia extends ArrayAdapter<Noticia> {

        public AdaptadorItemNoticia(Context context, List<Noticia> noticias) {
            super(context, R.layout.list_item, noticias);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.list_item, null);

            TextView titulo = (TextView)item.findViewById(R.id.titulo);
            titulo.setText(noticias.get(position).getTitulo());
			
			TextView link = (TextView)item.findViewById(R.id.link);
            link.setText(noticias.get(position).getLink());
			
            return(item);
        }
    }
	
    private class CargarXmlTask extends AsyncTask<String,Integer,Boolean> { ///// Create inner asyncTask

        protected Boolean doInBackground(String... params) {

            RssParserSax saxparser =
                    new RssParserSax(params[0]);

            noticias = saxparser.parse();

            return true;
        }

        protected void onPostExecute(Boolean result) {
            nots.setAdapter(adaptador);
        }
    }

}

