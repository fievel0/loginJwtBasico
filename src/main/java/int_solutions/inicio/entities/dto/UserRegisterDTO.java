package int_solutions.inicio.entities.dto;

public class UserRegisterDTO {
    private String username;

    private String password;

    private String groupName;


    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getGroupName(){ return groupName; }

    public void setUsername(String username) { this.username = username;}

    public void setPassword(String password) { this.password = password;}

    public void setGroupName(String groupName) { this.groupName = groupName;}
}
