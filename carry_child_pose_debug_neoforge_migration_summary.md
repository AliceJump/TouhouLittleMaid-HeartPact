# CarryChildPoseDebug NeoForge 迁移改动总结

## 文件

```text
com/example/maidmarriage/client/CarryChildPoseDebug.java
```

---

# 改动目标

将：

```text
Forge 事件系统
```

迁移为：

```text
NeoForge 1.21.1 事件系统
```

同时保持：

- 调试器逻辑不变
- 参数系统不变
- GUI 功能不变
- 热键行为不变

本次属于：

```text
纯 API / Event System 迁移
```

---

# 一、import 包迁移

## 修改前（Forge）

```java
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
```

---

## 修改后（NeoForge）

```java
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
```

---

# 二、EventBusSubscriber 注解迁移

NeoForge 不再使用：

```java
@Mod.EventBusSubscriber
```

而是改为：

```java
@EventBusSubscriber
```

---

## 修改前

```java
@Mod.EventBusSubscriber(
    modid = MaidMarriageMod.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.FORGE,
    value = Dist.CLIENT
)
```

---

## 修改后

```java
@EventBusSubscriber(
    modid = MaidMarriageMod.MOD_ID,
    bus = EventBusSubscriber.Bus.GAME,
    value = Dist.CLIENT
)
```

---

# 三、Bus 类型迁移

Forge：

```java
Mod.EventBusSubscriber.Bus.FORGE
```

NeoForge：

```java
EventBusSubscriber.Bus.GAME
```

---

# 四、Client Tick 事件迁移

Forge：

```java
TickEvent.ClientTickEvent
```

NeoForge：

```java
ClientTickEvent
```

---

## 修改前

```java
@SubscribeEvent
public static void onClientTick(TickEvent.ClientTickEvent event) {
```

---

## 修改后

```java
@SubscribeEvent
public static void onClientTick(ClientTickEvent event) {
```

---

# 五、删除 Tick Phase 判断

Forge 的：

```java
TickEvent.ClientTickEvent
```

存在：

```java
event.phase
```

因此旧代码需要：

```java
if (event.phase != TickEvent.Phase.END) {
    return;
}
```

---

NeoForge 的：

```java
ClientTickEvent
```

不再使用 phase。

因此直接删除。

---

## 删除前

```java
if (event.phase != TickEvent.Phase.END) {
    return;
}
```

---

## 删除后

```java
// NeoForge ClientTickEvent 不再需要 phase 判断
```

---

# 六、GUI Overlay 事件迁移

Forge：

```java
RenderGuiOverlayEvent.Post
```

NeoForge：

```java
RenderGuiEvent.Post
```

---

## 修改前

```java
@SubscribeEvent
public static void onRender(RenderGuiOverlayEvent.Post event) {
```

---

## 修改后

```java
@SubscribeEvent
public static void onRender(RenderGuiEvent.Post event) {
```

---

# 七、draw 方法参数同步修改

由于 GUI 事件类型发生变化，

draw 方法参数也需要同步迁移。

---

## 修改前

```java
private static void draw(RenderGuiOverlayEvent.Post event,
                         Font font,
                         int x,
                         int y,
                         String text,
                         int color)
```

---

## 修改后

```java
private static void draw(RenderGuiEvent.Post event,
                         Font font,
                         int x,
                         int y,
                         String text,
                         int color)
```

---

# 八、未改动部分

以下逻辑保持原样：

---

## 调试器控制逻辑

```text
F8 开关调试器
```

---

## 参数调节逻辑

```text
Alt + ← →
切换参数
```

```text
Alt + ↑ ↓
调整参数
```

```text
Shift
大步进
```

---

## 参数导出功能

```text
Alt + C
复制参数到剪贴板
```

---

## 参数重置功能

```text
Alt + R
恢复默认值
```

---

## 姿态参数系统

保留：

```text
GeckoLib 参数系统
```

以及：

```text
Bedrock 参数系统
```

包括：

- rotation
- shift
- translate

全部未改动。

---

## GUI 调试面板

左上角调试 UI 保持原样。

---

## PoseParam 枚举系统

参数枚举结构未改动。

---

# 最终结果

`CarryChildPoseDebug`

现已完全适配：

```text
NeoForge 1.21.1
```

包括：

- 新事件总线
- 新 Tick 事件
- 新 GUI 渲染事件
- 新 SubscribeEvent 包路径

并保持：

```text
原有调试功能完全一致
```

