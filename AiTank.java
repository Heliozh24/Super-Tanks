import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Graphics;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;

class AiTank extends Tank
{
    protected Tank playerTank;
    protected Rectangle hitbox;
    protected int cameraX;
    protected int cameraY;
    protected GameGui frame;
    protected ArrayList<AiTank> aiTankList;
    protected ArrayList<Block> blocksList;
    private Ball[] ballArray = new Ball[5];
    private Rocket rocket;
    private Laser laser;

    public AiTank(double movementSpeed, double turretSpeed, String weapon, Point coords,Tank playerTank,int armor, int minigunDamage, int shotgunDamage, int cannonDamage, int rocketDamage, int laserDamage)
    {
        super(movementSpeed,turretSpeed,Color.orange,weapon,coords,armor,0,0,0,0,minigunDamage,shotgunDamage,cannonDamage,rocketDamage,laserDamage);
        this.playerTank = playerTank;
        setOffsetX(playerTank.getCoords().x);
        setOffsetY(playerTank.getCoords().y);
        setColor();
        hitbox = new Rectangle(getCoords().x-5-cameraX,getCoords().y-25-cameraY,57,57);
    }

    public Rectangle getHitbox()
    {
        return hitbox;
    }

    public void updateData(ArrayList<Block> blocksList, ArrayList<AiTank> aiTanksList, int cameraX, int cameraY, GameGui frame)
    {
        this.frame = frame;
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.aiTankList = aiTanksList;
        this.blocksList = blocksList;
    }

    public void setColor()
    {
        if(weapon.equals("Minigun"))
        {
            bodyColor = Color.yellow;
            originalBodyColor = Color.yellow;
        }
        else if(weapon.equals("Shotgun"))
        {
            bodyColor = Color.magenta; 
            originalBodyColor = Color.magenta;
        }
        else if(weapon.equals("Cannon"))
        {
            bodyColor = Color.green;
            originalBodyColor = Color.green;
        }
        else if(weapon.equals("Rocket"))
        {
            bodyColor = Color.pink;
            originalBodyColor = Color.pink;
        }
        else
        {
            bodyColor = Color.cyan;
            originalBodyColor = Color.cyan;
        }
    }

    public void updateBarsAndHitboxPos()
    {
        backgroundBar.x = getCoords().x -5;
        backgroundBar.y = getCoords().y - 35;
        armorBar.x = getCoords().x -5;
        armorBar.y = getCoords().y -35;
        hitbox.x = getCoords().x-5-cameraX;
        hitbox.y = getCoords().y-25-cameraY;
    }

    public boolean aiCollided()
    {

        Rectangle hitbox = getHitbox();

        for(Block block: blocksList)
        {
            Rectangle obsHitbox = new Rectangle(block.getPosX()-cameraX,block.getPosY()-cameraY,block.getWidth(),block.getHeight());
            if(hitbox.intersects(obsHitbox))
            {
               return true; 
            }
        }

        for(AiTank ai: aiTankList)
        {
            if(ai != this)
            {
                Rectangle obsHitbox = new Rectangle(ai.getCoords().x-cameraX-5,ai.getCoords().y-cameraY-25,57,57);
                if(hitbox.intersects(obsHitbox))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void enableHuntBehavior()
    {
        Vector2D turretDirection = new Vector2D(2*playerTank.getCoords().x-getCoords().x,2*playerTank.getCoords().y-getCoords().y);
        turretDirection = turretDirection.normalize();
        setTurretAngle(Math.atan2(turretDirection.getY(),turretDirection.getX()));
        Vector2D tankDirection = new Vector2D(2*playerTank.getCoords().x-getCoords().x,2*playerTank.getCoords().y-getCoords().y);
        tankDirection = tankDirection.normalize();
        Point playerTankCenter = new Point(playerTank.getCoords().x+25, playerTank.getCoords().y); 
        Point aiTankCenter = new Point(getCoords().x+25-playerTank.getCoords().x,getCoords().y-playerTank.getCoords().y);
        if(playerTankCenter.distance(aiTankCenter) > 200)
        {
            if(!aiCollided())
            {
               
                relocateX((int) (tankDirection.getX() * getMovementSpeed()));
                relocateY((int) (tankDirection.getY() * getMovementSpeed()));
            }
        }
    }

    public void activateAi()
    {
        enableHuntBehavior(); 
    }  
}