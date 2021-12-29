package com.rafaelsramos.hermes.samples

import kotlin.math.roundToInt

object RandomMessages {

    private const val short = "Lorem ipsum dolor amet mustache knausgaard +1."
    private const val medium = "Lorem ipsum dolor amet mustache knausgaard +1, blue bottle waistcoat tbh semiotics artisan synth stumptown gastropub cornhole celiac swag."
    private const val long = "Lorem ipsum dolor amet mustache knausgaard +1, blue bottle waistcoat tbh semiotics artisan synth stumptown gastropub cornhole celiac swag. Brunch raclette vexillologist post-ironic glossier ennui XOXO mlkshk godard pour-over blog tumblr humblebrag. Blue bottle put a bird on it twee prism biodiesel brooklyn. Blue bottle ennui tbh succulents."

    val getSample get() = when ((Math.random().toFloat() * 3f).roundToInt()) {
        1 -> medium
        2 -> long
        else -> short
    }

}