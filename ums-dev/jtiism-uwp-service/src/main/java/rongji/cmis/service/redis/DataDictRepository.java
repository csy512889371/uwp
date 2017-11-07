package rongji.cmis.service.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.hash.BeanUtilsHashMapper;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.stereotype.Component;
import rongji.framework.base.pojo.ZtreeDictNote;
import rongji.framework.util.SettingUtils;
import rongji.redis.core.RedisService;
import rongji.redis.core.utils.KeyUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 
 * @Title: DataDictRepository.java
 * @Package rongji.cmis.service.redis
 * @Description: 标准代码 缓存
 * @author nick
 * @date 2015年12月22日 上午10:08:10
 * @version V1.0
 */
@Component("dataDictRepository")
public class DataDictRepository {

	private static final Logger logger = LoggerFactory.getLogger(DataDictRepository.class);

	@Resource(name = "stringRedisTemplate")
	private StringRedisTemplate template;

	@Resource(name = "redisServiceImpl")
	private RedisService redisService;

	private final HashMapper<ZtreeDictNote, String, String> dataDictMapper = new DecoratingStringHashMapper<>(new BeanUtilsHashMapper<>(ZtreeDictNote.class));



	/**
	 * 将 dataDict list 放入 reids
	 */
	public void setDataDictList(String codeTable, List<ZtreeDictNote> dataDictList) {
		logger.debug("set Data Dict List");

		if (redisService.canNotUse(SettingUtils.get().isNeedRedis())) {
			return;
		}

		// 1. 清除下原来的
		deleteByCodeTable(codeTable);

		for (ZtreeDictNote dict : dataDictList) {
			setDataDict(dict, codeTable);
			setDataDictCode(dict, codeTable);
		}

	}

	/**
	 * 获取dataDict列表
	 * 
	 * @Title: convertToDictList
	 */
	public List<ZtreeDictNote> getDataDictList(String codeTable) {

		if (redisService.canNotUse(SettingUtils.get().isNeedRedis())) {
			return null;
		}

		String key = KeyUtils.dataDictCodes(codeTable);
		String pid = KeyUtils.getDict() + codeTable + ":*->";
		final String code = "code";
		final String dmGrp = "dmGrp";
		final String codeName = "codeName";
		final String codeAbr1 = "codeAbr1";
		final String codeAbr2 = "codeAbr2";
		final String supCode = "supCode";
		final String codeSpelling = "codeSpelling";
		final String inino = "inino";
		final String codeAName = "codeAName";
		final String isCommon = "isCommon";
		final String codeNameSuffix = "codeNameSuffix";

		SortQuery<String> query = SortQueryBuilder.sort(key).noSort().get(pid + code).get(pid + dmGrp).get(pid + codeName).get(pid + codeAbr1).get(pid + codeAbr2)
				.get(pid + supCode).get(pid + codeSpelling).get(pid + inino).get(pid + codeAName).get(pid + isCommon).get(pid + codeNameSuffix).build();
		BulkMapper<ZtreeDictNote, String> hm = bulk -> {
            Map<String, String> map = new LinkedHashMap<>();
            Iterator<String> iterator = bulk.iterator();

            map.put(code, iterator.next());
            map.put(dmGrp, iterator.next());
            map.put(codeName, iterator.next());
            map.put(codeAbr1, iterator.next());
            map.put(codeAbr2, iterator.next());
            map.put(supCode, iterator.next());
            map.put(codeSpelling, iterator.next());
            map.put(inino, iterator.next());
            map.put(codeAName, iterator.next());
            map.put(isCommon, iterator.next());
            map.put(codeNameSuffix, iterator.next());
            return dataDictMapper.fromHash(map);
        };

		List<ZtreeDictNote> dataDictList = template.sort(query, hm);

		Collections.sort(dataDictList, Comparator.comparing(ZtreeDictNote::getInino));

		return dataDictList;
	}



	/**
	 * 根据 codeTable 删除
	 */
	public void deleteByCodeTable(String codeTable) {

		if (redisService.canNotUse(SettingUtils.get().isNeedRedis())) {
			return;
		}

		List<String> dictCodes = getCodes(codeTable);
		if (dictCodes == null || dictCodes.isEmpty()) {
			return;
		}

		for (String code : dictCodes) {
			redisService.del(KeyUtils.dataDict(codeTable, code));
		}
		redisService.del(KeyUtils.dataDictCodes(codeTable));
	}

	/**
	 * 
	 * set DataDict 对象放入redis
	 */
	private void setDataDict(ZtreeDictNote dict, String codeTable) {
		dataDict(codeTable, dict.getCode()).putAll(dataDictMapper.toHash(dict));
	}

	/**
	 * 
	 * 将字段的主建 code 放入 dataDictCodes list
	 */
	private void setDataDictCode(ZtreeDictNote dict, String codeTable) {
		dataDictCodes(codeTable).addLast(dict.getCode());
	}

	/**
	 * 
	 * 获取所有的 codes
	 */
	private List<String> getCodes(String codeTable) {
		return dataDictCodes(codeTable).range(0, -1);
	}

	private RedisMap<String, String> dataDict(String codeTable, String code) {
		return new DefaultRedisMap<>(KeyUtils.dataDict(codeTable, code), template);
	}

	private RedisList<String> dataDictCodes(String codeTable) {
		return new DefaultRedisList<>(KeyUtils.dataDictCodes(codeTable), template);
	}

	public ZtreeDictNote getDataDict(String codeTable, String code) {
		if (redisService.canNotUse(SettingUtils.get().isNeedRedis())) {
			return null;
		}
		if (code != null) {
			code = code.trim();
		}
		RedisMap<String, String> redisMap = this.dataDict(codeTable, code);
		ZtreeDictNote dictNote = dataDictMapper.fromHash(redisMap);
		if (dictNote == null || StringUtils.isEmpty(dictNote.getCode())) {
			return null;
		}
		return dictNote;
	}

	/**
	 * 添加
	 * 
	 * @Title: addDataDict
	 */
	public void addDataDict(ZtreeDictNote dataDict, String codeTable) {

		if (redisService.canNotUse(SettingUtils.get().isNeedRedis())) {
			return;
		}
		setDataDict(dataDict, codeTable);
		setDataDictCode(dataDict, codeTable);
	}

	/**
	 * 添加
	 * 
	 * @Title: updateDataDict
	 */
	public void updateDataDict(ZtreeDictNote dataDict, String codeTable) {

		if (redisService.canNotUse(SettingUtils.get().isNeedRedis())) {
			return;
		}
		dataDict(codeTable, dataDict.getCode()).clear();
		setDataDict(dataDict, codeTable);
	}

	/**
	 * 删除
	 * 
	 * @param codeTable
	 * @param id
	 */
	public void deleteDataDict(String codeTable, String id) {

		if (redisService.canNotUse(SettingUtils.get().isNeedRedis())) {
			return;
		}

		redisService.del(KeyUtils.dataDict(codeTable, id));
		redisService.del(KeyUtils.dataDictCodes(codeTable));
	}

	public boolean canNotUse() {
		return redisService.canNotUse(SettingUtils.get().isNeedRedis());
	}

}
