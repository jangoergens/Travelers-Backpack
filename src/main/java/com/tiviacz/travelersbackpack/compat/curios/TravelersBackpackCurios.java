package com.tiviacz.travelersbackpack.compat.curios;

/*public class TravelersBackpackCurios
{
    public static ICurio createBackpackProvider()
    {
        return new TravelersBackpackCurio(ItemStack.EMPTY);
    }

    public static Optional<SlotResult> getCurioTravelersBackpack(LivingEntity livingEntity)
    {
        Predicate<ItemStack> backpack = stack -> stack.getItem() instanceof TravelersBackpackItem;
        return CuriosApi.getCuriosInventory(livingEntity).flatMap(curio -> curio.findFirstCurio(backpack));
    }

    public static ItemStack getCurioTravelersBackpackStack(Player player)
    {
        if(getCurioTravelersBackpack(player).isPresent())
        {
            return getCurioTravelersBackpack(player).get().stack();
        }
        return ItemStack.EMPTY;
    }

    public static TravelersBackpackContainer getCurioTravelersBackpackInventory(Player player)
    {
        return CapabilityUtils.getCapability(player).map(ITravelersBackpack::getContainer).orElse(null);
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean renderCurioLayer(AbstractClientPlayer clientPlayer)
    {
        if(TravelersBackpackCurios.getCurioTravelersBackpack(clientPlayer).isPresent())
        {
            return CuriosApi.getCuriosInventory(clientPlayer).map(curios -> curios.findFirstCurio(stack -> stack.getItem() instanceof TravelersBackpackItem)
                    .map(result -> result.slotContext().visible()).orElse(false)).orElse(false);
        }
        return false;
    }

    public static void rightClickUnequip(Player player, ItemStack stack)
    {
        if(TravelersBackpack.enableCurios())
        {
            Optional<SlotContext> slotContext = CuriosApi.getCuriosInventory(player).flatMap(curios ->
                    curios.findFirstCurio(predicate -> ItemStack.isSameItemSameComponents(predicate, stack))).flatMap(result -> Optional.ofNullable(result.slotContext()));

            slotContext.ifPresent(context -> CuriosApi.getCuriosInventory(player).ifPresent(curios -> curios.getCurios().get("back").getStacks().setStackInSlot(context.index(), ItemStack.EMPTY)));
        }
    }

    public static boolean rightClickEquip(Player player, ItemStack stack, boolean simulate)
    {
        if(CuriosApi.getCurio(stack).isPresent())
        {
            ICurio curio = CuriosApi.getCurio(stack).get();
            Optional<ICuriosItemHandler> curiosHandler = CuriosApi.getCuriosInventory(player);

            if(curiosHandler.isPresent())
            {
                int index = -1;
                boolean isEmptySlot = false;

                ICurioStacksHandler curioHandler = curiosHandler.get().getCurios().get("back");

                for(int i = 0; i < curioHandler.getSlots(); i++)
                {
                    if(curioHandler.getStacks().getStackInSlot(i).isEmpty())
                    {
                        index = i;
                        isEmptySlot = true;
                    }
                }

                if(!isEmptySlot) return false;
                NonNullList<Boolean> renderStates = curioHandler.getRenders();

                SlotContext slotContext = new SlotContext(curioHandler.getIdentifier(), player, index, false, renderStates.size() > index && renderStates.get(index));

                if(curio.canEquip(slotContext))
                {
                    if(simulate) return true;
                    curioHandler.getStacks().setStackInSlot(index, stack.copy());
                    curio.onEquipFromUse(slotContext);

                    //Sound
                    player.level().playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.PLAYERS, 1.0F, (1.0F + (player.level().random.nextFloat() - player.level().random.nextFloat()) * 0.2F) * 0.7F);

                    //Sync
                    CapabilityUtils.synchronise(player);
                    CapabilityUtils.synchroniseToOthers(player);
                    return true;
                }
            }
        }
        return false;
    }
} */