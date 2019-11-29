const express = require('express');
const app = express();         
const bodyParser = require('body-parser');
const porta = 3000; // porta padrão
const sql = require('mssql');
const conexaoStr = "Server=regulus; Database=BD18206; User Id=BD18206; Password=InfinityYasuo777;";

// Conexao com BD
sql.connect(conexaoStr)
   .then(conexao => global.conexao = conexao)
   .catch(erro => console.log(erro));

// Configurando o body parser para pegar POSTS mais tarde   
app.use(bodyParser.urlencoded({ extended: true}));
app.use(bodyParser.json());
// Acrescentando informacoes de cabecalho para suportar o CORS
app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  res.header("Access-Control-Allow-Methods", "GET, POST, HEAD, OPTIONS, PATCH, DELETE");
  next();
});

// Definindo as rotas
const rota = express.Router();
rota.get('/', (requisicao, resposta) => resposta.json({ mensagem: 'Funcionando!'}));
app.use('/', rota);

// Inicia servidor
app.listen(porta);
console.log('API Funcionando! Dalezada!');

function execSQL(sql, resposta) {
  global.conexao.request()
    .query(sql)
    .then(resultado => resposta.json(resultado.recordset))
    //.then(resultado => console.log(resultado.recordset))
    .catch(erro => resposta.json(erro));
}

// Pegar todos os alunos
rota.get('/pegarAlunos', (requisicao, resposta) => {
  execSQL('SELECT * FROM AlunoAndroid', resposta);
})

// Pegar aluno por RA
rota.get('/pegarAlunos/:ra', (requisicao, resposta) => {
  const ra = requisicao.params.ra;

  execSQL(`SELECT * FROM AlunoAndroid WHERE ra = '${ra}'`, resposta);
})

// Cadastrar aluno
rota.post('/cadastrarAluno', (requisicao, resposta) =>{
    const ra = requisicao.body.ra;
    const nome = requisicao.body.nome;
    const email = requisicao.body.email;

    execSQL(`INSERT INTO AlunoAndroid VALUES ('${ra}', '${nome}', '${email}')`, resposta);
})

// Editar aluno
rota.post('/editarAluno', (requisicao, resposta) =>{
  const ra = requisicao.body.ra;
  const nome = requisicao.body.nome;
  const email = requisicao.body.email;

  if (email == null || email == "")
    execSQL(`UPDATE AlunoAndroid SET nome = '${nome}' WHERE ra = '${ra}'`, resposta);
  else if (nome == null || nome == "")
    execSQL(`UPDATE AlunoAndroid SET email = '${email}' WHERE ra = '${ra}'`, resposta);
  else
    execSQL(`UPDATE AlunoAndroid SET nome = '${nome}', email = '${email}' WHERE ra = '${ra}'`, resposta);
  
})

// Excluir aluno
rota.delete('/excluirAluno/:ra', (requisicao, resposta) =>{
  const ra = requisicao.params.ra;

  execSQL(`DELETE FROM AlunoAndroid WHERE ra = '${ra}'`, resposta);
})