package cn.hellohao.quartz.job;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

//@Component
//@EnableScheduling
public class ThirdJob {

	public void task() {
		System.out.println("任务3执行....当前时间为" + dateFormat().format(LocalDateTime.now()));
	}

	private DateTimeFormatter dateFormat() {
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("HH:mm:ss");
		return formatter;
	}
}
