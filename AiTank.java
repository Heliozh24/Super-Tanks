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

class AiTank extends Tank
{
    protected Tank playerTank;
    protected int[] relocationMetricsLeftAndTop = {0,-3,-2,-1};
    protected int[] relocationMetricsRightAndDown = {0,3,2,1};
    protected int[] generalRelocationMetrics = {-3,-2,-1,0,1,2,3};
    protected String[] relocationDirections = {"Top Right","Top Left", "Top Straight", "Down Right","Down Left","Down Straight","Left","Right"};
    protected int[] moveTimes = {300,500,700,900,1000,1100,1200,1300,1400};
    protected int[] relocationDurations = {300,400,500,600,700,800,900,1000,1100};
    protected Timer relocationTimer;
    protected Timer stopWatchTimer;
    protected Timer moveTimer;
    protected int relocationTime = 10;
    protected int selectedRelocationMetricX = 0;
    protected int selectedRelocationMetricY = 0;
    protected String selectedRelocationDirection = "";
    protected int selectedRelocationDuration;
    protected int selectedMoveTime;
    protected Rectangle hitbox;
    protected boolean upCollision;
    protected boolean downCollision;
    protected boolean leftCollision;
    protected boolean rightCollision;
    protected int cameraX;
    protected int cameraY;
    protected GameGui frame;
    protected ArrayList<AiTank> aiTankList;
    protected ArrayList<Block> blocksList;

    public AiTank(double movementSpeed, double turretSpeed, String weapon, Point coords,Tank playerTank,int armor, int minigunDamage, int shotgunDamage, int cannonDamage, int rocketDamage, int laserDamage)
    {
        super(movementSpeed,turretSpeed,Color.orange,weapon,coords,armor,0,0,0,0,minigunDamage,shotgunDamage,cannonDamage,rocketDamage,laserDamage);
        this.playerTank = playerTank;
        setOffsetX(playerTank.getCoords().x);
        setOffsetY(playerTank.getCoords().y);
        setColor();

        relocationTimer = new Timer(relocationTime, new ActionListener() {
            public void actionPerformed(ActionEvent avt)
            {
                relocateX(selectedRelocationMetricX * (int) movementSpeed);
                relocateY(selectedRelocationMetricY * (int) movementSpeed);
            } 
        });

        stopWatchTimer = new Timer(selectedRelocationDuration,new ActionListener() {
            public void actionPerformed(ActionEvent avt)
            {  
                relocationTimer.stop();
            }      
        });
        stopWatchTimer.setRepeats(false);

        moveTimer = new Timer(selectedMoveTime,new ActionListener() {
            public void actionPerformed(ActionEvent avt)
            {

                relocationTimer.start();
                stopWatchTimer.start();
            }    
        });

        moveTimer.setRepeats(false);
        hitbox = new Rectangle(getCoords().x-5,getCoords().y-25,57,57);
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

    public void changeDirection(Rectangle obsHitbox, Rectangle upHitbox, Rectangle downHitbox, Rectangle leftHitbox, Rectangle rightHitbox)
    {
        if(upHitbox.intersects(obsHitbox))
        {
            int numRelocationY = ThreadLocalRandom.current().nextInt(0,relocationMetricsRightAndDown.length);
            selectedRelocationMetricY = relocationMetricsRightAndDown[numRelocationY];
            int numRelocationX =  ThreadLocalRandom.current().nextInt(0,generalRelocationMetrics.length);
            selectedRelocationMetricX = generalRelocationMetrics[numRelocationX];
            generateNums();
            setTurretAngle(Math.atan2(selectedRelocationMetricY,selectedRelocationMetricX));
            moveTimer.setInitialDelay(selectedMoveTime);
            moveTimer.start();
        }

        if(downHitbox.intersects(obsHitbox))
        {
            int numRelocationY = ThreadLocalRandom.current().nextInt(0,relocationMetricsLeftAndTop.length);
            selectedRelocationMetricY = relocationMetricsLeftAndTop[numRelocationY];
            int numRelocationX =  ThreadLocalRandom.current().nextInt(0,generalRelocationMetrics.length);
            selectedRelocationMetricX = generalRelocationMetrics[numRelocationX];
            generateNums();
            setTurretAngle(Math.atan2(selectedRelocationMetricY,selectedRelocationMetricX));
            moveTimer.setInitialDelay(selectedMoveTime);
            moveTimer.start();
        }
                  
        if(leftHitbox.intersects(obsHitbox))
        {
            int numRelocationX = ThreadLocalRandom.current().nextInt(0,relocationMetricsRightAndDown.length);
            selectedRelocationMetricX = relocationMetricsRightAndDown[numRelocationX];
            int numRelocationY =  ThreadLocalRandom.current().nextInt(0,generalRelocationMetrics.length);
            selectedRelocationMetricY = generalRelocationMetrics[numRelocationY];
            generateNums();
            setTurretAngle(Math.atan2(selectedRelocationMetricY,selectedRelocationMetricX));
            moveTimer.setInitialDelay(selectedMoveTime);
            moveTimer.start();
        }

        if(rightHitbox.intersects(obsHitbox))
        {
            int numRelocationX = ThreadLocalRandom.current().nextInt(0,relocationMetricsLeftAndTop.length);
            selectedRelocationMetricX = relocationMetricsLeftAndTop[numRelocationX];
            int numRelocationY =  ThreadLocalRandom.current().nextInt(0,generalRelocationMetrics.length);
            selectedRelocationMetricY = generalRelocationMetrics[numRelocationY];
            generateNums();
            setTurretAngle(Math.atan2(selectedRelocationMetricY,selectedRelocationMetricX));
            moveTimer.setInitialDelay(selectedMoveTime);
            moveTimer.start();
        }
    }

    public void checkAiCollisionWithBlocks()
    {

        Rectangle upHitbox = new Rectangle(hitbox.x+8,hitbox.y-3,hitbox.width-12,1);
        Rectangle leftHitbox = new Rectangle(hitbox.x,hitbox.y+8,1,hitbox.height-12); ;
        Rectangle downHitbox = new Rectangle(hitbox.x+8,hitbox.y+hitbox.width+3,hitbox.width-12,1);
        Rectangle rightHitbox = new Rectangle(hitbox.x+3+hitbox.height,hitbox.y+8,1,hitbox.height-12);

       for(Block block: blocksList)
        {
            Rectangle obsHitbox = new Rectangle(block.getPosX()-cameraX,block.getPosY()-cameraY,block.getWidth(),block.getHeight());
            if(obsHitbox.x >=0 && obsHitbox.x <= frame.getWidth()-80 && obsHitbox.y >= 0 && obsHitbox.y <= frame.getHeight()-80)
            {
                changeDirection(obsHitbox,upHitbox,downHitbox,leftHitbox,rightHitbox);
            }
        }

        for(Tank tank: aiTankList)
        {
            if(tank != this)
            {
                Rectangle obsHitbox = new Rectangle(tank.getCoords().x-cameraX-5,tank.getCoords().y-cameraY-25,57,57);
                if(obsHitbox.x >=0 && obsHitbox.x <= frame.getWidth()-80 && obsHitbox.y >= 0 && obsHitbox.y <= frame.getHeight()-80)
                {
                    changeDirection(obsHitbox,upHitbox,downHitbox,leftHitbox,rightHitbox);
                }
            }
        }
        Rectangle playerHitbox = new Rectangle(playerTank.getCoords().x-5,playerTank.getCoords().y-25,57,57);
        changeDirection(playerHitbox,upHitbox,downHitbox,leftHitbox,rightHitbox);
    }

    public void generateNums()
    {
        int numRelocationDur = ThreadLocalRandom.current().nextInt(0,relocationDurations.length);
        selectedRelocationDuration = relocationDurations[numRelocationDur];
        stopWatchTimer.setInitialDelay(selectedRelocationDuration);
        int numSelMoveTime = ThreadLocalRandom.current().nextInt(0,moveTimes.length);
        selectedMoveTime = moveTimes[numSelMoveTime];
    }

    public void generateRelocations()
    {
        if(selectedRelocationDirection.equals("Top Right"))
        {
            int numRelocationX = ThreadLocalRandom.current().nextInt(0,relocationMetricsRightAndDown.length);
            selectedRelocationMetricX = relocationMetricsRightAndDown[numRelocationX];
            int numRelocationY = ThreadLocalRandom.current().nextInt(0,relocationMetricsLeftAndTop.length);
            selectedRelocationMetricY = relocationMetricsLeftAndTop[numRelocationY];
        }
        else if(selectedRelocationDirection.equals("Top Left"))
        {
            int numRelocationX = ThreadLocalRandom.current().nextInt(0,relocationMetricsLeftAndTop.length);
            selectedRelocationMetricX = relocationMetricsLeftAndTop[numRelocationX];
            int numRelocationY = ThreadLocalRandom.current().nextInt(0,relocationMetricsLeftAndTop.length);
            selectedRelocationMetricY = relocationMetricsLeftAndTop[numRelocationY];
        }
        else if(selectedRelocationDirection.equals("Top Straight"))
        {
            int numRelocationY = ThreadLocalRandom.current().nextInt(0,relocationMetricsLeftAndTop.length);
            selectedRelocationMetricY = relocationMetricsLeftAndTop[numRelocationY];
        }
        else if(selectedRelocationDirection.equals("Down Left"))
        {
            int numRelocationX = ThreadLocalRandom.current().nextInt(0,relocationMetricsLeftAndTop.length);
            selectedRelocationMetricX = relocationMetricsLeftAndTop[numRelocationX];
            int numRelocationY = ThreadLocalRandom.current().nextInt(0,relocationMetricsRightAndDown.length);
            selectedRelocationMetricY = relocationMetricsRightAndDown[numRelocationY];
        }
        else if(selectedRelocationDirection.equals("Down Right"))
        {
            int numRelocationX = ThreadLocalRandom.current().nextInt(0,relocationMetricsRightAndDown.length);
            selectedRelocationMetricX = relocationMetricsLeftAndTop[numRelocationX];
            int numRelocationY = ThreadLocalRandom.current().nextInt(0,relocationMetricsRightAndDown.length);
            selectedRelocationMetricY = relocationMetricsRightAndDown[numRelocationY];
        }
        else if(selectedRelocationDirection.equals("Down Straight"))
        {
            int numRelocationY = ThreadLocalRandom.current().nextInt(0,relocationMetricsRightAndDown.length);
            selectedRelocationMetricY = relocationMetricsRightAndDown[numRelocationY];
        }
        else if(selectedRelocationDirection.equals("Left"))
        {
            int numRelocationX = ThreadLocalRandom.current().nextInt(0,relocationMetricsLeftAndTop.length);
            selectedRelocationMetricX = relocationMetricsLeftAndTop[numRelocationX];
        }
        else if(selectedRelocationDirection.equals("Right"))
        {
            int numRelocationX = ThreadLocalRandom.current().nextInt(0,relocationMetricsRightAndDown.length);
            selectedRelocationMetricX = relocationMetricsRightAndDown[numRelocationX];
        }
    }

    public void activateAi()
    {  
        int numDirection = ThreadLocalRandom.current().nextInt(0,relocationDirections.length);
        selectedRelocationDirection = relocationDirections[numDirection];
        generateNums();
        generateRelocations();
        setTurretAngle(Math.atan2(selectedRelocationMetricY,selectedRelocationMetricX));
        moveTimer.setInitialDelay(selectedMoveTime);
        moveTimer.start();
    }
}