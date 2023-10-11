package co.neeve.nae2.mixin.patternmultitool.shared;

import appeng.container.AEBaseContainer;
import appeng.container.slot.AppEngSlot;
import appeng.container.slot.SlotDisabled;
import co.neeve.nae2.common.helpers.PlayerHelper;
import co.neeve.nae2.common.interfaces.IPatternMultiToolToolboxHost;
import co.neeve.nae2.common.items.patternmultitool.ObjPatternMultiTool;
import co.neeve.nae2.common.items.patternmultitool.ToolPatternMultiTool;
import co.neeve.nae2.common.slots.SlotPatternMultiTool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "EmptyMethod", "SameReturnValue", "AddedMixinMembersNamePattern" })
@Mixin(AEBaseContainer.class)
public class MixinAEBaseContainer extends Container {
	@Unique
	protected ObjPatternMultiTool patternMultiToolObject = null;
	@Unique
	protected ArrayList<AppEngSlot> patternMultiToolSlots = null;

	@Shadow
	public void lockPlayerInventorySlot(int idx) {}

	@Shadow
	public boolean canInteractWith(@NotNull EntityPlayer playerIn) {
		return false;
	}

	@Shadow
	public InventoryPlayer getInventoryPlayer() {
		return null;
	}

	@Shadow
	@SuppressWarnings({ "DataFlowIssue" }) // shadowed method, never called
	protected @NotNull Slot addSlotToContainer(@NotNull Slot newSlot) {
		return null;
	}

	@Unique
	public ObjPatternMultiTool getPatternMultiToolObject() {
		return this.patternMultiToolObject;
	}

	@Unique
	public List<AppEngSlot> getPatternMultiToolSlots() {
		return this.patternMultiToolSlots;
	}

	@Unique
	public void initializePatternMultiTool() {
		if (this instanceof IPatternMultiToolToolboxHost host) {
			final InventoryPlayer inventoryPlayer = this.getInventoryPlayer();
			final ItemStack patternMultiTool = PlayerHelper.getPatternMultiTool(inventoryPlayer.player);
			if (patternMultiTool == null) return;

			int slotId = PlayerHelper.getSlotFor(inventoryPlayer, patternMultiTool);
			if (slotId != -1)
				this.lockPlayerInventorySlot(slotId);

			this.patternMultiToolObject = ToolPatternMultiTool.getGuiObject(patternMultiTool);
			this.patternMultiToolSlots = new ArrayList<>();

			var lines = this.patternMultiToolObject.getInstalledCapacityUpgrades();
			for (int u = 0; u < 4; u++) {
				for (int v = 0; v < 9; v++) {
					AppEngSlot slot;
					int slotIndex = v + u * 9;
					int x = host.getPatternMultiToolToolboxOffsetX() + u * 18;
					int y = host.getPatternMultiToolToolboxOffsetY() + v * 18;
					if (u <= lines)
						slot = (new SlotPatternMultiTool(this.patternMultiToolObject.getPatternInventory(), host,
							slotIndex, x, y, u, inventoryPlayer));
					else
						slot = (new SlotDisabled(this.patternMultiToolObject.getPatternInventory(),
							slotIndex, x, y));

					slot.setPlayerSide();
					this.addSlotToContainer(slot);
					this.patternMultiToolSlots.add(slot);
				}
			}
		}
	}
}