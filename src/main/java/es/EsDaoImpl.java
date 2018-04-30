package es;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import io.searchbox.indices.DeleteIndex;
import model.esmodel.EsBackInfo;
import model.esmodel.EsWarc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EsDaoImpl implements EsDao {
    static protected final Log log = LogFactory.getLog(EsDaoImpl.class.getName());

    private static JestClient client;
    static{
        try{
            client = MyClient.getClient();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    /**
     *
     * @param type ：当前删除document名称
     * @return
     */
    public JestResult deleteIndex(String type) {
        DeleteIndex deleteIndex = new DeleteIndex.Builder(type).build();
        JestResult result = null ;
        try {
            result = client.execute(deleteIndex);
            log.info("deleteIndex == " + result.getJsonString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result ;
    }

    /**
     *
     * @param script
     * @param index ：文档在哪存放
     * @param type ： 文档表示的对象类别
     * @param id ：文档唯一标识
     * @return
     */
    public JestResult updateDocument(String script, String index, String type, String id) {
        Update update = new Update.Builder(script).index(index).type(type).id(id).build();
        JestResult result = null ;
        try {
            result = client.execute(update);
            log.info("updateDocument == " + result.getJsonString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result ;
    }

    /**
     *
     * @param index ：文档在哪存放
     * @param type ： 文档表示的对象类别
     * @param id ：文档唯一标识
     * @return
     */
    public JestResult deleteDocument(String index, String type, String id) {
        Delete delete = new Delete.Builder(id).index(index).type(type).build();
        JestResult result = null ;
        try {
            result = client.execute(delete);
            log.info("deleteDocument == " + result.getJsonString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param index
     * @param type
     * @param params
     * @return
     */
    public JestResult deleteDocumentByQuery(String index, String type, String params) {
        DeleteByQuery db = new DeleteByQuery.Builder(params)
                .addIndex(index)
                .addType(type)
                .build();

        JestResult result = null ;
        try {
            result = client.execute(db);
            log.info("deleteDocument == " + result.getJsonString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param object ：返回对象
     * @param index ：文档在哪存放
     * @param type ： 文档表示的对象类别
     * @param id ：文档唯一标识
     * @param <T>
     * @return
     */
    public <T> JestResult getDocument(T object , String index, String type, String id) {
        Get get = new Get.Builder(index, id).type(type).build();
        JestResult result = null ;
        try {
            result = client.execute(get);
            T o = (T) result.getSourceAsObject(object.getClass());
            for (Method method : o.getClass().getMethods()) {
                log.info("getDocument == " + method.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     *
     * @param index
     * @return map-String or list<warc>
     */
    public Map searchAll(String index) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(index)
                .build();
        SearchResult result = null ;
        List<?> hits = null ;
        Map map = new HashMap();
        try {
            result = client.execute(search);
            System.out.println("本次查询共查到："+result.getTotal()+"个关键字！");
            log.info("本次查询共查到："+result.getTotal()+"个关键字！");
            JsonObject jsonObject = result.getJsonObject();
            JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
            List<EsWarc> warcList = this.suggestForTransferHits(hitsobject);
            long took = jsonObject.get("took").getAsLong();
            long total = hitsobject.get("total").getAsLong();

            map.put("took",took);
            map.put("total",total);
            map.put("warcList",warcList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map ;
    }

    /**
     *
     * @param keyWord ：搜索关键字
     * @param fields
     * @return
     */
    public Map createSearch(String keyWord, String index, String... fields) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.queryStringQuery(keyWord));
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(index).build();
        SearchResult result = null ;
        List<?> hits = null ;
        Map map = new HashMap();
        try {
            result = client.execute(search);
            JsonObject jsonObject = result.getJsonObject();
            JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
            Long total = hitsobject.get("total").getAsLong();
            List<EsWarc> warcList = this.suggestForTransferHits(hitsobject);
            map.put("total",total);
            map.put("warcList",warcList);
            System.out.println("本次查询共查到："+result.getTotal()+"个结果！");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 用于把hits内容转换成Eswarc格式列表
     * @param hitsjson
     * @return
     */
    public List<EsWarc> suggestForTransferHits(JsonObject hitsjson){

        List<EsBackInfo> allInfoList = new LinkedList<EsBackInfo>();//专门装返回的所有数据,备用
        List<EsWarc> warcList = new LinkedList<EsWarc>();//专门装warc部分数据
        Gson gson = new Gson();
        Map tempmap = gson.fromJson(hitsjson,Map.class);
        //Gson解析
        List<Map> hitsList = (List<Map>) tempmap.get("hits");
        for (Map map:hitsList){
            EsBackInfo esBackInfo = new EsBackInfo();
            EsWarc esWarc = new EsWarc();
            esBackInfo.setId((String) map.get("_id"));
            esBackInfo.setIndex((String) map.get("_index"));
            esBackInfo.setType((String) map.get("_type"));
            esBackInfo.setScore((Double) map.get("_score"));
            esBackInfo.setSource((Map<String, Object>) map.get("_source"));
            allInfoList.add(esBackInfo);

            Map<String,Object > esmap = esBackInfo.getSource();
            esWarc.setMimeType((String)esmap.get("mimeType"));
            esWarc.setHeaderFields((String)esmap.get("headerFields"));
            esWarc.setHeaderWarcType((String)esmap.get("headerWarcType"));
            esWarc.setContent((String)esmap.get("content"));
            esWarc.setWarcUrl((String)esmap.get("warcUrl"));
            esWarc.setUpdateTime((String)esmap.get("updateTime"));
            esWarc.setResponseUrl((String)esmap.get("responseUrl"));
            if (esmap.get("create_at")!=null)
                esWarc.setCreate_at(Math.round((Double) esmap.get("create_at")));
            if (esmap.get("update_at")!=null)
                esWarc.setUpdate_at(Math.round((Double) esmap.get("update_at")));
            esWarc.setId(esBackInfo.getId());
            warcList.add(esWarc);
        }

        return warcList;
    }

    public static void main(String[] args) {
        EsDao esDao = new EsDaoImpl();
        String index = "warcs";
        String type = "warc";
        String keyword = "test";
        JestResult result = esDao.getDocument(Map.class,index,type,"1524555782845");
        System.out.println(result);
        /*for (SearchResult.Hit<EsWarc,Void> hit:result) {
            System.out.println(hit);
        }
        System.out.println(result);*/
    }
}
