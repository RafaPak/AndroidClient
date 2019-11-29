package cotuca.unicamp.clienteandroid;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {
    Retrofit retrofit;

    public  RetrofitConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://177.220.18.111:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public Conexao getAlunoService() {
        return this.retrofit.create(Conexao.class);
    }
}
