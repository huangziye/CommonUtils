[![](https://jitpack.io/v/huangziye/CommonUtils.svg)](https://jitpack.io/#huangziye/CommonUtils)

### 添加 `CommonUtils` 到项目

- 第一步： 添加 `JitPack` 到项目的根 `build.gradle` 中


```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

- 第二步：添加库依赖


```gradle
dependencies {
    implementation 'com.github.huangziye:CommonUtils:${latest_version}'
}
```

<br />



### 常用工具类

| 类名 | 描述 |
| --- | --- |
| `ACache` | 缓存工具类 |
| `ActivityUtil` | Activity 相关工具类 |
| `AesUtil` | AES 加密工具类 |
| `AppUtil` | App相关工具类 |
| `Base64Util` | Base64 相关工具类 |
| `BottomNavigationViewUtil` | BottomNavigationView 相关工具类(快速实现底部导航功能) |
| `CameraUtil` | Camera 相关工具类 |
| `ContactsUtil` | 联系人相关工具类 |
| `CountDownTimerUtil` | 倒计时工具类 |
| `DensityUtil` | 密度相关工具类 |
| `NetworkUtil` | 网络相关工具类 |
| `NotificationUtil` | 通知栏相关工具类 |
| `ShellUtil` | Shell 相关类 |
| `StatusBarUtil` | 状态栏相关工具类 |
| `ToastUtil` | 吐司相关工具类 |
| `VideoUtil` | 视频相关工具类 |









### 常量（按字母先后顺序排序）

| 类名 | 描述 |
| --- | --- |
| `NetworkType` | 网络类型 |
| `RegexConst` | 常用正则表达式 |







<br />

### 关于我


- [简书](https://user-gold-cdn.xitu.io/2018/7/26/164d5709442f7342)

- [掘金](https://juejin.im/user/5ad93382518825671547306b)

- [Github](https://github.com/huangziye)

<br />

### License

```
Copyright 2018, huangziye

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
