package br.com.uri.authenticationUri

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

// Definição da tabela "users" para a biblioteca Exposed
object UsersTable : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val age = integer("age")
    override val primaryKey = PrimaryKey(id)
}

/**
 * Função que executa as operações da Atividade 1:
 * 1. Cria a tabela.
 * 2. Insere um registro.
 * 3. Lê e exibe os registros no console (Logcat).
 */
fun runDatabaseActivity() {
    // Conecta a um banco de dados H2 em memória
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")

    transaction {
        addLogger(StdOutSqlLogger) // Loga o SQL gerado no Logcat

        Log.d("Atividade1_DB", "Criando a tabela 'UsersTable'...")
        SchemaUtils.create(UsersTable)
        Log.d("Atividade1_DB", "Tabela criada com sucesso.")

        Log.d("Atividade1_DB", "Inserindo um registro...")
        UsersTable.insert {
            it[name] = "Ana Clara"
            it[age] = 28
        }
        Log.d("Atividade1_DB", "Registro inserido com sucesso.")

        Log.d("Atividade1_DB", "--- Lendo todos os registros da tabela: ---")
        UsersTable.selectAll().forEach { row ->
            val id = row[UsersTable.id]
            val name = row[UsersTable.name]
            val age = row[UsersTable.age]
            Log.d("Atividade1_DB", "ID: $id, Nome: $name, Idade: $age")
        }
        Log.d("Atividade1_DB", "--- Leitura concluída. ---")
    }
}

@Composable
fun DatabaseScreen(navController: NavController) {
    var statusMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Atividade 1",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Integração com Banco de Dados (Exposed)",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                statusMessage = "Executando... Verifique o Logcat."
                // Executa as operações de DB fora da thread principal para não travar a UI
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        runDatabaseActivity()
                    }
                    statusMessage = "Operação concluída! O resultado está no Logcat."
                }
            }) {
            Text("Executar Atividade 1")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (statusMessage.isNotEmpty()) {
            Text(
                text = statusMessage,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Empurra o botão para baixo

        // Botão para voltar à tela anterior (FirestoreScreen)
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.popBackStack() }
        ) {
            Text("Voltar para Atividade 2")
        }
    }
}

