package br.com.order.app.utils

import android.annotation.SuppressLint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale


@SuppressLint("SimpleDateFormat")
fun String.formatDate(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
    return formatter.format(parser.parse(this)!!)
}

fun Double.formatPrice(): String {
    val brLocale = Locale("pt", "BR")
    val numberFormat = NumberFormat.getCurrencyInstance(brLocale)
    numberFormat.maximumFractionDigits = 2
    return numberFormat.format(this)
}
