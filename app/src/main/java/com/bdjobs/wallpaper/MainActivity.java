package com.bdjobs.wallpaper;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GridView grid;
    private List<Wallpaper_> wallpapers = new ArrayList<Wallpaper_>();
    TextView categoryTV;
    Button featuredBTN, editorBTN, popularBTN;
    int selected_item = 0;
    NavigationView navigationView;
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressBar progressBar;
    private Boolean exit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        categoryTV = (TextView) findViewById(R.id.categoryTV);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        featuredBTN = (Button) findViewById(R.id.featuredBTN);
        editorBTN = (Button) findViewById(R.id.editorBTN);
        popularBTN = (Button) findViewById(R.id.popularBTN);
        grid = (GridView) findViewById(R.id.grid);




        if (!isOnline(MainActivity.this)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("NO Internet Connection! Please connect to internet and Try again.");
            alertDialogBuilder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();

        } else {
            getServerData();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }


    }

    /*@Override
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {






        if (!isOnline(MainActivity.this)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("NO Internet Connection! Please connect to internet and Try again.");
            alertDialogBuilder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();

        } else {
            GoogleAnalyticsData.tracker().send(new HitBuilders.EventBuilder(String.valueOf(item.getTitle()), "Open")
                    .setLabel("Category")
                    .build());

            // Handle navigation view item clicks here.
            int id = item.getItemId();
            featuredBTN.setTextColor(getResources().getColor(R.color.inactive));
            editorBTN.setTextColor(getResources().getColor(R.color.inactive));
            popularBTN.setTextColor(getResources().getColor(R.color.inactive));
            if (id == R.id.abstractC) {
                selected_item = 0;
                categoryTV.setText("Abstract Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getAbstractWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            } else if (id == R.id.animals) {
                selected_item = 1;
                categoryTV.setText("Animals Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getAnimalandBirdsWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            } else if (id == R.id.architecture) {
                selected_item = 2;
                categoryTV.setText("Architecture Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getArchitectureWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            } else if (id == R.id.beach) {
                selected_item = 3;
                categoryTV.setText("Beach Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getBeachWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            } else if (id == R.id.bike) {
                selected_item = 4;
                categoryTV.setText("Bikes Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getBikeWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            } else if (id == R.id.business) {
                selected_item = 5;
                categoryTV.setText("Business Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getBusinessWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            } else if (id == R.id.city) {
                selected_item = 6;
                categoryTV.setText("City Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getCityWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            } else if (id == R.id.creative) {
                selected_item = 7;
                categoryTV.setText("Creative Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getCreativeWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            } else if (id == R.id.flowers) {
                selected_item = 8;
                categoryTV.setText("Flower Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getFlowersWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            } else if (id == R.id.food) {
                selected_item = 9;
                categoryTV.setText("Food Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getFoodWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            } /*else if (id == R.id.funny) {
                selected_item = 10;
                categoryTV.setText("Funny Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getFunnyWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            }*/ else if (id == R.id.games) {
                selected_item = 11;
                categoryTV.setText("Games Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getGamesWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            } /*else if (id == R.id.inspiraton) {
                selected_item = 12;
                categoryTV.setText("Inspirational Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getInspirationalWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            }*/ /*else if (id == R.id.landscape) {
                selected_item = 13;
                categoryTV.setText("Landscape Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getLandscapeWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            }*/ else if (id == R.id.macro) {
                selected_item = 14;
                categoryTV.setText("Macro Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getMacroWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            } /*else if (id == R.id.minimal) {
                selected_item = 15;
                categoryTV.setText("Minimal Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getMinimalWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            }*/ else if (id == R.id.nature) {
                selected_item = 16;
                categoryTV.setText("Nature Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getNatureWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            } else if (id == R.id.space) {
                selected_item = 17;
                categoryTV.setText("Space Wallpaper");
                wallpapers.clear();
                grid.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                API.Factory.getInstance().getSpaceWallpaper().enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        wallpapers = response.body().getWallpaper();
                        grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                        progressBar.setVisibility(View.GONE);
                        grid.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        FirebaseCrash.report(new Exception(t.getMessage()));
                    }
                });
            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getServerData() {

        GoogleAnalyticsData.tracker().send(new HitBuilders.EventBuilder("First Page", "Open")
                .setLabel("Category")
                .build());
        categoryTV.setText("Featured Wallpaper");
        featuredBTN.setTextColor(getResources().getColor(R.color.active));
        editorBTN.setTextColor(getResources().getColor(R.color.inactive));
        popularBTN.setTextColor(getResources().getColor(R.color.inactive));
        progressBar.setVisibility(View.VISIBLE);

        API.Factory.getInstance().getFeaturedWallpaper().enqueue(new Callback<Wallpaper>() {
            @Override
            public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                wallpapers = response.body().getWallpaper();
                grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Wallpaper> call, Throwable t) {
                FirebaseCrash.report(new Exception(t.getMessage()));
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void getFeaturedWallpaper(View view) {
        GoogleAnalyticsData.tracker().send(new HitBuilders.EventBuilder("Featured Wallpaper", "Open")
                .setLabel("Category")
                .build());

        navigationView.getMenu().getItem(selected_item).setChecked(false);
        categoryTV.setText("Featured Wallpaper");
        featuredBTN.setTextColor(getResources().getColor(R.color.active));
        editorBTN.setTextColor(getResources().getColor(R.color.inactive));
        popularBTN.setTextColor(getResources().getColor(R.color.inactive));
        wallpapers.clear();
        grid.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        if (!isOnline(MainActivity.this)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("NO Internet Connection! Please connect to internet and Try again.");
            alertDialogBuilder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();

        } else {

            API.Factory.getInstance().getFeaturedWallpaper().enqueue(new Callback<Wallpaper>() {
                @Override
                public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                    wallpapers = response.body().getWallpaper();
                    grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                    progressBar.setVisibility(View.GONE);
                    grid.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<Wallpaper> call, Throwable t) {
                    FirebaseCrash.report(new Exception(t.toString()));
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    public void getEditorWallpaper(View view) {
        GoogleAnalyticsData.tracker().send(new HitBuilders.EventBuilder("Editor Wallpaper", "Open")
                .setLabel("Category")
                .build());
        navigationView.getMenu().getItem(selected_item).setChecked(false);
        categoryTV.setText("Editor's Choice");
        featuredBTN.setTextColor(getResources().getColor(R.color.inactive));
        editorBTN.setTextColor(getResources().getColor(R.color.active));
        popularBTN.setTextColor(getResources().getColor(R.color.inactive));
        wallpapers.clear();
        grid.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        if (!isOnline(MainActivity.this)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("NO Internet Connection! Please connect to internet and Try again.");
            alertDialogBuilder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();

        } else {


            API.Factory.getInstance().getEditorWallpaper().enqueue(new Callback<Wallpaper>() {
                @Override
                public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                    wallpapers = response.body().getWallpaper();
                    grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                    progressBar.setVisibility(View.GONE);
                    grid.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<Wallpaper> call, Throwable t) {
                    FirebaseCrash.report(new Exception(t.toString()));
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    public void getPopularWallpaper(View view) {
        GoogleAnalyticsData.tracker().send(new HitBuilders.EventBuilder("Popular Wallpaper", "Open")
                .setLabel("Category")
                .build());
        navigationView.getMenu().getItem(selected_item).setChecked(false);
        categoryTV.setText("Popular Wallpaper");
        featuredBTN.setTextColor(getResources().getColor(R.color.inactive));
        editorBTN.setTextColor(getResources().getColor(R.color.inactive));
        popularBTN.setTextColor(getResources().getColor(R.color.active));
        wallpapers.clear();
        grid.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        if (!isOnline(MainActivity.this)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("NO Internet Connection! Please connect to internet and Try again.");
            alertDialogBuilder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();

        } else {


            API.Factory.getInstance().getPopularWallpaper().enqueue(new Callback<Wallpaper>() {
                @Override
                public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                    wallpapers = response.body().getWallpaper();
                    grid.setAdapter(new GridAdapter(MainActivity.this, wallpapers));
                    progressBar.setVisibility(View.GONE);
                    grid.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<Wallpaper> call, Throwable t) {
                    FirebaseCrash.report(new Exception(t.toString()));
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private class GridAdapter extends BaseAdapter {

        Context context;
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

            final String thumb = wallpapers.get(position).getThumb();

            Glide.with(context)
                    .load(thumb)
                    .override(200, 200)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            FirebaseCrash.report(new Exception(e.toString()));
                            FirebaseCrash.log("imageURL: "+thumb);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            return false;
                        }
                    })
                    .into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Main2Activity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, wallpapers.get(position).getCategory());
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, thumb);
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
                    intent.putExtra("thumb", thumb);
                    intent.putExtra("link", wallpapers.get(position).getPicurl());
                    intent.putExtra("rating", wallpapers.get(position).getRating());
                    intent.putExtra("title", wallpapers.get(position).getTitle());
                    intent.putExtra("description", wallpapers.get(position).getDescription());
                    intent.putExtra("source", wallpapers.get(position).getSource());
                    intent.putExtra("category", wallpapers.get(position).getCategory());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        GoogleAnalyticsData.tracker().send(new HitBuilders.EventBuilder(thumb, "Preview")
                                .setLabel("Image")
                                .build());
                        Bundle bundle1 = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, holder.img, holder.img.getTransitionName()).toBundle();
                        startActivity(intent, bundle1);
                    } else {
                        GoogleAnalyticsData.tracker().send(new HitBuilders.EventBuilder(thumb, "thumb_Preview")
                                .setLabel("Image")
                                .build());

                        startActivity(intent);
                    }
                }
            });

            //setAnimation(holder.img,position);
            return convertView;
        }

        public class Holder {
            ImageView img;
        }

        private void setAnimation(View viewToAnimate, int position) {
            // If the bound view wasn't previously displayed on screen, it's animated

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            viewToAnimate.startAnimation(animation);
            //lastPosition = position;

        }


    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
