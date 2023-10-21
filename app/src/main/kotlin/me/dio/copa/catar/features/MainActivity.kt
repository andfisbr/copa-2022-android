package me.dio.copa.catar.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import me.dio.copa.catar.extensions.observe
import me.dio.copa.catar.notification.scheduler.NotificationMatchesWorker
import me.dio.copa.catar.ui.theme.Copa2022Theme

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
        
        private val viewModel by viewModels<MainViewModel>()
        
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                
                observeActions()
                
                setContent {
                        Copa2022Theme { // A surface container using the 'background' color from the theme
                                val state by viewModel.state.collectAsState()
                                
                                MainScreen(matches = state.matches, viewModel::toggleNotification)
                        }
                }
        }
        
        private fun observeActions() {
                viewModel.action.observe(this) { action ->
                        when (action) {
                                is MainUiAction.DisableNotification -> NotificationMatchesWorker.cancel(applicationContext, action.match)
                                is MainUiAction.EnableNotification -> NotificationMatchesWorker.start(applicationContext, action.match)
                                is MainUiAction.MatchesNotFound -> TODO()
                                MainUiAction.Unexpected -> TODO()
                        }
                }
        }
        
        
}
