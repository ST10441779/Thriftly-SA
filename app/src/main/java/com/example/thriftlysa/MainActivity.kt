package com.example.thriftlysa

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContent {

            MaterialTheme {

                ThriftlyApp(
                    context = this
                )
            }
        }
    }
}

@Composable
fun ThriftlyApp(
    context: Context
) {

    val preferences =
        context.getSharedPreferences(
            "thriftly_preferences",
            Context.MODE_PRIVATE
        )

    var loggedIn by remember {

        mutableStateOf(
            preferences.getBoolean(
                "logged_in",
                false
            )
        )
    }

    if (loggedIn) {

        MainScreen(
            context = context,
            onLogout = {

                preferences
                    .edit()
                    .clear()
                    .apply()

                loggedIn = false
            }
        )

    } else {

        LoginScreen(
            onLogin = {

                preferences
                    .edit()
                    .putBoolean(
                        "logged_in",
                        true
                    )
                    .putInt(
                        "user_id",
                        it
                    )
                    .apply()

                loggedIn = true
            }
        )
    }
}

@Composable
fun LoginScreen(
    onLogin: (Int) -> Unit
) {

    val repository =
        remember {
            ThriftlyRepository()
        }

    val scope =
        rememberCoroutineScope()

    var registerMode by remember {
        mutableStateOf(false)
    }

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text = "Thriftly SA",
            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )

        Text(
            text =
                "Find it. List it. Reuse it."
        )

        Spacer(
            Modifier.height(24.dp)
        )

        if (registerMode) {

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("Full name")
                },
                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                Modifier.height(8.dp)
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Email")
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Password")
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            Modifier.height(16.dp)
        )

        Button(
            onClick = {

                if (
                    !Validation.validEmail(email)
                ) {

                    message =
                        "Please enter a valid email."

                    return@Button
                }

                if (
                    !Validation.validPassword(
                        password
                    )
                ) {

                    message =
                        "Password must contain at least 6 characters."

                    return@Button
                }

                if (
                    registerMode &&
                    name.isBlank()
                ) {

                    message =
                        "Please enter your name."

                    return@Button
                }

                scope.launch {

                    if (registerMode) {

                        val result =
                            repository.register(
                                name,
                                email,
                                password
                            )

                        message =
                            result.message

                        if (result.success) {

                            registerMode = false
                            password = ""
                        }

                    } else {

                        val result =
                            repository.login(
                                email,
                                password
                            )

                        message =
                            result.message

                        if (
                            result.success &&
                            result.userId != null
                        ) {

                            Log.d(
                                "ThriftlyLogin",
                                "Login successful"
                            )

                            onLogin(
                                result.userId
                            )
                        }
                    }
                }

            },
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                if (registerMode)
                    "Create Account"
                else
                    "Sign In"
            )
        }

        Spacer(
            Modifier.height(8.dp)
        )

        OutlinedButton(
            onClick = {

                registerMode =
                    !registerMode

                message = ""
            },
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                if (registerMode)
                    "Already have an account?"
                else
                    "Create a new account"
            )
        }

        Spacer(
            Modifier.height(12.dp)
        )

        Text(message)
    }
}

@OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class
)
@Composable
fun MainScreen(
    context: Context,
    onLogout: () -> Unit
) {

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text("Thriftly SA")
                }
            )
        },

        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected =
                        selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                    },
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription =
                                "Home"
                        )
                    },
                    label = {
                        Text("Home")
                    }
                )

                NavigationBarItem(
                    selected =
                        selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                    },
                    icon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription =
                                "Search"
                        )
                    },
                    label = {
                        Text("Search")
                    }
                )

                NavigationBarItem(
                    selected =
                        selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                    },
                    icon = {
                        Icon(
                            Icons.Default.Add,
                            contentDescription =
                                "Sell"
                        )
                    },
                    label = {
                        Text("Sell")
                    }
                )

                NavigationBarItem(
                    selected =
                        selectedTab == 3,
                    onClick = {
                        selectedTab = 3
                    },
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription =
                                "Profile"
                        )
                    },
                    label = {
                        Text("Profile")
                    }
                )
            }
        }

    ) { padding ->

        when (selectedTab) {

            0 ->
                HomeScreen(
                    Modifier.padding(padding),
                    context
                )

            1 ->
                SearchScreen(
                    Modifier.padding(padding),
                    context
                )

            2 ->
                SellScreen(
                    Modifier.padding(padding),
                    context
                )

            3 ->
                ProfileScreen(
                    Modifier.padding(padding),
                    context,
                    onLogout
                )
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier,
    context: Context
) {

    val repository =
        remember {
            ThriftlyRepository()
        }

    var listings by remember {
        mutableStateOf(
            emptyList<Listing>()
        )
    }

    var message by remember {
        mutableStateOf(
            "Loading listings..."
        )
    }

    LaunchedEffect(Unit) {

        val result =
            repository.getListings()

        listings =
            result.listings
                ?: emptyList()

        message =
            if (result.success) {

                "${listings.size} listings available"

            } else {

                result.message
                    ?: "Unable to load listings."
            }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp)
    ) {

        Text(
            text =
                "Welcome to Thriftly SA",
            style =
                MaterialTheme
                    .typography
                    .headlineSmall
        )

        Spacer(
            Modifier.height(8.dp)
        )

        Text(
            "Discover affordable pre-owned goods."
        )

        Spacer(
            Modifier.height(16.dp)
        )

        Text(message)

        Spacer(
            Modifier.height(12.dp)
        )

        LazyColumn {

            items(listings) { listing ->

                ListingCard(
                    listing,
                    context
                )
            }
        }
    }
}

@Composable
fun ListingCard(
    listing: Listing,
    context: Context
) {

    val repository =
        remember {
            ThriftlyRepository()
        }

    val scope =
        rememberCoroutineScope()

    val preferences =
        context.getSharedPreferences(
            "thriftly_preferences",
            Context.MODE_PRIVATE
        )

    val userId =
        preferences.getInt(
            "user_id",
            0
        )

    var favourite by remember {
        mutableStateOf(false)
    }

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 6.dp
                )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        listing.title,
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium
                    )

                    Text(
                        "R%.2f"
                            .format(
                                listing.price
                            )
                    )

                    Text(
                        "${listing.category} • ${listing.item_condition}"
                    )

                    Text(
                        listing.location
                    )

                    if (
                        !listing.seller_name
                            .isNullOrBlank()
                    ) {

                        Text(
                            "Seller: ${listing.seller_name}"
                        )
                    }
                }

                IconButton(
                    onClick = {

                        scope.launch {

                            val result =
                                repository
                                    .toggleFavourite(
                                        userId,
                                        listing.id
                                    )

                            if (result.success) {

                                favourite =
                                    result.favourite
                            }
                        }
                    }
                ) {

                    Icon(
                        Icons.Default.Favorite,
                        contentDescription =
                            "Favourite"
                    )
                }
            }
        }
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier,
    context: Context
) {

    val repository =
        remember {
            ThriftlyRepository()
        }

    var listings by remember {
        mutableStateOf(
            emptyList<Listing>()
        )
    }

    var searchText by remember {
        mutableStateOf("")
    }

    var category by remember {
        mutableStateOf("All")
    }

    LaunchedEffect(Unit) {

        listings =
            repository
                .getListings()
                .listings
                ?: emptyList()
    }

    val filtered =
        listings.filter {

            val searchMatch =
                it.title.contains(
                    searchText,
                    ignoreCase = true
                ) ||
                        it.description.contains(
                            searchText,
                            ignoreCase = true
                        )

            val categoryMatch =
                category == "All" ||
                        it.category.equals(
                            category,
                            ignoreCase = true
                        )

            searchMatch &&
                    categoryMatch
        }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp)
    ) {

        Text(
            "Search & Filter",
            style =
                MaterialTheme
                    .typography
                    .headlineSmall
        )

        Spacer(
            Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            label = {
                Text("Search")
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            Modifier.height(12.dp)
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {

            listOf(
                "All",
                "Clothing",
                "Shoes",
                "Accessories"
            ).forEach {

                OutlinedButton(
                    onClick = {
                        category = it
                    }
                ) {

                    Text(it)
                }
            }
        }

        Spacer(
            Modifier.height(12.dp)
        )

        LazyColumn {

            items(filtered) {

                ListingCard(
                    it,
                    context
                )
            }
        }
    }
}

@Composable
fun SellScreen(
    modifier: Modifier,
    context: Context
) {

    val repository =
        remember {
            ThriftlyRepository()
        }

    val scope =
        rememberCoroutineScope()

    val preferences =
        context.getSharedPreferences(
            "thriftly_preferences",
            Context.MODE_PRIVATE
        )

    val userId =
        preferences.getInt(
            "user_id",
            0
        )

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var category by remember {
        mutableStateOf("")
    }

    var condition by remember {
        mutableStateOf("")
    }

    var price by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp)
    ) {

        Text(
            "Create Listing",
            style =
                MaterialTheme
                    .typography
                    .headlineSmall
        )

        Spacer(
            Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = {
                Text("Title")
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            label = {
                Text("Description")
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = category,
            onValueChange = {
                category = it
            },
            label = {
                Text("Category")
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = condition,
            onValueChange = {
                condition = it
            },
            label = {
                Text("Condition")
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = {
                price = it
            },
            label = {
                Text("Price")
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = location,
            onValueChange = {
                location = it
            },
            label = {
                Text("Location")
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            Modifier.height(12.dp)
        )

        Button(
            onClick = {

                if (
                    !Validation.validListing(
                        title,
                        description,
                        category,
                        condition,
                        price,
                        location
                    )
                ) {

                    message =
                        "Please complete all fields correctly."

                    return@Button
                }

                scope.launch {

                    val result =
                        repository.createListing(

                            CreateListingRequest(
                                sellerId = userId,
                                title = title,
                                description = description,
                                category = category,
                                condition = condition,
                                price =
                                    price.toDouble(),
                                location = location
                            )
                        )

                    message =
                        result.message

                    if (result.success) {

                        title = ""
                        description = ""
                        category = ""
                        condition = ""
                        price = ""
                        location = ""
                    }
                }
            },
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("Publish Listing")
        }

        Spacer(
            Modifier.height(8.dp)
        )

        Text(message)
    }
}

@Composable
fun ProfileScreen(
    modifier: Modifier,
    context: Context,
    onLogout: () -> Unit
) {

    val preferences =
        context.getSharedPreferences(
            "thriftly_preferences",
            Context.MODE_PRIVATE
        )

    var language by remember {

        mutableStateOf(
            preferences.getString(
                "language",
                "English"
            ) ?: "English"
        )
    }

    var notifications by remember {

        mutableStateOf(
            preferences.getBoolean(
                "notifications",
                true
            )
        )
    }

    var darkMode by remember {

        mutableStateOf(
            preferences.getBoolean(
                "dark_mode",
                false
            )
        )
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp)
    ) {

        Text(
            "Profile & Settings",
            style =
                MaterialTheme
                    .typography
                    .headlineSmall
        )

        Spacer(
            Modifier.height(20.dp)
        )

        Text("Language")

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {

            listOf(
                "English",
                "isiZulu",
                "isiXhosa"
            ).forEach {

                OutlinedButton(
                    onClick = {

                        language = it

                        preferences
                            .edit()
                            .putString(
                                "language",
                                it
                            )
                            .apply()
                    }
                ) {

                    Text(it)
                }
            }
        }

        Spacer(
            Modifier.height(20.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text("Notifications")

            Switch(
                checked = notifications,
                onCheckedChange = {

                    notifications = it

                    preferences
                        .edit()
                        .putBoolean(
                            "notifications",
                            it
                        )
                        .apply()
                }
            )
        }

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text("Dark Mode Preference")

            Switch(
                checked = darkMode,
                onCheckedChange = {

                    darkMode = it

                    preferences
                        .edit()
                        .putBoolean(
                            "dark_mode",
                            it
                        )
                        .apply()
                }
            )
        }

        Spacer(
            Modifier.height(24.dp)
        )

        Button(
            onClick = onLogout,
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("Sign Out")
        }
    }
}