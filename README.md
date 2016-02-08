![The Flone](https://github.com/Fewlaps/flone-android/blob/master/art/flone-makers-shadow.png)<br/>

[![Build Status](https://travis-ci.org/Fewlaps/flone-android.svg?branch=master)](https://travis-ci.org/Fewlaps/flone-android)

# flone-android
A CleanFlight drone remote controller, driven with performance in mind.

### Current features
- Autoreconnection: if connection drops, the app will try to connect again to the drone automatically
- Automagic heading: the drone will mimic your orientation automatically
- Connects with the drone via Bluetooth
- The communication is handled in a Service, so the drone wouldn't fall if someone calls you
- Choose if you are a beginner or an expert pilot, to cap the pitch/roll values
- Put the drone in BARO/SONAR mode when untapping the phone screen
- It works with all CleanFlight powered boards, not only the Flone

### Next releases features
- Connection via WiFi Direct
- Connection via Internet (using a phone to drive the drone, and another one on the drone to connect the drone to the Internet)
- Get the altitude of the drone above the ground
- Get the GPS location of the multiwii board
- Get the GPS location of the phone, when connecting over the Internet (for boards without GPS)
- Do you have an idea? [Create an issue!](https://github.com/aeracoop/FloneRemote/issues/new) 

## The project nowadays
<img src="http://fewlaps.com/xtra/flone/app1.png" width="130">
<img src="http://fewlaps.com/xtra/flone/app2.png" width="130">
<img src="http://fewlaps.com/xtra/flone/app3.png" width="130">
<img src="http://fewlaps.com/xtra/flone/app4.png" width="130">
<img src="http://fewlaps.com/xtra/flone/app5.png" width="130">

## We introduced Flone at Maker Faire Barcelona 2016!
[![Flone at Maker Faire Barcelona 2016](https://github.com/Fewlaps/flone-android/blob/master/art/flone-youtube.png)](http://www.youtube.com/watch?v=jgnI5yOga6I)


## References
Like you guess, we didn't this magic alone. We want to thank these great projects:
- [@zjusbo's Multiwii-Remote](https://github.com/zjusbo/Multiwii-Remote): to connect the phone with the multiwii board
- [@greenrobot's EventBus](https://github.com/greenrobot/EventBus): the great way to have a Bus in your loved Android
- [@google's GSON](https://github.com/google/gson): a standard and simple way to work with JSON
- [@square's Phrase](https://github.com/square/phrase): a fancy way to format strings
- [@nenick's AndroidStudioAndRobolectric](https://github.com/nenick/AndroidStudioAndRobolectric): a great and simple example to add Roboelectric feature to your existing Android project

## Download
Get it at [Google Play!](https://play.google.com/store/apps/details?id=com.fewlaps.flone) 

# Want to know more about Flone?
Don't hesitate to read the official website: http://flone.cc/<br/><br/>
![Flone at Maker Faire Barcelona 2016](https://github.com/Fewlaps/flone-android/blob/master/art/flone-makers-stand.JPG)
