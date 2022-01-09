package com.rafaelsramos.hermes.samples

import android.content.Context
import android.widget.Toast
import com.spartancookie.hermeslogger.commands.models.HermesCommand

object CommandsSamples {

    fun getCommands(ctx: Context) = mutableListOf(
        HermesCommand(
            name = "Beauty General",
            description = "Enable/Disable beauty CodeGuard.",
            path = "Commands/Code Guards/Beauty",
            command = { toast(ctx, "Beauty ON") }),
        HermesCommand(
            name = "Beauty Layout",
            description = "Enable/Disable beauty layouts CodeGuard.",
            path = "Commands/Code Guards/Beauty",
            command = { toast(ctx, "Beauty layouts ON") }),
        HermesCommand(
            name = "Beauty Bag",
            description = "Enable/Disable beauty bag CodeGuard.",
            path = "Commands/Code Guards/Beauty",
            command = { toast(ctx, "Beauty bag ON") }),
        HermesCommand(
            name = "Final Sale",
            description = "Enable/Disable Final Sale CodeGuard.",
            path = "Commands/Code Guards/Final Sale",
            command = { toast(ctx, "Final Sale ON") }),

        HermesCommand(
            name = "Clear All",
            description = "Clear all shared preferences",
            path = "Commands/General/Shared Preferences",
            command = { toast(ctx, "All prefs cleared") }),
        HermesCommand(
            name = "Clear Product related",
            description = "Clear all shared preferences related to products.",
            path = "Commands/General/Shared Preferences/Product",
            command = { toast(ctx, "Product related prefs cleared") }),
        HermesCommand(
            name = "Clear Final sale products related",
            description = "Clear all shared preferences related to final sale products.",
            path = "Commands/General/Shared Preferences/Product",
            command = { toast(ctx, "Final sale related prefs cleared") }),
    ).apply {
        val europe = mutableListOf("Portugal", "Spain", "France", "Ireland", "England", "Wales", "Netherlands", "Latvia", "Austria", "Czech Republic", "Belgium", "Luxembourg", "Germany", "Scotland", "Ukraine")
        val asia = mutableListOf("Russia", "China", "India", "Pakistan", "Hong Kong", "Japan", "Iran", "Thailand", "Philippines")
        val southAmerica = mutableListOf("Brazil", "Argentina", "Venezuela", "Peru", "Bolivia", "Colombia", "Paraguay", "Ecuador")
        val northAmerica = mutableListOf("USA", "Canada", "Mexico", "Greenland", "Haiti", "Jamaica", "Cuba", "Panama", "The Bahamas")
        val africa = mutableListOf("Egypt", "South Africa", "Morocco", "Nigeria", "Mozambique", "Angola", "Ethiopia", "Kenya", "Algeria", "Sudan", "Madagascar", "Cameroon")

        europe.forEach { country -> add(fetchCountryCommand(ctx, country, "Europe")) }
        asia.forEach { country -> add(fetchCountryCommand(ctx, country, "Asia")) }
        southAmerica.forEach { country -> add(fetchCountryCommand(ctx, country, "South America")) }
        northAmerica.forEach { country -> add(fetchCountryCommand(ctx, country, "North America")) }
        africa.forEach { country -> add(fetchCountryCommand(ctx, country, "Africa")) }
    }

    private fun fetchCountryCommand(ctx: Context, country: String, continent: String) = HermesCommand(
        name = country,
        description = "Change the app country to $country",
        path = "Commands/General/Change country/$continent",
        command = { toast(ctx, "Changed to $country") }
    )

    private fun toast(ctx: Context, msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }

}

