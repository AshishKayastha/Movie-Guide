# Movie Guide

Movie Guide is an open source Android application for [TMDb](https://www.themoviedb.org). It showcases detailed information about movies, tv shows and actors from TMDb.

[![platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](https://github.com/AshishKayastha/Movie-Guide#license)
[![API](https://img.shields.io/badge/API-21%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![PRs Welcome](https://img.shields.io/badge/prs-welcome-brightgreen.svg)](http://makeapullrequest.com)

## Features
* Material design with delightful animations.
* Minimal and simple user interface, which user can get easily acquainted with.
* View movies, tv shows and actors information from TMDb.
* Search and filter your favorite movies, tv shows and people.
* View movie and tv show ratings from OMDb.

## Movie Guide Implementations
* Min SDK 21 with material design and animations
* Fully written in [Kotlin](https://kotlinlang.org/) language
* Follows MVP pattern for separation of concerns
* [Retrofit 2](http://square.github.io/retrofit/) for performing network requests
* [RxJava 2](https://github.com/ReactiveX/RxJava) and [RxAndroid 2](https://github.com/ReactiveX/RxAndroid) for asynchronous functionality
* [Dagger 2](http://google.github.io/dagger/) for dependency injection with subcomponent multibindings
* [KotterKnife](https://github.com/JakeWharton/kotterknife) for view bindings.
* [Timber](https://github.com/JakeWharton/timber) for logging
* [Glide](https://github.com/bumptech/glide/) for image loading
* [Paper Parcel](https://github.com/grandstaish/paperparcel) for creating Parcelable objects
* [Ice Pick](https://github.com/frankiesardo/icepick) for saving instance of objects
* [Leak Canary](https://github.com/square/leakcanary) for detecting memory leaks
* [StorIO](https://github.com/pushtorefresh/storio) for offline functionality(WIP)
* Custom Tabs support library for TMDb login functionality 
* Palette support library for extracting colors from image
* Other support libraries - AppCompat, RecyclerView, CardView, Design

## Screenshots

<img src="art/movie_list.png" width="25%" />
<img src="art/movie_detail.png" width="25%" />
<img src="art/movie_detail_2.png" width="25%" />
<img src="art/images_credits.png" width="25%" />
<img src="art/tv_shows.png" width="25%" />
<img src="art/tv_show_detail.png" width="25%" />

## Configuration

In order to run this application, you need to get your own key from TMDb. You can do that by clicking [here](https://www.themoviedb.org/account/signup).</br>

After you get an API key, put that key in `gradle.properties` file as follows:
```
TMDB_API_KEY="your_api_key"
```
</br>
Now you can run the app by clicking *Run -> Run 'app'*.

## Contributing
You can contribute to the project by either finding out bugs or by requesting new features. Besides that, if you are a developer then you can contribute by submitting pull requests. There is TODO list to get you started with.

Any feedback or contributions are welcome!

## TODO
- [ ] Write unit and integration tests
- [ ] Implement offline functionality using StorIO(WIP)
- [ ] UI Improvements

## License

```
Copyright 2017 Ashish Kayastha

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
