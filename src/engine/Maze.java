package engine;

import character.*;
import character.Elements;
import graphics.Effects;
import staticElement.Pellet;
import staticElement.PowerPellet;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * \class Maze
 * \brief Ez az osztály a játék lelke
 *
 * Gyakorlatilag ez az osztály tekinthető a játék engine-jének. Ez kommunikál a különbőző JFrame elemekkel (játék, menü),
 * itt történik a kirajzolás, itt történik a szellemekkel való ütközés, a pontok, vagy extra pontok megevése, az élet, a pontszám,
 * a szint változtatása. Minden, ami a játék logikájához kapcsolódik. Leszármazik a JPanel ősből.
 */
public class Maze extends JPanel {

    public static int BLOCK_SIZE; /**< A mindenkori blokkméret, pixelben*/
    public static int MAZE_COLUMN; /**< A pálya szélessége, blokkokban*/
    public static int MAZE_ROW; /**< A pálya magassága, blokkokban*/
    private BufferedImage gamePicture = null; /**< A pálya megjeleníéséhez használt Bufferedimage attribútum, hogy ne szaggasson a kirajzolás*/
    private PacMan player; /**< A játékos által irányított PacMan példány*/
    private final GameFrame game; /**< A játék kerete, amiben fut*/
    private final Timer timer; /**< Egy Swing Timer, aminek köszönhetően szisztematikusan időről időre meghívódhatnak a megfelelő metódusok*/
    private int powerTime; /**< Egy int változó, hogy meddig legyenek aktívak az extra pontok*/
    private boolean powerPelletActive = false; /**< Egy boolean változó, hogy éppen aktívak-e az extra pontok*/
    private int powerCalcTime; /**< Egy int változó, hogy milyen időközönként nézze meg a játék, hogy aktiválja-e az extra pontokat*/
    private boolean isRunning = false;

    private static final int fps = 120; /**< Egy "fps" változó, ami nagyjából annak felel meg, hogy milyen képfrissítéssel fut a játék*/

    ///oszlop,sor = x,y
    private final short[] walls = {
            0,0,1,0,2,0,3,0,4,0,5,0,6,0,7,0,8,0,9,0,10,0,11,0,12,0,13,0,14,0,15,0,16,0,
            0,1,8,1,16,1,
            0,2,2,2,3,2,5,2,6,2,8,2,10,2,11,2,13,2,14,2,16,2,
            0,3,2,3,3,3,5,3,6,3,8,3,10,3,11,3,13,3,14,3,16,3,
            0,4,16,4,
            0,5,2,5,3,5,5,5,7,5,8,5,9,5,11,5,13,5,14,5,16,5,
            0,6,5,6,8,6,11,6,16,6,
            0,7,1,7,2,7,3,7,5,7,6,7,8,7,10,7,11,7,13,7,14,7,15,7,16,7,
            3,8,5,8,11,8,13,8,
            0,9,1,9,2,9,3,9,5,9,7,9,8,9,9,9,11,9,13,9,14,9,15,9,16,9,
            0,10,7,10,9,10,16,10,
            0,11,1,11,2,11,3,11,5,11,7,11,8,11,9,11,11,11,13,11,14,11,15,11,16,11,
            3,12,5,12,11,12,13,12,
            0,13,1,13,2,13,3,13,5,13,7,13,8,13,9,13,11,13,13,13,14,13,15,13,16,13,
            0,14,7,14,9,14,16,14,
            0,15,1,15,3,15,4,15,5,15,11,15,12,15,13,15,15,15,16,15,
            0,16,1,16,3,16,7,16,8,16,9,16,13,16,15,16,16,16,
            0,17,5,17,8,17,11,17,16,17,
            0,18,2,18,3,18,4,18,5,18,6,18,8,18,10,18,11,18,12,18,13,18,14,18,16,18,
            0,19,16,19,
            0,20,1,20,2,20,3,20,4,20,5,20,6,20,7,20,8,20,9,20,10,20,11,20,12,20,13,20,14,20,15,20,16,20
    }; /**< A pálya falainak koordinátáit (oszlop;sor) tartalmazó short tömb*/

    ///oszlop,sor = x,y
    private final short[] dots = {
            2,1,3,1,4,1,5,1,6,1,7,1,9,1,10,1,11,1,12,1,13,1,14,1,
            1,2,4,2,7,2,9,2,12,2,15,2,
            1,3,4,3,7,3,9,3,12,3,15,3,
            1,4,2,4,3,4,4,4,5,4,6,4,7,4,8,4,9,4,10,4,11,4,12,4,13,4,14,4,15,4,
            1,5,4,5,6,5,10,5,12,5,15,5,
            1,6,2,6,3,6,4,6,6,6,7,6,9,6,10,6,12,6,13,6,14,6,15,6,
            4,7,7,7,9,7,12,7,
            4,8,6,8,7,8,8,8,9,8,10,8,12,8,
            4,9,6,9,10,9,12,9,
            4,10,5,10,6,10,10,10,11,10,12,10,
            4,11,6,11,10,11,12,11,
            4,12,6,12,7,12,8,12,9,12,10,12,12,12,
            4,13,6,13,10,13,12,13,
            1,14,2,14,3,14,4,14,5,14,6,14,8,14,10,14,11,14,12,14,13,14,14,14,15,14,
            2,15,6,15,7,15,9,15,10,15,14,15,
            2,16,4,16,5,16,6,16,10,16,11,16,12,16,14,16,
            1,17,2,17,3,17,4,17,6,17,7,17,9,17,10,17,12,17,13,17,14,17,15,17,
            1,18,7,18,9,18,15,18,
            2,19,3,19,4,19,5,19,6,19,7,19,8,19,9,19,10,19,11,19,12,19,13,19,14,19
    }; /**< A pálya pontjainak kezdőhelyeinek koordinátáit (oszlop;sor) tartalmazó short tömb*/

    private ArrayList<Elements> mazeElements = new ArrayList<>(); /**< A pályaelemeket (Elements) tartalmazó heterogén kollekció*/
    private ArrayList<PowerPellet> powerPellets = new ArrayList<>(); /**< Az extra pontokat tartalmazó tároló*/
    private final ArrayList<Ghost> ghosts = new ArrayList<>(); /**< A szellemek heterogén kollekciója*/

    private final Effects p = new Effects(); /**< A játék effektjeit tartalmazó objektum.*/

    /**
     * Az osztály konstruktora. Átveszi a pálya méreteit, inicializálja a szellemeket, beállítja a JPanel típusához tartozó
     * paramétereket, majd a powerCalcTime attribútumot beállítja az fps alapján, és a Timer-t is paraméterezi késleltetés, és
     * frekvencia szempontjából.
     * @param FRAME_COLUMN a játékkeret szélessége, blokkokban
     * @param FRAME_ROW a játékkeret magassága, blokkokban
     * @param BLOCK a játékkeret blokkmérete, pixelben
     * @param gameFrame a játék GameFrame kerete
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    public Maze(int FRAME_COLUMN, int FRAME_ROW, int BLOCK, GameFrame gameFrame) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        BLOCK_SIZE = BLOCK;
        MAZE_COLUMN = FRAME_COLUMN;
        MAZE_ROW = FRAME_ROW-3;
        game = gameFrame;

        setPreferredSize(new Dimension(MAZE_COLUMN*BLOCK_SIZE, MAZE_ROW*BLOCK_SIZE));
        setLayout(new BorderLayout());

        powerCalcTime = 5*fps;

        initGhosts();

        timer = new Timer(1000/fps,new Step());
        timer.setInitialDelay(3000);
    }

    /**
     * Ez a függvény minden játékinduláskor inicializálja a szellemeket, és hozzáadja pket a heterogén kolleckiójukhoz.
     */
    private void initGhosts(){
        Inky inky = new Inky(BLOCK_SIZE);
        Blinky blinky = new Blinky(BLOCK_SIZE);
        Pinky pinky = new Pinky(BLOCK_SIZE);
        Clyde clyde = new Clyde(BLOCK_SIZE);

        ghosts.add(inky);
        ghosts.add(blinky);
        ghosts.add(pinky);
        ghosts.add(clyde);
    }

    /**
     * Ez a subclass felel azért, hogy a Timer minden tick-jére meghívódjanak a megfelelő metódusok.
     */
    public class Step implements ActionListener{
        /**
         * Ez a függvény hívódik meg minden egyes tick-re. Mozgatja a PacMan-t a benne beállított irányba, majd mozgat
         * minden szellemet a PacMan szintjének megfelelő sebességgel, ezután ellenőrzi, hogy megevett-e a PacMan egy pontot,
         * majd ellenőrzi, hogy a PacMan ütközött-e szellemel. Ezután eldönti, hogy elindítsa-e az extra pontokat, és ha
         * ezeket elindította, akkor utána mindaddig, amíg abba nem marad az extra pontok megjelenítése, ellenőrzi, hogy a PacMan
         * megevett-e egyet. Ezek után megnézi, hogy kell-e növelni a PacMan szintjén, végezetül meghívja a repaint() metódust,
         * amivel újrarajzolja a megfelelő helyváltoztatásokkal a pályát.
         * @param e meghívták adott tick-re
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(isRunning) {
                player.move();

                try {
                    p.initSounds();
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ioException) {
                    ioException.printStackTrace();
                }

                for (Ghost gh : ghosts)
                    gh.move(player.getLevel());
                eatDot();
                ghostHitCalc();
                powerShouldStart();
                if (powerPelletActive) {
                    eatPowerPelletCalc();
                }
                newLevelRequired();

                repaint();
            }
        }
    }

    /**
     * A függvény megállítjaa Timer-t. ERre azért van szükség, mert néha a játékkeretnek kell ezt megtennie.
     */
    public void stopTimer(){
        timer.stop();
    }

    /**
     * Ez a metódus hívódik meg, ha új játékot indítunk. Új PacMan objektumot hoz létre, és új heterogén kollekciót a pályaelemeknek.
     * Ezután reseteli az eredményjelzőn a pontszámot, életszámot, és szintszámot, létrehozza az új pontokat, beállítja a
     * PacMan kezdőirányát, a szellemeket középre helyezi, újrarajzolja a pályát, és elindítja a Timer-t.
     * @param name a játékos által begépelt név
     */
    public void newGame(String name) {
        player = new PacMan(name, BLOCK_SIZE, walls);
        mazeElements = new ArrayList<>();
        game.updateScoreLevel(1);
        game.updateScorePoints(0);
        game.updateScoreLives(3);

        setGhostsCenter();
        player.setCentre();

        initDots();
        mazeElements.add(player);

        Clip c = Effects.Sounds.BEGINNING.getClip();
        c.start();

        isRunning = true;

        repaint();
        timer.start();
    }

    /**
     * Ez a metódus tölti be az elmentett játékot. Ehhez az átvett pályaelemeket tartalmazó listát beállítja az új
     * heterogén kollekciónak, majd minden pályaelemnek átállítja a blokkméretét, ha netán az előző mentésnél ez más
     *  érték lett volna, középre helyezi a PacMan-t, majd frissíti az eredményjelző megfelelő részeit, a kiindulópontjaikba
     *  helyezi a szellemeket, újrarajzolja a pályát, és elindítja a Timer-t.
     * @param list a mentett pályaelemeket tartalmazó lista
     */
    public void loadGame(ArrayList<Elements> list){
        mazeElements = list;
        for (Elements mE : mazeElements) {
            mE.changeBlockSize(BLOCK_SIZE);
            if (mE.getPlayerName() != null)
                player = (PacMan) mE;
        }
        game.updateScorePoints(player.getPoints());
        game.updateScoreLevel(player.getLevel());
        game.updateScoreLives(player.getLives());

        player.setCentre();
        setGhostsCenter();

        Clip c = Effects.Sounds.BEGINNING.getClip();
        c.start();

        isRunning = true;

        repaint();
        timer.start();
    }

    /**
     * Ez a függvény hívódik meg a game játékkeretből, ha valaki megnyomja az Escape-et, majd meghívja a keret saveGame
     * metódusát, és átadja neki a pályaelemeket tartalmazó tárolót, és a PacMan name attribútumát.
     * @throws IOException
     */
    public void saveGame() throws IOException {
        game.saveGame(mazeElements, player.getPlayerName());
    }

    /**
     * Ez a függvény hívódik meg, ha véget ért a játék. Ez két okból történhet, vagyeljutott a PacMan a harmadik szintre,
     * és minden pontot megevett, vagy elfogyott minden élete. Ezután ez meghívja a game keret addToLeaderboard metódusát átadva a
     * PacMan-t, és leállítja a Timer-t.
     */
    private void endGame(){
        timer.stop();
        isRunning = false;
        game.addToLeaderboard(player);
    }

    /**
     * Ez a föggvény megnézi, hogy ütközik-e a PacMan és valamelyik szellem. Ezt úgy teszi, hogy a PacMan és a szellem koordinátáit
     * összeveti, és ha elegendően kicsi a két objektum közti távolság, meghívja a hitByGhost metódust.
     */
    private void ghostHitCalc(){
        for(Ghost gh : ghosts){
            int tempX = Math.abs(gh.getX()-player.getX());
            int tempY = Math.abs(gh.getY()-player.getY());
                    if(tempX < BLOCK_SIZE-5 && tempY < BLOCK_SIZE-5 )
                        hitByGhost();
        }
    }

    /**
     * Ez a metódus hívódik meg akkor, ha a koordináták alapján a PacMan ütközik egy szellemmel. Csökkenti egyel a PacMan
     * életét, majd frissíti ezt az értéket az eredményjelzől, megállítja a Timer-t, ellenőrzi, hogy maradt-e még élete a
     * PacMan-nek, és ha nem, akkor hívja az endGame metódust, ha igen, akkor a kiindulópontjára helyez minden szellemet, és
     * a PacMan-t, majd újraindítja a Timer-t.
     */
    private void hitByGhost(){
        player.changeLivesNumber(false);
        game.updateScoreLives(player.getLives());
        timer.stop();

        if(player.getLives() == 0)
            endGame();
        else {
            player.setCentre();
            Clip c = Effects.Sounds.DEATH.getClip();
            c.start();
            for(Ghost g: ghosts)
                g.setCentre();
        }
        timer.start();
    }

    /**
     * Ez a metódus dönti el, hogy elindítsa-e az extra pontok funkciót. Ehhez emgvizsgálja, hogy éppen érvényben van-e ez a
     * funkció, és ha igen, akkor csökkenti a powerTime változó értékét, és ha ez eléri a nullát, akkor tudja, hogy lejárt az
     * aktivitásra szánt idő, ezért törli az extra pontokat a pályáról. Ha nem volt éppen aktív ez a funkció, akkor csökkenti
     * a powerCalcTime változó értékét, majd ha ez eléri a nullát, akkor generál egy random számot 0-1 között, és ha ez kisebb,
     * mint 0.5, akkor elindítja a funkciót. A funkció 10 másodpercig aktív, és 5 másodpercenként nézi meg a pálya, hogy elindítsa-e.
     */
    private void powerShouldStart(){
        Random rand = new Random();
        int[] coord = {
                1,1,
                15,1,
                1,19,
                15,19
        };

        if(powerPelletActive){
            if(powerTime > 0)
                powerTime--;
            if(powerTime == 0){
                powerPellets.clear();
                powerPelletActive = false;
            }
        }else {
            powerCalcTime--;
            if(powerCalcTime == 0) {
                if (rand.nextDouble() < 0.50D) {
                    powerPelletActive = true;
                    powerPellets = new ArrayList<>();
                    for (int i = 0; i < 8; i += 2)
                        powerPellets.add(
                                new PowerPellet(coord[i]*BLOCK_SIZE, coord[i + 1]*BLOCK_SIZE, BLOCK_SIZE));
                    powerTime = 10 * fps;
                }
                powerCalcTime = 5*fps;
            }
        }
    }

    /**
     * Ha aktív az extra pont funkció, akkor ez a függvény hívódik meg minden tick-re, hogy ellenőrizze, megevett-e a PacMan
     * egy PowerPellet-et. Ehhez a két objektum koordinátáit használja egy bizonyos tűréshatárral.
     */
    private void eatPowerPelletCalc(){
        for(Iterator<PowerPellet> it = powerPellets.iterator();it.hasNext(); ){
            PowerPellet elem = it.next();
                int tempX = Math.abs(elem.getX()-player.getX());
                int tempY = Math.abs(elem.getY()-player.getY());
                        if(tempX < BLOCK_SIZE/3 && tempY < BLOCK_SIZE/3) {
                            it.remove();
                            powerPelletEaten();
                        }
        }
    }

    /**
     * Ha a PacMan megevett egy PowerPellet-et, akkor meghívódik ez a metódus, és növeli a PacMan pontszámát, életét,
     * illetve ezek értékét frissíti az eredményjelzőn.
     */
    private void powerPelletEaten(){
        player.increasePoints(true);
        player.changeLivesNumber(true);
        game.updateScoreLives(player.getLives());
        game.updateScorePoints(player.getPoints());
        Clip c = Effects.Sounds.EXTRA.getClip();
        c.start();
    }

    /**
     * A függvény megvizsgálja minden tick-re, hogy kell-e szintet növelni. Ezt úgy teszi, hogy a mazeElements változó
     * darabszámát viozsgálja. Ha ez 1, azaz csak a PacMan maradt benne, akkor minden pont elfogyott, tehát vagy megnyerte a
     * PacMan a játékot, ha már a harmadik szinten van, vagy pedig szintet lép. Ilyenkor újrainicializálja a pontokat, és
     * frissíti az eredményjelzőt.
     */
    private void newLevelRequired() {
        if(mazeElements.size() == 1){
            timer.stop();
            if(player.getLevel() == 3){
                endGame();
            }
            player.increaseLevel();
            for(Ghost g : ghosts)
                g.setCentre();
            game.updateScoreLevel(player.getLevel());
            initDots();
            timer.start();
        }
    }

    /**
     * Ez a metódus hívódik meg minden tick-re, és ellenőrzi, hogy a PacMan-megevett-e egy pontot. Teszi mindezt a PacMan
     *  és a pontok koordinátáinak összehasonlításával.
     */
    private void eatDot(){
        for(Iterator<Elements> it = mazeElements.iterator();it.hasNext(); ){
            Elements elem = it.next();
            if(elem.getPlayerName() == null){
                int tempX = Math.abs(elem.getX()-player.getX());
                int tempY = Math.abs(elem.getY()-player.getY());
                        if(tempX < BLOCK_SIZE/4 && tempY < BLOCK_SIZE/4) {
                            it.remove();
                            pelletEaten();
                        }
                }
        }
    }

    /**
     * Ha a Pacman megevett egy pontot, meghívja ezt a függvényt, ami növeli a pontszámát, és frissíti az eredményjelzőt.
     */
    private void pelletEaten(){
        player.increasePoints(false);
        Clip c = Effects.Sounds.COIN.getClip();
        c.start();
        game.updateScorePoints(player.getPoints());
    }

    /**
     * Ez a metódus az összes szellemet a kiinduló helyzetbe helyezi.
     */
    private void setGhostsCenter(){
        for(Ghost g : ghosts)
            g.setCentre();
    }

    /**
     * Minden új játéknál, illetve szintlépésnél meghívódik ez a függvény, és létrehozza az új pontokat a játékmezőn,
     * hozáadva őket a pályaelemek heterogén kollekciójához.
     */
    private void initDots() {
           for(int i = 0; i<dots.length; i+=2){
               Pellet p = new Pellet(dots[i]*BLOCK_SIZE, dots[i+1]*BLOCK_SIZE, BLOCK_SIZE);
               mazeElements.add(0,p);
           }
    }

    /**
     * Ez a metódus hívódik meg, amikor a játékos megnyom egy nyilat, és átállítja a PacMan irányát.
     * @param direction az új irány
     */
    public void movePacMan(int direction){
        player.setDirection(direction);
    }

    /**
     * Amennyiben a gamePictre attribútum null lenne, ez a metódus inicializálja azt.
     */
    private void initGamePicture(){
        if(gamePicture == null)
            gamePicture = new BufferedImage(MAZE_COLUMN*BLOCK_SIZE, MAZE_ROW*BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Ez a függvény a JPanel egyik metódusa, amit felüldefiniálok itt. Ez inicializálja a gamePicture attribútumot, ha
     * erre szükség van, majd elkéri tőle a grafikai sémáját, beállítja rajta az Anialiasing paramétereket, és rárajzolja
     * a megfelelő elemeket. Ilyen például a falak, a kapu, a még meglévő pontok, a PacMan, és a szellemek. Ezután a gamePicture
     * attribútumot megjeleníti a saját JPanelen.
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        initGamePicture();

        Graphics2D frame_g = (Graphics2D) gamePicture.getGraphics();
        frame_g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //A pálya statikus részeinek kirajzolása
        frame_g.setColor(Color.BLACK);
        frame_g.fillRect(0,0,getWidth(),getHeight());

        for(int i = 0; i<walls.length; i+=2) {
            if(walls[i] == 8 && walls[i+1] == 9)
                frame_g.drawImage(
                        Effects.Statics.GATE.getStatPic(),walls[i] * BLOCK_SIZE, walls[i+1] * BLOCK_SIZE, BLOCK_SIZE - 1, BLOCK_SIZE - 1,this);
            else
                frame_g.drawImage(
                        Effects.Statics.WALL.getStatPic(), walls[i] * BLOCK_SIZE, walls[i+1] * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE,this);
        }

        ///A pálya dinamikus részeinek kirajzolása
        ///Extra pontok
        for (PowerPellet pellet : powerPellets)
            pellet.draw(frame_g, this);
        ///Pályaelemek (pontok, PacMan)
        for (Elements mE : mazeElements)
            mE.draw(frame_g,this);
        ///Szellemek
        for(Ghost gh : ghosts)
            gh.draw(frame_g,this);

        g.drawImage(gamePicture,0,0,this);
    }
}
