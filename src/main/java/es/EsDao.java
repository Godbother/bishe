package es;

import com.google.gson.JsonObject;
import io.searchbox.client.JestResult;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SuggestResult;

import java.util.List;
import java.util.Map;

public interface EsDao {
    /**
     * 删除索引
     * @param type ：当前删除document名称
     * @return
     */
    public JestResult deleteIndex(String type) ;

    /**
     * 更新Document
     * @param index ：文档在哪存放
     * @param type ： 文档表示的对象类别
     * @param id ：文档唯一标识
     */
    public JestResult updateDocument(String script , String index,String type,String id);

    /**
     * 删除Document
     * @param index ：文档在哪存放
     * @param type ： 文档表示的对象类别
     * @param id ：文档唯一标识
     * @return
     */
    public JestResult deleteDocument(String index,String type,String id) ;

    /**
     * 根据条件删除
     * @param index
     * @param type
     * @param params
     */
    public JestResult deleteDocumentByQuery(String index, String type, String params);


    /**
     * 获取Document
     * @param o ：返回对象
     * @param index ：文档在哪存放
     * @param type ： 文档表示的对象类别
     * @param id ：文档唯一标识
     * @return
     */
    public <T> JestResult getDocument(T o , String index , String type , String id) ;


    /**
     * 查询全部
     * @param index ：文档在哪存放
     * @return
     */
    public Map searchAll(String index);

    /**
     * 搜索
     * @param keyWord ：搜索关键字
     * @return
     */
    public Map createSearch(String keyWord , String index , String... fields) ;



}
