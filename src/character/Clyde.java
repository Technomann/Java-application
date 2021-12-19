package character;

import graphics.Effects;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * \class Clyde
 * \brief Clyde szellemet megvalósító osztály
 */
public class Clyde extends Ghost {

    private final short[] route = {
        8,9,8,8,9,8,10,8,10,9,10,10,11,10,12,10,12,9,12,8,12,7,12,6,13,6,14,6,15,6,15,5,15,4,
            14,4,13,4,12,4,11,4,10,4,9,4,9,3,9,2,9,1,
            10,1,11,1,12,1,13,1,14,1,15,1,15,2,15,3,
            15,4,15,5,15,6,14,6,13,6,12,6,12,5,12,4,
            11,4,10,4,10,5,10,6,9,6,9,7,9,8,10,8,10,9,
            10,10,11,10,12,10,12,9,12,8,12,7,12,6,13,16,
            14,6,15,6,15,5,15,4
    }; /**< A szellem útvonalát tároló objektum. X,Y koordináta párokat tárol, amik az oszlopszám, sorszám mintát követik.
     Fontos: ezek nem pixel koordináták, hanem blokk koordináták.*/

    private int direction; /**< Az irány, amerre a szellem éppen halad*/

    private int callNum; /** Ennek a változónak a feladata, hogy a szellem tudja, hogy hányadik lépését hajtja végre egy adott blokkon belül.*/
    private int iter; /**< Ezen változó segítségével iterál végig az útvonalon a szellem.*/

    /**
     * Az osztály konstruktora.
     * @param BLOCK_SIZE az objektum létrehozásakor érvényben lévő pályablokk méret
     *
     */
    public Clyde(int BLOCK_SIZE){
        super(8*BLOCK_SIZE, 10*BLOCK_SIZE, BLOCK_SIZE);

        iter = 0;
        callNum = 0;
        direction = 2;
    }

    /**
     * Ez a metúdus a kiindulási pontjába helyezi a szellemet, visszaállítja a kezdőirányt, és nullázza a hívásszámot,
     * és az iterátor értékét.
     */
    @Override
    public void setCentre(){
        X = 8*BLOCK_SIZE;
        Y = 10*BLOCK_SIZE;
        iter = 0;
        callNum = 0;
        direction = 2;
    }

    /**
     * Ez a metódus rajzolja ki Pinky szellem képét a megfelelő koordínátákra, felüldefiniálva az ős ugyanilyen absztrakt
     * metódusát.
     * @param frame_g Graphics2D objektum, amire rajzolunk
     * @param c ImageObserver objektum, rendszerint a hívó objektum
     */
    @Override
    public void draw(Graphics2D frame_g, Object c) {
        frame_g.drawImage(Effects.Statics.CLYDE.getStatPic(), X,Y,BLOCK_SIZE-1,BLOCK_SIZE-1,(ImageObserver) c);
    }

    /**
     * Ez a metódus minden hívsára annyival növeli a direction attribútum szerinti irányba a szellem sebességét, amekkora
     * szinten tart a PacMan.
     * @param level a PacMan szintje
     */
    private void step(int level){
        switch (direction) {
            case 1 -> X -= level;
            case 2 -> Y -= level;
            case 3 -> Y += level;
            default -> X += level;
        }
    }

    /**
     * Ez a metúdus felel annak eldöntéséért, hogy a szellem elért-e már egy adott pályablokk végére, és ha igen, akkor merre folytassa útját,
     * a route attribútum szerint. Itt hívódik a step metódus is. Ezt a heterogén kollekciónak köszönhetően egy for ciklussal
     * a Maze objektum el tudja végezni. A 34 azért kell, mert a központi helyről 17 blokkot megy a körkörös mozgás kezdőpontjáig,
     * viszont nem akarjuk, hogy visszamenjen a középső "zárkába".
     * @param level hányas szinten tart éppen a pacman
     */
    @Override
    public void move(int level) {
        callNum++;
        if(callNum == BLOCK_SIZE/level){
            short tempX = route[iter];
            short tempY = route[iter+1];
            iter+=2;
            callNum = 0;
            if(iter == route.length){
                iter = 34;
            }
            if(tempX > route[iter])
                direction = 1;
            else if(tempX < route[iter])
                direction = 4;
            else if(tempY > route[iter+1])
                direction = 2;
            else
                direction = 3;
        }
        step(level);
    }
}
