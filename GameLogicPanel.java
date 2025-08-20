import java.awt.Graphics;
import javax.lang.model.util.ElementScanner14;
import javax.sound.sampled.Line;
import javax.swing.JPanel;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.Timer;
import java.awt.Component;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Graphics2D;
import javax.swing.Timer;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.Vector;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Line2D;
import java.util.Iterator;

class GameLogicPanel extends JPanel 
{
    private int levelIndex;
    private GameGui frame;
    private boolean[] keys = new boolean[256];
    private ArrayList<AiTank> aiTankList = new ArrayList<>();
    private ArrayList<Block> blocksList = new ArrayList<>();
    private Rectangle[] hudRects = new Rectangle[5];
    private Rectangle armorBar;
    private Rectangle ammoBar;
    private Rectangle backgroundBar1;
    private Rectangle backgroundBar2;
    private Color[] hudColors = new Color[5];
    private Ball[] ballArray = new Ball[5];
    private Rocket rocket;
    private int mainTime = 20;
    private int damageTime = 300;
    private Timer mainTimer;
    private Timer damageTimer;
    private Tank playerTank;
    private Rectangle playerHitbox;
    private Laser laser;
    private boolean upCollision;
    private boolean downCollision;
    private boolean leftCollision;
    private boolean rightCollision;
    private int cannonTime = 1000;
    private int shotgunTime = 700;
    private int laserAmmoTime = 400;
    private int aiTankUpdateTime = 20;
    private int shotgunAmmoWidth;
    private int cannonAmmoWidth;
    private int rocketAmmoWidth;
    private int laserAmmoWidth;
    private Timer shotgunTimer;
    private Timer cannonTimer;
    private Timer laserAmmoTimer;
    private Timer explosionTimer;
    private Timer assistExplosionTimer;
    private Timer aiTankTimer;
    private Timer aiTankUpdaterTimer;
    private int explosionTime = 50;
    private int assistExplosionTime = 1;
    private int aiTankTime = 20; //2200
    private boolean shotgunCooldownIsOver = true;
    private boolean cannonCooldownIsOver = true;
    private int cameraX;
    private int cameraY;
    private boolean exploded = false;
    private Rectangle explosionRect;
    
    private class KeyboardListener implements KeyListener
    {
        public void keyTyped(KeyEvent e) {}

        public void keyReleased(KeyEvent e)
        {
            keys[e.getKeyCode()] = false;
            if(!keys[KeyEvent.VK_W] && !keys[KeyEvent.VK_S] )
                playerTank.relocateY(0);
            if(!keys[KeyEvent.VK_A] && !keys[KeyEvent.VK_D])
                playerTank.relocateX(0);

        }
        public void keyPressed(KeyEvent e) 
        {
            keys[e.getKeyCode()] = true;   
        }

    }

    public void activateAiTanks()
    {
        for(AiTank tank: aiTankList)
        {
            Rectangle hitbox = tank.getHitbox();
            if(hitbox.x >=0 && hitbox.x <= frame.getWidth()-80 && hitbox.y >= 0 && hitbox.y <= frame.getHeight()-80)
            {
                tank.activateAi();
            }
        }
    }

    public void shakeCamera()
    {
        cameraX -= 8;
        cameraY += 8;
    }
    public void resetInactiveRectColor()
    {
        for(int i = 0; i <=4; i++)
        {
            if(hudColors[i].equals(Color.white))
            {
                hudColors[i] = Color.gray;
                break;
            }
        }
    }
    public void checkWeaponChange()
    {
        if(keys[KeyEvent.VK_1])
        {
            playerTank.setWeapon("Minigun");
            resetInactiveRectColor();
            hudColors[0] = Color.white;
            rocket = null;
            laser = null;
            ammoBar.width = 250;
        } 
        else if(keys[KeyEvent.VK_2])
        {
            playerTank.setWeapon("Shotgun");
            resetInactiveRectColor();
            hudColors[1] = Color.white;
            rocket = null;  
            laser = null;
            ammoBar.width = shotgunAmmoWidth;
        }
        else if(keys[KeyEvent.VK_3])
        {
            playerTank.setWeapon("Cannon");
            resetInactiveRectColor();
            hudColors[2] = Color.white;
            rocket = null;   
            laser = null;  
            ammoBar.width = cannonAmmoWidth;  
        }    
        else if(keys[KeyEvent.VK_4])
        {
            playerTank.setWeapon("Rocket");
            resetInactiveRectColor();
            hudColors[3] = Color.white;
            laser = null;    
            ammoBar.width = rocketAmmoWidth;
        }
        else if(keys[KeyEvent.VK_5])
        {
            playerTank.setWeapon("Laser");
            resetInactiveRectColor();
            hudColors[4] = Color.white;
            rocket = null;    
            ammoBar.width = laserAmmoWidth; 
        }
    }

    public void moveTank()
    {
        if(keys[KeyEvent.VK_W] && !upCollision)
        {
            playerTank.relocateY((-5)* (int) playerTank.getMovementSpeed());
            playerHitbox.y = playerTank.getCoords().y - 25;
            downCollision = false;
        }
        if(keys[KeyEvent.VK_S] && !downCollision)
        {
            playerTank.relocateY((5)* (int) playerTank.getMovementSpeed());
            playerHitbox.y = playerTank.getCoords().y - 25;
            upCollision = false;
        }
        if(keys[KeyEvent.VK_A] && !leftCollision)
        {
            playerTank.relocateX((-5)* (int) playerTank.getMovementSpeed());
            playerHitbox.x = playerTank.getCoords().x - 5;
            rightCollision = false;

        }
        if(keys[KeyEvent.VK_D] && !rightCollision)
        {
            playerTank.relocateX((5)* (int) playerTank.getMovementSpeed());
            playerHitbox.x = playerTank.getCoords().x - 5;
            leftCollision = false;
        }
        if(keys[KeyEvent.VK_S] && keys[KeyEvent.VK_A] && !downCollision && !leftCollision )
        {
            playerTank.relocateY((5)* (int) playerTank.getMovementSpeed()-6);
            playerTank.relocateX((-5)* (int) playerTank.getMovementSpeed()+6);
            playerHitbox.x = playerTank.getCoords().x - 5;
            playerHitbox.y = playerTank.getCoords().y - 25;
            rightCollision = false;
            upCollision = false;
        }

        if(keys[KeyEvent.VK_S] && keys[KeyEvent.VK_D] && !downCollision && !rightCollision )
        {
            playerTank.relocateY((5)* (int) playerTank.getMovementSpeed()-6);
            playerTank.relocateX((5)* (int) playerTank.getMovementSpeed()-6);
            playerHitbox.x = playerTank.getCoords().x - 5;
            playerHitbox.y = playerTank.getCoords().y - 25;
            leftCollision = false;
            upCollision = false;
        }

        if(keys[KeyEvent.VK_W] && keys[KeyEvent.VK_A] && !upCollision && !leftCollision )
        {
            playerTank.relocateY((-5)* (int) playerTank.getMovementSpeed()+6);
            playerTank.relocateX((-5)* (int) playerTank.getMovementSpeed()+6);
            playerHitbox.x = playerTank.getCoords().x - 5;
            playerHitbox.y = playerTank.getCoords().y - 25;
            rightCollision = false;
            downCollision = false;
        }

        if(keys[KeyEvent.VK_W] && keys[KeyEvent.VK_D] && !upCollision && !rightCollision )
        {
            playerTank.relocateY((-5)* (int) playerTank.getMovementSpeed()+6);
            playerTank.relocateX((5)* (int) playerTank.getMovementSpeed()-6);
            playerHitbox.x = playerTank.getCoords().x - 5;
            playerHitbox.y = playerTank.getCoords().y - 25;
            leftCollision = false;
            downCollision = false;
        }
    }

    public void checkPlayerCollisionWithBlocks()
    {
        upCollision = false;
        downCollision = false;
        leftCollision = false;
        rightCollision = false;

        Rectangle upHitbox = new Rectangle(playerHitbox.x+8-cameraX+playerTank.getCoords().x,playerHitbox.y-3-cameraY+playerTank.getCoords().y,playerHitbox.width-12,1);
        Rectangle leftHitbox = new Rectangle(playerHitbox.x-cameraX+playerTank.getCoords().x,playerHitbox.y+8-cameraY+playerTank.getCoords().y,1,playerHitbox.height-12); ;
        Rectangle downHitbox = new Rectangle(playerHitbox.x+8-cameraX+playerTank.getCoords().x,playerHitbox.y+playerHitbox.width+3-cameraY+playerTank.getCoords().y,playerHitbox.width-12,1);
        Rectangle rightHitbox = new Rectangle(playerHitbox.x+3+playerHitbox.height-cameraX+playerTank.getCoords().x,playerHitbox.y+8-cameraY+playerTank.getCoords().y,1,playerHitbox.height-12);

       for(Block block: blocksList)
        {
            Rectangle obsHitbox = new Rectangle(block.getPosX()-cameraX,block.getPosY()-cameraY,block.getWidth(),block.getHeight());
            if(obsHitbox.x >=0 && obsHitbox.x <= frame.getWidth()-80 && obsHitbox.y >= 0 && obsHitbox.y <= frame.getHeight()-80)
            {
                if(upHitbox.intersects(obsHitbox))
                    upCollision = true;
                
                if(downHitbox.intersects(obsHitbox))
                    downCollision = true;
                
                if(leftHitbox.intersects(obsHitbox))
                    leftCollision = true;

                if(rightHitbox.intersects(obsHitbox))
                    rightCollision = true; 
            }
        }

        for(AiTank tank: aiTankList)
        {
            Rectangle obsHitbox = tank.getHitbox();
             if(obsHitbox.x >=0 && obsHitbox.x <= frame.getWidth()-80 && obsHitbox.y >= 0 && obsHitbox.y <= frame.getHeight()-80)
             {
                if(upHitbox.intersects(obsHitbox))
                    upCollision = true;
                
                if(downHitbox.intersects(obsHitbox))
                    downCollision = true;
                
                if(leftHitbox.intersects(obsHitbox))
                    leftCollision = true;

                if(rightHitbox.intersects(obsHitbox))
                    rightCollision = true; 
            }
        }
    }

    public void moveBalls()
    {
        for(int i = 0; i < ballArray.length; i++)
        {
            if(ballArray[i] != null)
            {
                ballArray[i].relocateX(ballArray[i].getDx());
                ballArray[i].relocateY(ballArray[i].getDy());
            }
        }   
    }

    public void moveLaser()
    {
        if(laser != null)
        {
            laser.relocateX(laser.getDx());
            laser.relocateY( laser.getDy());
        }
    }

    public void moveRocket()
    {
        if(rocket != null)
        {
            Point tankCenter = new Point(playerTank.getCoords().x+25, playerTank.getCoords().y); 
            Vector2D rocketDirection = new Vector2D(rocket.getMouseX()-tankCenter.x,rocket.getMouseY()-tankCenter.y);
            rocketDirection = rocketDirection.normalize();
            int speed = 2;
            rocket.setAngle(Math.atan2(rocket.getMouseY() - rocket.getPosY(),rocket.getMouseX() - rocket.getPosX()));
            rocket.relocateX((int) rocketDirection.multiply(speed).getX());
            rocket.relocateY( (int) rocketDirection.multiply(speed).getY());
        }
    }

    public Point2D rotatePoint(Point2D point, Point2D center, double angleRadians) {
        double dx = point.getX() - center.getX();
        double dy = point.getY() - center.getY();
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);
        double rotatedX = center.getX() + dx * cos - dy * sin;
        double rotatedY = center.getY() + dx * sin + dy * cos;
        return new Point2D.Double(rotatedX, rotatedY);
    }

    public boolean ballArrayIsEmpty()
    {   
        int nulls = 0;
        for(int i = 0; i < ballArray.length; i++)
        {
            if(ballArray[i] == null)
                nulls++;
        }
        return nulls == ballArray.length;
    }

    public void setMinigun()
    {
        Point tankCenter = new Point(playerTank.getCoords().x+25, playerTank.getCoords().y); 
        double angle = playerTank.getTurretAngle();
        Point2D muzzle1 = rotatePoint(new Point2D.Double(tankCenter.x - 6, tankCenter.y - 15), tankCenter, angle);
        Point2D muzzle2 = rotatePoint(new Point2D.Double(tankCenter.x - 6, tankCenter.y + 5), tankCenter, angle);
        ballArray[0] =  new Ball((int)muzzle1.getX(), (int)muzzle1.getY(), 15, 15, Color.darkGray);
        ballArray[1] = new Ball((int)muzzle2.getX(), (int)muzzle2.getY(), 15, 15, Color.darkGray);
        int speed = 20;
        for(int i = 0; i <2; i++)
        {
            ballArray[i].setDx((int)(Math.cos(angle) * speed));
            ballArray[i].setDy((int)(Math.sin(angle) * speed));
            ballArray[i].setDistanceLimit(300);
        }
    }

    public void updateRocketMouse(int mouseX, int mouseY)
    {
        if(rocket != null)
        {
            rocket.setMouseX(mouseX);
            rocket.setMouseY(mouseY);
        }
    }

    public void updateCamera()
    {
        if (rocket != null) 
        {
            cameraX = rocket.getPosX();
            cameraY = rocket.getPosY();
        } 
        else
        {
            cameraX = playerTank.getCoords().x;
            cameraY = playerTank.getCoords().y;
        }
    }

    public void setCannon()
    {
        Point tankCenter = new Point(playerTank.getCoords().x+25, playerTank.getCoords().y); 
        double angle = playerTank.getTurretAngle();
        Point2D muzzle = rotatePoint(new Point2D.Double(tankCenter.x + 5, tankCenter.y - 5), tankCenter, angle);
        ballArray[0] =  new Ball((int)muzzle.getX(), (int)muzzle.getY(), 25, 25, Color.darkGray);
        int speed = 30;
        ballArray[0].setDx((int)(Math.cos(angle) * speed));
        ballArray[0].setDy((int)(Math.sin(angle) * speed));
        ballArray[0].setDistanceLimit(5000);
    }

    public void setShotgun()
    {
        Point tankCenter = new Point(playerTank.getCoords().x+25, playerTank.getCoords().y); 
        double angle = playerTank.getTurretAngle();
        Point2D muzzle = rotatePoint(new Point2D.Double(tankCenter.x+5, tankCenter.y - 5), tankCenter, angle);
        for(int i = 0; i < 5; i++)
        {
            ballArray[i] = new Ball((int) muzzle.getX(),(int)muzzle.getY(),12, 12, Color.DARK_GRAY);
            ballArray[i].setDistanceLimit(400);
        }

        int speed = 12;
        int spreadDegrees = 35;
        int centerIndex = 2;
        for(int i = 0; i <5; i++)
        { 
            int offsetFromCenter = i - centerIndex;
            double spreadAngle = angle + Math.toRadians(offsetFromCenter * (spreadDegrees / 2.0));
            ballArray[i].setDx((int)(Math.cos(spreadAngle) * speed));
            ballArray[i].setDy((int)(Math.sin(spreadAngle) * speed)); 
        }
    }

    public void setRocket(int mouseX, int mouseY)
    {
        Point tankCenter = new Point(playerTank.getCoords().x+25, playerTank.getCoords().y); 
        double angle = playerTank.getTurretAngle();
        Point2D muzzle = rotatePoint(new Point2D.Double(tankCenter.x+6, tankCenter.y+3), tankCenter, angle);
        rocket = new Rocket((int) muzzle.getX(),(int)muzzle.getY(),30,10,30,10);
        int speed = 10;
        rocket.setDx((int)(Math.cos(angle) * speed));
        rocket.setDy((int)(Math.sin(angle) * speed));
        updateRocketMouse(mouseX, mouseY);
    }

    public void setLaser()
    {
        Point tankCenter = new Point(playerTank.getCoords().x+25, playerTank.getCoords().y); 
        double angle = playerTank.getTurretAngle();
        Point2D muzzle = rotatePoint(new Point2D.Double(tankCenter.x, tankCenter.y), tankCenter, angle);
        laser = new Laser((int) muzzle.getX()+23,(int) muzzle.getY()+3,2500,10); 
        laser.setAngle(angle);
        int speed = 30;
        laser.setDx((int)(Math.cos(angle))*speed);
        laser.setDy((int)(Math.sin(angle))*speed);
    }
    private class MouseLevelListener extends MouseAdapter
    {
        public void mousePressed(MouseEvent event)
        {
            if(ballArrayIsEmpty())
            {
                if(playerTank.getWeapon().equals("Minigun"))
                {
                    setMinigun();
                }
                else if(playerTank.getWeapon().equals("Shotgun") && shotgunCooldownIsOver && playerTank.getShotgunAmmo() > 0)
                {
                    setShotgun();
                    shotgunCooldownIsOver = false;
                    shotgunTimer.start();
                    shotgunAmmoWidth -= (shotgunAmmoWidth) / playerTank.getShotgunAmmo();
                    ammoBar.width = shotgunAmmoWidth; 
                    playerTank.setShotgunAmmo(playerTank.getShotgunAmmo() - 1);
                }
                else if(playerTank.getWeapon().equals("Cannon") && cannonCooldownIsOver && playerTank.getCannonAmmo() > 0)
                {
                    setCannon();
                    cannonCooldownIsOver = false;
                    cannonTimer.start();
                    cannonAmmoWidth -= (cannonAmmoWidth) / playerTank.getCannonAmmo();
                    ammoBar.width = cannonAmmoWidth;
                    playerTank.setCannonAmmo(playerTank.getCannonAmmo() - 1);
                }
                else if(playerTank.getWeapon().equals("Rocket"))
                {
                    if(rocket == null && playerTank.getRocketAmmo() >= 1 )
                    {
                        setRocket(event.getX(), event.getY());
                        rocketAmmoWidth -= (rocketAmmoWidth) / playerTank.getRocketAmmo();
                        ammoBar.width = rocketAmmoWidth;
                        playerTank.setRocketAmmo(playerTank.getRocketAmmo() - 1);
                    }
                    else
                        rocket = null;

                }
                else if(playerTank.getWeapon().equals("Laser") && playerTank.getLaserAmmo() > 0 ) 
                {
                    if(laser == null)
                    {
                        setLaser();
                        laserAmmoTimer.restart();
                        laserAmmoWidth -= (laserAmmoWidth) / playerTank.getLaserAmmo();
                        ammoBar.width = laserAmmoWidth;
                        playerTank.setLaserAmmo(playerTank.getLaserAmmo() - 1);
                    }
                }           
            }
        }
        public void mouseReleased(MouseEvent event)
        {
            laserAmmoTimer.stop();
            laser = null;
            for(Tank tank: aiTankList)
            {
                if(tank.getBodyColor().equals(new Color(255,0,0,100)))
                    tank.resetAllColors();
            }
        }

        public void mouseDragged(MouseEvent event)
        {
            if(laser != null)
            {
                int mouseX = event.getX(); 
                int mouseY = event.getY(); 
                Point tankCenter = new Point(playerTank.getCoords().x+25, playerTank.getCoords().y); 
                double dx = mouseX - tankCenter.x; 
                double dy = mouseY - tankCenter.y; 
                double angle = Math.atan2(dy, dx); 
                playerTank.setTurretAngle(angle*playerTank.getTurretSpeed());
                laser.setAngle(angle);
                int speed = 30;
                laser.setDx((int)(Math.cos(angle))*speed);
                laser.setDy((int)(Math.sin(angle))*speed);
                moveLaser();
                laser.setMousePos(mouseX,mouseY);
            }
        }

        public void mouseMoved(MouseEvent event)
        {
            int mouseX = event.getX(); // mouse coordinate X
            int mouseY = event.getY(); // mouse coordinate Y
            if(playerTank.getWeapon().equals("Rocket"))
                updateRocketMouse(mouseX, mouseY);
            Point tankCenter = new Point(playerTank.getCoords().x+25, playerTank.getCoords().y); // tracking the center of the tank
            double dx = mouseX - tankCenter.x; // calculating the distance (x) between the tank and the mouse
            double dy = mouseY - tankCenter.y; // calculating the distance (y) between the tank and the mouse
            double angle = Math.atan2(dy, dx); // this distance is basicly a line in the 2-dimensional space, we calculate the angle it creates with the xx' axis
            playerTank.setTurretAngle(angle*playerTank.getTurretSpeed()); // tank's turret will rotate with that angle (we multiply it by turret speed for more realism)
            repaint(); // repainting so changes will be visible
        }
    }

    public void checkBallDistance()
    {
        for(int i = 0; i < ballArray.length; i++)
        {

            if (ballArray[i] != null)
            {
                if(ballArray[i].getDistanceTravelled() > ballArray[i].getDistanceLimit())
                    ballArray[i] = null;
            }
        }
    }

    public void checkRocketDistance()
    {
        if(rocket != null && rocket.getDistanceTravelled() > 1200)
        {
            rocket = null;
        }
    }

    public void calcDamage(Block block)
    {
        block.setColor(new Color(255,255,250,190));
        double perc = block.getHealth() / playerTank.getDamageByWeapon(playerTank.getWeapon());
        double widthDecrement =  block.getHealthBar().width / perc;
        block.setHealth(block.getHealth()-playerTank.getDamageByWeapon(playerTank.getWeapon()));
        block.decrHealthBarWidth( widthDecrement );
        block.getDamageTimer().start();
    }

    public void calcDamage(Tank tank)
    {
        double perc = tank.getArmor() / playerTank.getDamageByWeapon(playerTank.getWeapon());
        double widthDecrement =  tank.getArmorBar().width / perc;
        tank.setArmor(tank.getArmor()-playerTank.getDamageByWeapon(playerTank.getWeapon()));
        tank.decrArmorBarWidth(widthDecrement);
        tank.setBodyWheelsColors(new Color(255, 255, 255, 220));
        tank.getDmgTimer().start();
    }

    public void calcDamageByBarrel(Tank tank,Barrel barrel)
    {
        double perc = tank.getArmor() / barrel.getDamage();
        double widthDecrement =  tank.getArmorBar().width / perc;
        tank.setArmor(tank.getArmor()-barrel.getDamage());
        tank.decrArmorBarWidth(widthDecrement);
        tank.setBodyWheelsColors(new Color(255, 255, 255, 220));
        tank.getDmgTimer().start();
    }

    public void calcDamageByLaser(Tank tank)
    {
        double perc = tank.getArmor() / playerTank.getDamageByWeapon("Laser");
        double widthDecrement =  tank.getArmorBar().width / perc;
        tank.setArmor(tank.getArmor()-playerTank.getDamageByWeapon("Laser"));
        tank.decrArmorBarWidth(widthDecrement);
        tank.setBodyWheelsColors(new Color(255,0,0,100));
        tank.getDmgTimer().start();
    }

    public void checkTanksDamagedByBarrel(Barrel barrel)
    {
        Iterator<AiTank> iterator = aiTankList.iterator();
        while(iterator.hasNext())
        {
            AiTank tank = iterator.next();
            Rectangle hitbox = tank.getHitbox();
            if(hitbox.x >= 0 && hitbox.x <= frame.getWidth() -80 && hitbox.y >= 0 && hitbox.y <= frame.getHeight()-80)
            {
                Rectangle explosionRadius = new Rectangle(barrel.getPosX()-cameraX-60,barrel.getPosY()-cameraY-60,barrel.getWidth()+120,barrel.getHeight()+120);
                if(hitbox.intersects(explosionRadius))
                {
                    if(tank.getArmor() > 0)
                    {
                        calcDamageByBarrel(tank,barrel);
                        if(tank.getArmor() <= 0)
                            iterator.remove();
                    }
                }
            }
        }
    }

    public void checkRocketCollision()
    {
        if(rocket != null)
        {
            Rectangle rocketHitbox = new Rectangle(rocket.getPosX()-5,rocket.getPosY()-7,rocket.getWidth()+5,rocket.getHeight()+15);
            for(Block block: blocksList)
            {
                Rectangle obsHitbox = new Rectangle(block.getPosX()-cameraX,block.getPosY()-cameraY,block.getWidth(),block.getHeight());
                if(obsHitbox.x >=0 && obsHitbox.x <= frame.getWidth()-80 && obsHitbox.y >= 0 && obsHitbox.y <= frame.getHeight()-80)
                {
                    if(rocketHitbox.intersects(obsHitbox))
                    {
                        rocket = null;
                        if(!(block instanceof Concrete) && block.getHealth() > 0)
                        {
                            calcDamage(block);
                            if(block.getHealth() <= 0)
                            {
                                if(block instanceof Barrel)
                                {
                                    shakeCamera();
                                    exploded = true;
                                    explosionRect = new Rectangle(block.getPosX()-cameraX-25,block.getPosY()-cameraY-25,block.getWidth(),block.getHeight());
                                    checkTanksDamagedByBarrel(( Barrel) block);
                                }
                                blocksList.remove(block);
                            }
                        }
                        break;
                    }
                }
            }
        }
        if(rocket != null)
        {
            Rectangle rocketHitbox = new Rectangle(rocket.getPosX()-5,rocket.getPosY()-7,rocket.getWidth()+5,rocket.getHeight()+15);
            for(AiTank tank: aiTankList)
            {
                Rectangle obsHitbox = tank.getHitbox();
                if(obsHitbox.x >=0 && obsHitbox.x <= frame.getWidth()-80 && obsHitbox.y >= 0 && obsHitbox.y <= frame.getHeight()-80)
                {
                    if(rocketHitbox.intersects(obsHitbox))
                    {
                        rocket = null;
                        if(tank.getArmor() > 0)
                        {
                            calcDamage(tank);
                            if(tank.getArmor() <= 0)
                            {
                                shakeCamera();
                                aiTankList.remove(tank);
                            }
                            
                        }
                        break;
                    }
                }
            }
        }
    }

    public void checkLaserCollision()
    {
        if(laser != null)
        {
            Point2D laserTip = rotatePoint(new Point2D.Double(playerTank.getCoords().x+25 + laser.getWidth()+20, playerTank.getCoords().y+2),new Point2D.Double(playerTank.getCoords().x+25,playerTank.getCoords().y),laser.getAngle());
            Line2D laserLine = new Line2D.Double(playerTank.getCoords().x+25,playerTank.getCoords().y+2,laserTip.getX(),laserTip.getY()-1); 
            laser.setWidth(2500);

            for(AiTank tank: aiTankList)
            {
                Rectangle obsHitbox = tank.getHitbox();
                if(laserLine.intersects(obsHitbox))
                {
                    if(tank.getArmor() > 0)
                    {
                        Point2D tankCenter = new Point2D.Double(tank.getCoords().x+25-cameraX,tank.getCoords().y-cameraY);
                        laser.setWidth((int) ((new Point2D.Double(playerTank.getCoords().x+25,playerTank.getCoords().y)).distance(tankCenter)));
                        calcDamageByLaser(tank);
                        if(tank.getArmor() <= 0)
                        {
                            shakeCamera();
                            aiTankList.remove(tank);    
                        }
                    }
                    break;
                }
            }  

            Iterator<Block> iterator = blocksList.iterator();
            while (iterator.hasNext()) 
            {
                Block block = iterator.next();
                Rectangle obsHitbox = new Rectangle(block.getPosX()-cameraX,block.getPosY()-cameraY,block.getWidth(),block.getHeight());
                if(laserLine.intersects(obsHitbox))
                {
                    Point2D rectCenter = new Point2D.Double(block.getCenterX()-cameraX,block.getCenterY()-cameraY);
                    laser.setWidth((int) ((new Point2D.Double(playerTank.getCoords().x+25,playerTank.getCoords().y)).distance(rectCenter)));
                    if(!(block instanceof Concrete) && block.getHealth() > 0 )
                    {
                        calcDamage(block);
                        if(block.getHealth() <= 0)
                        {
                            if(block instanceof Barrel)
                            {
                                shakeCamera();
                                exploded = true;
                                explosionRect = new Rectangle(block.getPosX()-cameraX-25,block.getPosY()-cameraY-25,block.getWidth(),block.getHeight());
                                checkTanksDamagedByBarrel((Barrel) block);
                            }
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    public void checkBallCollision()
    {

        if(!playerTank.getWeapon().equals("Rocket") && !playerTank.getWeapon().equals("Laser"))
        {
            for(int i = 0; i < ballArray.length; i++)
            {
                if(ballArray[i] != null)
                {
                    Rectangle ballhitBox = new Rectangle(ballArray[i].getPosX(),ballArray[i].getPosY(),ballArray[i].getWidth(),ballArray[i].getHeight());
                    for(Block block: blocksList)
                    {
                        Rectangle obsHitbox = new Rectangle(block.getPosX()-cameraX,block.getPosY()-cameraY,block.getWidth(),block.getHeight());
                        if(obsHitbox.x >=0 && obsHitbox.x <= frame.getWidth()-80 && obsHitbox.y >= 0 && obsHitbox.y <= frame.getHeight()-80 || playerTank.getWeapon().equals("Cannon"))
                        {
                            if(ballhitBox.intersects(obsHitbox))
                            {
                                ballArray[i] = null;
                                if(!(block instanceof Concrete) && block.getHealth() > 0)
                                {
                                   calcDamage(block);
                                   if(block.getHealth() <= 0)
                                    {
                                        if(block instanceof Barrel)
                                        {
                                            shakeCamera();
                                            exploded = true;
                                            explosionRect = new Rectangle(block.getPosX()-cameraX-25,block.getPosY()-cameraY-25,block.getWidth(),block.getHeight());
                                            checkTanksDamagedByBarrel((Barrel) block);
                                        }
                                        blocksList.remove(block);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                if(ballArray[i] != null)
                {
                    Rectangle ballhitBox = new Rectangle(ballArray[i].getPosX(),ballArray[i].getPosY(),ballArray[i].getWidth(),ballArray[i].getHeight());

                    Iterator<AiTank> iterator = aiTankList.iterator();
                    while (iterator.hasNext()) 
                    {
                        AiTank tank = iterator.next();
                        Rectangle obsHitbox = tank.getHitbox();
                        if(obsHitbox.x >=0 && obsHitbox.x <= frame.getWidth()-80 && obsHitbox.y >= 0 && obsHitbox.y <= frame.getHeight()-80 || playerTank.getWeapon().equals("Cannon"))
                        {
                            if(ballhitBox.intersects(obsHitbox))
                            {
                                ballArray[i] = null;
                                if(tank.getArmor() > 0)
                                {
                                    calcDamage(tank);
                                    if(tank.getArmor() <= 0)
                                    {
                                        shakeCamera();
                                        iterator.remove();
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }  
    }

    public GameLogicPanel(int levelIndex, GameGui frame)
    {
        this.levelIndex = levelIndex;
        this.frame = frame;
        addKeyListener(new KeyboardListener());
        addMouseListener(new MouseLevelListener());
        addMouseMotionListener(new MouseLevelListener());
        setFocusable(true); 
        setBackground(Color.black);
        for(int i = 0; i <= 4; i++)
        {
            hudRects[i] = new Rectangle(700+61*i,800,60,60);
            hudColors[i] = Color.gray;
        }
        hudColors[0] = Color.white;
        armorBar = new Rectangle(575,900,250,20);
        ammoBar = new Rectangle(885,900,250,20);
        backgroundBar1 = new Rectangle(575,900,250,20);
        backgroundBar2 = new Rectangle(885,900,250,20);
        playerTank = new Tank(1,0.8,new Color(255,90,0),"Minigun", new Point(400,310),500,250,220,50,250,10,20,30,40,50);
        shotgunAmmoWidth = playerTank.getShotgunAmmo();
        cannonAmmoWidth = playerTank.getCannonAmmo();
        rocketAmmoWidth =playerTank.getRocketAmmo();
        laserAmmoWidth = playerTank.getLaserAmmo();
        AiTank aiTankTest = new AiTank(7,0.8,"Laser",new Point(900,300),playerTank,300,0,0,0,0,40);
        AiTank tanktest = new AiTank(6,0.8,"Minigun", new Point(1000,300),playerTank,300,0,15,0,0,0);
        aiTankList.add(aiTankTest);
        aiTankList.add(tanktest);
        aiTankList.add(new AiTank(7,1,"Rocket", new Point(1200,400),playerTank,300,0,0,0,25,0));
        playerHitbox = new Rectangle(playerTank.getCoords().x-5,playerTank.getCoords().y-25,57,57);
        for(int i = 0; i <= 20; i++)
        {
            blocksList.add(new Concrete(i*70,100));
            blocksList.add(new Concrete(i*40,520));
            blocksList.add(new Concrete(1400,i*40));
            blocksList.add(new Concrete(520,i*40));
            blocksList.add(new Concrete(i*90,800));
        }
        blocksList.add(new Gate(700,700));
        blocksList.add(new Gate(760,800));
        blocksList.add(new Gate(820,800));
        blocksList.add(new Gate(820,1000));

        blocksList.add(new Barrel(1000,1190));
        blocksList.add(new Barrel(1030,1190));
        blocksList.add(new Barrel(1060,1190));
        blocksList.add(new Barrel(900,180));

        cannonTimer = new Timer(cannonTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cannonCooldownIsOver = true;
            }
        });
        cannonTimer.setRepeats(false);

        shotgunTimer = new Timer(shotgunTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                shotgunCooldownIsOver = true;
            }
        });
        shotgunTimer.setRepeats(false);

        laserAmmoTimer = new Timer(laserAmmoTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (playerTank.getLaserAmmo() > 0)
                {
                    laserAmmoWidth -= (laserAmmoWidth) / playerTank.getLaserAmmo();
                    ammoBar.width = laserAmmoWidth;
                    playerTank.setLaserAmmo(playerTank.getLaserAmmo() - 1);
                } 
                else
                {
                    laser = null;
                    laserAmmoTimer.stop();   
                }
            }
        });
    
        explosionTimer = new Timer(explosionTime,new ActionListener() {
            public void actionPerformed(ActionEvent evt)
            {
                if(explosionRect != null)
                {
                    if(explosionRect.width < 90)
                        assistExplosionTimer.start();
                    else
                    {
                        assistExplosionTimer.stop();
                        exploded = false;
                        explosionRect = null;
                    }
                }
            }
        });
        explosionTimer.setRepeats(false);

        assistExplosionTimer = new Timer(assistExplosionTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt)
            {
                if(explosionRect != null)
                {
                    explosionRect.width += 30;
                    explosionRect.height += 30;
                }
            } 

        });

        aiTankUpdaterTimer = new Timer(aiTankUpdateTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt)
            {
                for(AiTank tank: aiTankList)
                {
                   // Rectangle hitbox = tank.getHitbox();
                    tank.updateData(blocksList,aiTankList,cameraX,cameraY,frame);
                    tank.updateBarsAndHitboxPos();
                    moveBalls();
                    moveRocket();
                    moveLaser();
                }
            } 
        });
        aiTankUpdaterTimer.start();

        aiTankTimer = new Timer(aiTankTime, new ActionListener() {
            public void actionPerformed(ActionEvent avt)
            {
                activateAiTanks();
            }
        });
        aiTankTimer.start();

        mainTimer = new Timer(mainTime, new ActionListener() { // the main timer of the game
            public void actionPerformed(ActionEvent evt) {
                checkWeaponChange();
                checkPlayerCollisionWithBlocks();
                moveBalls();
                moveRocket();
                updateCamera();
                checkBallDistance();
                checkRocketDistance();
                checkRocketCollision();
                checkBallCollision();
                checkLaserCollision();
                moveTank();
                repaint(); 
            }
        });
        mainTimer.start();
    }

    public void paintComponent(Graphics gfx)
    {
        super.paintComponent(gfx);
        if(rocket != null)
        {
            playerTank.draw(gfx,cameraX-playerTank.getCoords().x,cameraY-playerTank.getCoords().y);
        }
        else
            playerTank.draw(gfx,0,0);

        for(AiTank tank: aiTankList)
        {
            Rectangle hitbox = tank.getHitbox();
            if(hitbox.x >=0 && hitbox.x <= frame.getWidth()-80 && hitbox.y >= 0 && hitbox.y <= frame.getHeight()-80)
            {
                tank.draw(gfx,cameraX,cameraY);
                if(tank.getArmor() < tank.getOgArmor())
                {
                    gfx.setColor(Color.white);
                    gfx.fillRect(tank.getBackgroundBar().x-cameraX,tank.getBackgroundBar().y-cameraY,tank.getBackgroundBar().width,tank.getBackgroundBar().height);
                    gfx.setColor(tank.getBarColor());
                    gfx.fillRect(tank.getArmorBar().x-cameraX, tank.getArmorBar().y-cameraY, tank.getArmorBar().width,tank.getArmorBar().height);
                }
            }
        }

        for(Block block: blocksList)
        {
            Rectangle hitbox = new Rectangle(block.getPosX()-cameraX,block.getPosY()-cameraY,block.getWidth(),block.getHeight());
            if(hitbox.x >=0 && hitbox.x <= frame.getWidth()-80 && hitbox.y >= 0 && hitbox.y <= frame.getHeight()-80)
            {
                gfx.setColor(block.getColor());
                if(!(block instanceof Barrel))
                {
                    gfx.fillRect(block.getPosX()-cameraX,block.getPosY()-cameraY,block.getWidth(),block.getHeight());
                }
                else
                {
                    gfx.fillOval(block.getPosX()-cameraX, block.getPosY()-cameraY, block.getWidth(), block.getHeight());
                    if(block.getColor().equals(new Color(255,255,250,190)))
                        gfx.setColor(new Color(255,255,250,190));
                    else
                        gfx.setColor(new Color(233, 65, 14));
                    gfx.fillOval(block.getPosX()-cameraX+7,block.getPosY()-cameraY+6,15,15);
                }

                if(block.getHealth() < block.getOgHealth())
                {
                    gfx.setColor(Color.white);
                    gfx.fillRect(block.getBackgroundBar().x-cameraX,block.getBackgroundBar().y-cameraY,block.getBackgroundBar().width,block.getBackgroundBar().height);
                    gfx.setColor(block.getBarColor());
                    gfx.fillRect(block.getHealthBar().x-cameraX,block.getHealthBar().y-cameraY,block.getHealthBar().width,block.getHealthBar().height);

                }
                if(exploded)
                {
                    gfx.setColor(Color.white);
                    gfx.fillOval(explosionRect.x,explosionRect.y,explosionRect.width,explosionRect.height);
                    explosionTimer.start();
                }
            }
        }
        for(int i = 0; i < ballArray.length; i++)
        {
            if(ballArray[i] != null)
            {
                gfx.setColor(ballArray[i].getColor());
                ballArray[i].draw(gfx);
            }
        }

        if(playerTank.getWeapon().equals("Rocket") && rocket != null)
        {
            Graphics2D gfx2d = (Graphics2D) gfx; // we need Graphics2D reference in order to translate and rotate
            AffineTransform old = gfx2d.getTransform(); // we save the old transformation so it does not affect other objects other than the rocket
            gfx2d.translate(rocket.getPosX()  + rocket.getWidth() / 2, rocket.getPosY()  + rocket.getHeight() / 2); // we translate the x-y system so the (0,0) point moves to the rocket centre point, this way we paint and rotate around the rocket centre and not the (0,0) which is set by default
            gfx2d.rotate(rocket.getAngle()); // we rotate based on the angle which the rocket turns, this is calculated by this way --> rocket.setAngle(Math.atan2(rocket.getMouseY() - rocket.getPosY(),rocket.getMouseX() - rocket.getPosX()));
            gfx.setColor(Color.red); // settting rocket color to red
            gfx2d.fillRoundRect(-rocket.getWidth() / 2, -rocket.getHeight() / 2, rocket.getWidth(), rocket.getHeight(), rocket.getArcWidth(), rocket.getArcHeight()); // painting the rocket with proper coords
            gfx2d.setTransform(old); // after we painted it we undo the transformation 
        }

        if(laser != null)
        {
            Graphics2D gfx2d = (Graphics2D) gfx; 
            AffineTransform old = gfx2d.getTransform(); 
            Point tankCenter = new Point(playerTank.getCoords().x+25, playerTank.getCoords().y); 
            gfx2d.translate(tankCenter.x,tankCenter.y);
            gfx2d.rotate(laser.getAngle());
            gfx.setColor(new Color(250, 30, 0));
            gfx.fillRect( 23,-laser.getHeight() / 2+3,laser.getWidth(),laser.getHeight());
            gfx2d.setTransform(old);
        }
        gfx.setColor(new Color(255,90,0));
        gfx.fillRect( (int) hudRects[0].getCenterX()-24, (int) hudRects[0].getCenterY(),50,2);
        gfx.fillRect((int) hudRects[0].getCenterX()-24, (int) hudRects[0].getCenterY()+10,50,2);
        gfx.fillRect((int) hudRects[1].getCenterX()-27, (int) hudRects[1].getCenterY(),50,13); 
        gfx.fillRect((int) hudRects[1].getCenterX()+21, (int) hudRects[1].getCenterY()-2,5,18);
        gfx.fillRect((int) hudRects[2].getCenterX()-25, (int) hudRects[2].getCenterY(),50,8);
        gfx.fillRect((int) hudRects[3].getCenterX()-26, (int) hudRects[3].getCenterY(),30,10);
        gfx.setColor(Color.red);
        gfx.fillRoundRect((int) hudRects[3].getCenterX(), (int) hudRects[3].getCenterY(),30,10,30,10);
        gfx.setColor(new Color(255,90,0));
        gfx.fillRect((int) hudRects[4].getCenterX()-18, (int) hudRects[4].getCenterY()-5,11,22);
        gfx.fillRect((int) hudRects[4].getCenterX()-13, (int) hudRects[4].getCenterY(),23,11);
        gfx.fillRect((int) hudRects[4].getCenterX()+9, (int) hudRects[4].getCenterY()+4,12,3);
        for(int i = 0; i <= 4; i++)
        {
            gfx.setColor(hudColors[i]);
            gfx.drawRect(hudRects[i].x,hudRects[i].y,hudRects[i].width,hudRects[i].height);
        }
        gfx.setColor(Color.white);
        gfx.fillRect(backgroundBar1.x,backgroundBar1.y,backgroundBar1.width,backgroundBar1.height);
        gfx.fillRect(backgroundBar2.x,backgroundBar2.y,backgroundBar2.width,backgroundBar2.height);    
        gfx.setColor(Color.red);
        gfx.fillRect(armorBar.x,armorBar.y,armorBar.width,armorBar.height);
        gfx.setColor(Color.green);
        gfx.fillRect(ammoBar.x,ammoBar.y,ammoBar.width,ammoBar.height); 
    }
}