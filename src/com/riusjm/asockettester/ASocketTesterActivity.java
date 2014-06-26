package com.riusjm.asockettester;

import java.util.ArrayList;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

public class ASocketTesterActivity extends TabActivity implements OnGesturePerformedListener {
	
	private static final String pckgname = "com.riusjm.asockettester";
	private static final String classid = "ASocketTesterActivity";
	private static int objectcounter = 0;
	private final int objectid;

	private GestureLibrary libreria;
	private TabHost tabHost;

	private Resources res;
	private TabHost.TabSpec spec;
	private Intent intent;

	private ArrayList<String> pestanas = new ArrayList<String>();
	private ArrayList<Activity> pestanasref = new ArrayList<Activity>();
	private ArrayList<Boolean> pestanasv = new ArrayList<Boolean>();


	public ASocketTesterActivity() {
		super();
		objectcounter++;
		objectid=objectcounter;
	}

	private String nuevapestana() {
		int auxint = pestanas.size();
		auxint++;
		String auxstring = "Client "+auxint;
		pestanas.add(auxstring);
		pestanasv.add(true);

		intent = new Intent().setClass(this, TestSocketActivity.class);
		intent.putExtra("clave01", auxstring);

		spec = tabHost.newTabSpec(auxstring).setIndicator(auxstring).setContent(intent);
		tabHost.addTab(spec);

		auxint = tabHost.getTabWidget().getChildCount();
		setpestana(auxint-1);

		return auxstring;
	}
	public void nuevapestanaref(Activity actividadhija) {
		pestanasref.add(actividadhija);
	}

	public void setpestana(int n) {
		tabHost.setCurrentTab(n);
	}

	private void tabhostdeletecurrenttab () {

		int newcurrenttab = 0;
		int currenttab = tabHost.getCurrentTab();
		int childcount = tabHost.getTabWidget().getChildCount();
		int npestanas = pestanas.size();
		int npestanasref = pestanasref.size();
		int npestanasv = pestanasv.size();
		int npestanasvtrue = 0;
		ArrayList<Integer> pestanasvtruearray = new ArrayList<Integer>();
		int auxint = 0;
		for (Boolean pestanasvelem : pestanasv) {
			if (pestanasvelem) {
				npestanasvtrue++;
				pestanasvtruearray.add(auxint);
			}
			auxint++;
		}
		auxint = pestanasvtruearray.size();


		if (npestanasvtrue > 1) {
			tabHost.getCurrentTabView().setVisibility(View.GONE);
			pestanasv.set(currenttab, false);
			npestanasvtrue--;

			boolean fin = false;
			auxint = 0;
			while (!fin && auxint<=(npestanasv-1)) {
				if (pestanasv.get(auxint) && auxint<currenttab) newcurrenttab = auxint;
				if (pestanasv.get(auxint) && auxint>currenttab) {newcurrenttab = auxint; fin=true;}
				auxint++;
			}

			setpestana(newcurrenttab);
		}

		currenttab = tabHost.getCurrentTab();
		childcount = tabHost.getTabWidget().getChildCount();
		npestanas = pestanas.size();
		npestanasref = pestanasref.size();
		npestanasv = pestanasv.size();
		npestanasvtrue = 0;
		pestanasvtruearray = new ArrayList<Integer>();
		auxint = 0;
		for (Boolean pestanasvelem : pestanasv) {
			if (pestanasvelem) {
				npestanasvtrue++;
				pestanasvtruearray.add(auxint);
				}
			auxint++;
		}
		auxint = pestanasvtruearray.size();

	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		final String methodname = "onCreate";

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		SharedPreferences pref = getSharedPreferences(pckgname+"_preferences",MODE_PRIVATE);

		String ip = pref.getString("ip", "158.42.146.127");

		int puerto = Integer.valueOf(pref.getString("puerto", "13"));

		String comando = pref.getString("comando", "command");

		int numtabs = Integer.valueOf(pref.getString("numtabs", "1"));

		boolean pasopaso = pref.getBoolean("prefcat_otros_pasopaso", false);
		
		libreria = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!libreria.load()) {
			finish();
		}
		GestureOverlayView gesturesView = (GestureOverlayView) findViewById(R.id.gestureoverlayvista);
		gesturesView.addOnGesturePerformedListener(this);

		tabHost = getTabHost();

		res = getResources();

		int auxint = 1;
		if (1<=numtabs && numtabs <= 10) auxint = numtabs;
		else if (10<numtabs) auxint = 10;
		else auxint = 1;

		for (int i = 1; i<=auxint; i++) {
			nuevapestana();			
		}
		
	}


	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

		String textlog = "";
		ArrayList<Prediction> predictions = libreria.recognize(gesture);
		for (Prediction prediction : predictions) {
			textlog = textlog.concat(prediction.name + " " + prediction.score + "\n");
		}

		if (predictions.size() > 0) {
			String comando = predictions.get(0).name;
			if (comando.equals("plus")) {
				String auxstring = nuevapestana();

			} else if (comando.equals("circ")) {
				 lanzarPreferencias();

			} else if (comando.equals("A")) {
				 lanzarAcercaDe();

			} else if (comando.equals("equis")) {
				tabhostdeletecurrenttab();
			} else {

			}
		}
	}
	

	@Override public boolean onCreateOptionsMenu (Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menuplus:
			String auxstring = nuevapestana();
			break;
		case R.id.menuminus:
			tabhostdeletecurrenttab();
			break;
		case R.id.menuconfig:
			 lanzarPreferencias();
			break;
		case R.id.menuabout:
			 lanzarAcercaDe();
			break;
		}
		return true;
	}
	
	public void lanzarPreferencias(){
		Intent i = new Intent(this,PreferenciasPreferenceActivity.class);
		startActivity(i);
	}
	
	public void lanzarAcercaDe(){
		Intent i = new Intent(this, AcercaDeActivity.class);
		startActivity(i);
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