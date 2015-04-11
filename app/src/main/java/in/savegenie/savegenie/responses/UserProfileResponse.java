package in.savegenie.savegenie.responses;

/**
 * Created by manish on 10/4/15.
 */
public class UserProfileResponse
{
        public String id;
        public String title;
        public String fname;
        public String lname;
        public String city;
        public String area;
        public String pincode;
        public String phone;
        public String email;
        public String userdetailid;

        public UserProfileResponse(String id,String title,String fname,String lname,String city,String area,String pincode,String phone,String email,String userdetailid) {
            this.id=id;
            this.title = title;
            this.fname = fname;
            this.lname = lname;
            this.city = city;
            this.area = area;
            this.pincode = pincode;
            this.phone = phone;
            this.email = email;
            this.userdetailid = userdetailid;
        }
}
