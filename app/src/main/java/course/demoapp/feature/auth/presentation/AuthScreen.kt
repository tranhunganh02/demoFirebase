package course.demoapp.feature.auth.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    signInAction: (String, String) -> Unit,
    signUpAction: (String, String, String, String) -> Unit,
    navigateToHome: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var isSignIn by remember { mutableStateOf(true) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    // State variables for error messag

    val state = viewModel.authState.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false)  }

    val context = LocalContext.current

    LaunchedEffect(key1 = state.value?.isError) {
        scope.launch {
            if (state.value?.isError?.isNotEmpty() == true) {
                val error = state.value?.isError
                Toast.makeText(context, "${error}", Toast.LENGTH_LONG).show()
            }
        }
    }

    LaunchedEffect(key1 = state.value?.isSuccess) {
        scope.launch {
            if (state.value?.isSuccess?.isNotEmpty() == true) {
                val success = state.value?.isSuccess
                Toast.makeText(context, "${success}", Toast.LENGTH_LONG).show()
                navigateToHome()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // First Name TextField


                if (!isSignIn) {
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = {
                            Text("First Name")
                        },
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Last Name TextField
                    TextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = {
                            Text("Last Name")
                        },
                    )
                }

                // Email TextField
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = {
                        Text("Email")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    ),
                    visualTransformation = if (isSignIn) VisualTransformation.None else VisualTransformation.None
                )

                // Password TextField
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = {
                        Text("Password")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = { isPasswordVisible = !isPasswordVisible }
                        ) {
                            val icon = if (isPasswordVisible) Icons.Default.Lock else Icons.Default.Search
                            Icon(
                                imageVector = icon,
                                contentDescription = "Toggle Password Visibility"
                            )
                        }
                    }
                )

                // ... (other content)
                Spacer(modifier = Modifier.height(16.dp))

                if (state.value?.isLoading == true) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(

                            color = Color.Green // Change color as needed
                        )
                    }
                }
                // Sign In/Sign Up Buttons
                Button(
                    onClick = {
                        if (isSignIn) {
                            signInAction(email, password)
                        } else {
                            signUpAction(email, password, firstName, lastName)

                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(8.dp),
                ) {
                    Text(
                        text = if (isSignIn) "Sign In" else "Sign Up",
                        fontSize = 18.sp,
                    )
                }


                // Clickable Text
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(8.dp),
                ) {
                    ClickableText(
                        text = AnnotatedString(buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Blue)) {
                                append(if (isSignIn) "Don't have an account? Sign Up" else "Already have an account? Sign In")
                            }
                        }.toString()),
                        onClick = {
                            email = ""
                            password = ""
                            isSignIn = !isSignIn
                        },
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }


    }
}