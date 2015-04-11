package in.savegenie.savegenie.responses;

public class LoginResponse {
	public String result;
    public String lname;
    public String fname;

	public LoginResponse(String result,String fname,String lname) {
		this.result = result;
        this.lname = lname;
        this.fname = fname;
	}
}
