package org.guardevour.developerdiary.screens

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import org.guardevour.developerdiary.PassWordManager
import org.guardevour.developerdiary.components.AuthKeyboard
import org.guardevour.developerdiary.components.RegKeyboard

@Composable
fun BiometricAuthenticationScreen(value: MutableState<Boolean>) {
    val context = LocalContext.current as FragmentActivity
    val biometricManager = BiometricManager.from(context)
    val canAuthenticateWithBiometrics = when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
        BiometricManager.BIOMETRIC_SUCCESS -> true
        else -> {
            Text(text = "Device does not support strong biometric authentication")
            false
        }
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val text = remember {
                mutableStateOf("")
            }

            val textInput = remember { mutableStateOf("")}
            val circleColor = MaterialTheme.colorScheme.onSurface
            LazyRow(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp)
            ) {
                items(4){index->
                    Canvas(modifier = Modifier.size(20.dp)) {
                        drawCircle(
                            color = if (index-1 < textInput.value.lastIndex) Color.Cyan else circleColor
                        )
                    }
                }
            }

            val passwordManager = PassWordManager(LocalContext.current)
            val haptic = LocalHapticFeedback.current
            if (passwordManager.getPassword() == ""){
                RegKeyboard(textInput = textInput, passwordManager = passwordManager, value = value)
            }else{
                AuthKeyboard(textInput = textInput, passwordManager = passwordManager, value = value, haptic = haptic, context = context)
            }

            if (canAuthenticateWithBiometrics && passwordManager.getPassword() != "") {
                Button(
                    onClick = {
                        try {
                            authenticateWithBiometric(context, value)
                        }
                        catch (ex: Exception){
                            text.value = ex.toString()
                        }

                    },
                    colors =ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .padding(8.dp)
                        .offset(y = 80.dp)
                        .alpha(0.75f)
                ) {
                    Text(text = "Authenticate with Biometric")
                }
                Text(text = text.value)
            }
        }
    }
}


fun authenticateWithBiometric(
    context: FragmentActivity,
    value: MutableState<Boolean>
) {
    val executor = context.mainExecutor
    val biometricPrompt = BiometricPrompt(
        context,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {

                value.value = true
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            }

            override fun onAuthenticationFailed() {
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setDescription("Place your finger the sensor.")
        .setNegativeButtonText("Cancel")
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        .build()

    biometricPrompt.authenticate(promptInfo)
}