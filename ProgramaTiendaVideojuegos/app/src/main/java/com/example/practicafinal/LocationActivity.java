package com.example.practicafinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, LocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                0,
                0
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationdrawer_view);
        navigationView.setNavigationItemSelectedListener(this);

        //pillar servicio de localizaci??n del usuario
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        TextView textView = (TextView) findViewById(R.id.text_kilometros);
        Location locationA = new Location("punto A");

        locationA.setLatitude(40.414821923253);
        locationA.setLongitude(-3.7033408687289855);

        Location locationB = new Location("punto B");

        locationB.setLatitude(location.getLatitude());
        locationB.setLongitude(location.getLongitude());

        float distance = locationB.distanceTo(locationA);
        distance /= 1000;
        textView.setText("Estas a " + Float.toString(distance) + " km de tu tienda m??s cercana");
    }

    @Override
    public void onMapReady (GoogleMap googleMap){
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(40.414821923253, -3.7033408687289855))
            .title("Marker")
        );

        //centrar punto en maps
        LatLngBounds gamestopShop = new LatLngBounds(
                new LatLng(40.414821923253, -3.7033408687289855), // SW bounds
                new LatLng(40.414821923253, -3.7033408687289855)  // NE bounds
        );
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gamestopShop.getCenter(), 15));
    }

    //Icono del men?? lateral izquierdo
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }

    }

    //Icono de la derecha carrito
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Darle funcionalidad al icono del carrito
    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.marketCart:
                Intent intent = new Intent(this, ShoppingCartActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    //funcionalidad men?? lateral izquierdo
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(this, SectionActivity.class);
        switch (id)
        {
            case R.id.navigation_home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.navigation_novedades:
                intent.putExtra("SECTION_ID", 0);
                startActivity(intent);
                break;
            case R.id.navigation_ofertas:
                intent.putExtra("SECTION_ID", 1);
                startActivity(intent);
                break;
            case R.id.navigation_ps4:
                intent.putExtra("SECTION_ID", 2);
                startActivity(intent);
                break;
            case R.id.navigation_xbox:
                intent.putExtra("SECTION_ID", 3);
                startActivity(intent);
                break;
            case R.id.navigation_carrito:
                Intent intentShopCart = new Intent(this, ShoppingCartActivity.class);
                startActivity(intentShopCart);
                break;
            case R.id.navigation_location:
                Intent intentLocation = new Intent(this, LocationActivity.class);
                startActivity(intentLocation);
                break;
            case R.id.navigation_contact:
                Intent intent_contact = new Intent(this, ContactActivity.class);
                startActivity(intent_contact);
                break;
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}