public class OtherUtils {
    public static String getCsrf(String cookies){
        return cookies.split("bili_jct=")[1].split(";")[0];
    }
}
