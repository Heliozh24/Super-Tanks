import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import javax.swing.Timer;
import java.awt.event.ActionEvent;

class Tank extends Component
{
    protected double movementSpeed;
    protected double turretSpeed;
    protected int armor;
    protected int ogArmor;
    protected Color bodyColor;
    protected Color originalBodyColor;
    protected Color wheelsColor;
    protected Color rocketColor;
    protected Color circleColor;
    protected String weapon;
    protected Point coords;
    protected double turretAngle = 0;
    protected int offsetX;
    protected int offsetY;
    protected Timer damageTimer;
    protected int damageTime = 200;
    protected int delta_x = 0;
    protected int delta_y = 0;
    protected int minigunDamage;
    protected int shotgunDamage;
    protected int cannonDamage;
    protected int rocketDamage;
    protected int laserDamage;
    protected int shotgunAmmo = 0;
    protected int cannonAmmo = 0;
    protected int rocketAmmo = 0;
    protected int laserAmmo = 0;
    protected Rectangle backgroundBar;
    protected Rectangle armorBar;
    protected Color barColor = new Color(255, 140, 0);

    public Tank(double movementSpeed, double turretSpeed, Color color, String weapon, Point coords,int armor, int shotgunAmmo, int cannonAmmo, int rocketAmmo, int laserAmmo, int minigunDamage, int shotgunDamage, int cannonDamage, int rocketDamage, int laserDamage)
    {
        setMovementSpeed(movementSpeed);
        setTurretSpeed(turretSpeed);
        setBodyColor(color);
        setWheelsColor(Color.gray);
        setRocketColor(Color.red);
        setCircleColor(Color.lightGray);
        setShotgunAmmo(shotgunAmmo);
        setCannonAmmo(cannonAmmo);
        setRocketAmmo(rocketAmmo);
        setLaserAmmo(laserAmmo);
        setArmor(armor);
        setMinigunDamage(minigunDamage);
        setShotgunDamage(shotgunDamage);
        setCannonDamage(cannonDamage);
        setRocketDamage(rocketDamage);
        setLaserDamage(laserDamage);
        this.ogArmor = armor;
        setWeapon(weapon);
        this.coords = coords;
        backgroundBar = new Rectangle(coords.x-5,coords.y-35,60,8);
        armorBar = new Rectangle(coords.x-5,coords.y-35,60,8);

        damageTimer = new Timer(damageTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt)
            {
                resetAllColors();
            }
        });
        damageTimer.setRepeats(false);

    }

    public int getDamageByWeapon(String weapon)
    {
        if(weapon.equals("Minigun"))
            return minigunDamage;
        else if(weapon.equals("Shotgun"))
            return shotgunDamage;
        else if(weapon.equals("Cannon"))
            return cannonDamage;
        else if(weapon.equals("Rocket"))
            return rocketDamage;
        else
            return laserDamage;
    }

    public int getOgArmor()
    {
        return ogArmor;
    }

    public Color getBarColor()
    {
        return barColor;
    }

    public Rectangle getArmorBar()
    {
        return armorBar;
    }

    public void decrArmorBarWidth(double width)
    {
        armorBar.width -= width;
    }
    
    public Rectangle getBackgroundBar()
    {
        return backgroundBar;
    }

    public void setMinigunDamage(int damage)
    {
        minigunDamage = damage;
    }

    public void setShotgunDamage(int damage)
    {
        shotgunDamage = damage;
    }

    public void setCannonDamage(int damage)
    {
        cannonDamage = damage;
    }

    public void setRocketDamage(int damage)
    {
        rocketDamage = damage;
    }

    public void setLaserDamage(int damage)
    {
        laserDamage = damage;
    }

    public int getShotgunAmmo()
    {
        return shotgunAmmo;
    }

    public int getCannonAmmo()
    {
        return cannonAmmo;
    }

    public int getRocketAmmo()
    {
        return rocketAmmo;
    }

    public int getLaserAmmo()
    {
        return laserAmmo;
    }


    public void setShotgunAmmo(int ammo)
    {
        shotgunAmmo = ammo;
    }

    public void setCannonAmmo(int ammo)
    {
        cannonAmmo = ammo;
    }

    public void setRocketAmmo( int ammo)
    {
        rocketAmmo = ammo;
    }

    public void setLaserAmmo( int ammo)
    {
        laserAmmo = ammo;
    }

    public Timer getDmgTimer()
    {
        return damageTimer;
    }

    public void setBodyWheelsColors(Color color)
    {
        bodyColor = color;
        wheelsColor = color;
    }

    public void resetAllColors()
    {
        setBodyColor(originalBodyColor);
        setWheelsColor(Color.gray);
    }

    public void setRocketColor(Color color)
    {
        rocketColor = color;
    }

    public void setCircleColor(Color color)
    {
        circleColor = color;
    }

    public void setWheelsColor(Color color)
    {
        wheelsColor = color;
    }

    public void setOffsetX(int offX)
    {
        offsetX = offX;
    }

    public void setOffsetY(int offY)
    {
        offsetY = offY;
    }

    public int getOffsetX()
    {
        return offsetX;
    }

    public int getOffsetY()
    {
        return offsetY;
    }

    public void relocateX(int dx)
    {
        coords.x += dx;
        setDx(dx);
    }

    public void relocateY(int dy)
    {
        coords.y += dy;
        setDy(dy);
    }

    public int getDx()
    {
        return delta_x;
    }

    public int getDy()
    {
        return delta_y;
    }

    public void setDx(int dx)
    {
        delta_x = dx;
    }

    public void setDy(int dy)
    {
        delta_y = dy;
    }

    public double getMovementSpeed()
    {
        return movementSpeed;
    }

    public double getTurretSpeed()
    {
        return turretSpeed;
    }

    public int getArmor()
    {
        return armor;
    }

    public Color getBodyColor()
    {
        return bodyColor;
    }

    public String getWeapon()
    {
        return weapon;
    }

    public void setMovementSpeed(double speed)
    {
        movementSpeed = speed;
    }

    public void setTurretSpeed(double speed)
    {
        turretSpeed = speed;
    }

    public void setArmor(int armr)
    {
        armor = armr;
    }

    public void setBodyColor(Color clr)
    {
        bodyColor = clr;
        originalBodyColor = clr;
    }

    public void setWeapon(String weapn)
    {
        weapon = weapn;
    }


    public Point getCoords()
    {
        return coords;
    }

    public void setCoords(int x, int y)
    {
        coords.x = x;
        coords.y = y;
    }

    public void setTurretAngle(double angle)
    {
        turretAngle = angle;
    }

    public double getTurretAngle()
    {
        return turretAngle;
    }


    public void draw(Graphics gfx, int cameraX, int cameraY)
    {
        Graphics2D gfx2d = (Graphics2D) gfx; //we need gfx2d reference in order to do the following functions
        AffineTransform old = gfx2d.getTransform();
        Point tankCenter = new Point(coords.x+25,coords.y); //we track again the centre of the tank
        gfx2d.translate(tankCenter.x-cameraX, tankCenter.y-cameraY); // we translate the x-y system so the (0,0) point moves to the (x,y) point (tank centre), this way we paint and rotate around the tank centre and not the (0,0) which is set by default
        gfx.setColor(wheelsColor);
        gfx.fillRect(-25,-24,50,7);
        gfx.fillRect(-25,22,50,7);
        gfx.setColor(bodyColor);
        gfx.fillRect(-20,-17,40,40);
        gfx.setColor(circleColor);
        gfx.fillOval(-15,-13,30,30);
        gfx2d.rotate(turretAngle); // after everything is painted (main tank body) we design the weapon which will be rotated with the turretAngle

        if(weapon.equals("Minigun"))
        {
            gfx.setColor(originalBodyColor);
            gfx.fillRect(0,-5,70,2);
            gfx.fillRect(0,8,70,2);
        }
        else if(weapon.equals("Shotgun"))
        {
            gfx.setColor(originalBodyColor);
            gfx.fillRect(0,-6,60,13); 
            gfx.fillRect(58,-8,5,18);
        }
        else if(weapon.equals("Cannon"))
        {
            gfx.setColor(originalBodyColor);
            gfx.fillRect(0,-1,60,8);
        }
        else if(weapon.equals("Rocket"))
        {
            gfx.setColor(originalBodyColor);
            gfx.fillRect(0,-5,30,10);
            gfx.setColor(rocketColor);
            gfx.fillRoundRect(27,-6,30,10,30,10);
        }
        else
        {
            gfx.setColor(originalBodyColor);
            gfx.fillRect(-5,-8,11,22);
            gfx.fillRect(5,-2,23,11);
            gfx.fillRect(25,2,12,3);
        }

        gfx2d.setTransform(old);
    }
    
}