#Family History App
This family history app is a local app developed in android studio. This was a solo project for my Advanced Programming Concepts class (CS 240). Details on both the back and front end can be found below.

Back End (found in src):
The Server class is really just an HttpServer object along with some contexts for my api calls. The bulk of this project was writing apis to query a SQL Lite database, which I created the tables for. These tables are filled with when the user registers, which makes use of the /fill api. The DAO classes are used to modify the database. There are tables for people, events, users, and authorization tokens. I wrote classes for all of these things, as well as request and response objects pertaining to them and put them in a shared module (Shared) so that the back end and front end could both use these classes. I created apis for clearing the database, displaying all the people/events, displaying a specific person/event based on its id (a randomly generated string), registering a new user, logging in an existing user, and loading the dummy data into the database. These all have specific request and response objects passed in and returned. Each also has a handler, which is basically just an http handler and a service, which uses the daos to perform the necessary database operations. 

Front End (found in app):
The front end allows the user to register/log in, and then displays a google map fragment with several markers. This is the main activity. There is also a bar at the top with a search bar, where you can enter a case insensitive string and it will show all people with first or last names containing the string and all events with types, cities, countries, and years containg the string. Each marker on the map pertains to the location of an event in the user's family tree. These mostly include births, deaths, and marriages. If you click on a marker, it will display the person's first and last names, an icon indicating the person's gender, and the event type, location, and year. This is the event activity. If you click on this item, the app will launch a person activity, which displays a screen listing the person's first and last names, gender, and collapsible lists of their life events and immediate family members (spouses and children). There is a back button shown in both the person and event activities that will take you back to the main activity. The layout for all of these is done in xml. 
