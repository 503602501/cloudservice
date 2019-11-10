import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.SpringBootMain;
import com.async.ScheduleTask;
import com.crawler.HotVideoListHandler;
import com.feigua.login.LoginService;
import com.mybatisplus.dao.domain.WordTask;
import com.mybatisplus.dao.mapper.HotVideoMapper;
import com.mybatisplus.dao.mapper.WordTaskMapper;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootMain.class)
public class DyKeyword {
	
	@Autowired
	private WordTaskMapper wordTaskMapper ;
	
	@Autowired
	HotVideoListHandler webDataHandler;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	private HotVideoMapper hotVideoMapper;
	
	@Autowired
	private ScheduleTask scheduleTask;
	
	@Test
	public void name() throws Exception {
		
	/*	hotVideoMapper.selectAll();
		HotVideo h = new HotVideo();
//		h.setId(123);
		h.setTitle("abc");
		h.setVideoUrl("aidu.com");
		h.setBloggerId("aa");
		h.setCreateDate(new Date());
//		hotVideoMapper.insertHotVideo(h);
		Integer i = hotVideoMapper.insert(h);
		System.out.println(i);*/
		
//		loginService.login();
//		webDataHandler.startWork();
		
//		scheduleTask.task();
//		hotVideoMapper.updateRepeat("2674884");
	}
	
	@Test
	public void addKeyWrod() {
		
		
//		List<String> list = Arrays.asList( "化妆品","面膜","手表","vlog");
		
		//0今天到 -> 今天+9
		for (int i = 0; i < 1; i++) {
			List<String> list = Arrays.asList("测试"); //化妆品 ， 面膜，手表  70周年
			WordTask task = new WordTask();
			for (String keyword : list) {
				task = new WordTask();
				task.setAccount("13535242305");
				task.setCreateDate(new Date());
				task.setKeyword(keyword);
				task.setStatus(0);
				task.setTaskTime( getDate(i) );
				task.setUpdateDate(new Date());
				wordTaskMapper.insert(task);
			}
		}
	}
	
	private static Date getDate(int i ){
		Date date = new Date();
		Calendar c = Calendar.getInstance();  
		c.setTime(date);  
		c.set(Calendar.HOUR_OF_DAY, 6);
		c.add(Calendar.DAY_OF_MONTH, i);
		
		return c.getTime();
		
	}
	
	
}
