package cotuca.unicamp.clienteandroid;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Conexao {
    @GET("pegarAlunos")
    Call<ArrayList<Aluno>> pegarAlunos();

    @GET("pegarAlunos/{ra}")
    Call<List<Aluno>> pegarAluno(@Path("ra") String ra);

    @FormUrlEncoded
    @POST("cadastrarAluno")
    Call<Void> cadastrarAluno(@Field("ra")String ra,
                               @Field("nome") String nome,
                               @Field("email") String email);

    @FormUrlEncoded
    @POST("editarAluno")
    Call<Void> editarAluno(@Field("ra") String ra,
                            @Field("nome") String nome,
                            @Field("email") String email);

    @DELETE("excluirAluno/{ra}")
    Call<Void> excluirAluno(@Path("ra") String ra);
}
