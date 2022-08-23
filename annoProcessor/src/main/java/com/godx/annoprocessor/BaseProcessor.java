package com.godx.annoprocessor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Map;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public abstract class BaseProcessor extends AbstractProcessor {


    protected Elements elementTool;
    protected Filer filer;
    protected Messager messager;
    protected Types typeTool;
    protected Map<String, String> options;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementTool = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        options = processingEnv.getOptions();
        typeTool = processingEnv.getTypeUtils();


    }
    protected void writeClass(String packageName, TypeSpec typeSpec) {
        JavaFile javaFile = JavaFile.builder(packageName,typeSpec)
                .skipJavaLangImports(true)
                .build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {

        }
    }
    public static String cature(String str){
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
        return cap;
    }
}
