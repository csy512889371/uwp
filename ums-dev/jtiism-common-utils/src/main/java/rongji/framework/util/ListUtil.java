package rongji.framework.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/8/17.
 */
public class ListUtil {

    public static List<Map<String,String>> ListChange(List<Map<String,Object>> orignList){
        List<Map<String,String>> returnList = new ArrayList<>();
        orignList.forEach(p->{
            Map<String,String> returnMap = new HashMap<>();
            p.forEach((k,v)->{
                returnMap.put(k,v==null?null:v.toString());

            });
            returnList.add(returnMap);
        });
        return returnList;
    }
}
