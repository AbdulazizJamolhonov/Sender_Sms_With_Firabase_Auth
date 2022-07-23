package developer.abdulaziz.myfirebaseapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import developer.abdulaziz.myfirebaseapp.databinding.ActivityMain2Binding
import java.util.concurrent.TimeUnit

@SuppressLint("SetTextI18n")
class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var n = MyObject.number
    private lateinit var handler: Handler
    private var countTime = 60
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        MyObject.init(this)
    }

    override fun onResume() {
        super.onResume()
        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("uz")
        handler = Handler(Looper.getMainLooper())
        handler.postDelayed(runnable, 1)

        sendVerificationCode(n)

        binding.edtSms.addTextChangedListener {
            if (it.toString().length == 6) {
                verifyCode()
            }
        }
        binding.edtSms.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                verifyCode()
                val view = currentFocus
                if (view != null) {
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
            true
        }
        mySubString()
    }

    private fun mySubString() {
        val bcode = "+998 (${n.substring(4, n.length - 7)})"
        val codes = "$bcode ${n.substring(6, n.length - 4)}-**-**"
        val ccode = "$bcode ${n.substring(6, n.length - 4)}"
        val dcode = "$ccode-${n.substring(n.length - 4, n.length - 2)}"
        val ecode = "$dcode-${n.substring(n.length - 2, n.length)}"
        binding.textNumber.text = "Bir martalik kod $codes raqamiga yuborildi"
        MyObject.number = ecode
    }

    private fun verifyCode() {
        try {
            val credential =
                PhoneAuthProvider.getCredential(
                    storedVerificationId,
                    binding.edtSms.text.toString()
                )
            signInWithPhoneAuthCredential(credential)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val list = MyObject.list
                    list.clear()
                    list.add(MyObject.number)
                    MyObject.list = list
                    startActivity(Intent(this, MainActivity3::class.java))
                    finish()
                }
            }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resentCode(phoneNimber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNimber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {}

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, token)
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            if (binding.time.text.toString() != "00:00") {
                countTime--
                if (countTime <= 9) binding.time.text = "00:0$countTime"
                else binding.time.text = "00:$countTime"
                handler.postDelayed(this, 1000)
            } else {
                binding.reset.visibility = View.VISIBLE
                binding.reset.setOnClickListener {
                    resentCode(n)
                    countTime = 59
                    binding.reset.visibility = View.INVISIBLE
                    if (countTime <= 9) binding.time.text = "00:0$countTime"
                    else binding.time.text = "00:$countTime"
                }
            }
        }
    }
}