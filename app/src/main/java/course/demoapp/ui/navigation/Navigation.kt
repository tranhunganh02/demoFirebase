package course.demoapp.ui.navigation
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import course.demoapp.feature.auth.presentation.AuthScreen
import course.demoapp.feature.auth.presentation.AuthViewModel
import course.demoapp.feature.home.presentation.HomeScreen
import androidx.compose.runtime.livedata.observeAsState
import course.demoapp.feature.auth.presentation.AuthState


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Navigation(
    authViewModel: AuthViewModel,
    navController: NavHostController = rememberNavController()
) {

    // Get the name of the current screen

    NavHost(navController = navController, startDestination = NavScreen.Auth.route) {
        composable(NavScreen.Home.route) {
            HomeScreen(
                logout = { authViewModel.signOut() },
                onBackPressed = { navController.navigate(NavScreen.Auth.route)},
                viewModel = authViewModel,
            )
        }
        composable(NavScreen.Auth.route) {

            AuthScreen(
                viewModel = authViewModel,

                signInAction = { email, password ->
                    authViewModel.signIn(email, password)
                },

                signUpAction = { email, password, firstname, lastname ->
                    authViewModel.signUp(email, password, firstname, lastname)
                },

                navigateToHome = { navController.navigate(NavScreen.Home.route) }
            )
        }

    }
}


sealed class NavScreen(val route: String) {
    object Home : NavScreen("home")
    object Auth : NavScreen("auth")
}




