package everlastingconflict.windows;

import everlastingconflict.elements.ElementoSimple;

import java.util.Comparator;

public class UnitSorter implements Comparator<ElementoSimple> {

    @Override
    public int compare(ElementoSimple e1, ElementoSimple e2) {
        if (e1.nombre.equals(e2.nombre)) {
            return 0;
        } else {
            if ((int) e1.nombre.charAt(0) < (int) e2.nombre.charAt(0)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}