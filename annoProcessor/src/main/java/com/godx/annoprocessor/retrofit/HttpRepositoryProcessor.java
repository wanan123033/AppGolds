package com.godx.annoprocessor.retrofit;

import com.godx.annoprocessor.BaseProcessor;
import com.godx.annotation.retrofit.HTTP;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.godx.annotation.retrofit.HTTP"})
public class HttpRepositoryProcessor extends BaseProcessor {
    private List<ExecutableElement> httpElements = new ArrayList<>();
    private String packageName;
    private TypeMirror observableMirror,callMirror;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        observableMirror = elementTool.getTypeElement(RetrofitFinal.OBSERABLE).asType();
        callMirror = elementTool.getTypeElement(RetrofitFinal.CALL).asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> httpElements = roundEnv.getElementsAnnotatedWith(HTTP.class);
        for (Element element : httpElements){
            packageName = elementTool.getPackageOf(element.getEnclosingElement()).getQualifiedName().toString();
            if (element.getKind() == ElementKind.METHOD){
                this.httpElements.add((ExecutableElement)element);
            }
        }
        for (int i = 0 ; i < this.httpElements.size() ; i++){
            RepositoryFactory repositoryFactory = new RepositoryFactory.Builder(this.httpElements.get(i))
                    .packageName(packageName)
                    .messager(messager)
                    .typeUtil(typeTool)
                    .observable(observableMirror)
                    .call(callMirror)
                    .elementTool(elementTool)
                    .build();
            repositoryFactory.createRepository();
            TypeSpec repository = repositoryFactory.getRepository();
            writeClass(packageName,repository);
        }
        return true;
    }
}
