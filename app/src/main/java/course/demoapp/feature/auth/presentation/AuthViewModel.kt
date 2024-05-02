package course.demoapp.feature.auth.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import course.demoapp.feature.auth.domain.AuthRepository
import course.demoapp.feature.auth.domain.AuthRepositoryImpl
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    @Suppress("unused")
    constructor() : this(AuthRepositoryImpl())

    val _authState = Channel<AuthState>()
    val authState = _authState.receiveAsFlow()

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    fun getUser(): FirebaseUser? {
        return authRepository.getCurrentUser()


    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.send(AuthState(isLoading= true))
                authRepository.signIn(email, password).onSuccess {

                    _currentUser.value = authRepository.getCurrentUser()
                    _authState.send(AuthState(isSuccess = "Sign in success"))
                    Log.d("userr", " login xong roi")
                }.onFailure {
                    _authState.send(AuthState(isError = "Đăng nhập không thành công. Vui lòng thử lại."))
                }

            } catch (e: Exception) {
                _authState.send(AuthState(isError = e.message))
            }
        }
    }

    fun signUp(email: String, password: String, firstName: String, lastName: String) {

        viewModelScope.launch {
            try {
                _authState.send(AuthState(isLoading= true))
                authRepository.signUp(email, password, firstName, lastName)
                val newUser = authRepository.getCurrentUser()

                // Kiểm tra xem người dùng đã được đăng ký thành công chưa
                if (newUser != null) {
                    // Cập nhật currentUser với thông tin người dùng mới
                    _currentUser.value = newUser
                    // Đánh dấu là đã xác thực
                    _authState.send(AuthState(isSuccess = "Sign up success"))
                }
            } catch (e: Exception) {
                _authState.send(AuthState(isError = e.message))
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
    }

}

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)