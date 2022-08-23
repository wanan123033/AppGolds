package com.godx.annoprocessor.retrofit;

import com.godx.annoprocessor.BaseProcessor;
import com.godx.annotation.retrofit.HttpModel;
import com.godx.annotation.retrofit.ICallAdapter;
import com.godx.annotation.retrofit.IConverter;
import com.godx.annotation.retrofit.OKHTTP;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Type;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.godx.annotation.retrofit.HttpModel","com.godx.annotation.retrofit.ICallAdapter",
        "com.godx.annotation.retrofit.IConverter","com.godx.annotation.retrofit.OKHTTP"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RetrofitProcessor extends BaseProcessor {
    private String baseUrl;
    private Element okhttp;
    private TypeMirror okhttpClientMirror;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        okhttpClientMirror = elementTool.getTypeElement("okhttp3.OkHttpClient").asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE,"HttpModel");
        Set<? extends Element> httpModelEles = roundEnv.getElementsAnnotatedWith(HttpModel.class);
        if (httpModelEles.size() != 1){
            messager.printMessage(Diagnostic.Kind.NOTE,"HttpModel 是一次性的，不能使用两次");
        }
        for (Element httpmodelEle : httpModelEles){
            if (httpmodelEle.getKind() == ElementKind.INTERFACE){
                baseUrl = httpmodelEle.getAnnotation(HttpModel.class).baseUrl();
            }else {
                messager.printMessage(Diagnostic.Kind.NOTE,"HttpModel 是一次性的，并且注解要在接口上 需要使用interface修饰");
            }
        }
        if ("".equals(baseUrl) || baseUrl == null){
            messager.printMessage(Diagnostic.Kind.NOTE,"baseUrl 为空");
        }
        Set<? extends Element> okhttpEle = roundEnv.getElementsAnnotatedWith(OKHTTP.class);
        if (okhttpEle.size() != 1){
            messager.printMessage(Diagnostic.Kind.NOTE,"OKHTTP 是一次性的，不能使用两次");
        }
        for (Element httpmodelEle : okhttpEle){
            if (httpmodelEle.getKind() == ElementKind.FIELD){
                if (typeTool.isSubtype(httpmodelEle.asType(),okhttpClientMirror)){
                    okhttp = httpmodelEle;
                }else {
                    messager.printMessage(Diagnostic.Kind.NOTE,"OKHTTP 只能用来注解OkHttpClient");
                }
            }else {
                messager.printMessage(Diagnostic.Kind.NOTE,"OKHTTP 是一次性的，并且注解要在成员变量上");
            }
        }
        Set<? extends Element> converterEle = roundEnv.getElementsAnnotatedWith(IConverter.class);
        TypeSpec.Builder retroUtil = TypeSpec.classBuilder("RetrofitUtils")
                .addModifiers(Modifier.PUBLIC);
        genBaseUrl(retroUtil);
        genOkHttpClient(retroUtil);
        genRetrofitMethod(roundEnv, converterEle, retroUtil);
        writeClass("com.app.retrofit",retroUtil.build());
        return false;
    }

    private void genRetrofitMethod(RoundEnvironment roundEnv, Set<? extends Element> converterEle, TypeSpec.Builder retroUtil) {
        MethodSpec.Builder method = MethodSpec.methodBuilder("getRetrofit")
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL,Modifier.STATIC)
                .returns(ClassName.get("com.appgodx.retrofit","Retrofit"));
        method.addCode("$T $N = new $T.Builder()\n",ClassName.get("com.appgodx.retrofit","Retrofit"),"retrofit",ClassName.get("com.appgodx.retrofit","Retrofit"));
        method.addCode(".baseUrl(baseUrl)\n.client(client)\n");
        for (Element element : converterEle){
            method.addCode(".addConverterFactory($T.$N())",ClassName.get((TypeElement)element.getEnclosingElement()),"get"+cature(element.getSimpleName().toString()));
        }
        Set<? extends Element> adapEle = roundEnv.getElementsAnnotatedWith(ICallAdapter.class);
        for (Element element : adapEle){
            method.addCode(".addCallAdapterFactory($T.$N())",ClassName.get((TypeElement)element.getEnclosingElement()),"get"+cature(element.getSimpleName().toString()));
        }
        method.addStatement(".build()");
        method.addStatement("return $N","retrofit");
        retroUtil.addMethod(method.build());
    }

    private void genOkHttpClient(TypeSpec.Builder retroUtil) {

        FieldSpec okhttpclient = FieldSpec.builder(ClassName.get(okhttpClientMirror),"client",
                        Modifier.PRIVATE,Modifier.STATIC,Modifier.FINAL)
                .initializer("$T.$N()", ClassName.get((TypeElement) okhttp.getEnclosingElement()),"get"+cature(okhttp.getSimpleName().toString())).build();
        retroUtil.addField(okhttpclient);
    }

    private void genBaseUrl(TypeSpec.Builder retroUtil) {
        FieldSpec baseUrl = FieldSpec.builder(String.class,"baseUrl",
                Modifier.PRIVATE,Modifier.STATIC,Modifier.FINAL)
                .initializer("$S",this.baseUrl).build();
        retroUtil.addField(baseUrl);
    }
}
