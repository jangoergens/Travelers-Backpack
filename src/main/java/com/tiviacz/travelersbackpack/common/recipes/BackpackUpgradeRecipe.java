package com.tiviacz.travelersbackpack.common.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tiviacz.travelersbackpack.components.BackpackContainerContents;
import com.tiviacz.travelersbackpack.components.FluidTanks;
import com.tiviacz.travelersbackpack.components.Settings;
import com.tiviacz.travelersbackpack.config.TravelersBackpackConfig;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import com.tiviacz.travelersbackpack.init.ModItems;
import com.tiviacz.travelersbackpack.init.ModRecipeSerializers;
import com.tiviacz.travelersbackpack.inventory.Tiers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

public class BackpackUpgradeRecipe extends SmithingTransformRecipe
{
    final Ingredient template;
    final Ingredient base;
    final Ingredient addition;
    final ItemStack result;

    public BackpackUpgradeRecipe(Ingredient pTemplate, Ingredient pBase, Ingredient pAddition, ItemStack pResult)
    {
        super(pTemplate, pBase, pAddition, pResult);

        this.template = pTemplate;
        this.base = pBase;
        this.addition = pAddition;
        this.result = pResult;
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput pInput, HolderLookup.Provider pRegistries)
    {
        ItemStack result = pInput.getItem(1).transmuteCopy(this.result.getItem(), this.result.getCount());
        result.applyComponents(this.result.getComponentsPatch());

        ItemStack base = pInput.getItem(1);
        ItemStack addition = pInput.getItem(2);

        int tier = base.getOrDefault(ModDataComponents.TIER.get(), 0);

        if(addition.is(Tiers.of(tier).getTierUpgradeIngredient()))
        {
            upgradeInventory(result, Tiers.of(tier).getNextTier());
            return result;
        }

        if(addition.is(ModItems.CRAFTING_UPGRADE.get()))
        {
            if(base.has(ModDataComponents.SETTINGS.get()))
            {
                List<List<Byte>> oldSettings = base.get(ModDataComponents.SETTINGS.get());
                List<Byte> craftingSettings = oldSettings.get(0);
                if(craftingSettings.get(0) == (byte)0)
                {
                    List<Byte> newCraftingSettings = Arrays.asList((byte)1, craftingSettings.get(1), craftingSettings.get(2));
                    List<List<Byte>> newSettings = Arrays.asList(newCraftingSettings, oldSettings.get(1));
                    result.set(ModDataComponents.SETTINGS.get(), newSettings);
                    return result;
                }
            }
            else
            {
                List<Byte> newCraftingSettings = Arrays.asList((byte)1, (byte)0, (byte)1);
                List<List<Byte>> newSettings = Settings.createSettings(newCraftingSettings, Settings.createDefaultToolSettings());
                result.set(ModDataComponents.SETTINGS.get(), newSettings);
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    public void upgradeInventory(ItemStack stack, Tiers.Tier nextTier)
    {
        //Tier
        stack.set(ModDataComponents.TIER.get(), nextTier.getOrdinal());

        //Inventory
        NonNullList<ItemStack> oldContents = stack.getOrDefault(ModDataComponents.BACKPACK_CONTAINER.get(), BackpackContainerContents.fromItems(nextTier.getStorageSlots(), NonNullList.withSize(nextTier.getStorageSlots(), ItemStack.EMPTY))).getItems();
        BackpackContainerContents newContents = BackpackContainerContents.upgradeContents(nextTier.getStorageSlots(), oldContents);
        stack.set(ModDataComponents.BACKPACK_CONTAINER.get(), newContents);

        //Tools
        NonNullList<ItemStack> oldTools = stack.getOrDefault(ModDataComponents.TOOLS_CONTAINER.get(), BackpackContainerContents.fromItems(nextTier.getToolSlots(), NonNullList.withSize(nextTier.getToolSlots(), ItemStack.EMPTY))).getItems();
        BackpackContainerContents newTools = BackpackContainerContents.upgradeContents(nextTier.getToolSlots(), oldTools);
        stack.set(ModDataComponents.TOOLS_CONTAINER.get(), newTools);

        //Tanks
        FluidTanks oldTanks = stack.getOrDefault(ModDataComponents.FLUID_TANKS.get(), FluidTanks.createTanks(nextTier.getTankCapacity()));
        FluidTanks newTanks = new FluidTanks(nextTier.getTankCapacity(), oldTanks.leftFluidStack(), oldTanks.rightFluidStack());
        stack.set(ModDataComponents.FLUID_TANKS.get(), newTanks);
    }

    @Override
    public boolean matches(SmithingRecipeInput pInput, Level level)
    {
        ItemStack addition = pInput.getItem(2);
        boolean flag = true;

        if(!TravelersBackpackConfig.SERVER.backpackSettings.craftingUpgrade.enableUpgrade.get())
        {
            flag = !addition.is(ModItems.CRAFTING_UPGRADE.get());
        }
        if(!TravelersBackpackConfig.SERVER.backpackSettings.enableTierUpgrades.get())
        {
            flag = !(addition.is(ModItems.IRON_TIER_UPGRADE.get()) || addition.is(ModItems.GOLD_TIER_UPGRADE.get())
                    || addition.is(ModItems.DIAMOND_TIER_UPGRADE.get()) || addition.is(ModItems.NETHERITE_TIER_UPGRADE.get()));
        }
        return /*matchesTier(container, level) &&*/ flag && super.matches(pInput, level);
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipeSerializers.BACKPACK_UPGRADE.get();
    }

    public static class Serializer implements RecipeSerializer<BackpackUpgradeRecipe>
    {
        private static final MapCodec<BackpackUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_340782_ -> p_340782_.group(
                                Ingredient.CODEC.fieldOf("template").forGetter(p_301310_ -> p_301310_.template),
                                Ingredient.CODEC.fieldOf("base").forGetter(p_300938_ -> p_300938_.base),
                                Ingredient.CODEC.fieldOf("addition").forGetter(p_301153_ -> p_301153_.addition),
                                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_300935_ -> p_300935_.result)
                        )
                        .apply(p_340782_, BackpackUpgradeRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, BackpackUpgradeRecipe> STREAM_CODEC = StreamCodec.of(
                BackpackUpgradeRecipe.Serializer::toNetwork, BackpackUpgradeRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<BackpackUpgradeRecipe> codec()
        {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BackpackUpgradeRecipe> streamCodec()
        {
            return STREAM_CODEC;
        }

        private static BackpackUpgradeRecipe fromNetwork(RegistryFriendlyByteBuf p_320375_) {
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(p_320375_);
            Ingredient ingredient1 = Ingredient.CONTENTS_STREAM_CODEC.decode(p_320375_);
            Ingredient ingredient2 = Ingredient.CONTENTS_STREAM_CODEC.decode(p_320375_);
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(p_320375_);
            return new BackpackUpgradeRecipe(ingredient, ingredient1, ingredient2, itemstack);
        }

        private static void toNetwork(RegistryFriendlyByteBuf p_320743_, BackpackUpgradeRecipe p_319840_) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(p_320743_, p_319840_.template);
            Ingredient.CONTENTS_STREAM_CODEC.encode(p_320743_, p_319840_.base);
            Ingredient.CONTENTS_STREAM_CODEC.encode(p_320743_, p_319840_.addition);
            ItemStack.STREAM_CODEC.encode(p_320743_, p_319840_.result);
        }
    }
}