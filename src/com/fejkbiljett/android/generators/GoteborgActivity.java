package com.fejkbiljett.android.generators;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.RadioButton;

import com.fejkbiljett.android.R;
import com.fejkbiljett.android.tickets.GoteborgTicket;
import com.fejkbiljett.android.tickets.Ticket;

public class GoteborgActivity extends TicketGeneratorActivity {
	public void onCreate(Bundle savedInstanceState) {
		mCityName = "Göteborg";
		
		setContentView(R.layout.ticket_goteborg);
		super.onCreate(savedInstanceState);
	}

	public Bundle getParams() {
		Bundle data = new Bundle();
		
		Editor editor = getPreferences(MODE_PRIVATE).edit();
		
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String myNumber = tMgr.getLine1Number();
		
		editor.putString("my_number", myNumber);
		
		boolean bReduced;
		if (((RadioButton) findViewById(R.id.price_reduced)).isChecked()) {
			bReduced = true;
		} else {
			bReduced = false;
		}
		editor.putBoolean("price_reduced", bReduced);
		
		String zone = "";
		if (((RadioButton) findViewById(R.id.zone_gbg)).isChecked()) {
			zone = "gbg";		
		} else if (((RadioButton) findViewById(R.id.zone_gbgp)).isChecked()) {
				zone = "gbg+";
		} else if (((RadioButton) findViewById(R.id.zone_gbgpp)).isChecked()) {
			zone = "gbg++";
		} else if (((RadioButton) findViewById(R.id.zone_gbgppp)).isChecked()) {
			zone = "gbg+++";
		} else if (((RadioButton) findViewById(R.id.zone_kalv)).isChecked()) {
			zone = "kalv";
		}
		
		editor.putString("zone", zone);
		editor.commit();
		
		data.putString("my_number", myNumber);
		data.putBoolean("price_reduced", bReduced);
		data.putString("zone", zone);
		
		return data;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		if (prefs.getBoolean("price_reduced", false)) {
			((RadioButton) findViewById(R.id.price_reduced)).setChecked(true);
		}
		
		String zone = prefs.getString("zone", "");
		if (zone.equals("gbg")) {
			((RadioButton) findViewById(R.id.zone_gbg)).setChecked(true);
		} else if (zone.equals("gbg+")) {
				((RadioButton) findViewById(R.id.zone_gbgp)).setChecked(true);
		} else if (zone.equals("gbg++")) {
			((RadioButton) findViewById(R.id.zone_gbgpp)).setChecked(true);
		} else if (zone.equals("gbg+++")) {
			((RadioButton) findViewById(R.id.zone_gbgppp)).setChecked(true);
		} else if (zone.equals("kalv")) {
			((RadioButton) findViewById(R.id.zone_kalv)).setChecked(true);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		Editor editor = getPreferences(MODE_PRIVATE).edit();
		Bundle data = getParams();
		
		editor.putBoolean("price_reduced", data.getBoolean("price_reduced"));
		
		editor.putString("zone", data.getString("zone"));

		editor.commit();
	}

	@Override
	public Ticket getTicket() {
		return new GoteborgTicket();
	}
}
