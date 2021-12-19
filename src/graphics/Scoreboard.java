package graphics;

import javax.swing.*;
import java.awt.*;

/**
 * \class Scoreboard
 * \brief Ez az osztály felel az eredménytábláért.
 *
 * Ebben az osztályban zajlik a pontszámok, életek, szintek kiíratása, és frissítése, ehhez leszármazik a JPanel ősből.
 */
public class Scoreboard extends JPanel {

    private JLabel score; /**< Erre a JLabelre kerülnek a pontok*/
    private JLabel levelNum; /**< Erre a JLabelre kerül a szint száma*/
    private JLabel lives; /**< Erre a JLabelre kerülnek az életek*/

    /**
     * Az osztály konstruktora. Itt állítja be a paramétereit.
     * @param FRAME_COLUMN a játékpanel szélessége, blokkokban
     * @param FRAME_ROW a játékpanel magassága, blokkokban
     * @param BLOCK_SIZE a pályablokk mérete
     */
    public Scoreboard(int FRAME_COLUMN, int FRAME_ROW, int BLOCK_SIZE){
        setPreferredSize(new Dimension(
                FRAME_COLUMN*BLOCK_SIZE, FRAME_ROW*BLOCK_SIZE-(FRAME_ROW*BLOCK_SIZE-3*BLOCK_SIZE)));
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        initLabels(BLOCK_SIZE);

        add(lives, BorderLayout.WEST);
        add(levelNum,BorderLayout.EAST);
        add(score, BorderLayout.NORTH);
    }

    /**
     * Ez a metódus hívja meg az erdménytábla részeinek inicializálásához szükséges metódusokat.
     * @param BLOCK_SIZE a pályablokk mérete
     */
    private void initLabels(int BLOCK_SIZE){
        initLives(BLOCK_SIZE);
        initLevelNum(BLOCK_SIZE);
        initScore(BLOCK_SIZE);
    }

    /**
     * Ez a metódus inicializálja az életek kijelzéséhez szükséges JPanelt.
     * @param BLOCK_SIZE a pályablokk mérete
     */
    private void initLives(int BLOCK_SIZE){
        lives = new JLabel();
        lives.setForeground(Color.WHITE);
        lives.setFont(new Font("Times New Roman", Font.BOLD, BLOCK_SIZE-5));
    }

    /**
     * Ez a metódus inicializálja a szint kijelzéséhez szükséges JPanelt.
     * @param BLOCK_SIZE a pályablokk mérete
     */
    private void initLevelNum(int BLOCK_SIZE){
        levelNum = new JLabel("LEVEL: " + 1);
        levelNum.setForeground(Color.WHITE);
        levelNum.setFont(new Font("Times New Roman", Font.BOLD, BLOCK_SIZE-5));
    }

    /**
     * Ez a metódus inicializálja a pontszámw kijelzéséhez szükséges JPanelt.
     * @param BLOCK_SIZE a pályablokk mérete
     */
    private void initScore(int BLOCK_SIZE){
        score = new JLabel("SCORE: " + 0);
        score.setForeground(Color.WHITE);
        score.setFont(new Font("Times New Roman", Font.BOLD, BLOCK_SIZE));
    }

    /**
     * Ez a metódus frissíti a paraméterben étvett értékkel a score "kijelzőt".
     * @param newScore hány pontja van a játékosnak
     */
    public void updatePoints(long newScore){
        score.setText("SCORE: " + newScore);
    }

    /**
     * Ez a metódus frissíti a paraméterben átvett értékkel a levelNum "kijelzőt".
     * @param level hányadik szinten jár a PacMan
     */
    public void updateLevel(int level){
        levelNum.setText("LEVEL: " + level);
    }

    /**
     * Ez a metódus frissíti a paraméterben átvett értékkel a lives "kijelzőt".
     * @param num hány életre frissítsen
     */
    public void updateLives(int num){
        lives.setText("LIVES: " + num);
    }
}
