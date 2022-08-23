package com.godx.annoprocessor.router;

import com.godx.annoprocessor.BaseProcessor;
import com.godx.annotation.router.RouterField;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;


public class ParameterFactory {
    private MethodSpec.Builder method;
    private ClassName className;
    private ParameterSpec target;
    private Messager messager;
    private ParameterFactory(Builder builder){
        className = builder.className;
        target = builder.target;
        messager = builder.messager;

        method = MethodSpec.methodBuilder(ARouterFinal.GETPARAMETER_METHOD_NAME)
                .addParameter(target)
                .addModifiers(Modifier.PUBLIC);
    }

    public void addFirstStatement() {
        method.addStatement("$T t = ($T) "+target.name,className,className);
    }

    public void buildElement(Element field) {
        String fieldName = field.getSimpleName().toString();

        RouterField annoValue = field.getAnnotation(RouterField.class);
        String anno = annoValue.value().equals("") ? fieldName : annoValue.value();
        int type = field.asType().getKind().ordinal();

        //t.setName(t.getIntent().getIntExtra("name",t.getName()))
        String methodContent = "t.set"+ BaseProcessor.cature(anno)+"(t.getIntent().";
        messager.printMessage(Diagnostic.Kind.NOTE,">>>>>>>>>"+field.asType().toString());
        if (field.asType().toString().equalsIgnoreCase("java.lang.Integer")){
            methodContent += "getIntExtra($S,t.get"+BaseProcessor.cature(anno)+"()))";
        }else if (field.asType().toString().equalsIgnoreCase("java.lang.String")){
            methodContent += "getStringExtra($S))";
        }else if (field.asType().toString().equalsIgnoreCase("java.lang.Double")){
            methodContent += "getDoubleExtra($S,t.get"+BaseProcessor.cature(anno)+"()))";
        }
        method.addStatement(methodContent,anno);
    }

    public MethodSpec getMethod() {
        return method.build();
    }

    public void buildCallElement(Element field) {
        /**
         * Router.with(t)
         *             .to("/opengltest/main")
         *             .router()
         */
        String fieldName = field.getSimpleName().toString();

        RouterField annoValue = field.getAnnotation(RouterField.class);
        String anno = annoValue.value().equals("") ? fieldName : annoValue.value();

        method.addStatement("t.set"+BaseProcessor.cature(fieldName)+"(($T)$T.with(t).to($S).router())",
                ClassName.get(field.asType()),
                ClassName.get("com.appgodx.router","Router"),anno);

    }

    public static final class Builder{
        private ParameterSpec target;
        private ClassName className;
        private Messager messager;

        public Builder(ParameterSpec target){
            this.target = target;
        }
        public Builder setClassName(ClassName className){
            this.className = className;
            return this;
        }
        public Builder setMessager(Messager messager){
            this.messager = messager;
            return this;
        }
        public ParameterFactory build(){
            return new ParameterFactory(this);
        }
    }
}
