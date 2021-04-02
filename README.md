Info for README.md

### About the project
Political Square (aka compass, spectrum) is a quiz about finding of your political view.  
Is a system of coordinates on which various political movements can be distributed depending on the attitude to personal and economic freedom of a person.
The quiz shows where you are in this system.

### Sources of information
The quiz for Ukraine was provided by VoxUkraine.  
The quiz for the World was made by the developer of this application.  
Descriptions for ideologies were taken from wikipedia.org and other open sources.  

### Website
http://eltonio.tilda.ws/political-compass

### Download
[Google Play](https://play.google.com/store/apps/details?id=eltonio.projects.politicalsquare)

### Instruction to Install:
1. Clone the project.
2. Setup DB.
> The original quiz for Ukraine is a property of VoxUkraine.  
> So to build the project use `PoliticalSquare-test.db` instead.  

In `.../util/ConstUtil.kt`, in the constant DB_NAME change DB name: from `PoliticalSquare.db` to `PoliticalSquare-test.db`.

3. Setup a Firebase `google-service.json` file. </br>  
> You should config this file only to run your project on Android Studio (not for using Firebase features)  

See [Setup Firebase to build the Political Square](https://github.com/ant-fedorich/political-square/wiki/Setup-Firebase-to-build-the-Political-Square/_edit)

4. Build it!

### Addition Info about the Project
#### Features that used in this project:
Jetpack: 
* ViewModel
* View Binding 
* LiveData
* DataStore - instead of SharedPreferences

DB:
* Room

Kotlin:
* Coroutine, Async
* Flow

ID
* Dagger-Hilt

Testing:
* JUnit
* Mokito, Mockk - for mocking
* Robolectric
* Truth - assertion library from Google

Firebase:
* Database
* Firestore
* Auth
* Analytics
* Crashlytics
* Test Lab

Other:
* Glide
* MotionLayout - for animation

#### Strengths of the current project
* MVVM architecture
* Used almost all main Jetpack components
* Hilt DI
* Kotlin

#### Weakness of the current project
* Instead of Navigation and fragments, used only activities
* No dark mode
* No landscape mode