package capstone.backend;

public class GithubResponseException extends RuntimeException {
    public GithubResponseException(String message) {
        super(message);
    }
}
