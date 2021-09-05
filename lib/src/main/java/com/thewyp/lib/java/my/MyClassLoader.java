package com.thewyp.lib.java.my;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyClassLoader extends ClassLoader {

    private final File classPathFile;

    public MyClassLoader() {
        String classPath = MyClassLoader.class.getResource("").getPath();
        this.classPathFile = new File(classPath);
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        String className = MyClassLoader.class.getPackage().getName() + "." + name;

        if (classPathFile != null) {
            File classFile = new File(classPathFile, name.replaceAll("\\.", "/") + ".class");
            if (classFile.exists()) {
                FileInputStream fis = null;
                ByteArrayOutputStream bos = null;
                try {
                    fis = new FileInputStream(classFile);
                    bos = new ByteArrayOutputStream();
                    byte[] bytes = new byte[4096];
                    int len;
                    while ((len = fis.read(bytes)) != -1) {
                        bos.write(bytes, 0, len);
                    }
                    return defineClass(className, bos.toByteArray(), 0, bos.size());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    try {
                        if (fis != null) {
                            fis.close();
                        }
                        if (bos != null) {
                            bos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }
}
