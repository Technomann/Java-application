package character;

import graphics.Effects;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.Serializable;

/**
 * \class PacMan
 * \brief Ez az osztály valósítja meg a PacMan karaktert, és annak működését.
 *
 * Itt történik annak az ellenőrzése, hogy a PacMan ne menjen át a falon, ahogy az is itt valósul meg, hogy mozogjon a
 * karakter. Ez az osztály szerializálódik a mentésekben. A Pellet osztállyal közös őse az Elements osztály.
 */
public class PacMan extends Elements implements Serializable {

    private static final long serialVersionUID = 30L; /**< Szerializáláshoz nem kötelezően szükséges egyéni azonosító.*/

    private int X; /**< A karakter X koordinátája, pixelben*/
    private int Y;/**< A karakter Y koordinátája, pixelben*/

    private int oppositeX;/**< A karakterrel szemközti fal X koordinátája, pixelben*/
    private int oppositeY;/**< A karakterrel szemközti fal Y koordinátája, pixelben*/

    private int BLOCK_SIZE;/**< A mindenkori pályablokk méret. Elengedhetetlen a logikák működéséhez*/

    private int level;/**< A karakter szintjének száma*/
    private int lives;/**< A karakter életeinek száma*/
    private long points;/**< A karakter pontjainak száma*/
    private final String name;/**< A játékos által kezdéskor begépelt név*/

    private int pacFrameState;/**< */
    private int frameNum = 0;/**< */
    public int direction = 1;/**< A karakter iránya, kezdetben 1, azaz balra*/

    private final short[] walls;/**< A játékban fixen elhelyezkedő falak koordinátáit tároló tömb*/

    /**
     * Az osztály konstruktora.
     * @param n a játékos által begépelt név
     * @param block az objektum inicializálásakor érvényben lévő blokkméret
     * @param w a fix falak koordinátáit tároló tömb a hívótól
     */
    public PacMan(String n, int block, short[] w){
        name = n;
        points = 0;
        BLOCK_SIZE = block;
        X=8*BLOCK_SIZE;
        Y=15*BLOCK_SIZE;
        pacFrameState = 0;
        //direction = 4;
        walls = w;
        level = 1;
        lives = 3;
    }

    /**
     * Minden egyes Timer tick-re ez a metódus hívódik meg, ami kirajzolja a pályára a PacMan-t. A PacMannek minden irányához
     * tartozik egy képeket tároló tömb. A pacFrameState attribútum mondja meg, hogy egymás után ennek a tömbnek melyik
     * elemét akarjuk megjeleníteni, a frameNum attribútum pedig azt, hogy ez a megjelenítés hány tick-enként változzon!
     * @param frame_g - az a Graphics2D elem, amire rajzolunk.
     * @param c - ImageObserver a drawImage metódushoz, az az objektum, ahonnan hívták a draw-t
     */
    @Override
    public void draw(Graphics2D frame_g, Object c) {
        frameNum++;
            switch (direction) {
                ///BALRA
                case 1:
                    frame_g.drawImage(Effects.Dynamics.PAC_LEFT.getDynPic(pacFrameState), X, Y,
                            BLOCK_SIZE, BLOCK_SIZE, (ImageObserver) c);
                    break;
                ///FEL
                case 2:
                    frame_g.drawImage(Effects.Dynamics.PAC_UP.getDynPic(pacFrameState), X, Y,
                            BLOCK_SIZE, BLOCK_SIZE, (ImageObserver) c);
                    break;
                ///LE
                case 3:
                    frame_g.drawImage(Effects.Dynamics.PAC_DOWN.getDynPic(pacFrameState), X, Y,
                            BLOCK_SIZE, BLOCK_SIZE, (ImageObserver) c);
                    break;
                ///JOBRRA
                case 4:
                    frame_g.drawImage(Effects.Dynamics.PAC_RIGHT.getDynPic(pacFrameState), X, Y,
                            BLOCK_SIZE, BLOCK_SIZE, (ImageObserver) c);
                    break;
                default:
                    break;
            }
        if(frameNum == 7) {
            pacFrameState++;
            frameNum = 0;
        }
        if(pacFrameState > 5)
            pacFrameState = 0;
    }

    /**
     * A függvény megváltoztatja az objektumban eltárolt blokkméretet, és ezzel együtt középre helyezi a PacMan-t,
     * így az a következő kirajzoláskor már alkalmazkodik a megváltozott pályaméretekhez.
     * @param d az új blokkméret
     */
    @Override
    public void changeBlockSize(int d) {
        BLOCK_SIZE = d;
        setCentre();
    }

    /**
     * Ez a metódus felel a PacMan mozgatásáért. Minden Timer tick-re meghívódik, és az adott szint értékével mozgatja a PacMan-t
     * attól függően, hogy milyen irányba (direction: 1 - balra, 3 - le, 2 - fel, 4 - jobbra) áll. Fontos, hogyha a measureOpposite
     * metódus -1;-1 koordinátákat adott vissza, akkor nem mozgatja a PacMant. Illetve akkor sem, hogyha az adott iránynak
     * megfelelő koordináták különbségének abszolót értéke kisebb, mint a pályablokk mérete. Ez biztosítja, hogy a PacMan
     * ne menjen bele a falba.
     */
    public void move(){
        switch (direction) {
            ///Mozgás balra
            case 1:
                if(Math.abs(oppositeX-X) > BLOCK_SIZE && oppositeX != -1 && oppositeY != -1)
                    X -= level;
                break;
            ///Mozgás felfele
            case 2:
                if(Math.abs(oppositeY-Y) > BLOCK_SIZE && oppositeX != -1 && oppositeY != -1)
                    Y -= level;
                break;
            ///Mozgás lefele
            case 3:
                if(Math.abs(oppositeY-Y) > BLOCK_SIZE && oppositeX != -1 && oppositeY != -1)
                    Y += level;
                break;
            ///Mozgás jobbra
            default:
                if(Math.abs(oppositeX-X) > BLOCK_SIZE && oppositeX != -1 && oppositeY != -1)
                    X += level;
                break;
        }
    }

    /**
     * Ez talán az osztály legkomplexebb metódusa. Az osztály konstruktorbáan megadott tömb alapján megkeresi a PacMan
     * mindig adott irány szerinti szemközti falának koordinátáit, majd az oppositeX és oppositeY változókat ezekre a koordinátákra
     * (pixelben) állítja be. A move metódus eszek ellenőrzése alapján mozgatja a PacMan-t. Ha nem talál szemközti falat,
     * akkor -1;-1 koordinátákra állítja az előbbi két változót. Ennél jobban sajnos nem tudtam rövidíteni rajta, mert
     * pl. más probléma megkeresni a balra legközelebb lévő falat, mint a jobbra lévőt.
     */
    private void measureOpposite(){
        int tempX = -1, tempY = -1;
        switch (direction){
            case 1:
                ///Szemközti fal balra
                for(short i = 0; i<walls.length; i+= 2){
                    if(walls[i]*BLOCK_SIZE < X && Math.abs(walls[i+1]*BLOCK_SIZE-Y) < BLOCK_SIZE/4){
                        tempX = walls[i]*BLOCK_SIZE;
                        tempY = walls[i+1]*BLOCK_SIZE;
                    }
                }
                break;
            case 2:
                ///Szemközti fal felfele
                for(short i = 0; i<walls.length; i += 2){
                    if(Math.abs(walls[i]*BLOCK_SIZE - X) < BLOCK_SIZE/4 && walls[i+1]*BLOCK_SIZE < Y){
                        tempX = walls[i]*BLOCK_SIZE;
                        tempY = walls[i+1]*BLOCK_SIZE;
                    }
                }
                break;
            case 3:
                ///Szemközti fal lefele
                short i = 0;
                boolean gotIt = false;
                for(; !gotIt && i<walls.length;){
                    if(Math.abs(walls[i]*BLOCK_SIZE - X) < BLOCK_SIZE/4 && walls[i+1]*BLOCK_SIZE > Y){
                        tempX = walls[i]*BLOCK_SIZE;
                        tempY = walls[i+1]*BLOCK_SIZE;
                        gotIt = true;
                    }
                    i+=2;
                }
                break;
            default:
                ///Szemközti fal jobbra
                short j = 0;
                gotIt = false;
                for(;!gotIt && j < walls.length;){
                    if(walls[j]*BLOCK_SIZE > X && Math.abs(walls[j+1]*BLOCK_SIZE - Y) < BLOCK_SIZE/4){
                        tempX = walls[j]*BLOCK_SIZE;
                        tempY = walls[j+1]*BLOCK_SIZE;
                        gotIt = true;
                    }
                    j+=2;
                }
                break;
        }
        oppositeX = tempX;
        oppositeY = tempY;
    }

    /**
     * Ez a metódus beállítja a PacMan irányát. Amikor a játékos megnyom egy nyilat, akkor csak ezt állítja. Az átállítás
     * után újraszámítja a szemközti fal koordinátáit.
     * @param d az új irány
     */
    public void setDirection(int d){
       direction = d;
       measureOpposite();
    }

    /**
     * Ez a metódus egyel növeli a PacMan szintjét, majd középre pozícionálja a PacMan-t.
     */
    public void increaseLevel(){
        level++;
        setCentre();
    }

    /**
     * Ez a metódus annak megfelelően növeli a PacMan pontszámát, hogy éppen aktívak-e az extra PowerPelletek, illetve, hogy
     * hányas szinten van a PacMan. Ha a powerTime paraméter értéke true, akkor 500-as növekszik a pontszám, ha ez false,
     * akkor 1-es szint esetén 20, 2-es szint esetén 40, 3-as szint esetén 80-al nő a pontszám.
     * @param powerTime éppen aktívak-e a PowerPellet-ek, true - igen, false - nem
     */
    public void increasePoints(boolean powerTime){
       if(powerTime)
           points += 500;
       else {
           switch (level) {
               case 1 -> points += 20;
               case 2 -> points += 40;
               default -> points += 80;
           }
       }
    }

    /**
     * Getter a PacMan name attribútumára.
     * @return az attribútum értéke
     */
    public String getPlayerName(){
        return name;
    }

    /**
     * Getter a PacMan pontszámára.
     * @return a pontszám
     */
    public long getPoints(){
        return points;
    }

    /**
     * Getter a PacMan szintjére.
     * @return a szint
     */
    public int getLevel() {
        return level;
    }

    /**
     * Getter a PacMan élteinek számára.
     * @return az életek száma
     */
    public int getLives(){
        return lives;
    }

    /**
     * Getter az X koordinátára.
     * @return az X koordináta
     */
    @Override
    public int getX() {
        return X;
    }

    /**
     * Getter az Y koordinátára.
     * @return az Y koordináta
     */
    @Override
    public int getY() {
        return Y;
    }

    /**
     * Ez a metódus a PacMan életeinek számát változtatja. Ha az increase paraméter értéke true, akkor növeli az életek számát 1-gyel,
     * de csak amennyiben az kevesebb volt, mint 3. Ha a paraméter értéke false, akkor csökkenti egyel az életek számát.
     * @param increase növeljen, vagy csökkentsen a fügvény, true - növeljen, false - ccsökkentsen
     */
    public void changeLivesNumber(boolean increase){
      if(increase) {
          if (lives < 3)
              lives++;
      }else{
          if(lives > 0)
            lives--;
      }

    }

    /**
     * Ez a metódus a kezdőpozícióba helyezi a PacMan-t, ami a 8;15 (X;Y) koordinátájú blokk, illetve a kezdőirányt
     * állítja be, és kiszámítja a szemközti fal koordinátáit.
     */
    public void setCentre(){
        X = 8*BLOCK_SIZE;
        Y = 15*BLOCK_SIZE;
        setDirection(4);
        measureOpposite();
    }
}
