package com.godx.annoprocessor.router;

import com.godx.annoprocessor.BaseProcessor;
import com.godx.annotation.router.ARouter;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.godx.annotation.router.ARouter"})
@SupportedOptions({ARouterFinal.MOUDLENAME,ARouterFinal.PACKAGENAME})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ARouterProcessor extends BaseProcessor {
    private Map<String, List<RouterBean>> pathMap = new HashMap<>();
    private Map<String,String> groupMap = new HashMap<>();
    private static TypeMirror activityMirror;
    private static TypeMirror fragmentMirror;
    private TypeMirror callTypeMirror;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        activityMirror = elementTool.getTypeElement(ARouterFinal.ACTIVITY).asType();
        fragmentMirror = elementTool.getTypeElement(ARouterFinal.FRAGMENT).asType();
        callTypeMirror = elementTool.getTypeElement(ARouterFinal.CALLTYPENAME).asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ARouter.class);
        for (Element element : elements) {
            ARouter aRouter = element.getAnnotation(ARouter.class);
            RouterBean routerBean = new RouterBean(aRouter.path(), aRouter.group(), element);
            TypeMirror typeMirror = element.asType();
            if (typeTool.isSubtype(typeMirror,activityMirror)){
                routerBean.setType(RouterBean.TypeEnum.ACTIVITY);
            }else if (typeTool.isSubtype(typeMirror,fragmentMirror)){
                routerBean.setType(RouterBean.TypeEnum.FRAGMENT);
            }else if (typeTool.isSubtype(typeMirror,callTypeMirror)){
                routerBean.setType(RouterBean.TypeEnum.CALL);
            }else {
                messager.printMessage(Diagnostic.Kind.ERROR,"ARouter注解类必须是Activity/Fragment/Call的子类:"+element.getSimpleName().toString());
            }
            if (checkRouterBean(routerBean)) {
                List<RouterBean> routerBeans = pathMap.get(routerBean.getGroup());
                if (routerBeans == null || routerBeans.isEmpty()) {
                    routerBeans = new ArrayList<>();
                    routerBeans.add(routerBean);
                    pathMap.put(routerBean.getGroup(), routerBeans);
                } else {
                    routerBeans.add(routerBean);
                }
            } else {
                messager.printMessage(Diagnostic.Kind.ERROR,"  ARouter path 不符合规则,如：/app/main"+element.getSimpleName().toString());
            }
        }
        TypeElement pathType = elementTool.getTypeElement(ARouterFinal.PATHROUTER);
        TypeElement groupType = elementTool.getTypeElement(ARouterFinal.GOROUPROUTER);

        createPathRouter(pathType);
        createGroupRouter(groupType,pathType);
        return false;
    }

    private void createGroupRouter(TypeElement groupType, TypeElement pathType) {
        //方法返回值：Map<String,Class<? extends PathRouter>>
        TypeName methodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathType))));

        MethodSpec.Builder getGroupRouter = MethodSpec.methodBuilder(ARouterFinal.GET_GROUP_ROUTER_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(methodReturn);

        getGroupRouter.addStatement("$T<$T,$T> $N = new $T<>()",
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathType))),
                ARouterFinal.GROUPMAP,HashMap.class);
        Set<String> keySet = groupMap.keySet();
        for (String key : keySet){
            getGroupRouter.addStatement("$N.put($S,$T.class)",
                    ARouterFinal.GROUPMAP,key,ClassName.get(options.get(ARouterFinal.PACKAGENAME),groupMap.get(key)));
        }
        getGroupRouter.addStatement("return $N",ARouterFinal.GROUPMAP);

        TypeSpec typeSpec = TypeSpec.classBuilder(cature(options.get(ARouterFinal.MOUDLENAME))+"$GroupRouter")
                .addMethod(getGroupRouter.build())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(groupType))
                .build();
        writeClass(options.get(ARouterFinal.PACKAGENAME),typeSpec);

    }

    private void createPathRouter(TypeElement pathType) {
        if (pathMap.isEmpty()) {
            return;
        }
        TypeName methodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ARouterFinal.ROUTERBEAN
        );
        MethodSpec.Builder getPathRouter = MethodSpec.methodBuilder(ARouterFinal.GET_PATH_ROUTER_METHOD_NAME)
                .returns(methodReturn)
                .addModifiers(Modifier.PUBLIC);

        Set<String> keys = pathMap.keySet();
        for (String key : keys){
            getPathRouter.addStatement("$T<$T,$T> $N = new $T<>()",
                    Map.class,String.class,ARouterFinal.ROUTERBEAN,ARouterFinal.PATHMAP,HashMap.class);
            List<RouterBean> routerBeans = pathMap.get(key);
            for (RouterBean routerBean : routerBeans){
                String str = null;
                if (routerBean.getType() == RouterBean.TypeEnum.ACTIVITY){
                    str = "ACTIVITY";
                }else if (routerBean.getType() == RouterBean.TypeEnum.CALL){
                    str = "CALL";
                }else if (routerBean.getType() == RouterBean.TypeEnum.FRAGMENT){
                    str = "FRAGMENT";
                }
                getPathRouter.addStatement("$N.put($S,new $T($S,$S,$T.class,$T.$L))",
                        ARouterFinal.PATHMAP,routerBean.getPath(),ARouterFinal.ROUTERBEAN,routerBean.getPath(),routerBean.getGroup(),
                        ClassName.get(elementTool.getPackageOf(routerBean.getElement()).getQualifiedName().toString(),routerBean.getElement().getSimpleName().toString()),
                        ClassName.get("com.appgodx.router","RouterBean.TypeEnum"),str);
            }

            getPathRouter.addStatement("return $N",ARouterFinal.PATHMAP);
            TypeSpec typeSpec = TypeSpec.classBuilder(cature(options.get(ARouterFinal.MOUDLENAME))+"$Router")
                    .addMethod(getPathRouter.build())
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.get(pathType))
                    .build();
            writeClass(options.get(ARouterFinal.PACKAGENAME),typeSpec);
            groupMap.put(key,typeSpec.name);
        }
    }



    private boolean checkRouterBean(RouterBean routerBean) {
        if (!routerBean.getPath().startsWith("/")) {
            return false;
        }
        if (routerBean.getPath().lastIndexOf("/") == 0) {
            return false;
        } else {
            if (routerBean.getGroup() == null || routerBean.getGroup().isEmpty()) {
                routerBean.setGroup(routerBean.getPath().substring(1, routerBean.getPath().lastIndexOf("/")));
            }
        }
        if (routerBean.getPath().contains(" ")) {
            return false;
        }
        return true;
    }
}
