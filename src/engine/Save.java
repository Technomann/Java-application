package engine;

import character.Elements;

import java.io.Serializable;
import java.util.ArrayList;

/**\class Save
 * \brief Ez az osztály felel a mentés megvalósításáért.
 *
 * Ez egy szerializálható osztály, ami eltárol minden oplyan Elemnts típusú pályaelemet, ami a játék folytatása szempontjából
 * releváns, plusz, a játékos nevét is, hogy visszatöltéskor könnyebben tudja azonosítani a játékmenetet. Az osztály két fájlt használ,
 * ezek a gamestate és leaderboard nevű fájlok, a savegame mappában. Ha a mappa nem található indításkor, a program létrehozza azt.
 * A gamestate fájlba szerializálódnak a mentések, a leaderboard fájlba pedig a ranglista.
 */
public class Save implements Serializable {

    private static final long serialVersionUID = 40L; /**< Szerializáláshoz nem kötelezően szükséges egyéni azonosító*/

    private ArrayList<Elements> list; /**< A játékelemeket tartalmazó lista*/
    private int age; /**< A Save objektum "kora". Ennek értékére akkor van szükség, ha már mind a 6 mentés slot betelt.
     Ilyenkor ez alapján választ a játék, hogy melyiket írja felül*/
    private String name; /**< A játékos neve, akinél elmentették az állást*/

    /**
     * Az osztály konstrukora.
     * @param n Az adott játékos neve
     */
    public Save (String n){
        name = n;
    }

    /**
     * Getter az objektumban eltárolt pályaelemeket (Elements) tartalmazó listára.
     * @return a pályaelemeket tartalmazó lista
     */
    public ArrayList<Elements> getList (){
        return list;
    }

    /**
     * Amikor mentésre kerül a sor, egy pályaelemeket tartalmazó listát adunk át az adott Save objektumnak.
     * @param l az átadandó lista
     * @param n a játékos neve, aknél a mentés történik
     */
    public void giveList(ArrayList<Elements> l, String n){
        list = l;
        name = n;
    }

    /**
     * A metódus növeli az objektum age változójának értékét. Ha ez nagyobb a növelés után, mint 6, akkor 1-re állítja.
     */
    public void incrementAge(){
        age++;
        if(age > 6)
            age = 1;
    }

    /**
     * Setter az osztály name attribútumára.
     * @param s beállítandó String, mint a játékos által begépelt név
     */
    public void setName(String s){
        name = s;
    }

    /**
     * Getter a korra.
     * @return az adott Save objektum kora
     */
    public int getAge(){
        return age;
    }

    /**
     * Getter a name attribútumra.
     * @return egy String, ami a játékos által begépelt név volt, amikor a mentés készült
     */
    public String getName(){
        return name;
    }
}
