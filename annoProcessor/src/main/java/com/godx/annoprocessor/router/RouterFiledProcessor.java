package com.godx.annoprocessor.router;

import com.godx.annoprocessor.BaseProcessor;
import com.godx.annotation.router.RouterField;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.godx.annotation.router.RouterField"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RouterFiledProcessor extends BaseProcessor {
    private Map<TypeElement, List<Element>> tempParameter = new HashMap<>();
    private static TypeMirror callTypeMirror;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        callTypeMirror = elementTool.getTypeElement(ARouterFinal.CALLTYPENAME).asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RouterField.class);
        for (Element element : elements){
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            if (tempParameter.containsKey(typeElement)){
                tempParameter.get(typeElement).add(element);
            }else {
                List<Element> fields = new ArrayList<>();
                fields.add(element);
                tempParameter.put(typeElement,fields);
            }
        }
        Set<TypeElement> keySet = tempParameter.keySet();
        for (TypeElement typeElement : keySet) {
            ParameterSpec target = ParameterSpec.builder(TypeName.OBJECT, "target").build();
            ParameterFactory parameterMethod = new ParameterFactory.Builder(target)
                    .setClassName(ClassName.get(typeElement))
                    .setMessager(messager)
                    .build();
            parameterMethod.addFirstStatement();
            List<Element> fields = tempParameter.get(typeElement);
            for (Element field : fields){

                if (typeTool.isSubtype(field.asType(),callTypeMirror)){
                    parameterMethod.buildCallElement(field);
                }else {
                    parameterMethod.buildElement(field);
                }
            }
            MethodSpec methodSpec = parameterMethod.getMethod();

            TypeSpec typeSpec = TypeSpec.classBuilder(typeElement.getSimpleName().toString()+"$RouterParameter")
                    .addMethod(methodSpec)
                    .addSuperinterface(ClassName.get(elementTool.getTypeElement(ARouterFinal.ROUTERPARAMETER)))
                    .addModifiers(Modifier.PUBLIC)
                    .build();
            writeClass(elementTool.getPackageOf(typeElement).getQualifiedName().toString(),typeSpec);
        }
        return false;
    }
}
