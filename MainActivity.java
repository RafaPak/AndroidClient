package cotuca.unicamp.clienteandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountAuthenticatorResponse;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    Button todos, consultaRa, incluir, excluir, editar;
    EditText ra, nome, email;
    ListView lista;
    ArrayAdapter<Aluno> alAdapter;
    ArrayList<Aluno> listaAlunos;
    RetrofitConfig retrofitConfig;
    boolean achou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todos = (Button)findViewById(R.id.btnTodos);
        consultaRa = (Button)findViewById(R.id.btnConsultaRa);
        incluir = (Button)findViewById(R.id.btnIncluir);
        excluir = (Button)findViewById(R.id.btnExcluir);
        editar = (Button)findViewById(R.id.btnEditar);
        ra = (EditText)findViewById(R.id.edRA);
        nome = (EditText)findViewById(R.id.edNome);
        email = (EditText)findViewById(R.id.edEmail);
        lista = (ListView)findViewById(R.id.lstAlunos);

        retrofitConfig = new RetrofitConfig();
        listaAlunos = new ArrayList<Aluno>();

        todos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Call<ArrayList<Aluno>> alunos = retrofitConfig.getAlunoService().pegarAlunos();
            alunos.enqueue(new Callback<ArrayList<Aluno>>() {
                @Override
                public void onResponse(Call<ArrayList<Aluno>> call, Response<ArrayList<Aluno>> response) {
                    if (response.body().isEmpty())
                        Toast.makeText(getBaseContext(), "Não tem alunos na lista", Toast.LENGTH_SHORT).show();

                    listaAlunos = response.body();
                    alAdapter = new ArrayAdapter<Aluno>(MainActivity.this, android.R.layout.simple_list_item_1, listaAlunos);
                    lista.setAdapter(alAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<Aluno>> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Erro ao buscar todos os alunos: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            }
        });

        consultaRa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String r = ra.getText().toString();

                if (!r.equals(""))
                {
                    Call<List<Aluno>> al = retrofitConfig.getAlunoService().pegarAluno(r);
                    al.enqueue(new Callback<List<Aluno>>() {
                        @Override
                        public void onResponse(Call<List<Aluno>> call, Response<List<Aluno>> response) {
                            if (response.body().isEmpty())
                                Toast.makeText(getBaseContext(), "Esse aluno não existe", Toast.LENGTH_SHORT).show();
                            else {
                                List<Aluno> l = response.body();
                                Aluno al = l.get(0);
                                ra.setText(al.getRa());
                                nome.setText(al.getNome());
                                email.setText(al.getEmail());
                            }
                        }
                        @Override
                        public void onFailure(Call<List<Aluno>> call, Throwable t) {
                            Toast.makeText(getBaseContext(), "Erro ao buscar aluno: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(getBaseContext(), "Insira um RA válido", Toast.LENGTH_SHORT).show();
            }
        });

        incluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String r = ra.getText().toString();
                String n = nome.getText().toString();
                String e = email.getText().toString();

                if (!r.equals("") && !n.equals("") && !e.equals("") && e.contains("@") && e.contains(".")) {
                    if (!existe(r)) {
                        Call<Void> c = retrofitConfig.getAlunoService().cadastrarAluno(r, n, e);
                        c.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getBaseContext(), "Erro ao incluir aluno: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                        Toast.makeText(getBaseContext(), "Esse aluno já existe", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getBaseContext(), "Insira os dados corretamente", Toast.LENGTH_LONG).show();

                ra.setText("");
                nome.setText("");
                email.setText("");
            }
        });

        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String r = ra.getText().toString();

                if (!r.equals("")) {
                    if (existe(r))
                    {
                        Call<Void> c = retrofitConfig.getAlunoService().excluirAluno(r);
                        c.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getBaseContext(), "Erro ao excluir aluno: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                        Toast.makeText(getBaseContext(), "Esse aluno não existe", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getBaseContext(), "Insira um RA válido", Toast.LENGTH_LONG).show();

                ra.setText("");
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String r = ra.getText().toString();
                String n = nome.getText().toString();
                String e = email.getText().toString();

                if (!r.equals("") && ((n.equals("") || e.equals("")) || (!n.equals("") && !e.equals("")))) {
                    if (existe(r)) {
                        Call<Void> c = retrofitConfig.getAlunoService().editarAluno(r, n, e);
                        c.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getBaseContext(), "Erro ao editar aluno: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                        Toast.makeText(getBaseContext(), "Esse aluno não existe", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getBaseContext(), "Insira os dados corretamente", Toast.LENGTH_LONG).show();

                ra.setText("");
                nome.setText("");
                email.setText("");
            }
        });
    }

    private boolean existe(String nRa)
    {
        Call<List<Aluno>> al = retrofitConfig.getAlunoService().pegarAluno(nRa);
        al.enqueue(new Callback<List<Aluno>>() {
            @Override
            public void onResponse(Call<List<Aluno>> call, Response<List<Aluno>> response) {
                if (response.body().isEmpty())
                    achou = false;
                else
                    achou = true;
            }

            @Override
            public void onFailure(Call<List<Aluno>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Erro ao buscar RA: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return achou;
    }
}
