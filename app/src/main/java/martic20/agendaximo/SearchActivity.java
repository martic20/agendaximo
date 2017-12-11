package martic20.agendaximo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener {

    private ListView mListView;

    public static final String URL = "http://" + LoginActivity.getIP() + "/agendaximo/search.php?value=";

    private List<HashMap<String, String>> mAndroidMapList = new ArrayList<>();

    private static final String KEY_VER = "ver";
    private static final String KEY_NAME = "name";
    private static final String KEY_API = "api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        Button search = (Button) findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadJSONTask(SearchActivity.this).execute(URL+getText());
            }
        });


    }

    private String getText() {
        EditText value = (EditText) findViewById(R.id.searchText);
        return value.getText().toString();
    }
    @Override
    public void onLoaded(List<ModelUser> androidList) {

        for (ModelUser android : androidList) {

            HashMap<String, String> map = new HashMap<>();


            map.put(KEY_NAME, android.getName());

            mAndroidMapList.add(map);
        }

        loadListView();
    }

    @Override
    public void onError() {

        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Toast.makeText(this, mAndroidMapList.get(i).get(KEY_NAME), Toast.LENGTH_LONG).show();
    }

    private void loadListView() {

        ListAdapter adapter = new SimpleAdapter(SearchActivity.this, mAndroidMapList, R.layout.list_item,
                new String[]{KEY_VER, KEY_NAME, KEY_API},
                new int[]{R.id.version, R.id.name, R.id.api});

        mListView.setAdapter(adapter);

    }
}
/*
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class SearchActivity extends AppCompatActivity {
    String[] listItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button search = (Button) findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread tr = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        final String out = doSearchGET(getText());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run(){
                                 }
                        });

                    }
                };
                tr.start();           }
        });

    }



    public String getText() {
        EditText value = (EditText) findViewById(R.id.searchText);
    return value.getText().toString();
    }
    public final String doSearchGET(String value) {
        URL url = null;
        String linea = "";
        int respuesta =0;
        StringBuilder resul = new StringBuilder();
        try {
            url = new URL("http://" + LoginActivity.getIP() + "/agendaximo/search.php?value=" + value);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            respuesta = connection.getResponseCode();

            if (respuesta == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                //LLENAMOS RESUL CON READER
                while ((linea = reader.readLine()) != null) {
                    resul.append(linea);
                }
            }
        } catch (Exception e) {
        }
        return resul.toString();
    }


}
*/