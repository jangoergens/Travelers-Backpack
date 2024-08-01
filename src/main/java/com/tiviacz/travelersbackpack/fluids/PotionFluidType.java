package com.tiviacz.travelersbackpack.fluids;

/*public class PotionFluidType extends FluidType
{
    public static final ResourceLocation POTION_STILL_RL = ResourceLocation.fromNamespaceAndPath(TravelersBackpack.MODID, "block/potion_still");
    public static final ResourceLocation POTION_FLOW_RL = ResourceLocation.fromNamespaceAndPath(TravelersBackpack.MODID, "block/potion_flow");

    public PotionFluidType(Properties properties)
    {
        super(properties);
    }

    @Override
    public Component getDescription(FluidStack stack)
    {
        return Component.translatable(this.getDescriptionId(stack));
    }

    @Override
    public String getDescriptionId(FluidStack stack)
    {
        return "not.implemented.conversion.not.possible";
        //return PotionUtils.getPotion(stack.getTag()).getName("item.minecraft.potion.effect.");
    }

    @Override
    public String getDescriptionId()
    {
        return "item.minecraft.potion.effect.empty";
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
    {
        consumer.accept(new IClientFluidTypeExtensions()
        {
            private static final int EMPTY_COLOR = 0xf800f8;

            @Override
            public int getTintColor()
            {
                return EMPTY_COLOR | 0xFF000000;
            }

            @Override
            public int getTintColor(FluidStack stack)
            {
                return getTintColor(stack.getTag()) | 0xFF000000;
            }

            private static int getTintColor(@Nullable CompoundTag tag)
            {
                if(tag != null && tag.contains("CustomPotionColor", Tag.TAG_ANY_NUMERIC))
                {
                    return tag.getInt("CustomPotionColor");
                }

                return 0xFF000000;

               /* if(PotionUtils.getPotion(tag) == Potions.EMPTY)
                {
                    return EMPTY_COLOR;
                }

                return PotionUtils.getColor(PotionUtils.getAllEffects(tag));
            }

            @Override
            public ResourceLocation getStillTexture() {
                return POTION_STILL_RL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return POTION_FLOW_RL;
            }
        });
    }
}  */