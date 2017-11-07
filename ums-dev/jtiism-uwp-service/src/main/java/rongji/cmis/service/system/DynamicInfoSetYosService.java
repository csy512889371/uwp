package rongji.cmis.service.system;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DynamicInfoSetYosService {


    Set<String> getDistinctYear(List<Map<String, String>> yosInfoSetList);
}
