package rongji.cmis.job.redis;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import rongji.redis.core.RedisService;

@Service("redisTask")
public class RedisTask {
	
	
	@Resource(name = "redisServiceImpl")
	private RedisService redisService;
	
	/**
	 * 
	 * @Title: redis 心跳
	 * @return
	 */
	@Scheduled(cron = "${job.redis_islive.cron}")
	public void redisIslive() {
		
		if (redisService.isLive()) {
			//可用
			redisService.setCanNotUseFlag(false);
		} else {
			//不可用
			redisService.setCanNotUseFlag(true);
		}
	}

}
