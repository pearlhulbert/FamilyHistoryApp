package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Data.DataCache;
import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    private static final int EVENT_ITEM_VIEW_TYPE = 0;
    private static final int FAMILY_ITEM_VIEW_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        SearchView searchView = findViewById(R.id.search_button);
        DataCache instance = DataCache.getInstance();

        List<Event> events = new ArrayList<>();
        events.addAll(instance.getEvents().values());
        List<Person> people = new ArrayList<>();
        people.addAll(instance.getPeople().values());
        List<Event> keepEvents = new ArrayList<>();
        List<Person> keepPeople = new ArrayList<>();

        Drawable drawable = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_search).
                colorRes(R.color.map_marker_icon).sizeDp(40);
        //searchView.setIcon(drawable);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                for (Event e : events) {
                    if (eventToText(e).contains(s)) {
                        keepEvents.add(e);
                    }
                }
                for (Person p : people) {
                    if (personToText(p).contains(s)) {
                        keepPeople.add(p);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                for (Event e : events) {
                    if (eventToText(e).contains(s)) {
                        keepEvents.add(e);
                    }
                }
                for (Person p : people) {
                    if (personToText(p).contains(s)) {
                        keepPeople.add(p);
                    }
                }
                return true;
            }
        });

        SearchAdapter adapter = new SearchAdapter(keepEvents, keepPeople);
        recyclerView.setAdapter(adapter);
    }

    private String eventToText(Event event) {
        DataCache instance = DataCache.getInstance();
        String eventString;
        Person currPerson = instance.getPersonById(event.getPersonId());
        eventString = event.getEventType().toUpperCase() + ": " + event.getCity() + ", " + event.getCountry() + " (" +
                event.getYear() + ")\n" + currPerson.getFirstName() + " " + currPerson.getLastName();
        return eventString;
    }

    private String personToText(Person person) {
        String personString;
        personString = person.getFirstName() + " " + person.getLastName();
        return personString;
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private final List<Event> eventList;
        private final List<Person> peopleList;

        SearchAdapter(List<Event> eventList, List<Person> peopleList) {
            this.eventList = eventList;
            this.peopleList = peopleList;
        }

        @Override
        public int getItemViewType(int position) {
            return position < eventList.size() ? EVENT_ITEM_VIEW_TYPE : FAMILY_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == EVENT_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.event_list_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.family_list_item, parent, false);
            }

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if(position < eventList.size()) {
                holder.bind(eventList.get(position));
            } else {
                holder.bind(peopleList.get(position - eventList.size()));
            }
        }

        @Override
        public int getItemCount() {
            return eventList.size() + peopleList.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;

        private final int viewType;
        private Event event;
        private Person person;

        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == EVENT_ITEM_VIEW_TYPE) {
                name = itemView.findViewById(R.id.eventTitle);
            } else {
                name = itemView.findViewById(R.id.familyTitle);
            }
        }

        private void bind(Event event) {
            this.event = event;
            name.setText(eventToText(event));
            Drawable eventIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker)
                    .colorRes(R.color.map_marker_icon).sizeDp(40);
            name.setCompoundDrawables(eventIcon, null, null, null);


        }

        private void bind(Person person) {
            this.person = person;
            name.setText(personToText(person));
        }

        private String eventToText(Event event) {
            DataCache instance = DataCache.getInstance();
            String eventString;
            Person currPerson = instance.getPersonById(event.getPersonId());
            eventString = event.getEventType().toUpperCase() + ": " + event.getCity() + ", " + event.getCountry() + " (" +
                    event.getYear() + ")\n" + currPerson.getFirstName() + " " + currPerson.getLastName();
            return eventString;
        }

        private String personToText(Person person) {
            String personString;
            personString = person.getFirstName() + " " + person.getLastName();
            return personString;
        }

        @Override
        public void onClick(View view) {
            if(viewType == EVENT_ITEM_VIEW_TYPE) {
                DataCache instance = DataCache.getInstance();
                instance.setCurrEvent(event);
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                startActivity(intent);
            } else {
                DataCache instance = DataCache.getInstance();
                instance.setCurrPerson(person);
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                startActivity(intent);
            }
        }
    }
}
