package com.kotlinproject.coroutineskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlinproject.coroutineskotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var unserAdapter = UnserAdapter()

    private var unsereListe: ArrayList<Int> = ArrayList()

    // 1 hoch 10 x 6 = 1.000.000
    private var nUnsereListe = 1e6.toLong() // Anzahl der Elemente

    // Variable für unsere Coroutine
    private val unsereCoroutine = CoroutineScope(Dispatchers.Default)
    // Dispatcher.Default aufgrund der erforderlichen Rechenleistung


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecView()

        // für Toastmessage
        binding.btnToast.setOnClickListener {
            Toast.makeText(this, "Huhu! Läuft!", Toast.LENGTH_SHORT).show()
        }
        // für das leeren der Liste -> RecyclerView hat nichts mehr zum zeigen
        binding.btnDelete.setOnClickListener { deleteContent() }

        // Befüllen der RecyclerView im Mainthread (Standard)
        binding.btnFill1.setOnClickListener { fill1() }

        // Sortieren der RecyclerView-Inhalts im Mainthread (Standard)
        binding.btnSort1.setOnClickListener { sort1() }

        // beim Zuweisen einer Funktion in einem extra Thread, müssen wir
        // unsere Coroutine (Scope) zuvor aufrufen
        binding.btnFill2.setOnClickListener {
            // Aufrufen unserer Coroutine
            unsereCoroutine.launch {
                // hier rufen wir die entsprechende Funktion außerhalb
                // des Mainthreads auf
                fill2()
            }
        }

        binding.btnSort2.setOnClickListener {
            Toast.makeText(this, "Sortierung läuft...", Toast.LENGTH_SHORT).show()
            unsereCoroutine.launch {
                sort2()
            }
        }

    }
    // Sortieren des RecyclerView-Inhalts im Mainthread (Standard)
    private fun sort1() {
        // Liste numerisch sortieren lassen
        unsereListe.sortBy{ it }
        // RecyclerView soll geupdatet werden
        unserAdapter.updateUnsereListe(unsereListe)
    }

    private suspend fun sort2() {
        // Liste numerisch sortieren lassen
        unsereListe.sortBy{ it }
        
        withContext(Dispatchers.Main){
            // RecyclerView soll geupdatet werden
            unserAdapter.updateUnsereListe(unsereListe)
            // Message, wenn Sortierung abgeschlossen / View aktualisiert
            Toast.makeText(this@MainActivity, "Sortierung abgeschlossen.", Toast.LENGTH_SHORT).show()
        }

    }

    // Befüllen der RecyclerView im Mainthread (Standard)
    private fun fill1() {
        // mit einer for-Schleife gehen wir durch die
        // Range: 0 bis 1.000.000 (1e6)
        // und setzen anschließend zufällige Zahlen in unsere Liste ein
        for (i in 0 until nUnsereListe){
            // in jedem Durchgang wird eine zufällige Zahl der Liste hinzugefügt
            unsereListe.add(Random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE))
        }
        // RecyclerView soll geupdatet werden
        unserAdapter.updateUnsereListe(unsereListe)
    }

    // suspend functions sind asynchron laufende Funktionen in Coroutines
    private suspend fun fill2() {
        // mit einer for-Schleife gehen wir durch die
        // Range: 0 bis 1.000.000 (1e6)
        // und setzen anschließend zufällige Zahlen in unsere Liste ein
        for (i in 0 until nUnsereListe){
            // in jedem Durchgang wird eine zufällige Zahl der Liste hinzugefügt
            unsereListe.add(Random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE))
        }

        // Rückkehr in den Main-Thread mit aktualisierter Liste
        // withContext() kann nur in coroutines oder suspend function ausgeführt werden
        withContext(Dispatchers.Main){
            unserAdapter.updateUnsereListe(unsereListe)
        }

    }

    private fun deleteContent() {
        unsereListe.clear()
        unserAdapter.updateUnsereListe(unsereListe)
    }

    private fun setupRecView() {
        binding.myRecView.adapter = unserAdapter
        binding.myRecView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}
