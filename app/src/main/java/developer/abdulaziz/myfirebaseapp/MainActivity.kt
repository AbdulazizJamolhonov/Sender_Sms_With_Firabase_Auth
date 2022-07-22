package developer.abdulaziz.myfirebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.TextView
import android.widget.Toast
import developer.abdulaziz.myfirebaseapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signIn.setOnClickListener {
            val text = binding.edtNumber.text.toString()
            if (text.isNotEmpty() && text.length == 13 &&
                text.substring(0, text.length - 9) == "+998"
            ) {
                MyObject.number = text
                startActivity(Intent(this, MainActivity2::class.java))
            } else Toast.makeText(this, "Bu Uzb raqami emas", Toast.LENGTH_SHORT).show()
        }
    }
}