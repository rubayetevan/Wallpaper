package com.bdjobs.wallpaper;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GridView grid;
    private List<Wallpaper_> wallpapers = new ArrayList<Wallpaper_>();
    TextView categoryTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categoryTV = (TextView) findViewById(R.id.categoryTV);


        grid = (GridView) findViewById(R.id.grid);
        getServerData();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        if (id == R.id.abstractC) {
            categoryTV.setText("Abstract Wallpaper");
            wallpapers.clear();
            API.Factory.getInstance().getAbstractWallpaper().enqueue(new Callback<Wallpaper>() {
                @Override
                public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                    wallpapers = response.body().getWallpaper();
                    grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                }

                @Override
                public void onFailure(Call<Wallpaper> call, Throwable t) {

                }
            });

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getServerData() {
        categoryTV.setText("Featured Wallpaper");
        API.Factory.getInstance().getFeaturedWallpaper().enqueue(new Callback<Wallpaper>() {
            @Override
            public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                wallpapers = response.body().getWallpaper();
                grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
            }

            @Override
            public void onFailure(Call<Wallpaper> call, Throwable t) {

            }
        });
    }

    public void getFeaturedWallpaper(View view) {
        categoryTV.setText("Featured Wallpaper");
        wallpapers.clear();
        API.Factory.getInstance().getFeaturedWallpaper().enqueue(new Callback<Wallpaper>() {
            @Override
            public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                wallpapers = response.body().getWallpaper();
                grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
            }

            @Override
            public void onFailure(Call<Wallpaper> call, Throwable t) {

            }
        });
    }


    private class GridAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> links = new ArrayList<>();
        List<Wallpaper_> wallpapers = new ArrayList<Wallpaper_>();

        public GridAdapter(Context context, List<Wallpaper_> wallpapers) {
            this.context = context;
            this.wallpapers = wallpapers;
        }

        @Override
        public int getCount() {
            return wallpapers.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Holder holder = new Holder();

            convertView = getLayoutInflater().inflate(R.layout.grid_item, parent, false);
            holder.img = (ImageView) convertView.findViewById(R.id.imgv);

            final String ln = wallpapers.get(position).getPicurl();

            Glide.with(context)
                    .load(ln)
                    .override(200, 200)
                    .into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Main2Activity.class);

                    intent.putExtra("link", ln);
                    intent.putExtra("rating", wallpapers.get(position).getRating());
                    intent.putExtra("title", wallpapers.get(position).getTitle());
                    intent.putExtra("description", wallpapers.get(position).getDescription());
                    intent.putExtra("source", wallpapers.get(position).getSource());
                    intent.putExtra("category", wallpapers.get(position).getCategory());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, holder.img, holder.img.getTransitionName()).toBundle();
                        startActivity(intent, bundle);
                    } else {

                        startActivity(intent);
                    }
                }
            });

            //setAnimation(holder.img,position);
            return convertView;
        }

        public class Holder {
            TextView tv;
            ImageView img;
        }

        private void setAnimation(View viewToAnimate, int position) {
            // If the bound view wasn't previously displayed on screen, it's animated

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            viewToAnimate.startAnimation(animation);
            //lastPosition = position;

        }


    }
}
