package course.demoapp.feature.auth.domain

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<FirebaseUser?>
    suspend fun signUp(email: String, password: String, firstName: String, lastName: String): Result<FirebaseUser?>
    fun signOut()
    fun getCurrentUser(): FirebaseUser?
}

class AuthRepositoryImpl : AuthRepository {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore_db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override suspend fun signIn(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<FirebaseUser?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            val userProfile = hashMapOf(
                "firstName" to firstName,
                "lastName" to lastName,
                "email" to email
            )
            val displayName = "$firstName $lastName"
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            user?.updateProfile(profileUpdate)?.await()

            firestore_db.collection("users").document(user!!.uid).set(userProfile).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}