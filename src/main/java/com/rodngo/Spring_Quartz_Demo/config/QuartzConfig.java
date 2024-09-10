package com.rodngo.Spring_Quartz_Demo.config;

import com.rodngo.Spring_Quartz_Demo.job.UpperCaseJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.HolidayCalendar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(UpperCaseJob.class)
            .withIdentity("upperCaseJob")
            .usingJobData("inputString", "hello world")
            .storeDurably()
            .build();
    }

    //cấu trúc Cron time: second minute hour day_of_month month day_of_week
    // theo phút: 0 0/1 * 1/1 * ? *
    // theo giờ : 0 55 23 9 9 ? 2024 ( có nghĩa là 23h55p ngày 9/9/2024 (dấu ? không xác định thứ trong tuần vì đã xác định rõ ngày tháng))
    @Bean
    public Trigger cronTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity("cronTrigger")
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *"))
            .build();
    }

    @Bean
    public HolidayCalendar holidayCalendar() {
        HolidayCalendar calendar = new HolidayCalendar();
        calendar.addExcludedDate(new java.util.Date());
        return calendar;
    }

    @Bean
    public Scheduler scheduler(Trigger cronTrigger, JobDetail jobDetail, HolidayCalendar holidayCalendar) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.addCalendar("holidays", holidayCalendar, false, false);
        scheduler.scheduleJob(jobDetail, cronTrigger);
        return scheduler;
    }
}
