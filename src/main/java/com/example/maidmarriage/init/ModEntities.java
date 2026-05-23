package com.example.maidmarriage.init;

import com.example.maidmarriage.MaidMarriageMod;
import com.example.maidmarriage.entity.LiftProxyEntity;
import com.example.maidmarriage.entity.MaidCarryProxyEntity;
import com.example.maidmarriage.entity.MaidChildEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 实体注册表：注册子代女仆实体类型。
 * 该类的具体逻辑可参见下方方法与字段定义。
 */
public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MaidMarriageMod.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<MaidChildEntity>> MAID_CHILD =
            ENTITY_TYPES.register("maid_child", () ->
                    EntityType.Builder.of(MaidChildEntity::new, MobCategory.CREATURE)
                            .sized(0.6F, 1.5F)
                            .clientTrackingRange(10)
                            .build("maid_child"));

    public static final DeferredHolder<EntityType<?>, EntityType<LiftProxyEntity>> LIFT_PROXY =
            ENTITY_TYPES.register("lift_proxy", () ->
                    EntityType.Builder.<LiftProxyEntity>of(LiftProxyEntity::new, MobCategory.MISC)
                            .sized(0.01F, 0.01F)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .noSave()
                            .noSummon()
                            .build("lift_proxy"));

    public static final DeferredHolder<EntityType<?>, EntityType<MaidCarryProxyEntity>> MAID_CARRY_PROXY =
            ENTITY_TYPES.register("maid_carry_proxy", () ->
                    EntityType.Builder.<MaidCarryProxyEntity>of(MaidCarryProxyEntity::new, MobCategory.MISC)
                            .sized(0.01F, 0.01F)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .noSave()
                            .noSummon()
                            .build("maid_carry_proxy"));

    private ModEntities() {
    }
}
