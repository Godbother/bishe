import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import model.esmodel.EsWarc;
import model.MyWarcRecord;
import org.archive.io.ArchiveRecordHeader;
import org.archive.io.warc.WARCReader;
import org.archive.io.warc.WARCReaderFactory;
import org.archive.io.warc.WARCRecord;
public class MyReader {
    /**
     * java中使用jar包提供的原生WARCRecord类来保存warc文件的信息，本类为读取到封装
     */
    /**
     * @author fei
     * @param filepath
     * @return MyWarcRecord
     * @tips 直接传入warc文件的本地路径，非大数据使用(本地转es数据)
     */
    public static EsWarc getMyRecord(String filepath) throws Exception{
        StringBuffer headerWarcType = new StringBuffer();
        StringBuffer headerFields = new StringBuffer();
        StringBuffer url = new StringBuffer();
        Long len = 0L;
        StringBuffer mimeType = new StringBuffer();
        StringBuffer recordIdentifier = new StringBuffer();
        StringBuffer allContent = new StringBuffer();
        StringBuffer httpContent = new StringBuffer();

        MyWarcRecord myWarcRecord = new MyWarcRecord();
        WARCReader warcreader = WARCReaderFactory
                .get(filepath);
        for (final Iterator i = warcreader.iterator(); i.hasNext();) {
            WARCRecord ar = (WARCRecord) i.next();
            ArchiveRecordHeader arh = ar.getHeader();
            if (arh.getHeaderValue("WARC-Type").equals("response")) {
                long length = arh.getLength();
                byte[] content = new byte[(int) length];
                ar.read(content);
                String strContent = new String(content,0,(int) length);
                len += length;
                headerFields.append(arh.getHeaderFields().toString());
                headerWarcType.append(arh.getHeaderValue("WARC-Type").toString());
                url.append(arh.getUrl());
                mimeType.append(arh.getMimetype());
                recordIdentifier.append(arh.getRecordIdentifier());
                allContent.append(strContent);
                httpContent.append(getHttpContent(strContent));
            }
        }
        myWarcRecord.setHeaderWarcType(headerWarcType.toString());
        myWarcRecord.setHeaderFields(headerFields.toString());
        myWarcRecord.setUrl(url.toString());
        myWarcRecord.setLength(len);
        myWarcRecord.setMimeType(mimeType.toString());
        myWarcRecord.setRecordIdentifier(recordIdentifier.toString());
        myWarcRecord.setContent(allContent.toString());
        myWarcRecord.setHttpContent(getHttpContent(httpContent.toString()));

        return transferToEs(myWarcRecord);
    }

    /**
     * fei
     * @param strContent
     * @return coding-name
     */
    private static String getHttpContent(String strContent){
        StringBuffer httpcontent = new StringBuffer();
        int nNN = strContent.indexOf("\n\n");
        if( nNN > 0 )
        {
            String strBody = strContent.substring( nNN + 2 );
            httpcontent.append("<body>");
            httpcontent.append(strBody);
        }
        else
        {
            nNN = strContent.indexOf("\n\r\n");
            if( nNN > 0 )
            {
                String strBody = strContent.substring( nNN + 3 );
                httpcontent.append("</body>");
                httpcontent.append(strBody);
            }
            else
            {
                httpcontent.append(nNN);
                httpcontent.append(strContent);
            }
        }
        return httpcontent.toString();
    }


    private static EsWarc transferToEs(MyWarcRecord myWarcRecord)throws Exception{
        EsWarc esWarc = new EsWarc();
        esWarc.setResponseUrl(myWarcRecord.getUrl());
        esWarc.setAddTime(new Date().toString());
        esWarc.setHttpContent(myWarcRecord.getHttpContent());
        esWarc.setHeaderFields(myWarcRecord.getHeaderFields());
        esWarc.setHeaderWarcType(myWarcRecord.getHeaderWarcType());
        esWarc.setMimeType(myWarcRecord.getMimeType());
        esWarc.setContent(myWarcRecord.getContent());
        esWarc.setRecordIdentifier(myWarcRecord.getRecordIdentifier());

        //下面这个统一判断是否已经保存好了addtime
        if (esWarc.getAddTime()==null) {
            Date date = new Date();
            esWarc.setCreate_at(date.getTime());
            esWarc.setUpdate_at(date.getTime());
            esWarc.setUpdateTime(date.toString());
            esWarc.setAddTime(date.toString());
        }else {
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(esWarc.getAddTime());
            esWarc.setUpdateTime(esWarc.getAddTime());
            esWarc.setCreate_at(date.getTime());
            esWarc.setUpdate_at(date.getTime());
        }

        return esWarc;
    }


    public static void main(String[] args) throws Exception{
        EsWarc esWarc = getMyRecord("G:/jp/recording-session-20171215134946.warc");
        System.out.println(esWarc.getHeaderFields());
    }
}


        /*public static void main(String[] args) throws MalformedURLException, IOException {
        WARCReader warcreader = WARCReaderFactory
                .get("G:/jp/recording-session-20171215134946.warc");
        for (final Iterator i = warcreader.iterator(); i.hasNext();) {
            WARCRecord ar = (WARCRecord) i.next();
            ArchiveRecordHeader arh = ar.getHeader();
//            System.out.println(arh);
//            System.out.println(arh.getUrl());
//            System.out.println(arh.getLength());
//            System.out.println(arh.getMimetype());
//            System.out.println(arh.getRecordIdentifier());
//            System.out.println(arh.getHeaderValue("WARC-Type"));
//            System.out.println(arh.getHeaderFields());
            if( arh.getHeaderValue("WARC-Type").equals("response"))
            {
//                System.out.println(arh.getHeaderValue("WARC-Type"));
//                System.out.println(arh.getHeaderFields());
                long length = arh.getLength();
                byte[] content = new byte[(int) length];
                ar.read(content);
                String strContent = new String(content,0,(int) length);//编码需要获取后填入
                int nNN = strContent.indexOf("\n\n");
                if( nNN > 0 )
                {
                    String strBody = strContent.substring( nNN + 2 );
                    System.out.println("------------ body ------------");
                    System.out.println( strBody );
                }
                else
                {
                    nNN = strContent.indexOf("\n\r\n");
                    if( nNN > 0 )
                    {
                        String strBody = strContent.substring( nNN + 3 );
                        System.out.println("------------ body \\r\\n------------");
                        System.out.println( strBody );
                    }
                    else
                    {
                        System.out.println(nNN);
                        System.out.println(strContent);
                    }
                }
//                System.out.println(arh.getLength());
//                System.out.println(coding);
//                System.out.println(strContent);
//                System.out.println(ar.CONTENT_TYPE);
            }
        }

    }*/
