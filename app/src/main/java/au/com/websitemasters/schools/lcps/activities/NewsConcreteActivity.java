package au.com.websitemasters.schools.lcps.activities;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import au.com.websitemasters.schools.lcps.R;
import au.com.websitemasters.schools.lcps.adapters.NewsMenuAdapter;
import au.com.websitemasters.schools.lcps.loader.LoaderBadge;
import au.com.websitemasters.schools.lcps.objects_for_adapters.NewsMenuObject;
import au.com.websitemasters.schools.lcps.utils.DateHelper;
import au.com.websitemasters.schools.lcps.utils.SQLiteHelper;


public class NewsConcreteActivity extends AppCompatActivity {

    private String title, date, photo_link, full_text;

    private NewsMenuAdapter adapter;

    LinearLayout linPhoto;

    private WebView myWebView;

    public SQLiteDatabase db;
    public SQLiteHelper dbHelper;

    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH.MM");
    private String currentDate = dateFormat.format(new Date());

    DateHelper dateHelper = new DateHelper();

    ArrayList<NewsMenuObject> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_concrete);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.news));
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new SQLiteHelper(this);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");

        //its an HTML of full news
        full_text = intent.getStringExtra("full_text");

        date = intent.getStringExtra("date");

        photo_link = intent.getStringExtra("photo_link");

        ((TextView) findViewById(R.id.tvTitle)).setText(title);

        ((TextView) findViewById(R.id.tvDate))
                .setText(dateHelper.convertSecondsToDate(date, "dd.MM.yyyy HH.mm"));
        linPhoto = (LinearLayout) findViewById(R.id.linPhoto);

        ImageView ivPhoto = ((ImageView) findViewById(R.id.ivPhoto));

        ImageView btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsConcreteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnAlerts = (ImageView) findViewById(R.id.btnAlerts);
        btnAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsConcreteActivity.this, AlertsMenuActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnFeed = (ImageView)findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsConcreteActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnCalendar = (ImageView)findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsConcreteActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        myWebView = (WebView)this.findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);

        String html =  full_text;

        if (html == null){
            html = "<html><body></body></html>";
        }

        html = html.replace("images/",
                "http://shthorn.wa.edu.au/images/");

        String mime = "text/html";
        String encoding = "utf-8";
        myWebView.loadDataWithBaseURL(null, html, mime, encoding, null);

        //count unreaded alerts, news, newsletters and show badge
        Loader<Bitmap> loaderBadge = new LoaderBadge(this, null);
        loaderBadge.forceLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings_inactive_white, menu);
        rotateSettingsItemAnimation(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void rotateSettingsItemAnimation(Menu menu) {

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Button iv = (Button) inflater.inflate(R.layout.set_inact_white, null);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);
        menu.findItem(R.id.settings).setActionView(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsConcreteActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
