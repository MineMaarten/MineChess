package chessMod.client;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import chessMod.common.ChessMod;
import chessMod.common.ChessModUtils;
import chessMod.common.ItemPieceMover;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 * This class is derived from Equivalent Exchange 3's DrawBlockHighlightHandler, found at  https://github.com/pahimar/Equivalent-Exchange-3/blob/master/common/com/pahimar/ee3/core/handlers/DrawBlockHighlightHandler.java
 */

public class ChessModDrawBlockHighlightHandler{
    public static int pulse = 0;
    public static int renderHeight = 0;
    private static boolean doInc = true;
    private static float pulseTransparency;

    @ForgeSubscribe
    public void onDrawBlockHighlightEvent(DrawBlockHighlightEvent event){
        if(event.currentItem != null && ChessMod.configRenderMovement) {
            if(event.currentItem.getItem().itemID == ChessMod.itemPieceMover.itemID) {
                ItemPieceMover pieceMover = (ItemPieceMover)event.currentItem.getItem();
                if(event.currentItem.getItemDamage() < 2) {// piece mover
                    if(pieceMover.entitySelected != null) {
                        pulseTransparency = getPulseValue() * 0.75F / 3000f;
                        // event.player.addChatMessage("Moves: ");
                        for(int i = 0; i < pieceMover.renderPositions.size(); i++) {
                            try {
                                highlightTile(event.player, pieceMover.renderPositions.get(i)[0] + pieceMover.entitySelected.getXOffset(), renderHeight, pieceMover.renderPositions.get(i)[1] + pieceMover.entitySelected.getZOffset(), event.partialTicks);
                            } catch(Exception e) {

                            }
                        }
                    }
                } else if(event.currentItem.getItemDamage() == 2) {// board
                    // generator
                    pulseTransparency = getPulseValue() * 0.75F / 3000f;
                    int orientation = ChessModUtils.determineOrientation(event.player);
                    int startX = event.target.blockX;
                    int startZ = event.target.blockZ;
                    switch(orientation){
                        case 0:
                            startX -= 7;
                            // startZ = 8;
                            break;
                        case 1:
                            startX -= 7;
                            startZ -= 7;
                            break;
                        case 2:
                            startZ -= 7;
                    }

                    for(int i = 0; i < 8; i++) {
                        for(int j = 0; j < 8; j++) {
                            highlightTile(event.player, startX + i, event.target.blockY, startZ + j, event.partialTicks);
                        }
                    }
                }
            }
        }
    }

    public static void highlightTile(EntityPlayer player, double x, double y, double z, float partialTicks){

        x += 0.5D;
        y += 0.5D;
        z += 0.5D;
        double iPX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
        double iPY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
        double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

        float xScale = 1.0F;
        float yScale = 1;
        float zScale = 1.0F;
        float xShift = 0.0F;
        float yShift = 0.01F;
        float zShift = 0.0F;

        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_CULL_FACE);

        for(int i = 4; i < 5; i++) {
            ForgeDirection forgeDir = ForgeDirection.getOrientation(i);
            int zCorrection = i == 2 ? -1 : 1;
            GL11.glPushMatrix();
            GL11.glTranslated(-iPX + x + xShift, -iPY + y + yShift, -iPZ + z + zShift);
            GL11.glScalef(1F * xScale, 1F * yScale, 1F * zScale);
            GL11.glRotatef(90, forgeDir.offsetX, forgeDir.offsetY, forgeDir.offsetZ);
            GL11.glTranslated(0, 0, 0.5f * zCorrection);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            drawQuad(-0.5F, -0.5F, 1F, 1F, 0F);
            GL11.glPopMatrix();
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(true);
    }

    private static int getPulseValue(){

        if(doInc) {
            pulse += 40;
        } else {
            pulse -= 40;
        }

        if(pulse >= 3000) {
            doInc = false;
        }

        if(pulse <= 1500) {
            doInc = true;
        }

        return pulse;
    }

    public static void drawQuad(float x, float y, float width, float height, double zLevel){

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(5.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0F, 1F, 0F, pulseTransparency);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertex(x + 0F, y + height, zLevel);
        tessellator.addVertex(x + width, y + height, zLevel);
        tessellator.addVertex(x + width, y + 0F, zLevel);
        tessellator.addVertex(x + 0F, y + 0F, zLevel);
        tessellator.draw();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

}
