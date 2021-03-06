package com.example.practicafinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class FinalizarCompraActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText editTextName;
    EditText editTextDir;
    EditText editTextTelefono;
    EditText editTextEmail;

    RadioButton radioButtonVisa;
    RadioButton radioButtonMastercard;
    RadioButton radioButtonPaypal;

    String precioTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_compra);

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

        editTextName = (EditText) findViewById(R.id.edit_text_nombre);
        editTextDir = (EditText) findViewById(R.id.edit_text_direccion);
        editTextTelefono = (EditText) findViewById(R.id.edit_text_telefono) ;
        editTextEmail = (EditText) findViewById(R.id.edit_text_email);

        radioButtonVisa = (RadioButton) findViewById(R.id.radio_visa);
        radioButtonMastercard = (RadioButton) findViewById(R.id.radio_mastercard);
        radioButtonPaypal = (RadioButton) findViewById(R.id.radio_paypal);

        precioTotal = getIntent().getStringExtra("PRECIO_TOTAL");
    }

    public void buyButton(View v) {
        if (editTextName.getText().toString().isEmpty() || editTextDir.getText().toString().isEmpty()
                || editTextTelefono.getText().toString().isEmpty() || editTextEmail.getText().toString().isEmpty()){
            Toast.makeText(FinalizarCompraActivity.this, "Rellene los campos en blanco", Toast.LENGTH_SHORT).show();
        }
        else if (!radioButtonVisa.isChecked() && !radioButtonMastercard.isChecked() && !radioButtonPaypal.isChecked()){
            Toast.makeText(FinalizarCompraActivity.this, "Seleccione un m??todo de pago", Toast.LENGTH_SHORT).show();
        }
        else {

            SQLiteOpenHelper gameDbHelper = new GameDataHelper(this) ;
            SQLiteDatabase db = gameDbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(" SELECT NAME FROM GAMES WHERE SHOPPING_CART=1", null);
            String[] videogameNames = new String[cursor.getCount()];
            int i = 0;
            cursor.moveToFirst();
            while (i < cursor.getCount()){
                videogameNames[i] = cursor.getString(0);
                cursor.moveToNext();
                i++;
            }

            String nombre = editTextName.getText().toString();
            String dir = editTextDir.getText().toString();
            String telefono = editTextTelefono.getText().toString();
            String email = editTextEmail.getText().toString();
            String[] emails = email.split(",");
            String metodoPago = null;
            if (radioButtonVisa.isChecked()){
                metodoPago = "Visa";
            }
            else if (radioButtonMastercard.isChecked()){
                metodoPago = "Mastercard";
            }
            else if (radioButtonPaypal.isChecked()){
                metodoPago = "Paypal";
            }

            String message = "Nombre: " + nombre + "\nDirecci??n: " + dir + "\nTel??fono: " + telefono + "\nM??todo de pago: " + metodoPago + "\nPrecio total: " + precioTotal + "???\nVideojuegos:\n";

            for (int j = 0; j < cursor.getCount(); j++){
                message = message + videogameNames[j] + "\n";
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, emails);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Confirmaci??n de pedido GameStop");
            intent.putExtra(Intent.EXTRA_TEXT, message);

            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "Elige c??mo enviarlo: "));
        }
    }


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

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
                Intent intentContact = new Intent(this, ContactActivity.class);
                startActivity(intentContact);
                break;
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}