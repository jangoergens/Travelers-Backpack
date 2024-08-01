package com.tiviacz.travelersbackpack.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tiviacz.travelersbackpack.items.TravelersBackpackItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public class ShapelessBackpackRecipe extends ShapelessRecipe
{
    public ShapelessBackpackRecipe(String groupIn, CraftingBookCategory category, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn)
    {
        super(groupIn, category, recipeOutputIn, recipeItemsIn);
    }

    @Override
    public ItemStack assemble(CraftingInput pInput, HolderLookup.Provider pRegistries)
    {
        final ItemStack output = this.result.copy();

        if(!output.isEmpty())
        {
            for(int i = 0; i < pInput.size(); i++)
            {
                final ItemStack ingredient = pInput.getItem(i);

                if(!ingredient.isEmpty() && ingredient.getItem() instanceof TravelersBackpackItem)
                {
                    output.applyComponents(ingredient.getComponentsPatch());
                    break;
                }
            }
        }
        return output;
    }

    private ItemStack damageShears(final ItemStack stack)
    {
        final Player craftingPlayer = ForgeHooks.getCraftingPlayer();

        if(craftingPlayer != null && craftingPlayer.level() != null && craftingPlayer instanceof ServerPlayer serverPlayer)
        {
            stack.hurtAndBreak(1, serverPlayer.serverLevel(), serverPlayer, item ->
            {
                ForgeEventFactory.onPlayerDestroyItem(craftingPlayer, stack, (InteractionHand)null);
                stack.setCount(0);
            });
        }
        else
        {
            if(stack.getDamageValue() + 1 <= stack.getMaxDamage())
            {
                stack.setDamageValue(stack.getDamageValue() + 1);
                return stack;
            }
            else
            {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(final CraftingInput pInput)
    {
        final NonNullList<ItemStack> remainingItems = NonNullList.withSize(pInput.size(), ItemStack.EMPTY);

        for(int i = 0; i < remainingItems.size(); ++i)
        {
            final ItemStack itemstack = pInput.getItem(i);

            if(!itemstack.isEmpty() && itemstack.getItem() instanceof ShearsItem)
            {
                remainingItems.set(i, damageShears(itemstack.copy()));
            }
            else
            {
                remainingItems.set(i, ForgeHooks.getCraftingRemainingItem(itemstack));
            }
        }
        return remainingItems;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    public static class Serializer implements RecipeSerializer<ShapelessBackpackRecipe>
    {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public MapCodec<ShapelessBackpackRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapelessBackpackRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static final MapCodec<ShapelessBackpackRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_340779_ -> p_340779_.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(p_301127_ -> p_301127_.getGroup()),
                                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_301133_ -> p_301133_.category()),
                                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_301142_ -> p_301142_.result),
                                Ingredient.CODEC_NONEMPTY
                                        .listOf()
                                        .fieldOf("ingredients")
                                        .flatXmap(
                                                p_301021_ -> {
                                                    Ingredient[] aingredient = p_301021_.toArray(Ingredient[]::new); // Neo skip the empty check and immediately create the array.
                                                    if (aingredient.length == 0) {
                                                        return DataResult.error(() -> "No ingredients for shapeless recipe");
                                                    } else {
                                                        return aingredient.length > 3 * 3
                                                                ? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(3 * 3))
                                                                : DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
                                                    }
                                                },
                                                DataResult::success
                                        )
                                        .forGetter(p_300975_ -> p_300975_.getIngredients())
                        )
                        .apply(p_340779_, ShapelessBackpackRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessBackpackRecipe> STREAM_CODEC = StreamCodec.of(
                ShapelessBackpackRecipe.Serializer::toNetwork, ShapelessBackpackRecipe.Serializer::fromNetwork
        );

        private static ShapelessBackpackRecipe fromNetwork(RegistryFriendlyByteBuf p_319905_) {
            String s = p_319905_.readUtf();
            CraftingBookCategory craftingbookcategory = p_319905_.readEnum(CraftingBookCategory.class);
            int i = p_319905_.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            nonnulllist.replaceAll(p_319735_ -> Ingredient.CONTENTS_STREAM_CODEC.decode(p_319905_));
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(p_319905_);
            return new ShapelessBackpackRecipe(s, craftingbookcategory, itemstack, nonnulllist);
        }

        private static void toNetwork(RegistryFriendlyByteBuf p_320371_, ShapelessBackpackRecipe p_320323_) {
            p_320371_.writeUtf(p_320323_.getGroup());
            p_320371_.writeEnum(p_320323_.category());
            p_320371_.writeVarInt(p_320323_.getIngredients().size());

            for (Ingredient ingredient : p_320323_.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(p_320371_, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(p_320371_, p_320323_.result);
        }
    }
}