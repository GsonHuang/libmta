### libmta
mta埋点信息上报Android库

---


### 依赖使用
implementation 'com.github.GsonHuang:libmta:v1.0.0'

---


### 项目使用

1.初始化

在applicaton oncreate调用init(),初始化初始化有两种方式


（1）参数1 上下文context，参数2 appid，参数3 上报url
```
    MTAManager.getInstance().init(this, "", "");
```
这种使用方式需要配合注解、注解库进行实现。注解库自行下载依赖，地址：https://github.com/GsonHuang/libmta

项目module中依赖注解库配置
```
api project(path: ':libannotation')
annotationProcessor project(path: ':libProcessor')//注解器依赖
```
具体使用：
application添加注解，数组内容是定义的事件ac值
    
```
@Action({
        "action1",
        "action2",
        "action3"
    })
```


取事件ac ：
```
Actions.ACTION1/Actions.ACTION2/Actions.ACTION3
```


（2）参数1 上下文context，参数2 appid，参数3 上报url，参数4 构建上报埋点事件ac列表。

后续调用上报的ac值需要是列表中定义的事件ac，否则不会进行上报

```
    MTAManager.getInstance().init(this, "", "", new MTAManager.ComfirmAction() {
        @Override
        public List<String> setActions() {
            List<String> actions = new ArrayList<>();
            return actions;
        }
    });
```


2.页面启动/离开事件上报

方式（1）:
每个activity页面继承MtaBaseActivity,会自动开启上报

方式（2）:
activity onCreate()中调用

```
@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MTAManager.getInstance().enterPage(getClass().getSimpleName());

    }
```


activity onDestory()中调用

```
@Override
    protected void onDestroy() {
        super.onDestroy();
        MTAManager.getInstance().leavepPage(getClass().getSimpleName());
    }
```


3.上报埋点事件

构造事件类，up()函数上报
    
```
    MtaEvent mtaEvent = new MtaEvent();
    mtaEvent.setAc(Actions.ACTION3);
    mtaEvent.setPath(MainActivity.class.getSimpleName());
    mtaEvent.setDac("1234");
    MTAManager.getInstance().up(mtaEvent);
```
