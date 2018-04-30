package es;


import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import model.esmodel.EsWarc;

import java.util.Date;

public class BaseCRUD{
    //类共享一个jestclient
    private static JestClient client;
    //warc用创建新文档，写死index和type
    public static void createDoc(EsWarc esWarc)throws Exception{
        client = MyClient.getClient();
        Index index = new Index.Builder(esWarc)
                .index("warcs").type("warc")
                .id(esWarc.getAddTime())//添加时间即其时间戳
                .id(esWarc.getCreate_at().toString())
                .id(esWarc.getResponseUrl())//responce部分的url
                .id(esWarc.getUpdateTime())//更新时间及其时间戳
                .id(esWarc.getUpdate_at().toString())
                .id(esWarc.getWarcUrl())//生成warc的那个URL
                .id(esWarc.getContent())//warc全内容
                .id(esWarc.getHeaderWarcType())
                .id(esWarc.getHeaderFields())
                .id(esWarc.getMimeType())
                .build();
//        System.out.println("到达此处");
        client.execute(index);
        client.shutdownClient();//在用完之后关闭
    }

    /*public static void main(String[] args) throws Exception{
        EsWarc esWarc = new EsWarc();
        esWarc.setAddTime(new Date().toString());
        esWarc.setCreate_at(new Date().getTime());
        esWarc.setResponseUrl("www.baidu.com");
        esWarc.setUpdateTime(new Date().toString());
        esWarc.setUpdate_at(new Date().getTime());
        esWarc.setWarcUrl("www.4399.com");
        esWarc.setContent("this is a test issue");
        esWarc.setHeaderWarcType("test issue header");
        esWarc.setHeaderFields("a,b,c");
        esWarc.setMimeType("Test");
        createDoc(esWarc);
    }*/

}
