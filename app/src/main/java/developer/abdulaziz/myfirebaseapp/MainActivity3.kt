package developer.abdulaziz.myfirebaseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import developer.abdulaziz.myfirebaseapp.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityMain3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.number.text = MyObject.number
    }
}