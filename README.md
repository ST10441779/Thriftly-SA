Thriftly SA

Find it. List it. Reuse it.

Thriftly SA is an Android marketplace application developed for the OPSC6312 Portfolio of Evidence. It allows users to register, log in, browse second-hand items, search and filter listings, favourite items, and create their own listings.

Student Information

* Student: Rethabile Minnaar
* Student Number: ST10441779
* Module: OPSC6312
* Application: Thriftly SA

Technologies

* Android Studio
* Kotlin
* Jetpack Compose
* Retrofit
* PHP REST API
* MySQL
* XAMPP
* GitHub
* GitHub Actions
* JUnit

Main Features

* User registration and login
* Password hashing
* Browse marketplace listings
* Search and filtering
* Favourite listings
* Create listings
* Application settings
* Input validation
* REST API integration

Architecture

Android App
    ↓
Retrofit
    ↓
PHP REST API
    ↓
MySQL Database

REST API

The application communicates with the following endpoints:

POST /register.php
POST /login.php
GET  /listings.php
POST /create_listing.php
POST /favourite.php

Database

The MySQL database is called:

thriftly_sa

Main tables:

users
listings
favourites

Passwords are securely hashed using PHP’s password_hash() and verified using password_verify().

Testing

JUnit tests are included to test:

* Email validation
* Password validation
* Price validation
* Listing validation

GitHub Actions automatically runs the tests and builds the Android application.

GitHub

The project is managed using Git and GitHub with regular commits to track development progress.



