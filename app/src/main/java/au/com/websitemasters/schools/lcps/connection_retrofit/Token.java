package au.com.websitemasters.schools.lcps.connection_retrofit;


public class Token {

    public Token() {}

    //@SerializedName("device_token")
    public String device_token;

    Token(String device_token){

        this.device_token = device_token;
    }

}
