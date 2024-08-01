package com.tiviacz.travelersbackpack.inventory.menu.slot;

import com.tiviacz.travelersbackpack.inventory.CraftingContainerImproved;
import com.tiviacz.travelersbackpack.inventory.ITravelersBackpackContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public class ResultSlotExt extends ResultSlot
{
    protected final ResultContainer inv;
    protected final ITravelersBackpackContainer container;

    public ResultSlotExt(ITravelersBackpackContainer container, Player player, CraftingContainerImproved matrix, ResultContainer inv, int slotIndex, int xPosition, int yPosition)
    {
        super(player, matrix, inv, slotIndex, xPosition, yPosition);
        this.inv = inv;
        this.container = container;
    }

    @Override
    public boolean mayPickup(Player player)
    {
        return container.getSettingsManager().hasCraftingGrid();
    }

    @Override
    public boolean isActive()
    {
        return container.getSettingsManager().hasCraftingGrid() && container.getSettingsManager().showCraftingGrid();
    }

    @Override
    public ItemStack remove(int amount)
    {
        if(this.hasItem())
        {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }
        return this.getItem().copy();
    }

    @Override
    protected void onSwapCraft(int numItemsCrafted)
    {
        super.onSwapCraft(numItemsCrafted);
        this.inv.setItem(0, this.getItem().copy()); // https://github.com/Shadows-of-Fire/FastWorkbench/issues/62 - Vanilla's SWAP action will leak this stack here.
    }

    @Override
    public void set(ItemStack stack) {}

    @Override
    protected void checkTakeAchievements(ItemStack stack)
    {
        if(this.removeCount > 0)
        {
            stack.onCraftedBy(this.player.level(), this.player, this.removeCount);
            ForgeEventFactory.firePlayerCraftingEvent(this.player, stack, this.craftSlots);
        }
        this.removeCount = 0;
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public void onTake(Player player, ItemStack stack)
    {
        this.checkTakeAchievements(stack);
        CraftingInput.Positioned craftinginput$positioned = this.craftSlots.asPositionedCraftInput();
        CraftingInput craftinginput = craftinginput$positioned.input();
        int i = craftinginput$positioned.left();
        int j = craftinginput$positioned.top();
        ForgeHooks.setCraftingPlayer(player);
        NonNullList<ItemStack> list;
        Recipe<CraftingInput> recipe = (Recipe<CraftingInput>) this.inv.getRecipeUsed().value();
        if(recipe != null && recipe.matches(craftinginput, player.level())) list = recipe.getRemainingItems(craftinginput); //#TODO check
        else list = ((CraftingContainerImproved)this.craftSlots).getStackList();
        ForgeHooks.setCraftingPlayer(null);

        for (int k = 0; k < craftinginput.height(); k++) {
            for (int l = 0; l < craftinginput.width(); l++) {
                int i1 = l + i + (k + j) * this.craftSlots.getWidth();
                ItemStack itemstack = this.craftSlots.getItem(i1);
                ItemStack itemstack1 = list.get(l + k * craftinginput.width());
                if (!itemstack.isEmpty()) {
                    this.craftSlots.removeItem(i1, 1);
                    itemstack = this.craftSlots.getItem(i1);
                }

                if (!itemstack1.isEmpty()) {
                    if (itemstack.isEmpty()) {
                        this.craftSlots.setItem(i1, itemstack1);
                    } else if (ItemStack.isSameItemSameComponents(itemstack, itemstack1)) {
                        itemstack1.grow(itemstack.getCount());
                        this.craftSlots.setItem(i1, itemstack1);
                    } else if (!this.player.getInventory().add(itemstack1)) {
                        this.player.drop(itemstack1, false);
                    }
                }
            }
        }
    }

    @Override
    public ItemStack getItem()
    {
        // Crafting Tweaks fakes 64x right click operations to right-click craft a stack to the "held" item, so we need to verify the recipe here.
        RecipeHolder<CraftingRecipe> recipe = (RecipeHolder<CraftingRecipe>)this.inv.getRecipeUsed();
        if (recipe != null && recipe.value().matches(this.craftSlots.asCraftInput(), player.level())) return super.getItem();
        return ItemStack.EMPTY;
    }

 /*   @Override
    public ItemStack getItem() //#TODO?
    {
        // Crafting Tweaks fakes 64x right click operations to right-click craft a stack to the "held" item, so we need to verify the recipe here.
        Recipe<CraftingContainer> recipe = (Recipe<CraftingContainer>)this.inv.m_40158_().f_291008_();
        if (recipe != null && recipe.matches(this.craftSlots, player.level())) return super.getItem();
        return ItemStack.EMPTY;
    } */
}