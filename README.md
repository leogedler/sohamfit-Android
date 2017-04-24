# Sohamfit-Android
Android native app for (http://sohamfit.com/) developed and built in Android Studio.

targetSdkVersion 25 (Android Nougat)
minSdkVersion 16 (Android Jelly Bean)

## Sohamfit project (http://sohamfit.com/)
Sohamfit is an online platform to learn and practice yoga (online spa).

This project was built by myself using MongoDB/Express/AngularJs/Node (MEAN-stack), leveraging in Parse Server technology. 

Sohamfit originally only had a Wep app, but because this test's requeriments fit perfectly in the main core features of sohamfit I decided to build an android app for sohamfit.

Other advantage to take sohamfit as a theme for this test is that I own the web server and this allow me to use sohamfit's API and other feature like authentication with free will. 

## App layout (Point 1, 2, 3 and 4)
The main activity of this app is a list view of all the videos in sohamfit.
This list is filteable (video type or video level), sortable (by video name or date) and searchable (searchable by  video name and/or video description).
There is a dedicated activity for the filtering and search of all videos.
If a list item is clicked a video player activity is opened where the video info is displayed and the video played.

## Web service (Point 5, 6, and 7 of the test with explanation)
### Point 5
API endpoint: http://sohamfit.com/api/
This is a real API endpoint, not a mockup.
This app consume the rest API using and http class service. This service can be modified to consume service from another rest API. (It is fully customizable).

### Point 6
Currently there are 59 video on sohamfit, but in the future more will be added. To handle this the app implements pagination in form of a infinity scroll. When the video list is created only 10 videos are loaded, when the user scroll to the bottom 10 more videos will be fetched from the server and added to the bottom of the list. This process is repeated until the last video is loaded.

### Point 7
All the data consumed by this android app comes from a Server deployed in AWS Web service and it also serve the web app (Currently this app is in production).
With this I mean that the data can be modified and consumed from differents threads and differents clients. To handle this in the app the main video list implements swipe to refresh, with only a swipe the user can refresh all the list keeping the selected filters.

## Project Structure.
There is a clear porject structure with:
* Activities. 
* Layouts.
* Service class (http requests).
* Drawables.
* Model (video model).
* Constants.
* Helpers classes (String helper, image transformation).
* Strings resources.
* Styles resources.
* Color resources.
* Dependencies (libraries).

## Extra features
### Email Authentication
This app has authentication and register service. 
In order to access an user has to either login or sign-up. Both feature are fully functional so any user can login or create a new account in sohamfit.
For the porpuse of this test I've created a test user if you don't want to creat a new user:
email: contacto@sohamfit.
password: sohamfit

### Facebook Authentication
This app has also Facebook authentication so is the user want, he can login or sign-up just with one click using Facebook android SDK.

### Video player
This app also has a video player activity to play the yoga videos.
In this video player activity the user can see the video description, instructor name, video duration and video level. If he rotate the device to landscape mode he can play the video in full screen.



