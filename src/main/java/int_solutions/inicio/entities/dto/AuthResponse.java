package int_solutions.inicio.entities.dto;

public class AuthResponse {
    private String message;
    private String token;
    private String groupName;


    public AuthResponse(String message, String token, String groupName) {
    this.message = message;
    this.token = token;
    this.groupName = groupName;
}

      public AuthResponse() {}

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getGroupName() { return groupName; }

    public void setGroupName(String groupName) { this.groupName = groupName;}
}
