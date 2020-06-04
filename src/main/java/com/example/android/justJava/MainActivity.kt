package com.example.android.justJava

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var quantity = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //Submits the order and display the order summary in an email app
    fun submitOrder(v: View) {

        //Getting name of the customer
        val name = findViewById<EditText>(R.id.editName)!!
        val valueName = name.text.toString()

        //Checking the state of cream check box
        val checkCream = findViewById<CheckBox>(R.id.checkCream)!!
        var value1 = false
        value1 = checkCream.isChecked

        //Checking the state of chocolate cream check box
        val checkChocolate = findViewById<CheckBox>(R.id.checkChocolate)!!
        var value2 = false
        value2 = checkChocolate.isChecked

        //Setting restrictions on order of coffee
        if (quantity < 0) {
            quantity = 0
            displayQuantity(quantity)
            Toast.makeText(this, "You cannot order less than 1 coffee", Toast.LENGTH_LONG).show()
        }
        if (quantity > 100) {
            quantity = 0
            displayQuantity(quantity)
            Toast.makeText(this, "You have crossed the limit of ordering", Toast.LENGTH_LONG).show()
        }

        //Calling fun and getting values of coffee order
        val price: Int = calculatePrice(valueName, value1, value2)

        //Calling order summary method and passing values
        val priceMessage = orderSummary(valueName, price, value1, value2)

        //Displaying the order summary to the user in a toast message
        Toast.makeText(this, priceMessage, Toast.LENGTH_LONG).show()

        //Navigating to the mail app , using Intents
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_SUBJECT, "Coffee order for $valueName")
            putExtra(Intent.EXTRA_TEXT, priceMessage)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }

    }

    //Calculates the price of the coffee
    private fun calculatePrice(valueName: String, hasWipCream: Boolean, hasChocolateCream: Boolean): Int {

        var basePrice = 5
        if (hasWipCream) {
            basePrice += 1
        }
        if (hasChocolateCream) {
            basePrice += 2
        }
        return quantity * basePrice
    }

    //Creating a order summary
    private fun orderSummary(valueName: String, price: Int, value1: Boolean, value2: Boolean): String {

        return getString(R.string.Name, valueName) + "\n" +
                " Order: $quantity \n " +
                " Total: $price\n" + "" +
                "  Whipped Cream: $value1\n" +
                "  Chocolate Cream: $value2\n" +
                getString(R.string.thankyou)
    }

    //Resetting the order
    fun reset(v: View) {
        quantity = 0
        displayQuantity(quantity)

        if (checkCream.isChecked || checkChocolate.isChecked) {
            checkCream.toggle()
            checkChocolate.toggle()

        }
        orderSummary("", price = 0, value1 = false, value2 = false)
    }

    //Increments the order
    fun increment(v: View) {
        if (quantity == 100) {
            Toast.makeText(this, "You cannot order more", Toast.LENGTH_LONG)
                    .show()
            return
        }
        quantity += 1
        displayQuantity(quantity)
    }

    //decrements the order
    fun decrement(v: View) {
        if (quantity == 1) {
            Toast.makeText(this, "You cannot order less than 1 cup of coffee", Toast.LENGTH_LONG).show()
        }
        quantity -= 1
        displayQuantity(quantity)
    }

    //Displays the quantity of the order to the screen
    private fun displayQuantity(i: Int) {
        val txtView = findViewById<TextView>(R.id.value)
        txtView.text = "$quantity"
    }


}
