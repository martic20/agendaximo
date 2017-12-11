package martic20.agendaximo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    Button btEntrar;
    EditText etUser;
    EditText etPass;
    EditText etIP;

    public static String IP="192.168.1.34";

    public static String getIP(){
        return IP;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btEntrar = (Button) findViewById(R.id.btEntrar);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);
        etIP = (EditText) findViewById(R.id.etIP);
        IP=etIP.getText().toString();

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread tr = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        final String resultado = enviarDatosGET(etUser.getText().toString(), etPass.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int r = obtenerDatosJson(resultado);
                                if (r > 0) {
                                    Intent intent = new Intent(getApplicationContext(), agenda.class);


                                    intent.putExtra("cod", etUser.getText().toString());
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "usuario o pass incorrectos with e " + String.valueOf(r), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                };
                tr.start();
            }
        });


    }
    /* with cokies
    public String enviarDatosGET(String usu, String pass){
        try
        {
            URL url = new URL("http://"+etIP.getText().toString()+"/agendaximo/login.php?user=" + usu + "&password=" + pass);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/xml");

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            String resul ="";
            out.write(resul);
            out.flush();
            out.close();

            String headerName = "";

            for (int i = 1; (headerName = connection.getHeaderFieldKey(i)) != null; i++)
            {
                if(headerName.equals("Set-Cookie"))
                {
                    cookieValue = connection.getHeaderField(i);
                }
            }
            return resul.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(connection != null)
                connection.disconnect();
        }
    }

*/
    //1 - ENVIAMOS DATOS
    public String enviarDatosGET(String usu, String pass) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;
        try {
            url = new URL("http://"+getIP()+"/agendaximo/login.php?user=" + usu + "&password=" + pass);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            respuesta = connection.getResponseCode();
            resul = new StringBuilder();
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
        IP=etIP.getText().toString();

        return resul.toString();
    }

    //2 - METODO PARA SABER SI ENVIARDATOSGET TIENE DATOS O NO
    public int obtenerDatosJson(String respuesta) {
        try {
            JSONObject json = new JSONObject(respuesta);
            int id = json.getInt("userId");
            if (id > 0) {
                return id;
            }
            return -1;
        } catch (Exception e) {
            return -2;
        }

    }
}


    /*

        protected Boolean doInBackground(Void... params) {
            String line;
            StringBuilder result=null;
            try {
                URL url = new URL("http://agenda.rf.gd/login.php?user=" + mEmail + "&password=" + mPassword);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                int answer = con.getResponseCode();
                result = new StringBuilder();
                if (answer == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(con.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                }
            }catch(Exception e){
                return false;
            }
            return readJSON(result.toString());
        }

        private boolean readJSON(String in){
            try{
                JSONObject json = new JSONObject(in);
                if(json.getBoolean("authorized")){
                    return true;
                }
                return false;
            }catch (Exception e){
                return false;
            }
        }

*/