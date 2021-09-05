package com.thewyp.lib.java.my;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class MyProxy {

    private static String Ln = "\n";

    public static Object newProxyInstance(MyClassLoader loader,
                                          Class<?>[] interfaces,
                                          MyInvocationHandler h)
            throws IllegalArgumentException {

        FileWriter fileWriter = null;
        try {

            // 1. 动态生成一个.java源文件
            String code = generateCode(interfaces);
            // 2. 把生成的这个.java源文件保存在磁盘上
            String filePath = MyProxy.class.getResource("").getPath();
            File file = new File(filePath, "$Proxy0.java");
            fileWriter = new FileWriter(file);
            fileWriter.write(code);
            fileWriter.flush();
            fileWriter.close();

            // 3. 把这个.java源文件编译成.class文件
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null);
            Iterable<? extends JavaFileObject> iterable = standardFileManager.getJavaFileObjects(file);
            JavaCompiler.CompilationTask task = compiler.getTask(null, standardFileManager, null, null, null, iterable);
            task.call();
            standardFileManager.close();

            // 4. 把编译后的.class文件加载到jvm内存中
            Class clazz = loader.findClass("$Proxy0");

            // 5. 根据加载到jvm中的.class字节码文件生成Class类，然后创建Class类的对象
            Constructor constructor = clazz.getConstructor(MyPrinterProxy.class);

            return constructor.newInstance(h);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return null;
    }

    private static String generateCode(Class<?>[] interfaces) {
        StringBuffer sb = new StringBuffer();
        sb.append("package com.thewyp.lib.java.my;").append(Ln);
        sb.append("import java.lang.reflect.Method;").append(Ln);
        sb.append("public class $Proxy0 implements ").append(interfaces[0].getName()).append("{").append(Ln);
        sb.append("public MyPrinterProxy h;").append(Ln);
        sb.append("public $Proxy0(MyPrinterProxy h) {").append(Ln);
        sb.append("this.h = h;").append(Ln);
        sb.append("}").append(Ln);

        for (Method method : interfaces[0].getMethods()) {
            sb.append("public ").append(method.getReturnType().getName()).append(" ").append(method.getName()).append("(){").append(Ln);
            sb.append("try{").append(Ln);
            sb.append("Method method = ").append(interfaces[0].getName()).append(".class.getMethod(\"").append(method.getName()).append("\", new Class[]{});").append(Ln);
            sb.append("this.h.invoke(this, method, null);").append(Ln);
            sb.append("}catch(Throwable e){").append(Ln);
            sb.append("e.printStackTrace();").append(Ln);
            sb.append("}").append(Ln);
            sb.append("}").append(Ln);
        }
        sb.append("}").append(Ln);
        return sb.toString();
    }
}
