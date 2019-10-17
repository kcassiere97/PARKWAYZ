package com.example.parkwayz;



import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import androidx.annotation.NonNull;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private FirebaseFirestore mapDB = FirebaseFirestore.getInstance();
    private CollectionReference ownerRef = mapDB.collection("owners");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    public void createOwner(String token, final GoogleMap gMap, final Owner owner) {

        ownerRef.document(token).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        GeoPoint gPoint = documentSnapshot.getGeoPoint("Coordinate");
                        double lat = gPoint.getLatitude();
                        double lng = gPoint.getLongitude();
                        LatLng coord = new LatLng(lat, lng);
                        String address = documentSnapshot.getString("Address");
                        String name = documentSnapshot.getString("Name");
                        boolean isAvailable = documentSnapshot.getBoolean("IsAvailable");

                        Marker marker = gMap.addMarker(new MarkerOptions()
                                .position(coord)
                                .title(address));

                        owner.setAddress(address);
                        owner.setName(name);
                        owner.setAvailable(isAvailable);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public class Owner{
        String address;
        String name;
        boolean isAvailable;

        public Owner(String address, String name, boolean isAvailable){

            this.address = address;
            this.name = name;
            this.isAvailable = isAvailable;
        }

        public void setAddress(String address){
            this.address = address;
        }
        public String getAddress(){
            return address;
        }

        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return name;
        }

        public void setAvailable(boolean isAvailable){
            this.isAvailable = isAvailable;
        }
        public boolean getAvailable(){
            return isAvailable;
        }
    }

    public Owner createNewOwner(String token, GoogleMap gMap){
        Owner owner = new Owner("N/A", "N/A", false);
        createOwner(token, gMap, owner);
        return owner;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final TextView Loc_Info = findViewById(R.id.LocInfo);


        final Owner owner1 = createNewOwner("2JybLG6fosj8DyAbbKaC", googleMap);
        final Owner owner2 = createNewOwner("Zg1ZAAVFpuP9sEjiRyuk", googleMap);
        //final Owner owner3 = createNewOwner("m670SYjzMbtKFsfIqGEj", googleMap);
        final Owner owner4 = createNewOwner("vIKUrHmmcN7mUdiHgfdT", googleMap);

        final ArrayList<Owner> ownerList = new ArrayList();
        ownerList.add(owner1);
        ownerList.add(owner2);
        //ownerList.add(owner3);
        ownerList.add(owner4);


        LatLng CSUF = new LatLng(33.882434, -117.882466);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(CSUF));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CSUF,14.0f));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int check;
                String str1;
                String str2;

                for (Owner o: ownerList) {
                    str1 = o.getAddress();
                    str2 = marker.getTitle();
                    check = str1.compareToIgnoreCase(str2);
                    if(check == 0){
                        Loc_Info.setText(o.getName() + "\n" + o.getAddress());
                        break;

                    }


                }
                return false;
            }
        });
    }
}
