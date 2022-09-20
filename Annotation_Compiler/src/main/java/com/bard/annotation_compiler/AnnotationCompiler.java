package com.bard.annotation_compiler;

import com.bard.annotation.BindPath;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;

/**
 * 注解处理器， 为了自动生成类
 *
 */

@AutoService(Processor.class)   //注册注解处理器
public class AnnotationCompiler extends AbstractProcessor {

    //用来生成文件的对象
    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    /**
     * 声明这个注解处理器要处理的注解是哪些 本例是BindPath注解，其他的注解例如Override则不处理
     * 所以这个module要依赖Annotation的module
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindPath.class.getCanonicalName());
        return types;
    }


    /**
     * 声明这个注解处理器支持的java sdk版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }


    /**
     *
     * 注解处理器的核心方法，写文件就在这个方法中
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //通过这个API可以拿到 所有这个模块中用到了BindPath注解的节点
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindPath.class);

        //初始化数据
        Map<String, String> map = new HashMap<>();
        for(Element element : elementsAnnotatedWith){
            TypeElement typeElement = (TypeElement) element;
            //获取到Activity集合map中的key
            String key = typeElement.getAnnotation(BindPath.class).value();
            String value = typeElement.getQualifiedName().toString();
            map.put(key, value);
        }

        //开始写文件
        if(map.size() > 0){
            Writer writer = null;
            //创建类名
            String utilName = "ActivityUtils_"+System.currentTimeMillis();
            //创建文件
            try {
                JavaFileObject sourceFile = filer.createSourceFile("com.bard.gplearning.utils." + utilName);
                writer = sourceFile.openWriter();

                writer.write("package com.bard.gplearning.utils;\n");
                writer.write("\n");
                writer.write("import com.bard.arouter.ARouter;\n");
                writer.write("import com.bard.arouter.IRouter;\n");
                writer.write("\n");
                writer.write("public class " + utilName + " implements IRouter{\n");
                writer.write( " @Override\n ");
                writer.write( " public void putActivity(){\n");
                Iterator<String> iterator = map.keySet().iterator();
                while(iterator.hasNext()){
                    String key = iterator.next();
                    String value = map.get(key);
                    writer.write(" ARouter.getInstance().putActivity(\""+ key +"\"," + value + ".class);\n");
                }
                writer.write(" }\n");
                writer.write("}");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(writer != null){
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //这时候文件就写好了，但是类文件中的方法并未执行

        return false;
    }
}
