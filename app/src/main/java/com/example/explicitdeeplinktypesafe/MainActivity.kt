package com.example.explicitdeeplinktypesafe

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.explicitdeeplinktypesafe.databinding.ActivityMainBinding
import com.example.explicitdeeplinktypesafe.ui.dashboard.DashboardFragment
import com.example.explicitdeeplinktypesafe.ui.home.HomeFragment
import com.example.explicitdeeplinktypesafe.ui.notifications.NotificationsFragment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navController: NavController
        get() {
            //needed to be done like this due to: https://issuetracker.google.com/issues/142847973?pli=1
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
            return navHostFragment.navController
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController.graph = navController.createGraph(
            startDestination = HomeDestination::class,
            route = HomeGraph::class
        ) {
            HomeDestination.into<HomeFragment>(this)
            DashboardDestination.into<DashboardFragment>(this)
            NotificationsDestination.into<NotificationsFragment>(this)
        }

    }
}

@Serializable
@SerialName("home")
object HomeGraph

@Serializable
@SerialName(HomeDestination.baseRoute)
object HomeDestination {
    inline fun <reified F : Fragment> into(builder: NavGraphBuilder) {
        builder.fragment<F, HomeDestination> {
            label = "Home"
        }
    }

    const val baseRoute = "home/main"
}

@Serializable
@SerialName(DashboardDestination.baseRoute)
object DashboardDestination {
    inline fun <reified F : Fragment> into(builder: NavGraphBuilder) {
        builder.fragment<F, DashboardDestination> {
            label = "Dashboard"
        }
    }

    const val baseRoute = "dashboard"
}

@Serializable
@SerialName(NotificationsDestination.baseRoute)
object NotificationsDestination {
    inline fun <reified F : Fragment> into(builder: NavGraphBuilder) {
        builder.fragment<F, NotificationsDestination> {
            label = "Notifications"
        }
    }

    const val baseRoute = "notifications"
}