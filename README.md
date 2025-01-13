# Random Bark - Android App

The app fetches random dog pictures from the Dog CEO API https://dog.ceo/dog-api/documentation/. 
It lets users view different dog breeds and enjoy cute dog images with each refresh.

### Note: 
- We have two different tabs, "Coroutines" and "RxJava".
- By default, the app uses the Coroutine approach to fetch the dog image.
- However, if you select the RxJava tab, the app uses the RxJava approach to fetch the data.

### Tech Stack
- **Kotlin** - primary language for the app
- **Hilt** - for dependency injection 
- **MVI** - for architecture pattern 
- **Retrofit** - for making API requests and handling responses
- **Jetpack Compose** - for building the user interface
- **Android Recommended Architecture** - The app follows the official https://developer.android.com/topic/architecture .
- **Concurrency** - this app uses both **Kotlin Coroutines** and **RxJava** for handling asynchronous operations.

### Demo Video: 
[demo-video.webm](https://github.com/user-attachments/assets/d041bb25-c6f6-448d-abe0-b56deede05a9)
