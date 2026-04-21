package pt.ipt.dama2026.datastorage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import pt.ipt.dama2026.datastorage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        // setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tarefa 1 - Shared Preferences
        viewBinding.btWrite1.setOnClickListener {
            escreveSharedPreferences()
            escondeTeclado(it)
        }

        viewBinding.btRead1.setOnClickListener {
            lerSharedPreferences()
        }

    }

    //++++++++++++++++++++++++++++++++
    // Tarefa 1 - Shared Preferences
    //++++++++++++++++++++++++++++++++
    /**
     * ler os dados da Shared Preferences e mostrá-los no ecrã
     */
    fun lerSharedPreferences() {
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        // ler o conteúdo da Shared Preferences. Se não for possível a leitura, devolve um texto alternativo
        // estou a usar o método getString() porque guardei 'texto'. Outros tipos de dados,
        // implicam outros métodos
        val texto = sharedPreferences.getString("SPreferenceSimples", getString(R.string.txt_vazio))

        // usar os dados lidos
        Snackbar.make(viewBinding.root, "Introduziu: $texto", Snackbar.LENGTH_SHORT).show()
    }

    /**
     * escreve os dados na Shared Preferences
     */
    fun escreveSharedPreferences() {
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // colocar o conteúdo que o utilizador escreveu nas Shared Preferences
        // Os dados são sempre escritos no formato: 'Variável'+'valor'
        // usa-se o método putXXXXX, onde XXXXX significa String, Int, Double, etc.
        editor.putString("SPreferenceSimples", viewBinding.editText1.text.toString())
        // guardar as alterações
        editor.commit()  // eventualmente, <=>   editor.apply()

        // neste caso, vamos mostrar uma mensagem de 'conforto' ao utilizador
        Toast.makeText(this, getString(R.string.dados_guardados), Toast.LENGTH_SHORT).show()
    }


    /**
     * esconde o teclado quando já não é necessário
     * @param view - referência à actividade em que se encontra o teclado
     */
    fun escondeTeclado(view: View) {
        // hide the keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}