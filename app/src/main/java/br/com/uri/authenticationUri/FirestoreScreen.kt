package br.com.uri.authenticationUri

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FirestoreScreen(navController: NavController, auth: FirebaseAuth) {
    var students by remember { mutableStateOf<List<String>>(emptyList()) }
    // MUDANÇA 2: Usamos o método .getInstance() para obter a base de dados
    val db = FirebaseFirestore.getInstance()

    fun addStudent() {
        val student = hashMapOf("name" to "Maria Souza", "age" to 25)
        db.collection("students").add(student)
            .addOnSuccessListener { Log.d("FIRESTORE", "Estudante adicionado com ID: ${it.id}") }
            .addOnFailureListener { e -> Log.w("FIRESTORE", "Erro ao adicionar estudante", e) }
    }

    fun listStudents() {
        db.collection("students").get()
            .addOnSuccessListener { result ->
                val studentList = result.documents.map { doc ->
                    val name = doc.data?.get("name") ?: "N/A"
                    val age = doc.data?.get("age") ?: "N/A"
                    "• $name (Idade: $age)"
                }
                students = studentList
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Atividade 2 - Firestore",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text("Logado como: ${auth.currentUser?.email}")
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { addStudent() }) {
            Text("Adicionar Estudante")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { listStudents() }) {
            Text("Listar Estudantes")
        }
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(students) { studentInfo ->
                Text(studentInfo)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("database_screen") }) {
            Text("Ir para Atividade 1")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                auth.signOut()
                navController.navigate("login_screen") {
                    popUpTo(0)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Logout")
        }
    }
}

