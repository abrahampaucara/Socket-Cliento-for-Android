package com.riusjm.asockettester;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferenciasPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferencefile);
	}

}
