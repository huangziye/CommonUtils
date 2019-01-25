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


### 常用工具类（按字母先后顺序排序）

- `ActivityUtil` Activity相关工具类
- `BitmapUtil` Bitmap 相关工具类
- `CameraUtil` Camera 相关工具类
- `ContactsUtil` 联系人相关工具类
- `DensityUtil` 密度相关工具类
- `NetworkUtil` 网络相关工具类
- `NotificationUtil` 通知栏相关类
- `ShellUtil` Shell 相关类
- `VideoUtil` 视频相关工具类







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
