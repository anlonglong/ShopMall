#简介
### 一 截图
- 应用的部分截图放在shopmall文件夹下面的screenshot文件中
### 二 APK下载
- 在shopmall文件夹下面的release文件中有打包好的apk
- [扫码下载APK的链接](https://fir.im/8t1r)
### 三 框架&技术
#### 框架
   项目的整体结构采用的是MVP分层架构，但是部分的页面并没有采用mvp架构，
   比如：分类，购物车等模块，这些模块
   采用的是google开发架构组件中的viewModel&&LiveData来配合开发的，
   在配合kotlin的函数式参数，很好的实
   了数据的展示，吧View层和model层进行了高度分离，达到了高耦合的目的。
#### 技术
1. 数据存储采用的是google推荐的repository层来维护的，
数据库采用的google开发组件中的另外一个组件Room，
一个官方的，增删改查效率高的ORM型关系型数据库。
repository层统一处理和数据存储有关的一切操作
2. 事件的传递采用的是EventBus来传递的。
3. 权限框架使用的是google官方出品的easypermissions库，
并在对其进行了二封装，使用起来如丝般柔滑和顺手。
4. 二维码扫描使用的是基于googlez的zxing封装的bga-qrcode-zxing，
可实现定制化的扫码界面，支持二维码和条形码的扫码秒。
### 四 感谢
因为以前没有写过电商之类的APP，这几年电商这么火，感觉作为一个Android开发人员，
没写过电商类的App，人生是不完整的，所以，学完kotlin后就想着写一个，
完整一下人生，哈哈哈...,随在github上搜到了一个，
本项目就是模仿github用户名是[AndroidHensen](https://github.com/AndroidHensen)的用户的YaNi项目，
[项目地址](https://github.com/AndroidHensen/YaNi),我只是用kotlin来实现的，
特此感谢@[AndroidHensen](https://github.com/AndroidHensen)提供的后端数据，和前端的逻辑，如有侵犯您的版权，请及时联系本人，我会删除所有的数据。
### 五 声明
本项目只能用来交流和学习，禁止一切商业用途。

### 关于我
Email： 940752944@qq.com
License
-------
Copyright 2018 anlonglong


    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.