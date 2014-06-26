package com.riusjm.asockettester;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TestSocketActivity extends Activity {
	
	private static final String pckgname = "com.riusjm.asockettester";
	private static final String classid = "TestSocketActivity";
	private static int objectcounter = 0;
	private final int objectid;
	
	private ASocketTesterActivity actividadpadre;

	private String ip = "";
	private int puerto = 0;
	private Socket sk = null;
	private String linea = "";
	
	private EditText EditText_IP;
	private EditText EditText_Port;
	private EditText EditText_SEND;
	private TextView TextView_LOG;
	
	private BufferedReader entrada;
	private PrintWriter salida;


    public TestSocketActivity() {
		super();

    	final String methodname = "TestSocketActivity";
    	

		objectcounter++;
		objectid=objectcounter;


		actividadpadre = (ASocketTesterActivity) this.getParent();


	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	final String methodname = "onCreate";
 
        super.onCreate(savedInstanceState);

        actividadpadre = (ASocketTesterActivity) this.getParent();

		actividadpadre.nuevapestanaref(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String auxstring = extras.getString("clave01");
		}

        setContentView(R.layout.testsocket);

        EditText_IP = (EditText) findViewById(R.id.EditText_IP);
        EditText_Port = (EditText) findViewById(R.id.EditText_Port);
        EditText_SEND = (EditText) findViewById(R.id.EditText_SEND);
        TextView_LOG = (TextView) findViewById(R.id.TextView_LOG);


        EditText_IP.setText("192.168.2.102");
        EditText_Port.setText("1234");
        EditText_SEND.setText("PUNTUACIONES");

		SharedPreferences pref = getSharedPreferences(pckgname+"_preferences",MODE_PRIVATE);

		String ip = pref.getString("ip", "158.42.146.127");

		int puerto = Integer.valueOf(pref.getString("puerto", "13"));

		String comando = pref.getString("comando", "command");

		int numtabs = Integer.valueOf(pref.getString("numtabs", "1"));

		boolean pasopaso = pref.getBoolean("prefcat_otros_pasopaso", false);
		

        EditText_IP.setText(ip);
        EditText_Port.setText(String.valueOf(puerto));
        EditText_SEND.setText(comando);
		
    }

	public void clicboton(View vista) {
		final String methodname = "clicboton";

		String tag = vista.getTag().toString();

		if (tag.toLowerCase().equals("Clear".toLowerCase())) {
////////////////////////////////////////////////////////////////////////////////////////////////////////////
			TextView_LOG.setText("LOG:\n");

		} else if (tag.toLowerCase().equals("Connect".toLowerCase())) {
////////////////////////////////////////////////////////////////////////////////////////////////////////////
			ip = EditText_IP.getText().toString();
			puerto = Integer.parseInt(EditText_Port.getText().toString());
			                     TextView_LOG.append("Connect - @="+ip+":"+puerto+"\n");

			try {
				                     TextView_LOG.append("conectaNdo...\n");
				if (sk==null) {

					sk = new Socket(ip, puerto);

					entrada = new BufferedReader(new InputStreamReader(sk.getInputStream()));
					salida = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()), true);
					
				}
				                     TextView_LOG.append("conectAdo\n");
			} catch (Exception e) {
				Log.e("TestSocketsActivity","clicboton - Connect - error:" + e.toString());
			}

		} else if (tag.toLowerCase().equals("RL".toLowerCase())) {
////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
			SharedPreferences pref = getSharedPreferences(pckgname+"_preferences",MODE_PRIVATE);

			boolean pasopaso = pref.getBoolean("prefcat_otros_pasopaso", false);
			
			if (sk!=null) {
				try {
					linea = entrada.readLine();

					if (linea==null) {
						
					}
					                             TextView_LOG.append(linea+"\n");
				} catch (Exception e) {
					Log.e("TestSocketsActivity","clicboton - if RL - error:"+e.toString());
				}
			}
			
		} else if (tag.toLowerCase().equals("SEND".toLowerCase())) {
////////////////////////////////////////////////////////////////////////////////////////////////////////////
			SharedPreferences pref = getSharedPreferences(pckgname+"_preferences",MODE_PRIVATE);

			boolean pasopaso = pref.getBoolean("prefcat_otros_pasopaso", false);
			

			linea  = EditText_SEND.getText().toString();

			if (sk != null) {
				try {
					                           TextView_LOG.append("S:"+linea+"\n");
					salida.println(linea+"\n");

					/*linea = ""; 

					if (!pasopaso) {
						do {
							linea = entrada.readLine();
							if (linea!=null) {
							                           TextView_LOG.append(linea+"\n");
							}
						} while (linea != null);

					}*/
		
					                           
				} catch (Exception e) {
					Log.e("TestSocketsActivity", "clicboton - SEND - error:" + e.toString());
				}
			}
			
		} else if (tag.toLowerCase().equals("Close".toLowerCase())) {
////////////////////////////////////////////////////////////////////////////////////////////////////////////
			try {

				if (sk != null) {
					sk.close();
					                     TextView_LOG.append("cerrAdo\n");
					sk = null;
				}
			} catch (Exception e) {
				Log.e("TestSocketsActivity", "clicboton - Close - error:" + e.toString());
			}			
			
		} else {
////////////////////////////////////////////////////////////////////////////////////////////////////////////

		}
	}

	@Override
	protected void onDestroy() {

		final String methodname = "onDestroy";

		super.onDestroy();
	}

	@Override
	protected void onPause() {

		final String methodname = "onPause";

		super.onPause();
	}

	@Override
	protected void onRestart() {

		final String methodname = "onRestart";

		super.onRestart();
	}

	@Override
	protected void onResume() {

		final String methodname = "onResume";

		super.onResume();
	}

	@Override
	protected void onStart() {

		final String methodname = "onStart";

		super.onStart();
	}

	@Override
	protected void onStop() {

		final String methodname = "onStop";

		super.onStop();
	}
}