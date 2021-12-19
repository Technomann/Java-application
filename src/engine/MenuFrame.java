package engine;

import character.PacMan;
import character.Elements;
import graphics.Effects;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * \class MenuFrame
 * \brief Ez az osztály felel a menü megvalósításáért
 *
 * Itt jön létre minden a menüben látható elem, itt töltődnek be a mentett állások, és itt mentődnek el, a ranglistával együtt.
 * A JFrame ősből származik le, és ActionListener subclass-okat használ a gombok érzékeléséhez.
 */
public class MenuFrame extends JFrame{

    private final JButton newGame = new JButton("NEW GAME"); /**< Az új játék felület indításáért felelős gomb*/
    private final JButton quit =  new JButton("QUIT"); /**< A kilépésért felelős gomb*/
    private final JPanel menu = new JPanel(); /**< A menüsor JPanel-je*/
    private final JPanel loadPanel = new JPanel(); /**< A betöltés rész JPanel-je*/
    private JTable board; /**< A ranglista JTable táblázata*/
    private final JLabel name = new JLabel("PLEASE ENTER YOUR NAME"); /**< Új játékhot a név beírására felszólító felirat*/
    private final JTextField nameTextField = new JTextField(20); /**< Az a JTextField, ahova beírhatja a nevét a játékos*/
    private final JButton start = new JButton("START"); /**< A játék kezdéséhez használható gomb*/
    private final JButton load = new JButton("LOAD GAME"); /**< A betöltés felület megjelenítéséhez használt JPanel*/

    GameFrame game; /**< Az a GameFrame objektum, ahol a játék fut*/

    private final int FRAME_COLUMN; /**< A menü szélessége, blokkokban*/
    private final int FRAME_ROW; /**< A menü magassága, blokkokban*/
    private final int BLOCK_SIZE; /**< A menü kirajzolásához tartozó blokkméret, pixelben*/

    ArrayList<Save> saves; /**< A Save osztály mentéseket tartalmazó tároló*/
    ArrayList<PacMan> leaderBoard; /**< A ranglistához PacMan-eket tartalmazó tároló*/
    ArrayList<JButton> button = new ArrayList<>(); /**< A könnyebb kezelhetőség érdekében a betöltési gombokat így tárolom*/

    /**
     * Az osztály konstruktora. Lényegében a JFrame őstől kapott elemeket állítja be, illetve inicializálja a menu, load, és
     * leaderboard paneleket.
     * @param column a menü szélessége, blokkokban
     * @param row a menü magassába, blokkokban
     * @param block a menü blokkmérete, pixelben
     * @param g az a GameFrame objektum, amit használ a játék
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public MenuFrame(int column, int row, int block, GameFrame g) throws IOException, ClassNotFoundException {
        FRAME_COLUMN = column;
        FRAME_ROW = row;
        BLOCK_SIZE = block;
        game = g;

        setIconImage(Effects.Dynamics.PAC_RIGHT.getDynPic(2));

        setSize(new Dimension(FRAME_COLUMN*BLOCK_SIZE, FRAME_ROW*BLOCK_SIZE));
        setLayout(new BorderLayout());
        setUndecorated(true);

        initMenuBar();
        initLoadBoard();
        initLeaderBoard();

        add(menu,BorderLayout.NORTH);
        add(loadPanel, BorderLayout.CENTER);
        add(board,BorderLayout.SOUTH);

        setBackground(Color.black);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Ebben a metódusban inicializálódik a mmenüsor, és a benne lévő gombokba is itt kerülnek bele az ActionListener
     * implementációk.
     */
    private void initMenuBar(){
        menu.setLayout(new FlowLayout());
        menu.setBackground(Color.BLACK);

        newGame.addActionListener(new NewGameButtonListener());
        load.addActionListener(new LoadButtonListener());
        quit.addActionListener(new QuitListener());

        menu.add(newGame);
        menu.add(load);
        menu.add(quit);
    }

    /**
     * Ebben a metódusban inicializálódik a mentéseket, és az új játk indításához szükséges Swing elemeket tartalmazó panel,
     *  itt adódik a Start gombhoz a megfelelő ActionListener.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void initLoadBoard() throws IOException, ClassNotFoundException {
        loadPanel.setLayout(new FlowLayout());
        loadPanel.setBackground(Color.BLACK);

        initSaves(loadPanel);

        name.setForeground(Color.YELLOW);
        start.addActionListener(new StartButtonListener());

        loadPanel.add(name);
        loadPanel.add(nameTextField);
        loadPanel.add(start);

        name.setVisible(false);
        nameTextField.setVisible(false);
        start.setVisible(false);
    }

    /**
     * Ez a metódus ellenőrzi, hogy van-e már a user.dir könyvtárban savegame mappa, és ha nincs, létrehozza.
     */
    private void initSaveFolder(){
        File f = new File(System.getProperty("user.dir") + File.separator + "savegame");
        if(!f.exists()){
           if(!f.mkdir())
               System.out.println("WARNING: NO SAVEGAME DIRECTORY WAS CREATED");
        }

    }

    /**
     * Ebben a metódusban inicializálódnak szerializálással a mentések. Itt töltödik fel a mentéseket tartalmazó tároló,
     * és itt alakulnak meg az ezek eléréséhez szükséges JButton-ok is.
     * @param panel a mentések megjelenítéséért felelős panel
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void initSaves(JPanel panel) throws IOException, ClassNotFoundException {
        initSaveFolder();
        File f = new File(
                System.getProperty("user.dir") + File.separator + "savegame" + File.separator + "gamestate");
        if(f.exists()){
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            saves = (ArrayList<Save>) ois.readObject();
            ois.close();
            fis.close();
        }else{
            saves = new ArrayList<>();
            for(int i = 0; i<6; ++i){
                Save s = new Save("empty");
                saves.add(s);
            }
        }
        for(int i = 0; i<6; ++i){
            JButton but = new JButton();
            but.setPreferredSize(new Dimension(FRAME_COLUMN*BLOCK_SIZE/3-5, BLOCK_SIZE*4));
            but.setText(saves.get(i).getName());
            but.addActionListener(new LoadGameListener(i));
            if(saves.get(i).getName().equals("empty"))
                but.setEnabled(false);
            button.add(but);
            panel.add(but);
        }
    }

    /**
     * Ebben a metódusban inicializálódik a ranglistát tároló JTable, majd meghívja az initLeadrList metódust.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void initLeaderBoard() throws IOException, ClassNotFoundException {
        board = new JTable(10,2);

        board.setRowHeight(BLOCK_SIZE);
        TableColumnModel model = board.getColumnModel();
        for(int i = 0; i<2; ++i){
            model.getColumn(i).setPreferredWidth(FRAME_COLUMN*BLOCK_SIZE/2);
        }

        board.setBackground(Color.BLACK);
        board.setSelectionBackground(Color.BLACK);
        board.setSelectionForeground(Color.BLACK);
        board.setRowSelectionAllowed(false);
        board.setGridColor(Color.ORANGE);
        board.setFocusable(false);

        DefaultCellEditor editor = (DefaultCellEditor) board.getDefaultEditor(Object.class);
        editor.setClickCountToStart(100000);

        board.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        board.setDefaultRenderer(Object.class, centerRenderer);

        initLeaderList();
    }

    /**
     * Ez a metódus inicializálja a ranglista tárolóját szerializálással.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void initLeaderList() throws IOException, ClassNotFoundException {
        File f = new File(
                System.getProperty("user.dir") + File.separator + "savegame" + File.separator + "leaderboard");
        if(f.exists()){
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            leaderBoard = (ArrayList<PacMan>) ois.readObject();
            ois.close();
            fis.close();

            for(int i =0; i<leaderBoard.size(); ++i){
                board.setValueAt(leaderBoard.get(i).getPlayerName(),i,0);
                board.setValueAt(leaderBoard.get(i).getPoints(),i,1);
            }
        }else
            leaderBoard = new ArrayList<PacMan>();
    }

    /**
     * Ez a metódus felel azért, hogy a ranglista szerializálással egy fájlba kerüljön.
     * @throws IOException
     */
    private void saveLeaderList() throws IOException {
        FileOutputStream fos = new FileOutputStream(
                System.getProperty("user.dir") + File.separator + "savegame" + File.separator + "leaderboard");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(leaderBoard);
        oos.close();
        fos.close();
    }

    /**
     * Ez a metódus felel azért, hogy a játék végeztével a ranglista frissüljön az új delikvenssel. Egyszerre csak tizen lehetnek
     * a ranglistán. A metódus rendezi is a listát a PMComparator osztály segítségével.
     * @param p az új PacMan objektum
     */
    public void addToLeaderList(PacMan p){
        leaderBoard.add(p);
        leaderBoard.sort(new PMComparator());
       // System.out.println("FÜGGVÉNY");
        if(leaderBoard.size() > 10){
            leaderBoard.remove(10);
           // System.out.println("ITER");
        }
        for(int i = 0; i<leaderBoard.size();++i){
            board.setValueAt(leaderBoard.get(i).getPlayerName(),i,0);
            board.setValueAt(leaderBoard.get(i).getPoints(),i,1);
            //System.out.println("ITER2");

        }
        removeSaveGame(p.getPlayerName());
        setVisible(true);
    }

    /**
     * Ebben a metódusban zajlik a mentések szerializálása fájlba.
     * @throws IOException
     */
    private void saveGameStates() throws IOException {
        FileOutputStream fos = new FileOutputStream(
                System.getProperty("user.dir") + File.separator + "savegame" + File.separator + "gamestate");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(saves);
        oos.close();
        fos.close();
    }

    /**
     * Ez egy viszonylag bonyolult metódus arra, hogy az adott játékállást elmentsük. Megnézi, hogy van-e ugyanolyan name
     * attribútumot tartalmazó Save obektum a saves tárolóban, ha van, akkor azt felülírja, ha nincs, akkor megnézi, hogy
     * van-e szabad ("empty") Save objektum benne, és oda ment, ha pedig nincs se üres, se ugyanolyan name attribútmú, akkor a
     * Save objektumok kora alapján a legöregebbet felülírja. Ehhhez természetesen megfelelő pillanatokban növeli a saves-ben található
     * objektumok korát.
     * @param list az elmentendő játékelemeket tartalmazó lista
     * @param n az elmentetndő játékos neve
     * @throws IOException
     */
    public void addToSaveStates(ArrayList<Elements> list, String n) throws IOException {
        boolean thereIsSame = false;
        boolean thereIsEmpty = false;
        for(int i = 0; i<6; ++i ){
            if(saves.get(i).getName().equals(n)) {
                saves.get(i).giveList(list,n);
                thereIsSame = true;
            }else if (saves.get(i).getName().equals("empty"))
                thereIsEmpty = true;
        }

        if(thereIsEmpty && !thereIsSame){
            for(int i = 0; i<6; ++i) {
                if(!saves.get(i).getName().equals("empty"))
                    saves.get(i).incrementAge();
            }
            boolean thereIsAppropriate = false;
            for(int i =0; i<6; ++i){
                if (!thereIsAppropriate && saves.get(i).getName().equals("empty")) {
                    saves.get(i).giveList(list,n);
                    saves.get(i).setName(n);
                    button.get(i).setText(n);
                    button.get(i).setEnabled(true);
                    thereIsAppropriate = true;
                }
            }
        }else if(!thereIsEmpty && !thereIsSame){
            for(int i = 0; i<6; ++i){
                if(saves.get(i).getAge() == 6){
                    saves.get(i).giveList(list,n);
                    saves.get(i).setName(n);
                    button.get(i).setText(n);
                }
            }
            for (int i = 0; i<6; ++i)
                saves.get(i).incrementAge();
        }
        saveGameStates();
    }

    /**
     * Ez a metódus mind a hat betöltési gombnak álltíja a láthatóságát.
     * @param visible a láthatóság, true - látható, false - nem látható
     */
    private void visibleLoadButtons(boolean visible){
      for(int i = 0; i<6; ++i)
          button.get(i).setVisible(visible);
    }

    /**
     * Ez a metódus a játékmenet végével "eltűnteti" a játékot befejező PacMan-hez tartozó eddigi mentést, ha az létezik.
     * @param s a PacMan neve
     */
    private void removeSaveGame(String s){
        for(int i = 0; i<6; ++i){
            if(saves.get(i).getName().equals(s)){
                saves.get(i).setName("empty");
                button.get(i).setEnabled(false);
            }
        }
    }

    /**
     * Ez az ActionListener subclass felel azért, hogy amikor a menüsoron megnyomjuk a LOAD GAME Jbuttont, akkor megjelenjenek
     * a mentéseket vezérlő gombok.
     */
    public class LoadButtonListener implements ActionListener {

        /**
         * Megjeleníti a betöltési gombokat, és eltűnteti az új játékhoz szükséges elemeket
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            visibleLoadButtons(true);
            name.setVisible(false);
            nameTextField.setVisible(false);
            start.setVisible(false);
        }
    }

    /**
     * Ez az ActionListener subclass felel azért, hogy amikor a menüsoron megnyomjuk a NEW GAME Jbuttont, akkor megjelenjenek
     * az új játék kezdését vezérlő elemet.
     */
    public class NewGameButtonListener implements ActionListener {

        /**
         * Eltűntei a betöltési gombokat, és megjelníti az új játékhoz szükséges elemeket
         */
        @Override
        public void actionPerformed(ActionEvent e) {
                visibleLoadButtons(false);
                name.setVisible(true);
                nameTextField.setVisible(true);
                start.setVisible(true);
            }
        }

    /**
     * Ez az ActionListener subclass felel azért, hogy az új játék elinduléjon, a játékos által megadott névvel.
     */
    public class StartButtonListener implements ActionListener {

        /**
         * Elindítja az őj játékot, a nameOfPlayer JTextFieldből kiolvasott névvel, meghívja a game objektum startNewGame
         * metódusát
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String nameOfPlayer = nameTextField.getText();
            if(nameOfPlayer.equals(""))
                nameOfPlayer+="Winch Eszter";
            setVisible(false);
            nameTextField.setText("");
            game.setVisible(true);
            game.startNewGame(nameOfPlayer);
        }
    }

    /**
     * Ez az ActionListener subclass felel azért, hogy a kiválasztott mentett állás betöltődjön, és induljon a játék.
     */
    public class LoadGameListener implements ActionListener{

        private final int serial; /**< Annak a mentésnek az index eleme, amiért ez a gomb felel*/

        /**
         * Az osztály konstruktora. Itt kapja meg, hogy melyik indexű Save objektumért felel
         * @param s a Save objektum indexe
         */
        public LoadGameListener(int s){
            serial = s;
        }

        /**
         * Ha megnyomjuk a gombot, elindítja a játékot a megfelelő Save objektumtól kapott játékelemeket tartalmazó listával,
         * meghívja a game objektum startLoadGame metódusát
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            game.setVisible(true);
            game.startLoadGame(saves.get(serial).getList());
            setVisible(false);
        }
    }

    /**
     * Ez az ActionListener subclass felel azért, hogy a játékból ki lehessen lépni.
     */
    public class QuitListener implements ActionListener {

        /**
         * Ez a függvény menti el kilépéskor a mentéseket, illetve a ranglistát tartalmazó tárolókat, majd bezárja a programot
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            ///Játékállások, azaz a Save ojbketumok mentése
            try {
                saveGameStates();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            ///A ranglista mentése
            try {
                saveLeaderList();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            System.exit(50);
        }
    }
}
