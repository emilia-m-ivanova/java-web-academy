package util;

public class InputUtils {
    public static boolean usernameValidator(String username){
        return username.matches("^[a-zAZ]{2,15}$");
    }

    public static boolean emailValidator(String email){
        return email.matches("^[\\w]+@[\\w]+\\.[a-z]+$");
    }

    public static boolean genderValidator(String gender){
        return gender.equals("MALE") || gender.equals("FEMALE");
    }

}
