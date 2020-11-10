package com.amrabed.tripplan

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amrabed.tripplan.auth.Authenticator.signOut
import com.amrabed.tripplan.data.models.TripViewModel
import com.amrabed.tripplan.data.models.UserViewModel
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {
    private val userViewModel by viewModels<UserViewModel>()
    private val tripViewModel by viewModels<TripViewModel>()
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.content) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.currentUser.observe(this) {
            setContentView(R.layout.main_activity)
            setSupportActionBar(toolbar)
            setupActionBarWithNavController(navController, AppBarConfiguration(navController.graph))
            if (userViewModel.isManager()) {
                navigation.setupWithNavController(navController)
                navigation.visibility = View.VISIBLE
            }
        }
        handleSearch(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.signOut) {
            signOut()
            startActivity(Intent(this, LauncherActivity::class.java))
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleSearch(intent)
    }

    private fun handleSearch(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            tripViewModel.search(intent.getStringExtra(SearchManager.QUERY))
        }
    }

    override fun onSupportNavigateUp() = navController.navigateUp() || super.onSupportNavigateUp()
}
