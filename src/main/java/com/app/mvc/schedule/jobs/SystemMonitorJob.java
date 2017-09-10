package com.app.mvc.schedule.jobs;

import com.app.mvc.schedule.AbstractScheduleJob;
import com.sun.management.OperatingSystemMXBean;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import sun.management.ManagementFactoryHelper;

import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jimin on 16/4/23.
 */
@Slf4j
public class SystemMonitorJob extends AbstractScheduleJob {

    @Override
    public void schedule(JobExecutionContext context) throws JobExecutionException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n==========================通过java来获取相关系统状态========================== ");
        sb.append("\n总的内存量: " + (Runtime.getRuntime().totalMemory() / 1024));//Java 虚拟机中的内存总量,以字节为单位
        sb.append("\n空闲内存量: " + (Runtime.getRuntime().freeMemory() / 1024));//Java 虚拟机中的空闲内存量
        sb.append("\n最大内存量: " + (Runtime.getRuntime().maxMemory() / 1024));

        //获取操作系统相关信息
        sb.append("\n=============================获取操作系统相关信息============================ ");
        OperatingSystemMXBean osm = (OperatingSystemMXBean) ManagementFactoryHelper.getOperatingSystemMXBean();
        sb.append("\n" + osm.getFreeSwapSpaceSize() / 1024);
        sb.append("\n" + osm.getFreePhysicalMemorySize() / 1024);
        sb.append("\n" + osm.getTotalPhysicalMemorySize() / 1024);
        sb.append("\narch: " + osm.getArch());
        sb.append("\navailableProcessors: " + osm.getAvailableProcessors());
        sb.append("\ncommittedVirtualMemorySize: " + osm.getCommittedVirtualMemorySize());
        sb.append("\nname: " + osm.getName());
        sb.append("\nprocessCpuTime: " + osm.getProcessCpuTime());
        sb.append("\nversion: " + osm.getVersion());
        //获取整个虚拟机内存使用情况
        sb.append("\n==========================获取整个虚拟机内存使用情况=========================== ");
        MemoryMXBean mm = ManagementFactoryHelper.getMemoryMXBean();
        sb.append("\nheapMemoryUsage: " + mm.getHeapMemoryUsage());
        sb.append("\nnonHeapMemoryUsage: " + mm.getNonHeapMemoryUsage());
        //获取各个线程的各种状态，CPU 占用情况，以及整个系统中的线程状况
        sb.append("\n============获取各个线程的各种状态，CPU 占用情况，以及整个系统中的线程状况========== ");
        ThreadMXBean tm = ManagementFactoryHelper.getThreadMXBean();
        sb.append("\nthreadCount: " + tm.getThreadCount());
        sb.append("\npeakThreadCount: " + tm.getPeakThreadCount());
        sb.append("\ncurrentThreadCpuTime: " + tm.getCurrentThreadCpuTime());
        sb.append("\ndaemonThreadCount: " + tm.getDaemonThreadCount());
        sb.append("\ncurrentThreadUserTime: " + tm.getCurrentThreadUserTime());

        //当前编译器情况
        sb.append("\n=================================当前编译器情况============================= ");
        CompilationMXBean gm = ManagementFactoryHelper.getCompilationMXBean();
        sb.append("\nname: " + gm.getName());
        sb.append("\ntotalCompilationTime: " + gm.getTotalCompilationTime());

        //获取多个内存池的使用情况
        sb.append("\n=========================获取多个内存池的使用情况============================ ");
        List<MemoryPoolMXBean> mpmList = ManagementFactoryHelper.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean mpm : mpmList) {
            sb.append("\nusage: " + mpm.getUsage());
            sb.append("\nmemoryManagerNames: " + mpm.getMemoryManagerNames().toString());
        }
        //获取GC的次数以及花费时间之类的信息
        sb.append("\n=====================获取GC的次数以及花费时间之类的信息======================== ");
        List<GarbageCollectorMXBean> gcmList = ManagementFactoryHelper.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcm : gcmList) {
            sb.append("\nname: " + gcm.getName());
            sb.append("\nmemoryPoolNames: " + gcm.getMemoryPoolNames());
        }
        //获取运行时信息
        sb.append("\n=============================获取运行时信息================================== ");
        RuntimeMXBean rmb = ManagementFactoryHelper.getRuntimeMXBean();
        sb.append("\nclassPath: " + rmb.getClassPath());
        sb.append("\nlibraryPath: " + rmb.getLibraryPath());
        sb.append("\nvmVersion: " + rmb.getVmVersion());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        log.info("当前时间{}, 系统信息如下:{}", dateFormat.format(new Date()), sb.toString());
    }

    @Override
    public String scheduleId() {
        return "system.monitor";
    }

    @Override
    public String groupId() {
        return "system";
    }
}
