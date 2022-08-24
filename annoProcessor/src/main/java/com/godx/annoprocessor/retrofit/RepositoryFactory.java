package com.godx.annoprocessor.retrofit;

import com.godx.annoprocessor.BaseProcessor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class RepositoryFactory {
    private TypeSpec.Builder repository;
    private String methodName;
    private ExecutableElement element;
    private Messager messager;
    private ParameterizedTypeName parameterizedType;
    private TypeName javaBeanClass;
    private Types typeUtil;

    private TypeMirror observableMirror,callMirror;
    private Elements elementTool;
    public RepositoryFactory(Builder builder) {
        element = builder.element;
        messager = builder.messager;
        typeUtil = builder.typeUtil;
        observableMirror = builder.observable;
        callMirror = builder.call;
        elementTool = builder.elementTool;
        methodName = element.getSimpleName().toString();
        TypeMirror returnType = element.getReturnType();
        parameterizedType = (ParameterizedTypeName) ParameterizedTypeName.get(returnType);
        javaBeanClass = parameterizedType.typeArguments.get(0);
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get("com.appgodx.rxmvvm","BaseRepository"),
                javaBeanClass,ClassName.get((TypeElement) element.getEnclosingElement()));
        repository = TypeSpec.classBuilder(BaseProcessor.cature(methodName)+"Repository")
                .addModifiers(Modifier.PUBLIC).addModifiers(Modifier.ABSTRACT)
                .superclass(parameterizedTypeName);
    }

    public void createRepository() {
        createConstructor();
        createRepositoryMethod();
    }

    /**
     *   public void search(String q,String m){
     *     Observable<SearchBean> search = httpApi.search(q, m);
     *     addHttpObservable(search);
     *   }
     */
    private void createRepositoryMethod() {
        MethodSpec.Builder method = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC);
        List<? extends VariableElement> parameters = element.getParameters();
        String content = "(";
        for (VariableElement variableElement : parameters){
            method.addParameter(ClassName.get(variableElement.asType()),variableElement.getSimpleName().toString());
            content += variableElement.getSimpleName().toString()+",";
        }
        content = content.substring(0,content.length() - 1)+")";

        method.addStatement("$T<$T> $N = httpApi.$N"+content,parameterizedType.rawType,javaBeanClass,methodName,methodName);
        TypeMirror typeMirror = elementTool.getTypeElement(parameterizedType.rawType.packageName() + "." + parameterizedType.rawType.simpleName()).asType();

        if (typeUtil.isSubtype(typeMirror,observableMirror)){
            method.addStatement("addHttpObservable($N)",methodName);
        }else if (typeUtil.isSubtype(typeMirror,callMirror)){
            method.addStatement("addCallRequest($N)",methodName);
        }
        repository.addMethod(method.build());
    }

    private void createConstructor() {
        MethodSpec.Builder method = MethodSpec.constructorBuilder()
                .addStatement("super($T.class)",ClassName.get((TypeElement) element.getEnclosingElement()))
                .addModifiers(Modifier.PUBLIC);
        repository.addMethod(method.build());
    }

    public TypeSpec getRepository(){
        return repository.build();
    }

    public static class Builder {
        private final ExecutableElement element;
        private String packageName;
        private Messager messager;
        private Types typeUtil;
        private TypeMirror observable;
        private TypeMirror call;
        private Elements elementTool;

        public Builder(ExecutableElement element) {
            this.element = element;
        }
        public Builder messager(Messager messager){
            this.messager = messager;
            return this;
        }

        public RepositoryFactory build() {
            return new RepositoryFactory(this);
        }

        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder typeUtil(Types typeTool) {
            this.typeUtil = typeTool;
            return this;
        }

        public Builder observable(TypeMirror observableMirror) {
            this.observable = observableMirror;
            return this;
        }

        public Builder call(TypeMirror callMirror) {
            this.call = callMirror;
            return this;
        }

        public Builder elementTool(Elements elementTool) {
            this.elementTool = elementTool;
            return this;
        }
    }
}
