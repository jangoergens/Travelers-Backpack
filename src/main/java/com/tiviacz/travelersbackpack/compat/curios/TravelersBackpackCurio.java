package com.tiviacz.travelersbackpack.compat.curios;

/*public class TravelersBackpackCurio implements ICurio
{
    public final ItemStack stack;

    public TravelersBackpackCurio(ItemStack stack)
    {
        this.stack = stack;
    }

    @Override
    public ItemStack getStack()
    {
        return this.stack;
    }

    @Override
    public boolean canEquip(SlotContext context)
    {
        return TravelersBackpackConfig.SERVER.backpackSettings.curiosIntegration.get();
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack)
    {
        if(!TravelersBackpackConfig.SERVER.backpackSettings.curiosIntegration.get()) return;

        if(slotContext.entity() instanceof Player player)
        {
            CapabilityUtils.getCapability(player).ifPresent(data ->
            {
                data.setWearable(stack);
                data.setContents(stack);
            });
        }
    }

    @Override
    public void onEquipFromUse(SlotContext slotContext)
    {
        if(!TravelersBackpackConfig.SERVER.backpackSettings.curiosIntegration.get()) return;

        if(slotContext.entity() instanceof Player player)
        {
            CapabilityUtils.getCapability(player).ifPresent(data ->
            {
                data.setWearable(stack);
                data.setContents(stack);
            });
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack)
    {
        if(!TravelersBackpackConfig.SERVER.backpackSettings.curiosIntegration.get()) return;

        if(slotContext.entity() instanceof Player player)
        {
            CapabilityUtils.getCapability(player).ifPresent(ITravelersBackpack::removeWearable);
        }
    }

    @Nonnull
    @Override
    public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit)
    {
        return DropRule.DEFAULT;
    }
} */