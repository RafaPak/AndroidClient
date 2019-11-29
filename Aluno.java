package cotuca.unicamp.clienteandroid;

public class Aluno implements Cloneable
{
    private String ra;
    private String nome;
    private String email;
 
    public void setRa (String ra) throws Exception
    {
        if (ra == null || ra.equals("") || ra.length() != 5)
            throw new Exception ("RA inválido");

        this.ra = ra;
    }   

    public void setNome (String nome) throws Exception
    {
        if (nome == null || nome.equals(""))
            throw new Exception ("Nome inválido");

        this.nome = nome;
    }

    public void setEmail (String email) throws Exception
    {
        if (email == null || email.equals("") || !email.contains("@") || !email.contains("."))
            throw new Exception ("E-mail inválido");

        this.email = email;
    }
 
    public String getRa ()
    {
        return this.ra;
    }

    public String getNome ()
    {
        return this.nome;
    }

    public String getEmail ()
    {
        return this.email;
    }

    public Aluno (String ra, String nome, String email) throws Exception
    {
        this.setRa(ra);
        this.setNome(nome);
        this.setEmail(email);
    }

    public String toString ()
    {
        String ret = "";

        ret += "RA: " + this.ra + "\n";
        ret += "Nome: " + this.nome + "\n";
        ret += "E-mail: " + this.email;
        
        return ret;
    }

    public boolean equals (Object obj)
    {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof Aluno))
            return false;

        Aluno al = (Aluno)obj;

        if (this.ra.equals(al.ra))
            return false;

        if (this.nome.equals(al.nome))
            return false;

        if (this.email.equals(al.email))
            return false;

        return true;
    }

    public int hashCode ()
    {
        int ret = 2;

        ret = 3*ret + this.ra.hashCode();
        ret = 3*ret + this.nome.hashCode();
        ret = 3*ret + this.email.hashCode();

        return ret;
    }


    public Aluno (Aluno modelo) throws Exception
    {
        this.ra = modelo.ra;
        this.nome = modelo.nome;
        this.email = modelo.email;             
    }

    public Object clone ()
    {
        Aluno ret = null;

        try
        {
            ret = new Aluno (this);
        }
        catch (Exception erro)
        {}

        return ret;
    }
}