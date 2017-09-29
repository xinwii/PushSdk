# PushSdk
集成小米，华为，魅族推送，大幅度提高推送到达率

###1.申请帐号
去华为，小米，魅族渠道申请，华为是根据包名推送，小米，魅族在String.xml中填写appid，appkey。

###2.初始化
建议在application中的onCreate中初始化,如果程序被杀死，系统级推送会调起app，经测试在application中初始化比较好。
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
              // PushDeviceInfoMgr.MI_UI_ROM;
              // PushDeviceInfoMgr.HUAWEI_EM_UI_ROM;
              //PushDeviceInfoMgr.MEIZU_FLYME_OS_ROM;
          }
      });
  }
```
###3.逻辑
手机系统默认四个分类，小米，华为，魅族，其他手机，其他手机采用小米推送，根据rom类型初始化对应推送sdk，
在receiver中接收小米的regId，魅族的pushId,华为的token，统一命名为token，把token以及rom类型（建议加上deviceId）传递给服务端。
这样可以针对特定用户推送。

###4.推送注意点
魅族和小米推送很像，华为不大一样，华为不支持推送别名，华为推送以key，value形式推送，别名的功能，key是alias，华为没有id，key，华为推送
是按照包名进行推送的。

###5.推送结果
成功：魅族mx4,华为测过3款，幸好记不得了，红米not4x,红米not3，还有几款也没注意型号，2333
失败：魅蓝note1，问过技术人员，魅族建议flyme5和6测试。支持flyme4,但是不保证成功。

###6.推送版本号
魅族：3.4.2
华为：V2705
小米：3_4_5
2017年9月26号的最新版本