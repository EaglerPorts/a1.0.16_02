package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class GuiContainer extends GuiScreen {
	private static RenderItem itemRenderer = new RenderItem();
	private ItemStack l = null;
	protected int xSize = 176;
	protected int ySize = 166;
	protected List inventorySlots = new ArrayList();

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		int var4 = (this.width - this.xSize) / 2;
		int var5 = (this.height - this.ySize) / 2;
		this.drawGuiContainerBackgroundLayer(var3);
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)var4, (float)var5, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		for(int var6 = 0; var6 < this.inventorySlots.size(); ++var6) {
			SlotInventory var7 = (SlotInventory)this.inventorySlots.get(var6);
			this.drawSlotInventory(var7);
			if(var7.getIsMouseOverSlot(var1, var2)) {
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				int var8 = var7.xDisplayPosition;
				int var9 = var7.yDisplayPosition;
				this.drawGradientRect(var8, var9, var8 + 16, var9 + 16, -2130706433, -2130706433);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		if(this.l != null) {
			GL11.glTranslatef(0.0F, 0.0F, 32.0F);
			itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, this.l, var1 - var4 - 8, var2 - var5 - 8);
			itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, this.l, var1 - var4 - 8, var2 - var5 - 8);
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		this.drawGuiContainerForegroundLayer();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}

	protected void drawGuiContainerForegroundLayer() {
	}

	protected abstract void drawGuiContainerBackgroundLayer(float var1);

	private void drawSlotInventory(SlotInventory var1) {
		IInventory var2 = var1.inventory;
		int var3 = var1.slotNumber;
		int var4 = var1.xDisplayPosition;
		int var5 = var1.yDisplayPosition;
		ItemStack var6 = var2.getStackInSlot(var3);
		if(var6 == null) {
			int var7 = var1.getBackgroundIconIndex();
			if(var7 >= 0) {
				GL11.glDisable(GL11.GL_LIGHTING);
				this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
				this.drawTexturedModalRect(var4, var5, var7 % 16 * 16, var7 / 16 * 16, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
				return;
			}
		}

		itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, var6, var4, var5);
		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, var6, var4, var5);
	}

	private SlotInventory getSlotAtPosition(int var1, int var2) {
		for(int var3 = 0; var3 < this.inventorySlots.size(); ++var3) {
			SlotInventory var4 = (SlotInventory)this.inventorySlots.get(var3);
			if(var4.getIsMouseOverSlot(var1, var2)) {
				return var4;
			}
		}

		return null;
	}

	protected void mouseClicked(int var1, int var2, int var3) {
		if(var3 == 0 || var3 == 1) {
			SlotInventory var4 = this.getSlotAtPosition(var1, var2);
			int var6;
			if(var4 != null) {
				var4.onSlotChanged();
				ItemStack var5 = var4.getStack();
				if(var5 != null || this.l != null) {
					if(var5 != null && this.l == null) {
						var6 = var3 == 0 ? var5.stackSize : (var5.stackSize + 1) / 2;
						this.l = var4.inventory.decrStackSize(var4.slotNumber, var6);
						if(var5.stackSize == 0) {
							var4.putStack((ItemStack)null);
						}

						var4.onPickupFromSlot();
					} else if(var5 == null && this.l != null && var4.isItemValid(this.l)) {
						var6 = var3 == 0 ? this.l.stackSize : 1;
						if(var6 > var4.inventory.getInventoryStackLimit()) {
							var6 = var4.inventory.getInventoryStackLimit();
						}

						var4.putStack(this.l.splitStack(var6));
						if(this.l.stackSize == 0) {
							this.l = null;
						}
					} else if(var5 != null && this.l != null) {
						if(var4.isItemValid(this.l)) {
							if(var5.itemID != this.l.itemID) {
								if(this.l.stackSize <= var4.inventory.getInventoryStackLimit()) {
									var4.putStack(this.l);
									this.l = var5;
								}
							} else if(var5.itemID == this.l.itemID) {
								if(var3 == 0) {
									var6 = this.l.stackSize;
									if(var6 > var4.inventory.getInventoryStackLimit() - var5.stackSize) {
										var6 = var4.inventory.getInventoryStackLimit() - var5.stackSize;
									}

									if(var6 > this.l.getMaxStackSize() - var5.stackSize) {
										var6 = this.l.getMaxStackSize() - var5.stackSize;
									}

									this.l.splitStack(var6);
									if(this.l.stackSize == 0) {
										this.l = null;
									}

									var5.stackSize += var6;
								} else if(var3 == 1) {
									var6 = 1;
									if(var6 > var4.inventory.getInventoryStackLimit() - var5.stackSize) {
										var6 = var4.inventory.getInventoryStackLimit() - var5.stackSize;
									}

									if(var6 > this.l.getMaxStackSize() - var5.stackSize) {
										var6 = this.l.getMaxStackSize() - var5.stackSize;
									}

									this.l.splitStack(var6);
									if(this.l.stackSize == 0) {
										this.l = null;
									}

									var5.stackSize += var6;
								}
							}
						} else if(var5.itemID == this.l.itemID && this.l.getMaxStackSize() > 1) {
							var6 = var5.stackSize;
							if(var6 > 0 && var6 + this.l.stackSize <= this.l.getMaxStackSize()) {
								this.l.stackSize += var6;
								var5.splitStack(var6);
								if(var5.stackSize == 0) {
									var4.putStack((ItemStack)null);
								}

								var4.onPickupFromSlot();
							}
						}
					}
				}
			} else if(this.l != null) {
				int var8 = (this.width - this.xSize) / 2;
				var6 = (this.height - this.ySize) / 2;
				if(var1 < var8 || var2 < var6 || var1 >= var8 + this.xSize || var2 >= var6 + this.xSize) {
					EntityPlayerSP var7 = this.mc.thePlayer;
					if(var3 == 0) {
						var7.dropPlayerItem(this.l);
						this.l = null;
					}

					if(var3 == 1) {
						var7.dropPlayerItem(this.l.splitStack(1));
						if(this.l.stackSize == 0) {
							this.l = null;
						}
					}
				}
			}
		}

	}

	protected void mouseMovedOrUp(int var1, int var2, int var3) {
		if(var3 == 0) {
		}

	}

	protected void keyTyped(char var1, int var2) {
		if(var2 == 1 || var2 == this.mc.options.keyBindInventory.keyCode) {
			this.mc.displayGuiScreen((GuiScreen)null);
		}

	}

	public void onGuiClosed() {
		if(this.l != null) {
			this.mc.thePlayer.dropPlayerItem(this.l);
		}

	}

	public void a(IInventory var1) {
	}

	public boolean doesGuiPauseGame() {
		return false;
	}
}
