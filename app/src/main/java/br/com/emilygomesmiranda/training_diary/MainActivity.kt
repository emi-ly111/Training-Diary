package br.com.emilygomesmiranda.training_diary

import android.annotation.SuppressLint
import android.os.Bundle
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fun abrirExercicio(nome: String, series: Int, reps: String, grupo: String) {
            val intent = Intent(this, WorkoutActivity::class.java)
            intent.putExtra("nome", nome)
            intent.putExtra("series", series)
            intent.putExtra("reps", reps)
            intent.putExtra("grupo", grupo)
            startActivity(intent)
        }

        findViewById<MaterialCardView>(R.id.workout_supino).setOnClickListener {
            abrirExercicio("Supino Reto", 4, "12", "Peito")
        }

        findViewById<MaterialCardView>(R.id.workout_agachamento).setOnClickListener {
            abrirExercicio("Agachamento", 3, "15", "Pernas")
        }

        findViewById<MaterialCardView>(R.id.workout_rosca_direta).setOnClickListener {
            abrirExercicio("Rosca Direta", 3, "10", "Bíceps")
        }

        // BOTTOM NAV
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_inicio

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> true
                R.id.nav_progresso -> {
                    startActivity(Intent(this, ProgressActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
    override fun onResume() {
        super.onResume()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_inicio
    }
}