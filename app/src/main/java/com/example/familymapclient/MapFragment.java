package com.example.familymapclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import Data.DataCache;
import model.Event;
import model.Person;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;



public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private Map<String, Float> markerColors = new HashMap<>();
    private final float[] colorChoices = {BitmapDescriptorFactory.HUE_AZURE, BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_CYAN, BitmapDescriptorFactory.HUE_GREEN,
    BitmapDescriptorFactory.HUE_MAGENTA, BitmapDescriptorFactory.HUE_ORANGE, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_ROSE, BitmapDescriptorFactory.HUE_VIOLET,
    BitmapDescriptorFactory.HUE_YELLOW};
    private TextView eventView;
    private View view;
    private Set<Integer> usedIndexes = new TreeSet<>();
    private DataCache instance;
    private String currGender;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*final ActivityResultLauncher<Intent> orderInfoActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent resultData = result.getData();
                            if(resultData != null) {

                            }
                        }
                    }
                });*/

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);

        addEventMarkers();
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (marker != null) {
                    Event currEvent = (Event) marker.getTag();
                    eventView = view.findViewById(R.id.mapTextView);
                    eventView.setText(eventToText(currEvent));
                    Drawable genderIcon;
                    if (currGender.equals("m")) {
                        genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                                colorRes(R.color.male_icon).sizeDp(40);
                        eventView.setCompoundDrawables(genderIcon, null, null, null);
                    }
                    else if (currGender.equals("f")) {
                        genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                                colorRes(R.color.female_icon).sizeDp(40);
                        eventView.setCompoundDrawables(genderIcon, null, null, null);
                    }
                    map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    instance.setCurrPerson(instance.getPersonById(currEvent.getPersonId()));
                    return true;
                }
                return false;
            }
        });
        //eventView.setOnClickListener(new onClickListener());
    }

    private void addEventMarkers() {
        instance = DataCache.getInstance();
        Map<String, Event> events = instance.getEvents();
        for (Event e : events.values()) {
            LatLng currPosition = new LatLng(e.getLatitude(), e.getLongitude());
            Marker newMarker = map.addMarker(new MarkerOptions().position(currPosition).title(e.getEventType()));
            newMarker.setTag(e);
            setMarkerColor(newMarker, e);
        }
    }

    private void setMarkerColor(Marker currMarker, Event currEvent) {
        if (markerColors.isEmpty() || !markerColors.containsKey(currEvent.getEventType())) {
            int index = new Random().nextInt(colorChoices.length);
            if (usedIndexes.size() != colorChoices.length) {
                while (usedIndexes.contains(index)) {
                    index = new Random().nextInt(colorChoices.length);
                }
            }
            float newColor = colorChoices[index];
            usedIndexes.add(index);
            markerColors.put(currEvent.getEventType(), newColor);
        }
        currMarker.setIcon(BitmapDescriptorFactory.defaultMarker(markerColors.get(currEvent.getEventType())));
    }

    private String eventToText(Event event) {
        instance = DataCache.getInstance();
        String eventString;
        Person currPerson = instance.getPersonById(event.getPersonId());
        currGender = currPerson.getGender();
        eventString = currPerson.getFirstName() + " " + currPerson.getLastName() + "\n" +
                event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" +
                event.getYear() + ")";
        return eventString;
    }

    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }
}
