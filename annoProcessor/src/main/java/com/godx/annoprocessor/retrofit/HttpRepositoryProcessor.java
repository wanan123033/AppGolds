package com.godx.annoprocessor.retrofit;

import com.godx.annoprocessor.BaseProcessor;
import com.godx.annotation.retrofit.HTTP;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.godx.annotation.retrofit.HTTP"})
public class HttpRepositoryProcessor extends BaseProcessor {
    private List<Element> httpElements = new ArrayList<>();
    private String packageName;
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> httpElements = roundEnv.getElementsAnnotatedWith(HTTP.class);
        for (Element element : httpElements){
            messager.printMessage(Diagnostic.Kind.NOTE,element.getSimpleName().toString()+"-------"+element.asType());
            packageName = elementTool.getPackageOf(element.getEnclosingElement()).getQualifiedName().toString();
            if (element.getKind() == ElementKind.METHOD){
                this.httpElements.add(element);
            }
        }
        for (int i = 0 ; i < this.httpElements.size() ; i++){
            RepositoryFactory repositoryFactory = new RepositoryFactory.Builder(this.httpElements.get(i))
                    .packageName(packageName)
                    .build();
            repositoryFactory.createRepository();
        }
        return false;
    }
}
