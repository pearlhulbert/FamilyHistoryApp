package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

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

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        DataCache instance = DataCache.getInstance();
        Event[] eventArray = new Event[instance.getEvents().size()];
        Person[] personArray = new Person[instance.getPeople().size()];
        Event[] events = instance.getEvents().values().toArray(eventArray);
        Person[] people = instance.getPeople().values().toArray(personArray);

        SearchAdapter adapter = new SearchAdapter(events, people);
        recyclerView.setAdapter(adapter);
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private final Event[] eventList;
        private final Person[] peopleList;

        SearchAdapter(Event[] eventList, Person[] peopleList) {
            this.eventList = eventList;
            this.peopleList = peopleList;
        }

        @Override
        public int getItemViewType(int position) {
            return position < eventList.length ? EVENT_ITEM_VIEW_TYPE : FAMILY_ITEM_VIEW_TYPE;
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
            if(position < eventList.length) {
                holder.bind(eventList[position]);
            } else {
                holder.bind(peopleList[position - eventList.length]);
            }
        }

        @Override
        public int getItemCount() {
            return eventList.length + peopleList.length;
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
            Drawable eventIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker)
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
                // This is were we could pass the skiResort to a ski resort detail activity

                Toast.makeText(SearchActivity.this, String.format("Enjoy skiing %s!",
                       event.getEventType()), Toast.LENGTH_SHORT).show();
            } else {
                // This is were we could pass the hikingTrail to a hiking trail detail activity

                Toast.makeText(SearchActivity.this, String.format("Enjoy hiking %s",
                        person.getFirstName()), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
