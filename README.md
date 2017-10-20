# PushSdk

集成小米，华为，魅族推送，最稳定的推送方式


###1.申请帐号

去华为，小米，魅族渠道申请，华为是根据包名推送，小米，魅族在String.xml中填写appid，appkey。

因为申请开发者帐号，还涉及到身份证注册信息之类的，我就不提供了，申请好后，记得修改包名。


###2.初始化

*建议在application中的onCreate中初始化,如果程序被杀死，系统级推送会调起app，经测试在application中初始化比较好。
```
 /**
   * 初始化系统级推送
   */
   private void initRomPush() {

      GPushWrapper.getInstance().init(this);
      GPushWrapper.getInstance().setClickCallBack(new PushSdkCallBack() {
          @Override
          public void onNotificationMessageClicked(Context context, String message) {
              //可以不做任何处理，采用系统推送的方案，但是会有很多问题，比如，app被杀死，应该从欢迎页进入
              Log.e("GPush", "GPush click");
              boolean isLogin = true;
              if (isLogin) {
                  //有登录态跳往哪里
              } else {
                  //没有登录态从欢迎界面进入，
                  fromPushSdk = true;
                  /*Intent intent = new Intent(context, WelcomeActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  context.startActivity(intent);*/
              }
          }

          @Override
          public void upLoadToken(String token,int RomType) {
              //网络请求上传token
              // PushDeviceInfoMgr.MI_UI_ROM;  小米
              // PushDeviceInfoMgr.HUAWEI_EM_UI_ROM; 华为
              //PushDeviceInfoMgr.MEIZU_FLYME_OS_ROM; 魅族
          }
      });
  }
```
###3.逻辑

手机系统默认四个分类，小米，华为，魅族，其他手机，其他手机采用小米推送，根据rom类型初始化对应推送sdk，

在receiver中接收小米的regId，魅族的pushId,华为的token，统一命名为token，把token以及rom类型（建议加上deviceId）传递给服务端。

这样可以针对特定用户推送。


在点击通知栏时，可以采用后端控制跳转某个acitivity，并且传递参数，三个平台都支持，但是如果跳转逻辑比较复杂，需要自定义，可以实现

onNotificationMessageClicked，如果后台有活动的activity，并且已经登录跳转某个界面，如果后台有活动的activity没有登录，一般跳转后台活动

activity中的最后一个activity，如果后台没有活动的activity，这时一般从启动页重新启动app，并且设置一个标记，方便启动完成后跳转某个界面。


###4.推送注意点

魅族和小米推送很像，华为不大一样，华为不支持推送别名，华为推送以key，value形式推送，别名的功能，key是alias，华为没有id，key，华为推送

是按照包名进行推送的。

###5.推送结果

* 成功：魅族mx4,华为测过3款，型号记不得了，红米not4x,红米not3，还有几款也没注意型号，2333

* 失败：魅蓝note1，问过技术人员，魅族建议flyme5和6测试。支持flyme4,但是不保证成功，华为mate7。

###6.推送版本号

魅族：3.4.2

华为：V2705

小米：3_4_5

2017年9月26号的最新版本

###7.官方支持

*华为：
各版本emui对华为push的支持
华为移动服务2.5.2.300，emui不同版本到达率不同
Emui3.0上，Push广播有很大概率被限制，如： Mate7 3.0版本，荣耀6plus，P7 3.0版本，4X， 4A等。
Emui3.1上，Push广播基本不被限制，但个别型号机型存在问题，如：荣耀5x等。
Emui4.0及以上，Push广播有较高概率被限制，不被限制的机型如：荣耀畅玩4C，荣耀畅玩4X，Mate S，P8 MAX等。
Emui4.1 , ROM升级到了最新版本的（80%已升），通知消息不走广播，不会被限制，透传消息走广播，会被限制。
Emui5.0以上 ,通知消息不走广播，不会被限制，透传消息走广播，会被限制
解决方法开启应用相关权限。
