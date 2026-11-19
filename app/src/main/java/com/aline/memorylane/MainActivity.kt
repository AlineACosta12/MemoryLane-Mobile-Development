
package com.aline.memorylane

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.aline.memorylane.ui.screens.HomeScreen
import com.aline.memorylane.ui.theme.MemoryLaneTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MemoryLaneTheme {
                HomeScreen(
                    onAddMemory = {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                AddMemoryActivity::class.java
                            )
                        )
                    },

                    onViewAll = {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                AllMemoriesActivity::class.java
                            )
                        )
                    },
                    onOpenMemory = { _ ->
                        startActivity(
                            Intent(this@MainActivity, MemoryDetailActivity::class.java)
                        )
                    }
                )
            }
        }
    }
}
