# Reddit News Reader Example

![Reddit Reader CI](https://github.com/inRivalz/redditreader/workflows/Reddit%20Reader%20CI/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=inRivalz_redditreader&metric=alert_status)](https://sonarcloud.io/dashboard?id=inRivalz_redditreader)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=inRivalz_redditreader&metric=bugs)](https://sonarcloud.io/dashboard?id=inRivalz_redditreader)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=inRivalz_redditreader&metric=coverage)](https://sonarcloud.io/dashboard?id=inRivalz_redditreader)

Sample application to read posts from Reddit. Used as an example on how to build applications using architecture components and other common libraries for Android.

This was created as a challenge and to experiment with the new components and libraries offered by google. It was also an opportunity to learn how Github Actions work and how to fix the coverage reports with SonarCloud.

The application uses a `Master-Detail` architecture with `MVVM` and repositories using the paging libraries from google. Instead of `Dagger` I wanted to experiment how `Koin` works and even if it's not "as magical" as Dagger2 it works really well, specially with `lifeCycleScopes` and `SavedState`.

#### TODO
There are some improvements I want to add to this, like a loader item for the recyclerview (that shouldn't be able to swipe to dismiss), more unit tests, specially when calling the paging libraries and UI test. For the last one I need to find a way to run emulators inside github actions, I'm investigating what are the best options.

Other improvements are splitting the business module in a "data" one and add abstractions for Room to be able to make it a plain kotlin module.

### Video
![add-image-support-and-improve-design](https://user-images.githubusercontent.com/4906698/81562365-1917df00-936b-11ea-8891-3d872b7ab37d.gif)


### Libraries
* [Android Support Library][support-lib]
* [Android Architecture Components][arch]
* [Retrofit][retrofit] for REST api communication
* [RxJava][rxjava] for use in observable patterns
* [Koin][koin] for dependency injections
* [Glide][glide] to load images
* [Room][room] As a cache and storage layer for the posts.
* [PrettyTime][prettytime] To show dates in a friendly way
* [mockito][mockito] for mocking in tests
* [AssertJ][assertj] for test assertions

[support-lib]: https://developer.android.com/topic/libraries/support-library/index.html
[arch]: https://developer.android.com/arch
[retrofit]: http://square.github.io/retrofit
[mockito]: http://site.mockito.org
[rxjava]: https://github.com/ReactiveX/RxJava
[assertj]: https://assertj.github.io/doc/
[koin]: https://doc.insert-koin.io/
[room]: https://developer.android.com/topic/libraries/architecture/room
[glide]: https://github.com/bumptech/glide
[prettytime]: https://www.ocpsoft.org/prettytime/
