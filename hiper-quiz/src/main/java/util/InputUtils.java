package util;

public class InputUtils {
    public static boolean usernameValidator(String username){
        return username.matches("^[a-zAZ]{2,15}$");
    }

    public static boolean emailValidator(String email){
        return email.matches("^[\\w]+@[\\w]+\\.[a-z]+$");
    }

    public static boolean genderValidator(String gender){
        return gender.equals("M") || gender.equals("F");
    }

    public static boolean passwordValidator(String email){
        return email.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,15}$");
    }

}
