# HQ_AndroidTest1

NOTES

*Pre-caching Mechanism*

I explored several options for pre-caching including standard webcache and AppCache (HTML5 caching). I ended up simply downloading the "cache" flagged URLs along with the relevant formatting files (CSS files and fonts) using the Android standard HTTP client .

The downloads occur on separate threads and you'll see them asyncronously appear in the standard internal app directory (accessed with the 'getFilesDir()' method).

This mechanism seemed the fastest and most reliable, and didn't require any 3rd party libraries. It would also be back-compatible with old Android verions.

*Maintanability*

I used fragments to keep things modular -- just use the back button to return to the ListView from each WebView. The DownloadHelper class became a bit of a catchall, but helped keep things less cluttered in the main classes.

*Life Cycle Flow*

The 'android:configChanges="orientation|screenSize"' line prevents re-downloadings of the webviews on orientation changes. There are less hackish ways to do this, but this suffices for this single activity, simple app.

*Bonus Questions*

I've elected to make the provided JSON object containing all URLs an asset. But I could also try to download it on the fly each time the app starts -- this was my original design -- and, if the file is not accessible, then fall back to a cached version. That way, any changes to URLs would be picked up by the app.

I've left it as a hardcoded asset for now as the app starts faster.

As for starting an Android app from an HTML link, this would be possible via an Intent:

http://stackoverflow.com/questions/3469908/make-a-link-in-the-android-browser-start-up-my-app

*APK*

You'll find a signed, sideloadable APK in /app/app-release.apk.

*Man Hours*

This took a while because I had to do it at odd time while on the road and I made quite a lot of changes to my dev process while doing this test. I started using Android Studio for the first time rather than Eclipse -- I'm still getting used to it. I also tried to use some newer SDK constructs such as RecycleView, but found that too much boilerplate code was required for the simpler requirements of this test. 

Not counting all this configuration time, I'd day this took 6 hours or so. (I'm not the fastest coder, but I do try to be thorough. :) )
