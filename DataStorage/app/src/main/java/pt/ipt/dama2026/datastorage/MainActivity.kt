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
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import java.util.Scanner

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


        // Tarefa 2 - Shared Preferences com múltiplos ficheiros
        viewBinding.btWrite2.setOnClickListener {
            escreveSharedPreferencesMulti()
            escondeTeclado(it)
        }

        viewBinding.btRead2.setOnClickListener {
            lerSharedPreferencesMulti()
        }


        // Tarefa 3 - Cache
        viewBinding.btWrite3.setOnClickListener {
            escreverCache()
            escondeTeclado(it)
        }

        viewBinding.btRead3.setOnClickListener {
            lerCache()
        }

    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Tarefa 3 - Cache
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Escrever na Cache do dispositivo
     */
    fun escreverCache() {
        // endereço da pasta onde será gerado o ficheiro
        // associado à área de atuação da aplicação
        val directory: File = cacheDir
        // nome do ficheiro na Cache
        val file: File = File(directory, "dadosCache-SharedData.txt")
        try {
            val fo: FileOutputStream = FileOutputStream(file)
            val ps: PrintStream = PrintStream(fo)
            ps.println(viewBinding.editText3a.text)
            ps.println(viewBinding.editText3b.text)
            ps.close()
            fo.close()

            // neste caso, vamos mostrar uma mensagem de 'conforto' ao utilizador
            Toast.makeText(this, getString(R.string.dados_guardados), Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, getString(R.string.escritaErradaCache), Toast.LENGTH_LONG).show()
        }
    }

    /**
     * ler os dados da Cache
     */
    fun lerCache() {
        val directory: File = cacheDir // <=> getCacheDir()
        val file = File(directory, "dadosCache-SharedData.txt")
        try {
            val fi = FileInputStream(file)
            val sc = Scanner(fi)
            val nome = sc.nextLine()
            val idade = sc.nextLine()
            sc.close()
            fi.close()

            // mostrar os dados
            Snackbar.make(
                viewBinding.root,
                "Você chama-se ${nome.toString().uppercase()} e tem $idade anos!",
                Snackbar.LENGTH_LONG
            ).show()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, getString(R.string.cache_vazia), Toast.LENGTH_LONG).show()
        }
    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Tarefa 2 - Shared Preferences com múltiplos ficheiros
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /**
     * ler os dados das duas Shared Preferences e mostrá-los no ecrã
     */
    fun lerSharedPreferencesMulti() {
        val sharedPreferencesNome = getSharedPreferences("nomes.dat", MODE_PRIVATE)
        val sharedPreferencesIdade = getSharedPreferences("idades.dat", MODE_PRIVATE)
        // ler o conteúdo da Shared Preferences. Se não for possível a leitura, devolve um texto alternativo
        // estou a usar o método getString() porque guardei 'texto'. Outros tipos de dados,
        // implicam outros métodos
        val nome = sharedPreferencesNome.getString("NOME", getString(R.string.txt_vazio))
        val idade = sharedPreferencesIdade.getInt("IDADE", 0)

        // usar os dados lidos
        Snackbar.make(
            viewBinding.root,
            "Você chama-se ${nome.toString().uppercase()} e tem $idade anos!",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    /**
     * escreve os dados na Shared Preferences usando dois documentos com dados
     */
    fun escreveSharedPreferencesMulti() {
        val sharedPreferencesNomes = getSharedPreferences("nomes.dat", MODE_PRIVATE)
        val sharedPreferencesIdades = getSharedPreferences("idades.dat", MODE_PRIVATE)

        val editorNomes: SharedPreferences.Editor = sharedPreferencesNomes.edit()
        val editorIdades: SharedPreferences.Editor = sharedPreferencesIdades.edit()

        // colocar o conteúdo que o utilizador escreveu nas Shared Preferences
        // Os dados são sempre escritos no formato: 'Variável'+'valor'
        // usa-se o método putXXXXX, onde XXXXX significa String, Int, Double, etc.
        editorNomes.putString("NOME", viewBinding.editText2a.text.toString())
        editorIdades.putInt(
            "IDADE",
            viewBinding.editText2b.text.toString().toIntOrNull() ?: 0
        ) // se a Idade=NULL, coloca 0
        // guardar as alterações
        editorNomes.commit()  // eventualmente, <=>   editorNomes.apply()
        editorIdades.commit()

        // neste caso, vamos mostrar uma mensagem de 'conforto' ao utilizador
        Toast.makeText(this, getString(R.string.dados_guardados), Toast.LENGTH_SHORT).show()
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