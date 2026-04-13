package br.com.emilygomesmiranda.training_diary

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    private var diaSelecionado = "Seg"

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

        val dateText = findViewById<TextView>(R.id.date)

        fun atualizarData() {
            val data = when (diaSelecionado) {
                "Dom" -> "Domingo, 12 de Abril"
                "Seg" -> "Segunda, 13 de Abril"
                "Ter" -> "Terça, 14 de Abril"
                "Qua" -> "Quarta, 15 de Abril"
                "Quin" -> "Quinta, 16 de Abril"
                "Sex" -> "Sexta, 17 de Abril"
                "Sab" -> "Sábado, 18 de Abril"
                else -> ""
            }

            dateText.text = data
        }

        fun selecionarDia(dia: String, id: Int) {
            val botoesDias = listOf(
                R.id.button_seg,
                R.id.button_ter,
                R.id.button_qua,
                R.id.button_quin,
                R.id.button_sex,
                R.id.button_sab,
                R.id.button_dom
            )
            for (botaoId in botoesDias) {
                val botao = findViewById<TextView>(botaoId)
                botao.setBackgroundResource(R.drawable.bg_day_pill)
                botao.setTextColor(getColor(R.color.white))
            }

            diaSelecionado = dia
            atualizarData()
            atualizarTudo()
            findViewById<TextView>(id).setBackgroundResource(R.drawable.bg_day_pill_active)
            findViewById<TextView>(id).setTextColor(getColor(R.color.orange_primary))
        }

        findViewById<TextView>(R.id.button_seg).setOnClickListener { selecionarDia("Seg", R.id.button_seg) }
        findViewById<TextView>(R.id.button_ter).setOnClickListener { selecionarDia("Ter", R.id.button_ter) }
        findViewById<TextView>(R.id.button_qua).setOnClickListener { selecionarDia("Qua", R.id.button_qua) }
        findViewById<TextView>(R.id.button_quin).setOnClickListener { selecionarDia("Quin", R.id.button_quin) }
        findViewById<TextView>(R.id.button_sex).setOnClickListener { selecionarDia("Sex", R.id.button_sex) }
        findViewById<TextView>(R.id.button_sab).setOnClickListener { selecionarDia("Sab", R.id.button_sab) }
        findViewById<TextView>(R.id.button_dom).setOnClickListener { selecionarDia("Dom", R.id.button_dom) }

        fun abrirExercicio(nome: String, series: Int, reps: String, grupo: String) {
            val intent = Intent(this, WorkoutActivity::class.java)
            intent.putExtra("nome", nome)
            intent.putExtra("series", series)
            intent.putExtra("reps", reps)
            intent.putExtra("grupo", grupo)
            intent.putExtra("dia", diaSelecionado)
            startActivity(intent)
        }

        findViewById<MaterialCardView>(R.id.workout_supino).setOnClickListener {
            abrirExercicio("Supino Reto", 3, "12", "Peito")
        }

        findViewById<MaterialCardView>(R.id.workout_agachamento).setOnClickListener {
            abrirExercicio("Agachamento", 3, "15", "Pernas")
        }

        findViewById<MaterialCardView>(R.id.workout_rosca_direta).setOnClickListener {
            abrirExercicio("Rosca Direta", 3, "10", "Bíceps")
        }

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
        atualizarTudo()
    }

    private fun atualizarTudo() {

        val prefs = getSharedPreferences("treinos", MODE_PRIVATE)

        val progressText = findViewById<TextView>(R.id.progress_text)
        val progressFill = findViewById<View>(R.id.progressFill)

        fun atualizarBarra(concluidos: Int, total: Int) {
            val progresso = if (total > 0) concluidos.toFloat() / total else 0f

            progressFill.post {
                val container = progressFill.parent as View
                val larguraTotal = container.measuredWidth
                val novaLargura = (larguraTotal * progresso).toInt()

                val anim = ValueAnimator.ofInt(progressFill.width, novaLargura)
                anim.duration = 300

                anim.addUpdateListener {
                    val params = progressFill.layoutParams
                    params.width = it.animatedValue as Int
                    progressFill.layoutParams = params
                }

                anim.start()
            }

            progressText.text = "$concluidos/$total concluídos"
        }

        fun treinoConcluido(nome: String): Boolean {
            return prefs.getBoolean("${diaSelecionado}_${nome}_${R.id.checkbox_1}", false) &&
                    prefs.getBoolean("${diaSelecionado}_${nome}_${R.id.checkbox_2}", false) &&
                    prefs.getBoolean("${diaSelecionado}_${nome}_${R.id.checkbox_3}", false)
        }

        fun atualizarStatus(cardId: Int, statusId: Int, nome: String): Boolean {
            val concluido = treinoConcluido(nome)

            val card = findViewById<MaterialCardView>(cardId)
            val status = card.findViewById<TextView>(statusId)

            if (concluido) {
                status.text = "Feito"
                status.setBackgroundResource(R.color.teal_light)
                status.setTextColor(getColor(R.color.teal_primary))
            } else {
                status.text = "Pendente"
                status.setBackgroundResource(R.color.orange_pale)
                status.setTextColor(getColor(R.color.coral_dark))
            }

            return concluido
        }

        var concluidos = 0
        val total = 3

        if (atualizarStatus(R.id.workout_supino, R.id.status_supino, "Supino Reto")) concluidos++
        if (atualizarStatus(R.id.workout_agachamento, R.id.status_agachamento, "Agachamento")) concluidos++
        if (atualizarStatus(R.id.workout_rosca_direta, R.id.status_rosca_direta, "Rosca Direta")) concluidos++

        atualizarBarra(concluidos, total)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_inicio
    }
}