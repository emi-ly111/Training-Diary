package br.com.emilygomesmiranda.training_diary

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox


class WorkoutActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_workout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nome = intent.getStringExtra("nome")
        val series = intent.getIntExtra("series", 0)
        val reps = intent.getStringExtra("reps")
        val grupo = intent.getStringExtra("grupo")

        findViewById<TextView>(R.id.workout_header_title).text = nome
        findViewById<TextView>(R.id.date).text = "$grupo - $series séries de $reps reps"

        // BOTAO VOLTAR
        findViewById<ImageView>(R.id.button_back).setOnClickListener {
            finish()
        }

        fun configurarSerie(checkId: Int, statusId: Int) {
            val check = findViewById<AppCompatCheckBox>(checkId)
            val status = findViewById<TextView>(statusId)
            val dia = intent.getStringExtra("dia") ?: "Seg"

            val prefs = getSharedPreferences("treinos", MODE_PRIVATE)

            val nomeExercicio = intent.getStringExtra("nome") ?: ""
            val key = "${dia}_${nomeExercicio}_$checkId"

            fun atualizarUI(isChecked: Boolean) {
                if (isChecked) {
                    status.text = "Feito"
                    status.setBackgroundResource(R.color.teal_light)
                    status.setTextColor(getColor(R.color.teal_primary))
                } else {
                    status.text = "Pendente"
                    status.setBackgroundResource(R.color.orange_pale)
                    status.setTextColor(getColor(R.color.coral_dark))
                }
            }

            // RECUPERA estado salvo
            val salvo = prefs.getBoolean(key, false)
            check.isChecked = salvo
            atualizarUI(salvo)

            // SALVA quando muda
            check.setOnCheckedChangeListener { _, isChecked ->
                prefs.edit().putBoolean(key, isChecked).apply()
                atualizarUI(isChecked)
            }
        }

        configurarSerie(R.id.checkbox_1, R.id.card_status_1)
        configurarSerie(R.id.checkbox_2, R.id.card_status_2)
        configurarSerie(R.id.checkbox_3, R.id.card_status_3)
    }
}