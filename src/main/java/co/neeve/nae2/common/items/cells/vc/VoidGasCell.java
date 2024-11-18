package co.neeve.nae2.common.items.cells.vc;

import appeng.api.AEApi;
import appeng.api.storage.IStorageChannel;
import appeng.items.contents.CellConfig;
import com.mekeng.github.common.item.ItemDummyGas;
import com.mekeng.github.common.me.data.IAEGasStack;
import com.mekeng.github.common.me.data.impl.AEGasStack;
import com.mekeng.github.common.me.storage.IGasStorageChannel;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class VoidGasCell extends VoidCell<IAEGasStack> {
	@Override
	public IStorageChannel<IAEGasStack> getStorageChannel() {
		return AEApi.instance().storage().getStorageChannel(IGasStorageChannel.class);
	}

	@Override
	@Nullable
	public IAEGasStack handleConfigStack(ItemStack stack) {
		if (stack.getItem() instanceof ItemDummyGas gasItem) {
			return AEGasStack.of(gasItem.getGasStack(stack));
		}
		return null;
	}
}
