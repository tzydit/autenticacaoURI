package br.com.uri.authenticationUri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                val navController = rememberNavController()
                val startDestination = if (auth.currentUser != null) "firestore_screen" else "login_screen"

                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login_screen") {
                        AuthenticationScreen(auth = auth, navController = navController)
                    }
                    composable("firestore_screen") {
                        FirestoreScreen(navController = navController, auth = auth)
                    }
                    composable("database_screen") {
                        DatabaseScreen(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun AuthenticationScreen(auth: FirebaseAuth, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }

    // **A CORREÇÃO ESTÁ AQUI**
    // Este bloco "ouve" as mudanças no auth.currentUser.
    // Assim que o login é bem-sucedido e currentUser já não é nulo,
    // ele navega para a tela principal.
    LaunchedEffect(auth.currentUser) {
        if (auth.currentUser != null) {
            navController.navigate("firestore_screen") {
                // Limpa a tela de login do histórico para que o utilizador não possa voltar a ela
                popUpTo("login_screen") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isLogin) "Login" else "Criar Conta",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        // O botão agora apenas inicia a tentativa de login.
                        // O LaunchedEffect acima é que trata da navegação.
                        val authAction = if (isLogin) {
                            auth.signInWithEmailAndPassword(email, password)
                        } else {
                            auth.createUserWithEmailAndPassword(email, password)
                        }
                        // Se houver um erro, mostramos a mensagem.
                        authAction.addOnFailureListener {
                            message = "Erro: ${it.message}"
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(if (isLogin) "Entrar" else "Registar", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = { isLogin = !isLogin }) {
                    Text(if (isLogin) "Criar uma conta" else "Já tenho uma conta")
                }
                if (message.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = message, color = Color.Red, fontSize = 14.sp)
                }
            }
        }
    }
}

