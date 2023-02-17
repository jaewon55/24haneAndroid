package com.hane24.hoursarenotenough24

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.hane24.hoursarenotenough24.databinding.ActivityMainBinding
import com.hane24.hoursarenotenough24.error.NetworkErrorDialog
import com.hane24.hoursarenotenough24.error.NetworkObserverImpl
import com.hane24.hoursarenotenough24.inoutlog.LogListFragment
import com.hane24.hoursarenotenough24.inoutlog.LogListViewModel
import com.hane24.hoursarenotenough24.login.LoginActivity
import com.hane24.hoursarenotenough24.login.State
import com.hane24.hoursarenotenough24.overview.OverViewFragment
import com.hane24.hoursarenotenough24.overview.OverViewViewModel
import com.hane24.hoursarenotenough24.utils.SharedPreferenceUtils
import com.hane24.hoursarenotenough24.widget.BasicWidget

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater).apply { lifecycleOwner = this@MainActivity }
    }
    private val pager by lazy { binding.contentMain.viewpager }
    private val overViewViewModel: OverViewViewModel by viewModels()
    private val logListViewModel: LogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        refreshWidget()
        setStatusAndNavigationBar()
        setToolbar()
        setViewPager()
        setRefreshButtonListener()
        setFragmentsViewModel()
    }

    override fun onStop() {
        super.onStop()
        this.sendBroadcast(Intent(this, BasicWidget::class.java).apply {
            this.action = "ANIM_OFF"
        })
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerVisible(binding.navView)) {
            binding.drawerLayout.closeDrawers()
        } else if (pager.currentItem != 0) {
            pager.currentItem--
        } else {
            super.onBackPressed()
        }
    }

    private fun refreshWidget() = this.sendBroadcast(Intent(this, BasicWidget::class.java).apply {
        this.action = "REFRESH"
    })

    private fun setStatusAndNavigationBar() {
        val controller = WindowInsetsControllerCompat(window, window.decorView)

        controller.isAppearanceLightStatusBars = true
        controller.isAppearanceLightNavigationBars = true
    }

    private fun setToolbar() {
        setSupportActionBar(binding.appBar.toolbar)
        setDrawerLayout()
        setNavigationItemListener()
    }

    private fun setRefreshButtonListener() {
        binding.appBar.refreshButton.setOnClickListener {
            handleNetworkState(NetworkObserverImpl().isConnected())
        }
    }

    private fun handleNetworkState(networkStatus: Boolean) {
        val onClickDialog = DialogInterface.OnClickListener { _, _ ->
            binding.appBar.refreshButton.callOnClick()
        }
        when (networkStatus) {
            false -> {
                NetworkErrorDialog.showNetworkErrorDialog(supportFragmentManager, onClickDialog)
            }

            true -> {
                overViewViewModel.refreshButtonOnClick()
                logListViewModel.refreshButtonOnClick()
                refreshWidget()
            }
        }
    }

    private fun setFragmentsViewModel() {
        binding.overViewViewModel = overViewViewModel
        binding.appBar.logListViewModel = logListViewModel
        overViewViewModel.intraId.observe(this) {
            binding.navView.getHeaderView(0)
                .findViewById<TextView>(R.id.nav_header_text).text = it
        }
    }

    private fun setDrawerLayout() {
        val drawerLayout = binding.drawerLayout

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        binding.appBar.menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED)
            }

            override fun onDrawerClosed(drawerView: View) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })
    }

    private fun setNavigationItemListener() {
        binding.navView.setNavigationItemSelectedListener { item ->
            val browserIntent: Intent? = when (item.itemId) {
                R.id.nav_item_use_guide -> Intent(Intent.ACTION_VIEW, Uri.parse(APP_GUIDE))
                R.id.nav_item_subsidy_guide -> Intent(Intent.ACTION_VIEW, Uri.parse(PAGE_GUIDE))
                R.id.nav_item_inquire_mobile ->
                    Intent(Intent.ACTION_VIEW, Uri.parse(INQUIRE_MOBILE))
                R.id.nav_item_inquire_attendance ->
                    Intent(Intent.ACTION_VIEW, Uri.parse(INQUIRE_ATTENDANCE))
                else -> null
            }
            browserIntent?.let {
                startActivity(browserIntent)
            }
            true
        }
        binding.navFooterView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_item_logout -> logOutOnClick()
                R.id.nav_item_license -> licenseOnClick()
            }
            true
        }
    }

    private fun setViewPager() {
        val adapter = PagerAdapter(this)
        pager.adapter = adapter
        TabLayoutMediator(binding.contentMain.pagerTabLayout, pager)
        { _, _ -> }.attach()
        createAllFragment()
    }

    private fun createAllFragment() {
        pager.currentItem = NUM_PAGES - 1
        while (pager.currentItem != 0) pager.currentItem--
    }


    private fun logOutOnClick() {
        SharedPreferenceUtils.saveAccessToken("")

        val intent = Intent(this, LoginActivity::class.java)
            .putExtra("loginState", State.LOGIN_FAIL)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent).also { finish() }
    }

    private fun licenseOnClick() {
        val dialog = LicenseDialogFragment()
        supportFragmentManager.let {
            dialog.show(it, "OpenSourceLicenses")
        }
    }

    companion object {
        private const val APP_GUIDE = BuildConfig.APP_GUIDE
        private const val PAGE_GUIDE = BuildConfig.PAGE_GUIDE
        private const val INQUIRE_ATTENDANCE = BuildConfig.INQUIRY_ATTENDANCE
        private const val INQUIRE_MOBILE = BuildConfig.INQUIRY_MOBILE
        private const val NUM_PAGES = 2
    }

    private inner class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OverViewFragment()
                else -> LogListFragment()
            }
        }
    }

}