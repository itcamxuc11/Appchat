package com.uiresource.appchat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.view.Gravity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView chats;
    NavigationView navigationView, navigationViewBottom;
    DrawerLayout drawer;

    //Link ảnh của nav_header_main.xml
    CircleImageView profile_image;
    TextView username;


    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar(R.id.toolbar, "Messages");


   //Add list Chat vao Main
        FragmentTransaction ft;
        FragmentChat fragmentChat = new FragmentChat();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frameLayout, fragmentChat).commit();

        //Add profile vao Main
       FragmentProfile fragmentProfile=new FragmentProfile();
       ft=getSupportFragmentManager().beginTransaction();
       ft.add(R.id.nav_view,fragmentProfile).commit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationViewBottom = (NavigationView) findViewById(R.id.nav_view_bottom);
        navigationViewBottom.setNavigationItemSelectedListener(this);


        chats =(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_chats));
        initializeCountDrawer();

    }


    private void initializeCountDrawer(){
        chats.setGravity(Gravity.CENTER);
        chats.setTypeface(null, Typeface.BOLD);
        chats.setTextColor(getResources().getColor(R.color.colorAccent));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            chats.setTextAppearance(R.style.LightNav);
            chats.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        chats.setText("99+");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction ft;
        int id=item.getItemId();
       /* switch (item.getItemId()){
            case R.id.nav_contacts:

        }*/

        if (id == R.id.nav_contacts) {
            FragmentContacts fragmentContacts = new FragmentContacts();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, fragmentContacts).addToBackStack(null).commit();
        } else if (id == R.id.nav_chats) {
            FragmentChat fragmentChat = new FragmentChat();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, fragmentChat).commit();
        } else if (id == R.id.nav_trash) {
        } else if (id == R.id.nav_settings) {
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,IndexActivity.class));
            finish();
            return  true;

        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }




}
