# HarvestRushRestaurant 丰收急餐餐厅
> 基于 Java Swing 的课程设计项目，综合运用多种设计模式，模拟菜园种植、后厨做菜、顾客点餐的小游戏。

## ✨ 项目简介
双人分工模拟餐厅系统：一边种植作物收获食材，一边接收顾客订单、烹饪出餐。
项目核心目标：**实践六大经典设计模式**。

## 🧩 使用的设计模式
- 单例模式：全局唯一的时间管理器、资源管理器
- 简单工厂模式：菜品、作物对象创建
- 策略模式：不同顾客的消费、等待行为策略
- 装饰器模式：菜品加料、附加属性增强
- 观察者模式：时间推进，通知菜园、后厨、UI界面刷新
- 命令模式：操作撤销、重做（种植、做菜指令）

## 🛠 技术栈
- 语言：Java 8
- GUI：Swing
- 构建工具：Maven

## 📁 目录结构
```text
src
├── main
│   ├── java
│   │   ├── command                   # 命令模式（操作撤销/重做）
│   │   │   ├── CommandInvoker
│   │   │   ├── PlantCropCommand
│   │   │   ├── RestaurantCommand
│   │   │   ├── ServeOrderCommand
│   │   │   └── StartCookCommand
│   │   ├── crop                      # 作物实体与简单工厂
│   │   │   ├── Crop
│   │   │   ├── Tomato
│   │   │   ├── Veg
│   │   │   ├── Wheat
│   │   │   └── CropFactory
│   │   ├── customer                  # 顾客实体与策略模式
│   │   │   ├── Customer
│   │   │   ├── NormalCus
│   │   │   ├── RushCus
│   │   │   ├── VipCus
│   │   │   └── CustomerFactory
│   │   ├── kitchen                   # 后厨、菜品与烹饪策略
│   │   │   ├── Bake
│   │   │   ├── CookStrategy
│   │   │   ├── Steam
│   │   │   ├── StirFry
│   │   │   ├── Stove
│   │   │   └── StoveFactory
│   │   ├── manager                   # 核心管理器（单例模式）
│   │   │   └── RestaurantManager
│   │   ├── observer                  # 观察者模式（时间流转发）
│   │   │   ├── RestaurantObserver
│   │   │   └── RestaurantSubject
│   │   ├── order                     # 订单实体
│   │   │   └── Order
│   │   ├── ui                        # Swing 界面
│   │   │   └── RestaurantFrame
│   │   └── Main                      # 主启动类
│   └── resources                     # 静态资源（图片、配置文件等）
└── test                              # 单元测试目录

```

## ▶ 运行方式
1. 使用 IDEA 打开项目，等待 Maven 依赖加载完成
2. 找到主启动类，直接 Run 运行
3. 界面分为菜园面板、后厨面板、日志控制台

## 📌 功能说明
- 菜园：选择种子种植，随时间生长成熟、收获食材
- 后厨：接收顾客订单，消耗食材制作菜品
- 时间流：全局统一时间步进，驱动所有业务更新
- 日志：记录种植、出餐、顾客离开等事件

## 👥 团队
- Wang Youyi：负责项目方向和基础功能
- Wang 
- Li

