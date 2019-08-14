package com.ratwareid.letschallenge;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Constant {

    public final static String rootEmail  = "jerryerlangga82@gmail.com";
    public static String loginemail;

    public static String getLoginemail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getEmail();
    }

    public static void setLoginemail(String loginemail) {
        Constant.loginemail = loginemail;
    }
}
