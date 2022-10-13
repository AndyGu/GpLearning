package com.bard.arouter_api;


/**
 * 这和接口是所有activity工具类的基类
 *
 * 用法：分别在各个module中建立activity工具类（ActivityUtils），实现该接口
 * 在实现方法中将module中的各个activity通过putActivity方法加入到ARouter的activity管理列表中
 * 例ARouter.getInstance().putActivity("app/XX", XXXActivity.class);
 *
 * 不友好，不优雅，放弃使用
 * 使用注解+注解处理器解决这个问题
 */
//@Deprecated
//public interface IRouter {
//    void putActivity();
//}
