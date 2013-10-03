package chessMod.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import chessMod.common.ai.ChessMove;
import chessMod.common.ai.ChessPosition;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public abstract class EntityBaseChessPiece extends EntityLiving{
    private static final ResourceLocation RESOURCE_WHITE_PIECE = new ResourceLocation("chessmod:textures/entities/white.png");
    private static final ResourceLocation RESOURCE_BLACK_PIECE = new ResourceLocation("chessmod:textures/entities/black.png");
    public boolean isBlackTurn = false; // white's first.
    public boolean firstMove = true; // the first move of the piece (used in pawns, and castling)
    public boolean isCapturing = false; // this variable is only set during moving when the piece is going to capture a piece from the other color
    public boolean enPassePossibility = false;
    public boolean resignConfirmed = false;
    public boolean computerPiece = false;
    public boolean turnToMobOnDeath = false;
    public boolean solvedPuzzle = true;
    private boolean offsetNeedsUpdate = false;
    public int mateInTimes = -1;
    public int targetX = 0;
    public int targetY = 0;
    public int targetZ = 0;
    public int xOffset = 0;
    public int zOffset = 0;
    public int deathTimer = -1;// disable the timer by default.
    private static final int X_OFFSET_DATAWATCHER_ID = 28;
    private static final int Z_OFFSET_DATAWATCHER_ID = 29;
    private static final int IS_BLACK_DATAWATCHER_ID = 30;
    private EntityPlayer playerLoser = null;
    private float moveSpeed;

    public EntityBaseChessPiece(World par1World){
        super(par1World);
        moveSpeed = 0.05F;
    }

    @Override
    protected void entityInit(){
        super.entityInit();
        dataWatcher.addObject(IS_BLACK_DATAWATCHER_ID, new Byte((byte)1));
        dataWatcher.addObject(X_OFFSET_DATAWATCHER_ID, new Integer(xOffset));
        dataWatcher.addObject(Z_OFFSET_DATAWATCHER_ID, new Integer(zOffset));
    }

    @Override
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.5D);
    }

    public ResourceLocation getTexture(){
        if(isBlack()) return RESOURCE_BLACK_PIECE;
        return RESOURCE_WHITE_PIECE;
    }

    @Override
    public void updateEntityActionState(){
        // don't move
    }

    @Override
    public void setPosition(double x, double y, double z){
        super.setPosition(x, y, z);
        moveSpeed = 0.1F;
        offsetNeedsUpdate = true;
    }

    public void setPositionAndOffset(double x, double y, double z){
        setPosition(x, y, z);
        xOffset = MathHelper.floor_double(x);
        zOffset = MathHelper.floor_double(z);
    }

    // overriding this method makes this entity invulnerable, unless the entity
    // gets captured or when the game is over.
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
        if(par1DamageSource.getDamageType() == "outOfWorld") {
            return super.attackEntityFrom(par1DamageSource, par2);
        }
        return false;
    }

    @Override
    public boolean canBePushed(){
        return false;
    }

    public void setIsBlack(boolean isBlack){
        dataWatcher.updateObject(IS_BLACK_DATAWATCHER_ID, new Byte((byte)(isBlack ? 1 : 0)));
    }

    public boolean isBlack(){
        return dataWatcher.getWatchableObjectByte(IS_BLACK_DATAWATCHER_ID) == 1;
    }

    public int getXOffset(){
        return dataWatcher.getWatchableObjectInt(X_OFFSET_DATAWATCHER_ID);
    }

    public int getZOffset(){
        return dataWatcher.getWatchableObjectInt(Z_OFFSET_DATAWATCHER_ID);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound){
        super.writeEntityToNBT(compound);
        compound.setBoolean("isBlackTurn", isBlackTurn);
        compound.setBoolean("isBlack", isBlack());
        compound.setBoolean("isCapturing", isCapturing);
        compound.setBoolean("firstMove", firstMove);
        compound.setBoolean("enPasse", enPassePossibility);
        compound.setBoolean("computerPiece", computerPiece);
        compound.setBoolean("solvedPuzzle", solvedPuzzle);
        compound.setInteger("targetX", targetX);
        compound.setInteger("targetY", targetY);
        compound.setInteger("targetZ", targetZ);
        compound.setInteger("xOffset", xOffset);
        compound.setInteger("zOffset", zOffset);
        compound.setInteger("deathTimer", deathTimer);
        compound.setInteger("mateInTimes", mateInTimes);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound){
        super.readEntityFromNBT(compound);
        isBlackTurn = compound.getBoolean("isBlackTurn");
        firstMove = compound.getBoolean("firstMove");
        isCapturing = compound.getBoolean("isCapturing");
        enPassePossibility = compound.getBoolean("enPasse");
        computerPiece = compound.getBoolean("computerPiece");
        solvedPuzzle = compound.getBoolean("solvedPuzzle");
        targetX = compound.getInteger("targetX");
        targetY = compound.getInteger("targetY");
        targetZ = compound.getInteger("targetZ");
        xOffset = compound.getInteger("xOffset");
        zOffset = compound.getInteger("zOffset");
        setIsBlack(compound.getBoolean("isBlack"));
        deathTimer = compound.getInteger("deathTimer");
        mateInTimes = compound.getInteger("mateInTimes");
    }

    public void setTargetPosition(int x, int z){
        targetX = x;
        targetY = (int)posY;
        targetZ = z;
    }

    public int[] getTargetPosition(){
        int[] target = new int[2];
        target[0] = targetX;
        target[1] = targetZ;
        return target;
    }

    @Override
    public boolean interact(EntityPlayer player){
        if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID == ChessMod.itemPieceMover.itemID && player.inventory.getCurrentItem().getItemDamage() < 2) {
            ItemPieceMover pieceMover = (ItemPieceMover)player.inventory.getCurrentItem().getItem();
            if(pieceMover.entitySelected != null && isBlack() ^ pieceMover.entitySelected.isBlack()) {
                // try to move to the enemy's piece if there is an ally piece selected.
                if(!worldObj.isRemote) {
                    if(!getEnemyPiece().computerPiece) setLosingPlayer(player, isBlack()); //only set the XP repel to not puzzle boards.
                    pieceMover.entitySelected.tryToGoTo(targetX, targetZ, player);
                } else {
                    pieceMover.renderPositions.clear();
                }
            } else if(player.inventory.getCurrentItem().getItemDamage() == 0 && isBlack() || player.inventory.getCurrentItem().getItemDamage() == 1 && !isBlack()) {
                // else, select another ally piece
                if(computerPiece && !worldObj.isRemote) {
                    ChessModUtils.sendUnlocalizedMessage(player, "message.error.computerPiece", EnumChatFormatting.RED.toString());
                    return false;
                }
                if(!worldObj.isRemote && this instanceof EntityKing && player.isSneaking()) {
                    if(resignConfirmed) {
                        setLosingPlayer(player, true);// the surrendering player shouldnt be able to pick up the black orbs.
                        setLosingPlayer(player, false);// neither the white orbs
                        ChessModUtils.sendUnlocalizedMessage(player, "message.player.youSurrender" + (isBlack() ? "WhiteWon" : "BlackWon"), EnumChatFormatting.RED.toString());
                        sendChatToNearbyPlayers(player, "message.broadcast." + (isBlack() ? "blackSurrender" : "whiteSurrender"), EnumChatFormatting.DARK_GREEN.toString());
                        setDeathTimer(!isBlack());
                    } else {
                        ChessModUtils.sendUnlocalizedMessage(player, "message.player.confirmSurrender");
                        resignConfirmed = true;
                    }
                    return false;
                } else {
                    resignConfirmed = false;
                }

                if(!worldObj.isRemote) ChessModUtils.sendUnlocalizedMessage(player, "message.player.selectPiece", EnumChatFormatting.DARK_AQUA.toString(), "entity." + EntityList.getEntityString(this) + ".name");
                pieceMover.entitySelected = this;

                if(!worldObj.isRemote) updateClient(getValidMoves(), player);
                return false;
            } else {
                if(!worldObj.isRemote) ChessModUtils.sendUnlocalizedMessage(player, "message.error.notYourPiece", EnumChatFormatting.RED.toString());
            }
            if(!worldObj.isRemote) pieceMover.entitySelected = null;
        } else if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID == ChessMod.itemPieceMover.itemID && player.inventory.getCurrentItem().getItemDamage() == 4) {
            if(!computerPiece) {
                EntityBaseChessPiece enemy = getEnemyPiece();
                if(enemy != null && enemy.mateInTimes < 0) {
                    setMateTimes(-1);
                    setComputerPiece(true);
                    if(!worldObj.isRemote) ChessModUtils.sendUnlocalizedMessage(player, "message.player.transform" + (isBlack() ? "BlackAI" : "WhiteAI"), EnumChatFormatting.DARK_BLUE.toString());
                } else {
                    if(!worldObj.isRemote) ChessModUtils.sendUnlocalizedMessage(player, "message.error.aiPuzzle", EnumChatFormatting.RED.toString());
                }
            } else {
                if(!worldObj.isRemote) ChessModUtils.sendUnlocalizedMessage(player, "message.error.alreadyAI", EnumChatFormatting.RED.toString());
            }
        } else {
            if(!worldObj.isRemote) ChessModUtils.sendUnlocalizedMessage(player, "message.error.noPieceMover", EnumChatFormatting.RED.toString());
        }

        return false;
    }

    /**
     * sets the repelling XP orbs to the player given to the pieces of the given color.
     * @param player
     * @param isBlack
     */
    public void setLosingPlayer(EntityPlayer player, boolean isBlack){
        List<EntityBaseChessPiece> pieces = getChessPieces(false);
        for(int i = 0; i < pieces.size(); i++) {
            if(pieces.get(i).isBlack() == isBlack) pieces.get(i).playerLoser = player;
        }
    }

    /**
     * Sends al the valid moves of this chesspiece to the client, which will update the rendering.
     * @param validMoves
     * @param player
     */
    private void updateClient(List<int[]> validMoves, EntityPlayer player){
        PacketDispatcher.sendPacketToPlayer(PacketHandlerChessMod.getPreviewUpdatePacket(validMoves, targetY - 1), (Player)player);
    }

    public void sendChatToNearbyPlayers(EntityPlayer playerToExclude, String chatMessage){
        sendChatToNearbyPlayers(playerToExclude, chatMessage, EnumChatFormatting.WHITE.toString());
    }

    public void sendChatToNearbyPlayers(EntityPlayer playerToExclude, String chatMessage, String... replacements){
        AxisAlignedBB bbBox = AxisAlignedBB.getAABBPool().getAABB(xOffset - 5, (int)posY - 5, zOffset - 5, xOffset + 13, posY + 8, zOffset + 13);
        List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, bbBox);
        for(int i = 0; i < players.size(); i++) {
            if(playerToExclude == null || players.get(i) != playerToExclude) {
                ChessModUtils.sendUnlocalizedMessage(players.get(i), chatMessage, replacements);
            }
        }
    }

    // try to go to the position specified
    public boolean tryToGoTo(int x, int z, EntityPlayer player){
        if(isDead) {
            ChessModUtils.sendUnlocalizedMessage(player, "message.error.pieceCaptured", EnumChatFormatting.RED.toString());
            return false;
        }
        if(isBlack() == isBlackTurn && !isPieceCapturing()) {
            if(canGoTo(x, z, true, player)) {
                EntityBaseChessPiece pieceToBeCaptured = getPieceAt(x, z);
                int oldX = targetX;// save the old targets for when the move isn't possible due to king danger.
                int oldZ = targetZ;
                setTargetPosition(x, z);
                if(pieceToBeCaptured != null) pieceToBeCaptured.setTargetPosition(10, 10);//virtually move the piece of the board
                boolean kingInDanger = isKingInDanger(isBlack(), true); //check if the king is in danger when this piece would be gone.
                if(pieceToBeCaptured != null) pieceToBeCaptured.setTargetPosition(x, z);//move the piece back on the board.
                if(kingInDanger) {// if the king's in danger,
                    setTargetPosition(oldX, oldZ);// take the old positions again.
                    isCapturing = false;
                    if(player != null) ChessModUtils.sendUnlocalizedMessage(player, "message.error.kingInCheck", EnumChatFormatting.RED.toString());
                    return false;
                } else {
                    if(!handleCastling()) {// if the player wasn't able to castle (due to moving the king over an in chess zone)
                        setTargetPosition(oldX, oldZ);
                        if(player != null) ChessModUtils.sendUnlocalizedMessage(player, "message.error.kingCheckCastling", EnumChatFormatting.RED.toString());
                        return false;
                    }
                    clearEnPasse(); // none of the pawns have jumped two rows at once.
                    enPassePossibility = this instanceof EntityPawn && firstMove && (targetZ == 3 || targetZ == 4);
                    firstMove = false; // set false if it weren't already.
                    if(this instanceof EntityKnight) motionY = 0.8D;
                    if(player != null) ChessModUtils.sendUnlocalizedMessage(player, "message.player.movePiece", EnumChatFormatting.DARK_GREEN.toString(), "entity." + EntityList.getEntityString(this) + ".name", getColumnName(x) + (z + 1));
                    sendChatToNearbyPlayers(player, "message.broadcast.move" + (isBlack() ? "BlackPiece" : "WhitePiece"), EnumChatFormatting.DARK_GREEN.toString(), "entity." + EntityList.getEntityString(this) + ".name", getColumnName(x) + (z + 1));
                    if(!isCapturing) {
                        handleAfterTurn(player);
                    }
                    return true;
                }
            }
        } else {
            if(player != null) ChessModUtils.sendUnlocalizedMessage(player, "message.error." + (isBlackTurn ? "blacksTurn" : "whitesTurn"), EnumChatFormatting.RED.toString());
        }
        return false;
    }

    private void handleAfterTurn(EntityPlayer player){
        if(this instanceof EntityPawn && targetZ % 7 == 0 && !worldObj.isRemote && !isDead) {
            //Promovate the pawn
            EntityQueen queen = new EntityQueen(worldObj);
            queen.setPosition(posX, posY, posZ);
            queen.xOffset = xOffset;
            queen.zOffset = zOffset;
            queen.setTargetPosition(targetX, targetZ);
            queen.setIsBlack(isBlack());
            worldObj.spawnEntityInWorld(queen);
            setDead();
            for(int i = 0; i < 40; i++) {
                ChessModUtils.spawnParticle("explode", posX, posY + rand.nextDouble() * 1.5D, posZ, rand.nextDouble() / 10 - 0.05D, rand.nextDouble() / 10 - 0.05D, rand.nextDouble() / 10 - 0.05D);
            }
        }
        List<EntityBaseChessPiece> pieces = getChessPieces(true);
        EntityKing king = null;
        for(EntityBaseChessPiece piece : pieces) {
            if(piece instanceof EntityKing && piece.isBlack()) {//only the black king keeps track of the positions.
                king = (EntityKing)piece;
                break;
            }
        }
        if(king != null) {
            ChessPosition curPos = new ChessPosition(this);
            king.lastPositions.add(curPos);
        }
        int movesAvailable = isGameOver();
        if(movesAvailable == 0) {
            if(isKingInDanger(!isBlack(), false)) {
                if(player != null) ChessModUtils.sendUnlocalizedMessage(player, "message.broadcast.checkmate", EnumChatFormatting.BLUE.toString());
                sendChatToNearbyPlayers(player, "message.broadcast.checkmate", EnumChatFormatting.DARK_RED.toString());
            } else {
                if(player != null) ChessModUtils.sendUnlocalizedMessage(player, "message.broadcast.stalemate", EnumChatFormatting.BLUE.toString());
                sendChatToNearbyPlayers(player, "message.broadcast.stalemate", EnumChatFormatting.DARK_RED.toString());
            }
            setDeathTimer(isBlack());
        } else if(isKingInDanger(!isBlack(), false)) {
            if(player != null) ChessModUtils.sendUnlocalizedMessage(player, "message.broadcast.check", EnumChatFormatting.YELLOW.toString());
            sendChatToNearbyPlayers(player, "message.broadcast.check", EnumChatFormatting.RED.toString());
        }
        if(king != null) {
            king.checkForDraw(true);
        }

        switchTurns();
        handlePuzzles(player);

    }

    private EntityBaseChessPiece getPieceAt(int x, int z){
        List<EntityBaseChessPiece> pieces = getChessPieces(true);
        for(EntityBaseChessPiece piece : pieces) {
            if(piece.targetX == x && piece.targetZ == z) {
                return piece;
            }
        }
        return null;
    }

    protected boolean isPieceCapturing(){
        List<EntityBaseChessPiece> pieces = getChessPieces(true);
        for(EntityBaseChessPiece piece : pieces) {
            if(piece.isCapturing) return true;
        }
        return false;
    }

    private void handlePuzzles(EntityPlayer player){
        EntityBaseChessPiece enemy = getEnemyPiece();
        if(enemy != null && enemy.computerPiece) {
            List<EntityBaseChessPiece> pieces = getChessPieces(true);
            for(int i = 0; i < pieces.size(); i++) {
                if(enemy.isBlack() == pieces.get(i).isBlack()) {
                    List<int[]> moves = pieces.get(i).getValidMoves();
                    if(moves.size() > 0) {
                        if(mateInTimes == 1) {
                            setDeathTimer(enemy.isBlack());// enemy won.
                            setPuzzleFail();
                            ChessModUtils.onPuzzleFail(worldObj, player, this, xOffset, targetY, zOffset, rand);
                        }
                        mateInTimes--;
                        setMateTimes(mateInTimes);
                        return;
                    }
                }
            }
        }
    }

    public void setMateTimes(int mateInTime){
        List<EntityBaseChessPiece> pieces = getChessPieces(false);
        for(int i = 0; i < pieces.size(); i++) {
            pieces.get(i).mateInTimes = mateInTime;
        }
    }

    public void setPuzzleFail(){
        List<EntityBaseChessPiece> pieces = getChessPieces(false);
        for(int i = 0; i < pieces.size(); i++) {
            pieces.get(i).solvedPuzzle = false;
        }
    }

    public void setComputerPiece(boolean isComputer){
        List<EntityBaseChessPiece> pieces = getChessPieces(false);
        for(int i = 0; i < pieces.size(); i++) {
            if(pieces.get(i).isBlack() == isBlack()) pieces.get(i).computerPiece = isComputer;
        }
    }

    public void setDeathTimer(boolean winnerIsBlack){
        List<EntityBaseChessPiece> pieces = getChessPieces(false);
        for(int i = 0; i < pieces.size(); i++) {
            pieces.get(i).deathTimer = rand.nextInt(100) /* + 280 */
                    + (winnerIsBlack ^ pieces.get(i).isBlack() ? pieces.get(i) instanceof EntityKing ? 0 : 120 : 160);
        }
    }

    private boolean handleCastling(){
        if(!(this instanceof EntityKing) || !firstMove) return true; // don't influence moves that haven't to do with castling at all.
        List<EntityBaseChessPiece> pieces = getChessPieces(true);
        for(int i = 0; i < pieces.size(); i++) {
            if(pieces.get(i) instanceof EntityRook && isBlack() == pieces.get(i).isBlack()) {
                if(pieces.get(i).targetX == 0 && targetX == 1) {
                    targetX = 2;
                    if(isKingInDanger(isBlack(), true)) return false;// the player can't move the King over an in check tile.
                    targetX = 1;
                    pieces.get(i).targetX = 2;
                    pieces.get(i).firstMove = false;
                    pieces.get(i).motionY = 0.8D;
                } else if(pieces.get(i).targetX == 7 && targetX == 5) {
                    targetX = 4;
                    if(isKingInDanger(isBlack(), true)) return false;// the player can't move the King over an in check tile.
                    targetX = 5;
                    pieces.get(i).targetX = 4;
                    pieces.get(i).firstMove = false;
                    pieces.get(i).motionY = 0.8D;
                }
            }
        }
        return true;
    }

    public String getColumnName(int x){
        switch(x){
            case 0:
                return "H";
            case 1:
                return "G";
            case 2:
                return "F";
            case 3:
                return "E";
            case 4:
                return "D";
            case 5:
                return "C";
            case 6:
                return "B";
            default:
                return "A";// which is 7
        }
    }

    /**
     * returns true if this entity can go to the position specified. If the
     * boolean's set to true, the entity will be flagged to capture the piece
     * potentially standing on the position specified.
     * @param x
     * @param z
     * @param setCaptureIfneccessary
     * @param player
     * @return
     */
    public boolean canGoTo(int x, int z, boolean setCaptureIfneccessary, EntityPlayer player){
        List<int[]> possibleMoves = getPossibleMoves();
        if(possibleMoves == null) return false;
        for(int i = 0; i < possibleMoves.size(); i++) {
            // if the desired move is in the move list, and there's no piece in
            // between if the piece isn't a knight, and we are not moving to the
            // space we already are
            if(possibleMoves.get(i)[0] == x && possibleMoves.get(i)[1] == z && (!isPieceInBetween(x, z) || this instanceof EntityKnight) && (x != targetX || z != targetZ)) {
                // Check for occupying pieces
                List<EntityBaseChessPiece> chessPieces = getChessPieces(true);
                for(int j = 0; j < chessPieces.size(); j++) {
                    if(chessPieces.get(j).getTargetPosition()[0] == x && chessPieces.get(j).getTargetPosition()[1] == z) {// when a chesspiece is occupying this space.
                        if(chessPieces.get(j).isBlack() != isBlack()) { //if the piece is different colored.
                            if(setCaptureIfneccessary) {
                                isCapturing = true;
                            }
                            return true;
                        } else {
                            if(player != null) ChessModUtils.sendUnlocalizedMessage(player, "message.error.ownPieceInWay", EnumChatFormatting.RED.toString());
                            return false;
                        }
                    } else if(chessPieces.get(j).enPassePossibility && chessPieces.get(j).targetZ == targetZ && (chessPieces.get(j).targetX == targetX - 1 || chessPieces.get(j).targetX == targetX + 1) && chessPieces.get(j).isBlack() ^ isBlack() && targetX != x && setCaptureIfneccessary) { // passe
                        chessPieces.get(j).kill();
                    }
                }
                return true;
            }
        }
        if(player != null) ChessModUtils.sendUnlocalizedMessage(player, "message.error.movementRuleBlocks", EnumChatFormatting.RED.toString());

        return false;
    }

    /**
     * Returns true when there is a piece between the given coordinate and the current coordinate of this entity.
     * @param x
     * @param z
     * @return
     */
    private boolean isPieceInBetween(int x, int z){

        List<EntityBaseChessPiece> chessPieces = getChessPieces(true);
        int checkingX = targetX;
        int checkingZ = targetZ;
        while(checkingX != x || checkingZ != z) {
            for(int i = 0; i < chessPieces.size(); i++) {
                // filter out the to be moved and the target piece.
                if(chessPieces.get(i).getTargetPosition()[0] == checkingX && chessPieces.get(i).getTargetPosition()[1] == checkingZ && (checkingX != targetX || checkingZ != targetZ) && (checkingX != x || checkingZ != z)) {
                    return true;
                }
            }
            if(checkingX < x) checkingX++;
            if(checkingX > x) checkingX--;
            if(checkingZ < z) checkingZ++;
            if(checkingZ > z) checkingZ--;
        }
        return false;
    }

    /**
     * Returns true if this entity can capture the king of the color given.
     * @param kingIsBlack
     * @return
     */
    public boolean canCaptureKing(boolean kingIsBlack){
        List<EntityBaseChessPiece> pieces = getChessPieces(false);
        for(int i = 0; i < pieces.size(); i++) {
            // if the piece is the other side's king
            if(pieces.get(i) instanceof EntityKing && pieces.get(i).isBlack() == kingIsBlack) {
                int[] kingPos = pieces.get(i).getTargetPosition();
                if(canGoTo(kingPos[0], kingPos[1], false, null)) {
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * Returns true if the king of the given color can be captured by the other team.
     * @param isBlack
     * @param impliedMove
     * @return
     */
    public boolean isKingInDanger(boolean isBlack, boolean impliedMove){
        List<EntityBaseChessPiece> pieces = getChessPieces(true);
        for(int i = 0; i < pieces.size(); i++) {
            // when the piece is from the other color, and he can capture the
            // king, and this entity isn't going to be killed by the implied
            // move, return true.
            if(pieces.get(i).isBlack() ^ isBlack && pieces.get(i).canCaptureKing(isBlack) && (pieces.get(i).getTargetPosition()[0] != targetX || pieces.get(i).getTargetPosition()[1] != targetZ || !impliedMove)) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns all valid moves of this entity.
     * @return
     */
    public List<int[]> getValidMoves(){
        List<int[]> moves = new ArrayList<int[]>();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(canGoTo(i, j, false, null)) {
                    int oldX = targetX;
                    int oldZ = targetZ;
                    EntityBaseChessPiece pieceToBeCaptured = getPieceAt(i, j);
                    targetX = i;
                    targetZ = j;
                    if(pieceToBeCaptured != null) pieceToBeCaptured.setTargetPosition(10, 10);
                    boolean kingInDanger = isKingInDanger(isBlack(), true);
                    if(pieceToBeCaptured != null) pieceToBeCaptured.setTargetPosition(i, j);
                    targetX = oldX;
                    targetZ = oldZ;
                    if(kingInDanger) continue;
                    int[] move = new int[2];
                    move[0] = i;
                    move[1] = j;
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    public EntityBaseChessPiece getEnemyPiece(){
        List<EntityBaseChessPiece> pieces = getChessPieces(true);
        for(int i = 0; i < pieces.size(); i++) {
            if(pieces.get(i).isBlack() ^ isBlack()) {
                return pieces.get(i);
            }
        }
        return null;
    }

    /**
     * Returns the amount of moves the opposite team has.
     * @return
     */
    public int isGameOver(){
        List<EntityBaseChessPiece> pieces = getChessPieces(true);
        int totalValidMoves = 0;
        for(int i = 0; i < pieces.size(); i++) {
            if(pieces.get(i).isBlack() ^ isBlack()) {
                totalValidMoves += pieces.get(i).getValidMoves().size();
            }
        }
        return totalValidMoves;
    }

    public abstract List<int[]> getPossibleMoves();

    public void switchTurns(){
        boolean turn = !isBlackTurn;
        List<EntityBaseChessPiece> pieces = getChessPieces(false);
        for(int i = 0; i < pieces.size(); i++) {
            pieces.get(i).isBlackTurn = turn; // notify to all the pieces that the other color is to move.
        }
    }

    public void clearEnPasse(){
        List<EntityBaseChessPiece> pieces = getChessPieces(false);
        for(int i = 0; i < pieces.size(); i++) {
            pieces.get(i).enPassePossibility = false; // notify to all the pieces that they are not able to En Passe.
        }
    }

    /**
     * Returns a list of all the chess pieces on the board. This method also filters
     * pieces which belong to an other board.
     * @param filterToBeCapturedPieces filter out pieces that are to be captured.
     * @return
     */
    public List<EntityBaseChessPiece> getChessPieces(boolean filterToBeCapturedPieces){
        AxisAlignedBB bbBox = AxisAlignedBB.getAABBPool().getAABB(xOffset - 1, (int)posY - 1, zOffset - 1, xOffset + 8, posY + 2, zOffset + 8);
        List<EntityBaseChessPiece> pieces = worldObj.getEntitiesWithinAABB(EntityBaseChessPiece.class, bbBox);
        filterPieces(pieces, filterToBeCapturedPieces);
        return pieces;
    }

    //TODO refactoring: This can be done non-recursively.
    private void filterPieces(List<EntityBaseChessPiece> pieces, boolean filterToBeCapturedPieces){
        for(int i = 0; i < pieces.size(); i++) {
            if(pieces.get(i).dead || pieces.get(i).xOffset != xOffset || pieces.get(i).zOffset != zOffset) {
                pieces.remove(i);
                filterPieces(pieces, filterToBeCapturedPieces);
                return;
            }
            if(pieces.get(i).isCapturing && filterToBeCapturedPieces) {
                for(int j = 0; j < pieces.size(); j++) {
                    if(pieces.get(i).targetX == pieces.get(j).targetX && pieces.get(i).targetZ == pieces.get(j).targetZ && pieces.get(i) != pieces.get(j)) {
                        pieces.remove(j);
                        filterPieces(pieces, filterToBeCapturedPieces);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onEntityUpdate(){
        if(!worldObj.isRemote) {
            if(offsetNeedsUpdate) {
                dataWatcher.updateObject(X_OFFSET_DATAWATCHER_ID, new Integer(xOffset));
                dataWatcher.updateObject(Z_OFFSET_DATAWATCHER_ID, new Integer(zOffset));
                offsetNeedsUpdate = false;
            }
            if(deathTimer > 0) {
                deathTimer--;
                if(turnToMobOnDeath && rand.nextInt(40) == 0) { //indicate the piece is going to transform
                    int iterations = rand.nextInt(3) + 3;
                    for(int i = 0; i < iterations; i++) {
                        ChessModUtils.spawnParticle("flame", posX, posY + rand.nextDouble() * 1.5D, posZ, rand.nextDouble() / 10 - 0.05D, rand.nextDouble() / 10 - 0.05D, rand.nextDouble() / 10 - 0.05D);
                    }
                }
            } else if(deathTimer == 0) {
                if(turnToMobOnDeath) {
                    if(!worldObj.isRemote) {
                        Entity mob = getMob();
                        mob.setPosition(posX, posY, posZ);
                        worldObj.spawnEntityInWorld(mob);
                        setDead();
                        worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)posX, (int)posY, (int)posZ, 0);
                        for(int i = 0; i < 40; i++) {
                            ChessModUtils.spawnParticle("flame", posX, posY + rand.nextDouble() * 1.5D, posZ, rand.nextDouble() / 10 - 0.05D, rand.nextDouble() / 10 - 0.05D, rand.nextDouble() / 10 - 0.05D);
                        }
                    }
                } else {
                    kill();
                }
            }

            //Actually move the pieces to the target location
            int xCoord = targetX + xOffset;
            int zCoord = targetZ + zOffset;

            if(xCoord + 0.5D > posX + 0.1D) {
                motionX = moveSpeed;
            } else if(xCoord + 0.5D < posX - 0.1D) {
                motionX = -moveSpeed;
            }
            if(zCoord + 0.5D > posZ + 0.1D) {
                motionZ = moveSpeed;
            } else if(zCoord + 0.5D < posZ - 0.1D) {
                motionZ = -moveSpeed;
            }

            if(getDistance(xCoord, targetY, zCoord) < 1.0D) {//When the piece is arrived at destination
                if(isCapturing) { //and the piece is capturing
                    List<EntityBaseChessPiece> chessPieces = getChessPieces(false);
                    for(int i = 0; i < chessPieces.size(); i++) {
                        if(chessPieces.get(i).getTargetPosition()[0] == targetX && chessPieces.get(i).getTargetPosition()[1] == targetZ && chessPieces.get(i) != this) {
                            if(chessPieces.get(i) instanceof EntityKing) {
                                setDeathTimer(isBlack()); //remove all the pieces, as the game is over without king.
                                if(playerLoser != null) ChessModUtils.sendUnlocalizedMessage(playerLoser, "message.error.bugReport.capturedKing", EnumChatFormatting.RED.toString());
                            }
                            chessPieces.get(i).kill();
                            break;
                        }
                    }
                    handleAfterTurn(playerLoser);
                    isCapturing = false;
                }
                moveSpeed = 0.05F;
            } else {
                moveSpeed = 0.1F;
            }
        }

        super.onEntityUpdate();

        // this will make the pieces always visually stand right. Also it turns
        // the black pieces 180 degrees.
        rotationYaw = isBlack() ? 180F : 0F;
        renderYawOffset = rotationYaw;
    }

    /**
     * Invoked by the AI, this method executes the given move, and beforehand checks if this move is valid.
     * @param move
     */
    public void executeMove(ChessMove move){
        if(move != null) {
            EntityBaseChessPiece piece = getPieceAt(move.from % 10, move.from / 10);
            if(piece != null) {
                if(piece.tryToGoTo(move.to % 10, move.to / 10, null)) return;
            } else {
                System.err.println("[Chess AI --> board] No piece (or white piece) found at " + move.from % 10 + ", " + move.from / 10);
            }
        }

        //catch when the AI doesn't deliver a valid move. Execute the first valid move we can find then.
        List<EntityBaseChessPiece> pieces = getChessPieces(true);
        for(EntityBaseChessPiece piece : pieces) {
            if(piece.isBlack() == isBlack()) {
                List<int[]> moves = getValidMoves();
                if(moves.size() > 0) {
                    piece.tryToGoTo(moves.get(0)[0], moves.get(0)[1], null);
                    return;
                }
            }
        }

        //When we still weren't able to execute a move, remove the ai.
        if(!worldObj.isRemote) sendChatToNearbyPlayers(null, "message.error.bugReport.aiWrongMove", EnumChatFormatting.RED.toString());
        setComputerPiece(false);
    }

    /**
     * Should return the entity the piece should transform into when the mob attack punish gets executed.
     * @return
     */
    public abstract Entity getMob();

    @Override
    public void setDead(){
        super.setDead();
        if(!worldObj.isRemote && solvedPuzzle) {
            EntityPickyXPOrb xpOrb = new EntityPickyXPOrb(worldObj, posX, posY, posZ, 1, playerLoser);
            worldObj.spawnEntityInWorld(xpOrb);
        }
    }

    /**
     * Overriden to make the method public
     */
    @Override
    public void kill(){
        super.kill();
    }
}
