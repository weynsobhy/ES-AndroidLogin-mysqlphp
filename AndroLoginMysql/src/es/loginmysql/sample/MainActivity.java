package es.loginmysql.sample;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.logreg.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity  {

	Button daftar, login;
	Intent a;
	EditText email, password;
	String url, success;
	SessionManager session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		session = new SessionManager(getApplicationContext());
		Toast.makeText(getApplicationContext(),
				"User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG)
				.show();
		
		daftar = (Button) findViewById(R.id.daftar);
		login = (Button) findViewById(R.id.login);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		
		daftar.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0) {
				Intent daftar = new Intent(MainActivity.this, Register.class);
				startActivity(daftar);
				
			}
		});
		
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				url = "http://10.0.2.2/androidlogin/login.php?" + "email="
						+ email.getText().toString() + "&password="
						+ password.getText().toString();

				if (email.getText().toString().trim().length() > 0
						&& password.getText().toString().trim().length() > 0) 
				{
					new Masuk().execute();
				} 
				else 
				{
					Toast.makeText(getApplicationContext(), "Username/password masih kosong gan.!!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	
	
	
	public class Masuk extends AsyncTask<String, String, String> 
	{
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
		ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Tunggu Bentar ya...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			JSONParser jParser = new JSONParser();

			JSONObject json = jParser.getJSONFromUrl(url);

			try {
				success = json.getString("success");

				Log.e("error", "nilai sukses=" + success);

				JSONArray hasil = json.getJSONArray("login");

				if (success.equals("1")) {

					for (int i = 0; i < hasil.length(); i++) {

						JSONObject c = hasil.getJSONObject(i);

					
						String nama = c.getString("nama").trim();
						String email = c.getString("email").trim();
						session.createLoginSession(nama, email);
						Log.e("ok", " ambil data");

					}
				} else {
					Log.e("erro", "tidak bisa ambil data 0");
				}

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("erro", "tidak bisa ambil data 1");
			}

			return null;
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (success.equals("1")) {
				a = new Intent(MainActivity.this, MyAccount.class);
				startActivity(a);
				finish();
			} else {
				
				Toast.makeText(getApplicationContext(), "Username/password salah gan.!!", Toast.LENGTH_LONG).show();
			}

		}
		
	}

}
