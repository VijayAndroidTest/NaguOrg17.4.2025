package com.example.naguorg.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.naguorg.ui.theme.NaguOrgTheme
import com.cashfree.pg.api.CFPaymentGatewayService
import com.cashfree.pg.core.api.CFSession

import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback
import com.cashfree.pg.core.api.exception.CFException
import com.cashfree.pg.core.api.utils.CFErrorResponse

import com.cashfree.pg.ui.api.CFDropCheckoutPayment
import com.example.naguorg.domain_api.OrderResponse
import com.example.naguorg.feature_products.domain.Product
import com.example.naguorg.repository.CheckoutRepository
import com.example.naguorg.feature_cart.data.saveImageToGallery
import com.example.naguorg.viewmodel.CheckoutViewModel

class CheckoutActivity : ComponentActivity(), CFCheckoutResponseCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CFPaymentGatewayService.getInstance().setCheckoutCallback(this)

        val cartItems: ArrayList<Product>? = intent.getParcelableArrayListExtra("cart_items")

        setContent {
            val viewModel = remember { CheckoutViewModel(CheckoutRepository()) }

            NaguOrgTheme {
                CheckoutScreen(
                    cartItems = cartItems ?: emptyList(),
                    upiId = "sthennarasu1996s@okaxis",
                    viewModel = viewModel,
                    onPayClick = { amount -> viewModel.createOrder(amount) }
                )
            }
        }
    }
    fun startCashfreePayment(order: OrderResponse) {
        try {
            val session = CFSession.CFSessionBuilder()
                .setEnvironment(CFSession.Environment.SANDBOX)
                .setOrderId(order.orderId)
                .setPaymentSessionID(order.paymentSessionId)
                .build()

            val payment = CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder()
                .setSession(session)
                .build()

            CFPaymentGatewayService.getInstance().doPayment(this, payment)
        } catch (e: CFException) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentVerify(orderID: String) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentFailure(
        p0: CFErrorResponse?,
        p1: String?
    ) {
        Toast.makeText(this, "Payment Failed: ", Toast.LENGTH_LONG).show()
    }

    fun captureScreenAndSave() {
        // Ensure saveImageToGallery is defined elsewhere in your project
        val view = window.decorView.rootView
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        saveImageToGallery(this, bitmap, "Checkout_Screenshot")
    }
}


