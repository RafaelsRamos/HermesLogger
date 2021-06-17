package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.debugToaster.LongToastDuration
import com.example.myapplication.debugToaster.Toaster
import java.util.*


class MainActivity : AppCompatActivity(), Toaster.CopyToClipboardGenericInfoBuilder {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)

        Toaster.success()
            .withMessage("0")
            .withExtraInfo("The service was successfully called :+1:")
            .show()

        Toaster.other().withMessage("other1").show()
        Toaster.other().withMessage("other2").show()
        Toaster.other().withMessage("other3").show()
        Toaster.other().withMessage("other4").show()
        Toaster.other().withMessage("other5").show()

        Toaster
            .success()
            .withMessage("Buenos dias")
            .show(this, this)

        Handler(Looper.getMainLooper()).postDelayed({
            generateExampleSet()
        }, 10000)
    }

    override fun buildGenericInfo(): String {
        return "My Generic message!!!!"
    }

    // -------------------------------------------------------------

    private fun generateExampleSet() {

        Toaster.success()
            .withMessage("Buenos dias 1")
            .withExtraInfo("The service was successfully called :+1:")
            .show()

        Toaster.error().withMessage("Buenos dias 2").setDuration(LongToastDuration).show()
        Toaster.debug().withMessage("Buenos dias 3").setDuration(1000).show()
        Toaster.warning().withMessage("Buenos dias 5").setDuration(4000).show()
        Handler(Looper.getMainLooper()).postDelayed({
            generateExampleSet()
        }, 2000)
    }
}