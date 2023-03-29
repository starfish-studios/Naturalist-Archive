//package com.starfish_studios.naturalist.item;
//
//import com.starfish_studios.naturalist.registry.NaturalistItems;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.util.LazyLoadedValue;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.item.ArmorMaterial;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.level.ItemLike;
//
//import java.util.function.Supplier;
//
//public enum NaturalistArmorMaterials implements ArmorMaterial {
//    REPTILE("reptile", 9, new int[]{1,4,4,1}, 12, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
//        return Ingredient.of(NaturalistItems.REPTILE_SKIN.get());
//    })
//    ;
//
//    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
//    private final String name;
//    private final int durabilityMultiplier;
//    private final int[] slotProtections;
//    private final int enchantmentValue;
//    private final SoundEvent sound;
//    private final float toughness;
//    private final float knockbackResistance;
//    private final LazyLoadedValue<Ingredient> repairIngredient;
//
//    private NaturalistArmorMaterials(String name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier repairIngredient) {
//        this.name = name;
//        this.durabilityMultiplier = durabilityMultiplier;
//        this.slotProtections = slotProtections;
//        this.enchantmentValue = enchantmentValue;
//        this.sound = soundEvent;
//        this.toughness = toughness;
//        this.knockbackResistance = knockbackResistance;
//        this.repairIngredient = new LazyLoadedValue(repairIngredient);
//    }
//
//    public int getDurabilityForSlot(EquipmentSlot slot) {
//        return HEALTH_PER_SLOT[slot.getIndex()] * this.durabilityMultiplier;
//    }
//
//    public int getDefenseForSlot(EquipmentSlot slot) {
//        return this.slotProtections[slot.getIndex()];
//    }
//
//    public int getEnchantmentValue() {
//        return this.enchantmentValue;
//    }
//
//    public SoundEvent getEquipSound() {
//        return this.sound;
//    }
//
//    public Ingredient getRepairIngredient() {
//        return (Ingredient)this.repairIngredient.get();
//    }
//
//    public String getName() {
//        return this.name;
//    }
//
//    public float getToughness() {
//        return this.toughness;
//    }
//
//    public float getKnockbackResistance() {
//        return this.knockbackResistance;
//    }
//}
