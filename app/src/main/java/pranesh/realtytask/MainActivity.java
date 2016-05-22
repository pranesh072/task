package pranesh.realtytask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.pm.PackageManager.NameNotFoundException;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import pranesh.realtytask.gcm.RegistrationIntentService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SQLiteHandler db;
    private SessionManager session;
    ListView listView;
    Toolbar toolbar;
    public static String[] petition_id;
    public static String[] petition_addressed_to;
    public static String[] petition_title;
    public static String[] petition_description;
    public static String[] petition_started_by;
    public static String[] petition_supporters;
    private static String petitions_url = "http://lifelineapp.hol.es/reality_task_api/getPetitions.php";
    private static final String TAG_PETITIONS = "petitions";
    private static final String TAG_ID = "id";
    private static final String TAG_ADDRESSED = "addressed_to";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DESC = "desc";
    private static final String TAG_STARTEDBY = "started_by";
    private static final String TAG_SUPPORTERS = "supporters";
    JSONArray petitions = null;

    GoogleCloudMessaging gcm;
    Context context;
    String regId;
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";

    static final String TAG = "Register Activity";
    AsyncTask<Void, Void, String> shareRegidTask;
    ShareExternalServer appUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


// To track statistics around application


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        if (TextUtils.isEmpty(regId)) {
            regId = registerGCM();
            Log.d("RegisterActivity", "GCM RegId: " + regId);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Already Registered with GCM Server!",
                    Toast.LENGTH_LONG).show();
        }

        appUtil = new ShareExternalServer();

      //  regId = getIntent().getStringExtra("regId");
       // Log.d("MainActivity", "regId: " + regId);
        shareRegidTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = appUtil.shareRegIdWithAppServer(context, regId);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
               // shareRegidTask = null;
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }

        };
        shareRegidTask.execute(null, null, null);








        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listView = (ListView) findViewById(R.id.listView);
        getPetitions();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            drawer.closeDrawer(GravityCompat.START);


        } else if (id == R.id.nav_gallery) {
            Intent intent1 = new Intent(MainActivity.this, Listpost.class);
            startActivity(intent1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getPetitions() {

        String tag_json_obj = "json_obj_req";
        StringRequest jsonStrReq = new StringRequest(Request.Method.POST,
                petitions_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                String jsonStr = s.toString();
                Log.d("Petitions JSON :", jsonStr);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        // Getting JSON Array node
                        petitions = jsonObj.getJSONArray(TAG_PETITIONS);
                        if (petitions.length() == 0) {
                            Toast.makeText(getApplicationContext(), "no petitions found", Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                        } else {

                            petition_id = new String[petitions.length()];
                            petition_addressed_to = new String[petitions.length()];
                            petition_title = new String[petitions.length()];
                            petition_description = new String[petitions.length()];
                            petition_started_by = new String[petitions.length()];
                            petition_supporters = new String[petitions.length()];

                            // looping through All Contacts
                            for (int i = 0; i < petitions.length(); i++) {
                                JSONObject c = petitions.getJSONObject(i);

                                String id = String.valueOf(c.getInt(TAG_ID));
                                String addressed_to = c.getString(TAG_ADDRESSED);
                                String title = c.getString(TAG_TITLE);
                                String description = c.getString(TAG_DESC);
                                String started_by = c.getString(TAG_STARTEDBY);
                                String supporters = String.valueOf(c.getInt(TAG_SUPPORTERS));

                                // adding each child node to HashMap key => value
                                petition_id[i] = id;
                                petition_addressed_to[i] = addressed_to;
                                petition_title[i] = title;
                                petition_description[i] = description;
                                petition_started_by[i] = started_by;
                                petition_supporters[i] = supporters;
                            }

                            //progressBar.setVisibility(View.GONE);
                            /**
                             * Updating parsed JSON data into ListView
                             * */
                            listView.setAdapter(new PetitionListAdapter(MainActivity.this, petition_addressed_to, petition_title, petition_description, petition_started_by, petition_supporters));
                            //Intent intent1 = new Intent(MainActivity.this,Listpost.class);
                            //startActivity(intent1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                //progressBar.setVisibility(View.GONE);
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonStrReq, tag_json_obj);
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
    }

    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("RegisterActivity",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);


        } else {
            Toast.makeText(getApplicationContext(),
                    "RegId already available. RegId: " + regId,
                    Toast.LENGTH_LONG).show();
        }
        return regId;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(Config.GOOGLE_PROJECT_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                    storeRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }


            @Override
            protected void onPostExecute(String msg) {


                Toast.makeText(getApplicationContext(),
                        "Post with GCM Server." + msg, Toast.LENGTH_LONG)
                        .show();

            }
        }.execute(null, null, null);

    }
    private void shareRegidTask() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = appUtil.shareRegIdWithAppServer(context, regId);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                shareRegidTask = null;
                Log.v(TAG,result);
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }

        }.execute(null, null, null);;
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }

}
