package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.debugToaster.Toaster
import com.example.myapplication.ui.fragments.InfoOverviewFragment
import java.util.*


class MainActivity : AppCompatActivity(), Toaster.CopyToClipboardGenericInfoBuilder {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)

        Toaster.show(this, this).success("bla bla bla", "My extra inffoooooooo")
        Toaster.show()?.error("bl1 bl1 bl1 bl1 bl1 bl1", "lol, this extra info....")
        Toaster.show()?.warning("Warning test. My example warininningg", "App extra info...")
        Toaster.show(duration = Toast.LENGTH_SHORT)?.debug("Service successful")

        Handler(Looper.getMainLooper()).postDelayed({
            generateExampleSet()
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

    private fun generateExampleSet() {
        Toaster.show()?.error("bl bl1 bl1 bl1 bl1 bl1", "lol, this extra info....")
        Toaster.show()?.warning("Warning test. My example warininningg", "App extra info...")
        Toaster.show()?.warning("Warning test. My example warininningg", "App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...App extra info...")
        Toaster.show(duration = Toast.LENGTH_SHORT)?.debug("Service successful")

        Handler(Looper.getMainLooper()).postDelayed({
            generateExampleSet()
        }, 2000)
    }
}