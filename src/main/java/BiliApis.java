import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import pojo.Account;

public class BiliApis {
    //登录取cookies自己解决，不会的话再问我
    //获取账号相关信息，确认cookies可以使用
    public static Account getAccountInfo(String cookies){
        String responseStr=HttpUtils.get("https://api.bilibili.com/x/web-interface/nav",cookies);
        JSONObject respongseJSON= JSON.parseObject(responseStr);
        if (respongseJSON.getInteger("code")!=0)return null;//这里也可以处理为抛出异常
        Account account=new Account(respongseJSON.getJSONObject("data").getString("mid"),
                respongseJSON.getJSONObject("data").getString("uname"));
        return account;
    }

    //根据uid获取用户的信息
    public static Account getOtherUserInfo(String uid){
        String responseStr=HttpUtils.get("https://api.bilibili.com/x/space/acc/info?mid="+uid,"");
        JSONObject respongseJSON= JSON.parseObject(responseStr);
        if (respongseJSON.getInteger("code")!=0)return null;//这里也可以处理为抛出异常
        Account account=new Account(uid,
                respongseJSON.getJSONObject("data").getString("name"));
        return account;
    }

    //点赞,这些参数都是抓包才知道的，在浏览评论的时候可以得到这些参数
    public static Boolean like(String cookies,String oid,String rpid,String type){
        String data="oid="+oid+"&type="+type+"&rpid="+rpid+"&action=1&ordering=heat&jsonp=jsonp&csrf="+OtherUtils.getCsrf(cookies);
        String responseStr=HttpUtils.post("https://api.bilibili.com/x/v2/reply/action",data,cookies);
        JSONObject respongseJSON= JSON.parseObject(responseStr);
        if (respongseJSON.getInteger("code")!=0)return Boolean.FALSE;//这里也可以处理为抛出异常
        return Boolean.TRUE;
    }

    //扣1
    public static Boolean reply1(String cookies,String oid,String rpid,String type){
        String data="oid="+oid+"&type="+type+"&root="+rpid+"&parent="+rpid+"&message=1&plat=1&ordering=heat&jsonp=jsonp&csrf="+OtherUtils.getCsrf(cookies);
        String responseStr=HttpUtils.post("https://api.bilibili.com/x/v2/reply/add",data,cookies);
        JSONObject respongseJSON= JSON.parseObject(responseStr);
        if (respongseJSON.getInteger("code")!=0)return Boolean.FALSE;//这里也可以处理为抛出异常
        return Boolean.TRUE;
    }

    //获取动态类型（有无图片）和对应的真实动态id（oid）{"oid":oid,"type":type}
    public static JSONObject getOidAndType(Long dynamicId){
        String responseStr=HttpUtils.get("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/get_dynamic_detail?dynamic_id="+dynamicId,"");
        JSONObject respongseJSON= JSON.parseObject(responseStr);
        if (respongseJSON.getInteger("code")!=0)return null;//这里也可以处理为抛出异常
        JSONObject jsonObject=new JSONObject();
        Integer type=respongseJSON.getJSONObject("data").getJSONObject("card").getJSONObject("desc").getInteger("type");
        jsonObject.put("type",type);
        if (type==4){
            jsonObject.put("oid",dynamicId);
        }else{
            jsonObject.put("oid",respongseJSON.getJSONObject("data").getJSONObject("card").getJSONObject("desc").getLong("rid"));
        }
        return jsonObject;
    }

    //前两个参数通过上面的方法获得，第三个参数为页数，需要通过这个方法获取,第一页的next为0,返回{"next":next,replay:[replayArray]}
    public static JSONObject getReplayList(Integer type,Long oid,Integer next){
        String responseStr=HttpUtils.get("https://api.bilibili.com/x/v2/reply/main?type="
                +type.toString()+"&oid="+oid.toString()+"&next="+next.toString()+"&mode=2","");//这里的cookies酌情处理，请根据自己的程序设计思路处理
        JSONObject respongseJSON= JSON.parseObject(responseStr);
        if (respongseJSON.getInteger("code")!=0)return null;//这里也可以处理为抛出异常
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("next",respongseJSON.getJSONObject("data").getJSONObject("cursor").getInteger("next"));
        jsonObject.put("replay",respongseJSON.getJSONObject("data").getJSONArray("replies"));
        return jsonObject;
    }
}
