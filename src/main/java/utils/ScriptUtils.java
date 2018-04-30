package utils;

import model.esmodel.EsWarc;

import java.text.SimpleDateFormat;

public class ScriptUtils {
    //本类用于生成各种需要的字符串脚本

    //生成warc文件下的doc json
    public static String getWarcScript(EsWarc esWarc){
        String script = "{" +
                "    \"doc\" : {" +
                "        \"addTime\" : \""+esWarc.getAddTime()+"\"," +
                "        \"updateTime\" : \""+esWarc.getUpdateTime()+"\"," +
                "        \"warcUrl\" : \""+esWarc.getWarcUrl()+"\"," +
                "        \"responseUrl\" : \""+esWarc.getResponseUrl()+"\"," +
                "        \"create_at\" : \""+esWarc.getCreate_at()+"\"," +
                "        \"update_at\" : \""+esWarc.getUpdate_at()+"\"" +
                "        \"mimeType\" : \""+esWarc.getMimeType()+"\"" +
                "        \"headerFields\" : \""+esWarc.getHeaderFields()+"\"" +
                "        \"headerWarcType\" : \""+esWarc.getHeaderWarcType()+"\"" +
                "        \"content\" : \""+esWarc.getContent()+"\"" +
                "    }" +
                "}";
        return script;
    }
}
