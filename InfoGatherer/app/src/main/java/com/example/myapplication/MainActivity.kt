package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.debugToaster.Toaster
import com.example.myapplication.ui.InfoListTabAdapter
import com.example.myapplication.ui.fragments.InfoOverviewFragment
import java.util.*


class MainActivity : AppCompatActivity(), Toaster.CopyToClipboardGenericInfoBuilder {

    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)

        Toaster.show(this, this).success("bla bla bla", "My extra inffoooooooo")
        Toaster.show()?.error("bl1 bl1 bl1 bl1 bl1 bl1", "lol, this extra info....")
        Toaster.show()?.warning("Warning test. My example warininningg", "App extra info...")
        Toaster.show(duration = Toast.LENGTH_SHORT)?.debug("Service successful")

        Handler(Looper.getMainLooper()).postDelayed({
            generateSet()
        }, 2000)

        addFragment(InfoOverviewFragment())
    }

    private fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun buildGenericInfo(): String {
        return "${Calendar.getInstance().time}"
    }

    // -------------------------------------------------------------

    private fun generateSet() {
        Toaster.show()?.error("bl${counter++} bl1 bl1 bl1 bl1 bl1", "lol, this extra info....")
        Toaster.show()?.warning("Warning${counter++} test. My example warininningg", "App extra info...")
        Toaster.show()?.warning("Warning test.${counter++} My example warininningg", "App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...")
        Toaster.show(duration = Toast.LENGTH_SHORT)?.debug("Service successful")

        Handler(Looper.getMainLooper()).postDelayed({
            generateSet()
        }, 2000)
    }
}