package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.debugToaster.Toaster
import java.util.*

class MainActivity : AppCompatActivity(), Toaster.CopyToClipboardGenericInfoBuilder {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toaster.show(this, this).success("bla bla bla", "My extra inffoooooooo")
        Toaster.show()?.error("bl1 bl1 bl1 bl1 bl1 bl1","lol, this extra info....")
        Toaster.show()?.warning("Warning test. My example warininningg", "App extra info...")
        Toaster.show(duration = Toast.LENGTH_SHORT)?.debug("Service successful")
        //Toaster.show()?.errorLong("bl2 bl2 bl2 bl2 bl2 bl2")
        //Toaster.show()?.warningLong("bl3 bl3 bl3 bl3 bl3 bl3")
        //Toaster.show()?.debugLong("bl4 bl4 bl4 bl4 bl4 bl4")
    }

    override fun buildGenericInfo(): String {
        return "${Calendar.getInstance().time}"
    }
}