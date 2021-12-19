package engine;

import character.PacMan;
import character.Elements;
import graphics.Effects;
import graphics.Scoreboard;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;

/**
 * \class GameFrame
 * \brief A játék keretét megvalósító osztály.
 *
 * Ez az osztály felel azért, hogy a pálya, és az ereményjelző szekció össze legyen fogva, és egyszerre legyenek kezelhetőek.
 * Ha valaki megváltoztatja a páyablokk méretet a Game osztályban, akkor ez az ablak, benne a játékkal együtt átméreteződik.
 * A JFrame ősből származik le, és a KeyListener interfészt valósítja meg. Ez az osztály érzékeli a játékos által lenyomott billentyűket.
 *
 */
public class GameFrame extends JFrame implements KeyListener{

    private Scoreboard score; /**< Az eredményjelző Scoreboard osztály egy példánya*/
    private Maze maze;/**< A pálya (Maze) osztály egy példánya*/
    private MenuFrame menu;/**< A menüt megalkotó MenuFrame osztály. A MenuFrame objektum inicializálása után a GameFrame megkapja azt*/
    private final Effects pics = new Effects();/**< A képekért felelős, enumerációkat tartalmazó Pictures osztály egy példánya*/

    private int FRAME_COLUMN;/**< A játékpanel szélessége, blokkokban*/
    private int FRAME_ROW;/**< A játékpanel magassága, az eredményjelzővel együtt, blokkokban*/
    private int BLOCK_SIZE;/**< Az indításkor érvényben lévő blokkméret a Game osztálytól, pixelben*/

    /**
     * Az osztály konstruktora. Itt kikapcsoljuk a dekorálást, azaz az operációs rendszer ablakkeretét, illetve az átméretezhetőséget is,
     * majd középre igazítjuk a keretet. A jobb átláthatóság kedvéért az inicializálást három függvénybe szerveztem ki.
     * @param column a Game osztály példányától kapott szélesség, blokkokban
     * @param row a Game osztály példányától kapott magasság, blokkokban
     * @param block a Game osztály példányától kapott blokkméret, pixelben
     * @throws IOException
     * @throws LineUnavailableException
     * @throws UnsupportedAudioFileException
     */
    public GameFrame(int column, int row, int block) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        initVariables(column, row, block);
        initPanels();
        initFrame();

        add(score, BorderLayout.SOUTH);
        add(maze, BorderLayout.NORTH);

        setUndecorated(true);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        addKeyListener(this);
    }

    /**
     * Mivel a mentés/betöltésért a MenuFrame osztály felel, így a GameFrame-nek tudnia kell annak példányáról. Erre szolgál
     * ez a metódus.
     * @param f a használandó MenuFrame objektum
     */
    public void giveMenuFrame(MenuFrame f) {
        menu = f;
    }

    /**
     * Ez a metódus felel a változók inicializásáért.
     * @param c a játékkeret szélessége, blokkokban
     * @param r a játékkeret magassága az eredményjelzővel együtt, blokkokban
     * @param b az indításkor érvényben lévő blokkméret, pixelben
     */
    private void initVariables(int c, int r, int b){
        FRAME_COLUMN = c;
        FRAME_ROW = r;
        BLOCK_SIZE = b;
    }

    /**
     * A JFrame őshöz kapcsolódó inicializálások.
     */
    private void initFrame(){
        setLayout(new BorderLayout());
        setTitle("PacMan by Márton Reiter");;
        setIconImage(Effects.Dynamics.PAC_RIGHT.getDynPic(2));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Itt jön létre a pályát magába foglaló Maze osztály egy példánya, illetve az eredményjelzésért felelős Scoreboard
     * osztály egy példánya.
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    private void initPanels() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        score = new Scoreboard(FRAME_COLUMN,FRAME_ROW, BLOCK_SIZE);
        maze = new Maze(FRAME_COLUMN,FRAME_ROW,BLOCK_SIZE, this);
    }

    /**
     * Ha a menüből új játékot indít valaki, akkor ez a metódus hívódik meg, ami meghívja a pálya newGame metódusát.
     * @param name a játékos által új játék kezdésekor begépelt név
     */
    public void startNewGame(String name){
        maze.newGame(name);
    }

    /**
     * Ha a menüből valaki betölt egy játékot, akkor ez a metódus hívódik meg, ami meghívja a pálya loadGame metódusát.
     * @param list a Save osztály megfelelő példányából kinyert játékelemeket tároló lista
     */
    public void startLoadGame(ArrayList<Elements> list){
        maze.loadGame(list);
    }

    /**
     * Minden egyes Escape billentyű leütésre a pálya elmetni a játékállást, amivel meghívja ezt a metódust, ez pedig meghívja a
     * menü addToSaveStates metódusát.
     * @param list a játékelemeket tartalmazó lista
     * @param name a PacMan-ben eltárolt név
     * @throws IOException
     */
    public void saveGame(ArrayList<Elements> list, String name) throws IOException {
        menu.addToSaveStates(list, name);
    }

    /**
     * Ha a pálya azt mondja, hogy frissíteni kell az eredményjelzőn a pontok állását, akkor ezt a metódust hívja meg, ami
     * meghívja a Scoreboard osztály példányának updatePoints metódusát.
     * @param points long típusú szám, amire kell frissíteni
     */
    public void updateScorePoints(long points){
        score.updatePoints(points);
    }

    /**
     * Ha a pálya azt mondja, hogy frissíteni kell az eredményjelzőn a szintek állását, akkor ezt a metódust hívja meg, ami
     * meghívja a Scoreboard osztály példányának updateLevel metódusát.
     * @param level int típusú szám, amire kell frissíteni
     */
    public void updateScoreLevel(int level){
        score.updateLevel(level);
    }

    /**
     * Ha a pálya azt mondja, hogy frissíteni kell az eredményjelzőn az életek számának állását, akkor ezt a metódust hívja meg, ami
     * meghívja a Scoreboard osztály példányának updateLives metódusát.
     * @param num int típusú szám, amire kell frissíteni
     */
    public void updateScoreLives(int num){
        score.updateLives(num);
    }

    /**
     * Ha egy játékos meghal, vagy a harmadik szinten is összeszedte az összes kicsi pontot, akkor vége a játéknak, és felkerül
     * az eredményjelzőre. Ilyenkor ez a metódus hívódik meg, ami továbbítja a játékban résztvevő PacMan példányt a menünek.
     * @param p a PacMan példány
     */
    public void addToLeaderboard(PacMan p){
        System.out.println("MEGHÍVTAK" + System.currentTimeMillis());
        menu.addToLeaderList(p);
        setVisible(false);
    }

    /**
     * Mivel az osztály megvalósítja a KeyListener intrefészt, ezért ez egy kötelezően implementálandó metódus, de ezt nem használom
     * semmire.
     * @param e billentyűleütés történt
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Mivel az osztály megvalósítja a KeyListener intrefészt, ezért ez egy kötelezően implementálandó metódus. Itt kezelem le,
     * hogy játék közben milyen billentyűleütések történnek. Ez lehet az Escape, illetve a négy nyíl.
     * @param e billentyűleütés történt
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                try {
                    setVisible(false);
                    maze.stopTimer();
                    maze.saveGame();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                menu.setVisible(true);
                break;
            case KeyEvent.VK_RIGHT:
                maze.movePacMan(4);
                break;
            case KeyEvent.VK_LEFT:
                maze.movePacMan(1);

                break;
            case KeyEvent.VK_UP:
                maze.movePacMan(2);
                break;
            case KeyEvent.VK_DOWN:
                maze.movePacMan(3);
                break;
            default:
                break;
        }
    }

    /**
     * Mivel az osztály megvalósítja a KeyListener intrefészt, ezért ez egy kötelezően implementálandó metódus, de ezt nem használom
     * semmire.
     * @param e billentyűfelengedés történt
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
