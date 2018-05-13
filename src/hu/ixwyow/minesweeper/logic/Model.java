package hu.ixwyow.minesweeper.logic;

import hu.ixwyow.minesweeper.ui.View;

/**
 * Az adatokat tartalmazó, Model típusú osztályok közös metódusait tartalmazza 
 *
 */
public abstract class Model {

	/**
	 * A Model-hez tartozó megjelenítő, amit nézetnek hívunk
	 */
    protected transient View view;

    /**
     * Nézet lekérdezése
     * @return A nézet
     */
    public View getView() {
        return view;
    }

    /**
     * Nézet beállítása
     * @param view Az új nézet
     */
    public void setView(View view) {
        this.view = view;
        this.view.update();
    }
    
    /**
     * A nézet frissítése
     */
    public void updateView() {
        if (view != null)
            view.update();
    }

}