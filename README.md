帮助文档编写。随时更新

一。软件配置：
1.工程build.gradle配置（R2.java相关配置）

		maven{
            url("build/localMaven/R2GenCreator")
        }

        classpath "com.gwm.gradlewPlugin:R2GenCreator:1.0"

    2.app ---- build.gradle配置：
    	需要复制以下参数。    

    	multiDexEnabled true     //开启dex多包编译
        javaCompileOptions{      //room schema文件配置
            annotationProcessorOptions{
                arguments=["room.schemaLocation":"$projectDir/schemas".toString()]
            }
        }
        ndk{                //ndk 编译配置
            abiFilters "armeabi-v7a"
        }

	    sourceSets{
	        main{
	            assets.srcDirs += files("$projectDir/schemas".toString())   //配置room 数据库自动版本更新的比对文件目录
	            jniLibs.srcDirs = ['libs']
	        }
	    }

    //引入frameworkx框架项目
    apply plugin: "com.gwm.gradlewPlugin"

    implementation project(':frameworkx')
    annotationProcessor project(":compiler")
    annotationProcessor "android.arch.persistence.room:compiler:1.1.1"

二。frameworkx框架的使用。
功能描述：
1.Application代理模式 可以编写多个Application
2.Arouter组件化路由器框架
3.常用基类以及权限动态注册@Permission注解
4.http框架
5.界面的IOC框架
6.rxmvvm框架
7.room的加密与自动升级
8.类的双亲委托机制
9.常用类（Handler,SpareArray）优化处理
三。框架描述以及相关功能使用说明

	说明：这套架构是建立在AndroidX与JetPack之上的，有关AndroidX与JetPack的相关配置不在此处加以说明。是一款APP 通用型架构设计，不针对特定业务特定邻域封装。该架构采用了MVVM设计模式设计一个单独的模块。多模块之间采用ARouter封装。软件层次结构分明。在一定程度上要求了使用人员的分层思想的使用。软件大体上可分为以下几层
		1>.Activity/Fragment 页面UI处理层
		2>.ViewModel：业务功能逻辑封装层
		3>.dataPresenter:子功能（单一设计原则）封装层
		4>.entity:数据库bean层
		5>.dao 数据库Dao层
		6>.接口与常量层。
		7>.bean 网络请求的bean层。

	以下参照图片上传的软件代码对该框架加以说明：
		1.Application代理模式 可以编写多个Application
			@IOCWork("app")
			@Module("app")
			@AutoService(ApplicationDelegate.class)
			public class MyApplicationDelegate extends BaseApplicationDelegate {}

			使用该框架。需要注意要使用下面三个注解：
				1>。@IOCWork  开启界面的IOC处理，app是moudle的名字
				2>.@Module  表明要使用Arouter架构封装
				3>.@AutoService 注册该代理到frameworkx的Application的代理模式中。
			使用时需要继承BaseApplicationDelegate类。并重写其相关方法。  相关方法是对Application的生命周期的监听。 原则上支持一个Application多个ApplicationDelegate的模式。这个是解决了在组件化架构中不能写多个Application。但是在这里可以使用多个ApplicationDelegate处理

		2.Arouter组件化路由器框架，原则上参照了阿里云的ARouter框架的设计，只不过是去掉了该框架的Group部分。支持大数据传递以及RouterFiled注解的功能。这个ARouter是支持组件化的。

		@ARouter("app/mainActivity")     //给Activity定义路径名称
		public class MainActivity extends BaseTitleActivity{}

		Router.getInstance(getActivity())  //来自于哪里
			.from("business_id",id)       //from传递的参数
			.to(RouterContract.ROUTER_BUSINESSINFOACTIVITY)   //去哪里  RouterContract.ROUTER_BUSINESSINFOACTIVITY这个常量是自动生成的。一个Activity对应一个常量。生成原则：ROUTER_ + Activity的名字全大写。
			.router();   //执行当前跳转动作

		@RouterField("business_id")   //取出当前跳转动作上所携带的参数，需要public修饰一下。
    	public Long business_id;

    	路由拦截器：
    		路由拦截器分为全部拦截器跟局部拦截器两部分。这里的全部拦截是指它会拦截所有通过ARouter跳转的动作。局部拦截器是指只会拦截当前链上的跳转动作。如果全部拦截器与局部拦截器同时存在，则两者都会调用。拦截器的使用方法如下：  
    			1.全部拦截器调用如下：(在ApplicationDelegate或者Application中调用如下方法添加全局拦截器)

    				Router.wholeInterceptor(RouterInterceptor interceptor);

    			2.局部拦截使用interceptor(RouterInterceptor interceptor)指定，示例如下：

    				Router.getInstance(getActivity())  //来自于哪里
						.from("business_id",id)       //from传递的参数
						.interceptor(RouterInterceptor)  //指定当前链上的拦截器
						.to(RouterContract.ROUTER_BUSINESSINFOACTIVITY)   //去哪里  
						.router();   //执行当前跳转动作

		3.常用基类以及@Permission注解。
			1>.常用基类包括：BaseActivity,BaseFragment,BaseTitleActivity,BaseSplshActivity.BaseRecyclerAdapter
			2>.@Permission注解：权限动态注册注解。使用方法如下：

				@Permission({Manifest.permission.READ_PHONE_STATE, 
					Manifest.permission.CAMERA,
					Manifest.permission.WRITE_EXTERNAL_STORAGE, 
					Manifest.permission.READ_EXTERNAL_STORAGE})
		public class SplshActivity extends BaseSplshActivity<activity_splsh> {
			/**
			 *
			 *已经全部同意了访问权限
			 *
			 */
			@Override
		    public void onRequestPermissionsGranted() {

		    }
		    /**
			 *
			 *有的访问权限被拒绝
			 *
			 */
		    @Override
		    public void onRequestPermissionsDenied(){

		    }
		}

			使用该注解需要继承BaseSplshActivity 相当于在欢迎页处理动态权限的授权。

	4.http框架

		注解部分：
			@Files : 多文件上传时要用到该注解。需要与@MultiPart注解一起使用, 注解 List<File>
			@FileUpload: 单文件上传时用该注解，注解File
			@Header:HTTP请求中的Hearder部分，@Header({"key:value","key:value"})
			@HeaderMap:Http请求中Header部分，此注解用在形参上，参数类型要求是Map类型
			@HeaderString：Http请求中的Header部分，此注解有一个key,此注解用在形参上，参数类型要求是String类型
			@HTTP:HTTP的请求地址，需要指定请求地址url跟请求方式way
			@HttpInterceptor:HTTP拦截器
			@HttpStream：下载时用该注解。注解在方法上
			@JSON : 表明HTTP请求中的body部分是JSON,此注解用在形参上，参数类型要求是String类型
			@JSONRequest: 表明这条请求是JSON请求。此注解用在方法上。
			@MultiPart:  表明该请求需要上传文件。常用的有form表单形式。
			@Param:用于注解JSON请求中的字段部分。需要与@JSON注解联合使用。
			@Path:与Retrofit中的@Path用法一致。
			@PostFormRequest:POST请求中所必须携带的方法上的注解。与@MultiPart会发生冲突，该注解只是表明它是FROM表单形式，但不能用它去做文件上传，文件上传必须使用@MultiPart
			@Query:表明HTTP请求中请求参数不是以JSON的格式封装，主要是以key=value的形式封装的，需要使用该注解，与JSON请求不同。
			@QueryMap:与@Query注解一致，只不过表明它是Map类型的参数。
			@QueryUrl:与@Query注解一致，只不过它是用在拼接地址后面的参数。
			@RequestBody: 表明请求体需要自定义，此注解用在形参上，参数类型要求是RequestBody类型。
			@StreamPath:下载文件时，指定文件的下载路径。
			@Url:用于指定Http请求中地址，此注解用在形参上，参数类型要求是String类型
			@WebSocket:表明这个请求是一条webSocket推送。
			@HttpModel:用于注解HTTP请求接口类。可以在该注解中指明baseUrl。
			@DNS:用于注解地址服务器，相当于DNS.
			@OkHttp:用于注解OkHttp请求的OKHttpClient，要求注解类是接口
			@ResponseConverter:用于注解HTTP中的结果处理器。比如服务器返回的结果需要解密，JSON解析等操作

		使用示例：

		@HttpModel(baseUrl = "www.baidu.com")  //表明是HTTP请求接口类，可以在这里指定baseurl
		public interface HttpContract {

			@OkHttp(connectTimeout = 300,
			        readTimeout = 300,
			        writeTimeout = 300,
			        retryOnConnectionFailure = true,
			        retry = 3)   //注明OKHTTP的一些参数
			OkHttpClient client = null;

			/**
			 * @PostFormRequest，表示是POST请求。表单提交，此时在HTTP中不能指明way参数。不然会报错
			 * @HTTP(url = "/auth/token/form")  只指明了URL  请求方式默认是POST
			 */
			@PostFormRequest
			@HTTP(url = "/auth/token/form")
			Observable<LoginBean> login(@HeaderString("Authorization") String token,   //表明这个参数要传到Header里面。字段名称：Authorization  字段的值是token的值
			                            @Query("username") String username,     //声明了一个请求参数，在BODY部分，封装格式以key=value的形式封装。字段名称：username  字段的值是参数username的值
			                            @Query("password") String password);

			/**
			 * @MultiPart，表示是POST请求。表单提交，可以作为文件上传，也可以不做文件上传  @PostFormRequest不能用于文件上传
			 */
			@MultiPart(type = MultiPart.MediaType.FROM)
			@HTTP(url = "/run/uploadBizFile")
			Observable<BaseBean> uploadBizFile(@HeaderString("Authorization") String token,
			                                   @Query("bizId")String bizId,
			                                   @Query("examId")String examId,
			                                   @Query("bizTypeId")String bizTypeId,
			                                   @Files("files")List<File> files);  //添加文件上传注解。此处是@Files表明是多文件类型，需要用List集合

			/**
			 * @com.gwm.annotation.http.RequestBody  表明请求体BODY部分已经自定义了
			 */
			@com.gwm.annotation.http.RequestBody
			@HTTP(url = "/run/queryBizQrInfo")
			Observable<QrInfoBean> onBusiness(@HeaderString("Authorization")String token, @com.gwm.annotation.http.RequestBody RequestBody body);

			/**
			 * @JSONRequest 表明请求体是以JSON的形式封装
			 *
			 */
			@JSONRequest
		    @HTTP(url = "/run/downSiteStudent")
		    Observable<SiteStudentBean> downSiteStudent(@HeaderString ("Authorization")String token, @JSON String json);  //@JSON 代表JSON的内容

			/**
			 * @HttpStream 此链接是下载文件的，此处的下载与普通的下载不同，需要经过服务端处理才行。不是直接指向文件的下载。与@JSONRequest连用表明请求体部分有参数，而且还是以JSON形式封装
			 * 如果没有参数@JSONRequest可以不用写。
			 */
	        @JSONRequest
		    @HttpStream
		    @HTTP(url = "/run/batchDownloadPhotos")
		    Observable<String> downPacket(@HeaderString ("Authorization")String token, @JSON String json,@StreamPath String streamPath);//@StreamPath 传入的文件保存的绝对路径
		}

		5.room的加密与自动升级
	该套架构的数据库部分采用的ROOM封装。关于ROOM的部分，这里不做叙述，如果不知道的请移步百度。百度里面写的已经比较详细了。这里的只是我个人对ROOM数据库的再一次封装。减少了一定的代码量。

		注解部分：
			@RoomDatabaseOpenHelper  数据库版本配置，配置当前的数据库版本。此注解需要声明在类上面。
			@RoomFactory  数据库工厂配置，指明数据库的工厂类，此处只是针对加密才会用得到这个注解。建议不要使用。因为加密之后解密过程比较复杂。我至今还没弄懂解密过程。需要百度参考 SQLCipher数据库解密。
			@Migration 数据库版本发生变化时，对应的数据库升级注解，详细使用方法如下：

				@RoomDatabaseOpenHelper(version = 1)     //声明当前的数据库版本是1
				public class AppDatabase {


				    @RoomFactory    //声明数据库工厂类 加密时要指定该工厂，并把密码写进去。  "12432efd3@#$%1212sd"  这个就是密码  但是我不知道如何解密。
				    public static final SafeHelperFactory factory = new SafeHelperFactory("12432efd3@#$%1212sd".getBytes());
					@Migration
					public static final Migration migration1_2 = new Migration(1,2){     // 1,2  表示从一号版本升级到二号版本的迁移操作。
						public void migrate(@NonNull SupportSQLiteDatabase database){
							//在这里写升级操作。
						}
					};
				}

		6.rxmvvm框架

			这里只有一点要加以说明：
				在viewmodel中，如何发送数据到Activity/Fragment中。采用以下方式发送数据到Activity/Fragment中
					有的地方使用的是：   DataBusUtils.getInstance().post(new DataBusEventBean(DataBusContract.SEARCH_STU,student));
					有的地方时是：  postValue(studentBusiness, MVVMContract.BUSINESS_DISPLAY);

					原因是两处所使用的框架版本不一致导致的，在后期我都采用的后面的那种当时。也就是  postValue(studentBusiness, MVVMContract.BUSINESS_DISPLAY);

					跟EventBus差不多的用法,此处不做叙述。需要了解参考EventBus   用法是一样的。


https://www.cnblogs.com/gwm-Android/p/16579918.html