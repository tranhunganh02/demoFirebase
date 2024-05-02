package course.demoapp.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser
import course.demoapp.feature.auth.presentation.AuthViewModel

@Composable
fun HomeScreen(
    logout: () -> Unit,
    onBackPressed: () -> Unit,
    viewModel: AuthViewModel,
) {
    val currentUser = viewModel.currentUser.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Home Screen ${currentUser.value?.email} ${currentUser.value?.displayName}",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = {
            logout()
            onBackPressed()
        }) {
            Text(text = "Logout")
        }

    }
}
