package com.vergilyn.examples.system;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/10/19
 */
public class SystemProperty {
    public static void main(String args[]) {
        System.out.println("java.version: " + System.getProperty("java.version"));				//Java 运行时环境版本
        System.out.println("java.vendor: " + System.getProperty("java.vendor"));					//Java 运行时环境供应商
        System.out.println("java.vendor.url: " + System.getProperty("java.vendor.url"));			//Java 供应商的 URL

        System.out.println("java.home: " + System.getProperty("java.home"));						//Java 安装目录
        System.out.println("java.compiler: " + System.getProperty("java.compiler"));				//要使用的 JIT 编译器的名称

        System.out.println("java.library.path: " + System.getProperty("java.library.path"));		//加载库时搜索的路径列表
        System.out.println("java.io.tmpdir: " + System.getProperty("java.io.tmpdir"));			//默认的临时文件路径
        System.out.println("java.ext.dirs: " + System.getProperty("java.ext.dirs"));				//一个或多个扩展目录的路径

        System.out.println("java.vm.specification.version: " + System.getProperty("java.vm.specification.version"));		//Java 虚拟机规范版本
        System.out.println("java.vm.specification.vendor: " + System.getProperty("java.vm.specification.vendor"));		//Java 虚拟机规范供应商
        System.out.println("java.vm.specification.name: " + System.getProperty("java.vm.specification.name"));			//Java 虚拟机规范名称
        System.out.println("java.vm.version: " + System.getProperty("java.vm.version"));									//Java 虚拟机实现版本
        System.out.println("java.vm.vendor: " + System.getProperty("java.vm.vendor"));									//Java 虚拟机实现供应商
        System.out.println("java.vm.name: " + System.getProperty("java.vm.name"));										//Java 虚拟机实现名称

        System.out.println("java.specification.version: " + System.getProperty("java.specification.version"));		//Java 运行时环境规范版本
        System.out.println("java.specification.vendor: " + System.getProperty("java.specification.vendor"));			//Java 运行时环境规范供应商
        System.out.println("java.specification.name: " + System.getProperty("java.specification.name"));				//Java 运行时环境规范名称

        System.out.println("java.class.version: " + System.getProperty("java.class.version"));		//Java 类格式版本号
        System.out.println("java.class.path: " + System.getProperty("java.class.path"));				//Java 类路径

        System.out.println("os.name: " + System.getProperty("os.name"));				//操作系统的名称
        System.out.println("os.arch: " + System.getProperty("os.arch"));				//操作系统的架构
        System.out.println("os.version: " + System.getProperty("os.version"));		//操作系统的版本

        System.out.println("file.separator: " + System.getProperty("file.separator"));		//文件分隔符（在 UNIX 系统中是“/”）
        System.out.println("path.separator: " + System.getProperty("path.separator"));		//路径分隔符（在 UNIX 系统中是“:”）
        System.out.println("line.separator: " + System.getProperty("line.separator"));		//行分隔符（在 UNIX 系统中是“/n”）

        System.out.println("user.name: " + System.getProperty("user.name"));		//用户的账户名称
        System.out.println("user.home: " + System.getProperty("user.home"));		//用户的主目录
        System.out.println("user.dir: " + System.getProperty("user.dir"));		//用户的当前工作目录

    }
}
