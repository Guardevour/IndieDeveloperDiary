package org.guardevour.developerdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.guardevour.developerdiary.room.entities.Project
import org.guardevour.developerdiary.screens.BiometricAuthenticationScreen
import org.guardevour.developerdiary.screens.MainScreen
import org.guardevour.developerdiary.screens.Module
import org.guardevour.developerdiary.screens.ProjectInfoScreen
import org.guardevour.developerdiary.screens.ProjectScreen
import org.guardevour.developerdiary.ui.theme.DeveloperDiaryTheme

class MainActivity : FragmentActivity() {

    private var isAuthEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeveloperDiaryTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isBiometricSucceed = remember {
                        mutableStateOf(false)
                    }
                    AnimatedVisibility(visible = !isBiometricSucceed.value  && isAuthEnabled) {
                        BiometricAuthenticationScreen(value = isBiometricSucceed)
                    }

                   if (isBiometricSucceed.value || !isAuthEnabled){
                       isAuthEnabled = false
                       val navController = rememberNavController()
                       NavHost(navController = navController, startDestination = "main") {

                               composable("main") {
                                   MainScreen(
                                       navController
                                   )
                               }
                               composable(
                                   "project/{projectId}",
                                   arguments = listOf(navArgument("projectId") {
                                       type = NavType.IntType
                                   })
                               ) { backStackEntry ->
                                   ProjectScreen(
                                       navController, backStackEntry.arguments?.getInt("projectId")!!
                                   )
                               }
                               composable(
                                   "projectInfo/{projectId}",
                                   arguments = listOf(navArgument("projectId") {
                                       type = NavType.IntType
                                   })
                               ) { backStackEntry ->
                                   ProjectInfoScreen(backStackEntry.arguments?.getInt("projectId")!!,
                                       navController
                                   )
                               }


                       }
                   }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("ISAUTHENABLED", isAuthEnabled)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isAuthEnabled= savedInstanceState.getBoolean("ISAUTHENABLED", true)
    }
}
