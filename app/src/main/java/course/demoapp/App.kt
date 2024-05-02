package course.demoapp

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun App(auth: FirebaseAuth) {

    var showSplashScreen by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(showSplashScreen) {
        delay(2000)
        showSplashScreen = false
    }

        Crossfade(targetState = showSplashScreen, label = "") { isSplashScreenVisible ->
            if (isSplashScreenVisible){
                showSplashScreen = false
            }else {

            }
        }


}