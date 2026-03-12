package com.adriangm.motodirex.gestionaverias.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.adriangm.motodirex.gestionaverias.R
import com.adriangm.motodirex.gestionaverias.databinding.ActivityMainBinding
import com.adriangm.motodirex.gestionaverias.ui.login.LoginActivity

/**
 * Contenedor principal de la app.
 * No muestra contenido propio, solo aloja los Fragments
 * y gestiona la Toolbar y la navegación.
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar la Toolbar
        setSupportActionBar(binding.toolbar)

        // Conectar el NavController con la Toolbar
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            // Estas pantallas NO mostrarán flecha de volver atrás
            setOf(R.id.listadoAveriasFragment)
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        // Flecha de volver en rojo MotoDirex
        binding.toolbar.navigationIcon?.setTint(
            getColor(R.color.primary)
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.title = destination.label
        }
    }

    // Menú superior con opción de cerrar sesión
    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.perfilFragment -> {
                navController.navigate(R.id.perfilFragment)
                true
            }
            R.id.action_logout -> {
                cerrarSesion()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cerrarSesion() {
        val intent = Intent(this, LoginActivity::class.java)
        // Limpiamos el back stack para que no pueda volver atrás
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}