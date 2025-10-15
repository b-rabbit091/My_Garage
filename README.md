# My Garage

**Default username:** user@email.com  
**Default password:** 1234

## Risky Components:

Database Management and Performance:

* As users add multiple vehicles and expense records, managing and retrieving data efficiently becomes important. If not handled properly, large data sets could slow down the app or cause crashes. To avoid this, we will design a well-structured local database and optimize our queries for smooth performance.

Data Storage and Security:

* The app handles personal and sensitive information such as user credentials (email and password) and vehicle details. Storing this data locally without encryption poses a privacy risk. Unauthorized access or data leaks could occur if the device is compromised. To minimize this risk, the project should implement secure data storage using encrypted SharedPreferences or a local Room database with encryption, and avoid storing plain-text passwords.

Reminder and Notification Reliability:

* The reminder feature depends on Android’s background services for timely notifications. On newer Android versions, background task restrictions or battery optimization settings may prevent notifications from triggering as expected. This could lead to missed reminders for servicing or maintenance. To reduce this risk, the app can use WorkManager or AlarmManager for scheduling tasks and prompt users to allow the necessary permissions for background processes.

•	Uses an outside API

Currently, our project does not use any external API. All the features and data management are handled locally within the application itself. However, in future updates, we plan to enhance the app by integrating useful APIs to provide additional functionality.
For example, we could use a Fuel Price API to automatically update the latest fuel prices based on the user’s location. This would make expense tracking more accurate and dynamic. Another possible enhancement is integrating the Google Maps API to help users locate nearby fuel stations, service centers, or car washes directly through the app.

•	Requires functionality we will not talk about : 

In the current version of our project, we will not be using any advanced or external functionalities that go beyond the topics covered in class. The app will work completely offline, and all the data will be stored locally on the user’s device.
We will not be using any external APIs or advanced features such as live fuel price updates, real-time map integration, or cloud synchronization. The app also will not connect to any external servers or databases, ensuring that all user information remains private and device-specific

•	Requires functionality we will talk about later (maps, media, data base services, etc.):

Our project includes some features that depend on concepts and functionalities which will be discussed in later parts of the course. For example, the app will eventually need to use a local database service (such as SQLite or Room) to store and manage user data, vehicle details, and expense records efficiently.
Additionally, in the future, we plan to enhance the app by incorporating Google Maps functionality to help users find nearby fuel stations, service centers, or other vehicle-related locations. There is also potential to include media-related features, such as allowing users to upload photos of receipts or vehicle documents


