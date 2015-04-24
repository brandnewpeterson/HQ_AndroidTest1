# HQ_AndroidTest1

NOTES

Pre-caching Mechanism
I explored several options for pre-caching including standard webcache and AppCache (HTML5 caching). I ended up simply downloading the "cache" flagged URLs using and the Android standard HTTP client along with relevant formatting files (CSS files and fonts).

The downloads occur on separate threads and you'll see them asyncronously appear in the standard internal app directory (accessed with the 'getFilesDir()' method).

This mechanisms seemed the fastest and most reliable, and didn't require any 3rd party libraries. It would also be back-compatible with very old Android verions.

Maintanability
I used fragments to keep things modular. The DownloadHelper class became a bit of a catchall, but helped keep things less cluttered in the main classes.

Life Cycle Flow
The 'android:configChanges="orientation|screenSize"' line prevents re-downloadings of the webviews on orientation changes. There are less hackish ways to do this, but this suffices for this single activity, simple app.

Bonus Questions
I've elected to make the provided JSON object containing all URLs an asset. But I could also try to download it on the fly each time the app starts -- this was my original design -- and if the file is not accessible fall back to a cahed version. That way, any changes to URLs would be picked up by the app.

I've left it as a hardcoded assent for now as the app starts faster.

As for starting an Android app from an HTML link, this would be possible via an Intent:

http://stackoverflow.com/questions/3469908/make-a-link-in-the-android-browser-start-up-my-app

APK




