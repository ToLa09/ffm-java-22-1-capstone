package neuefische.capstone.camunda;

public class GithubResponseException extends RuntimeException {
    public GithubResponseException(String message) {
        super(message);
    }
}
