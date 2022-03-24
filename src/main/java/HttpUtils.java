import okhttp3.*;

import java.io.IOException;

/**
 * 我这里用的同步请求，如果你要写安卓，请用异步，或者把同步请求放在子线程中进行
 * **/
public class HttpUtils {
    private static OkHttpClient client=new OkHttpClient();
    private static Request.Builder requestBuilder=new Request.Builder();
    private static final String userAgent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.82 Safari/537.36";

    //这里cookies请写死，因为b站可能会乱动你的cookies，所以不要用okhttp的cookies自动管理
    public static String get(String url,String cookies){
        Request request=requestBuilder.url(url).header("Cookie",cookies).header("User-Agent",userAgent).build();
        try{
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                return response.body().string();
            }else {
                System.out.println("请求失败");//自己设计如何处理异常
            }
        }catch (IOException e){
            System.out.println("请求失败");//自己设计如何处理异常
        }
        return null;
    }

    //我习惯传入String的data，你喜欢别的数据类型比如json也可以
    public static String post(String url,String data,String cookies){
        Request request=requestBuilder.url(url).header("Cookie",cookies).header("User-Agent",userAgent).
                post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),data)).build();
        try{
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                return response.body().string();
            }else {
                System.out.println("请求失败");//自己设计如何处理异常
            }
        }catch (IOException e){
            System.out.println("请求失败");//自己设计如何处理异常
        }
        return null;
    }
}
