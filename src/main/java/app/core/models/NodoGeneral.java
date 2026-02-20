    package app.core.models;

    import java.util.ArrayList;
    import java.util.List;

    public class NodoGeneral<T> //T de Type
    {
        //Atributos
        private T dato;
        private int frecuencia;
        private List<NodoGeneral<T>> hijos;

        //Constructor
        public NodoGeneral(T dato) {
            this.dato = dato;
            this.frecuencia = 1;
            this.hijos = new ArrayList<>();
        }

        //Getters y Setters
        //Nodo
        public T getDato()
        {
            return dato;
        }
        public int getFrecuencia()
        {
            return frecuencia;
        }
        public void setFrecuencia(int frecuencia)
        {
            this.frecuencia = frecuencia;
        }
        public void setDato(T dato)
        {
            this.dato = dato;
        }
        //Hijos
        public List<NodoGeneral<T>> getHijos()
        {
            return hijos;
        }

        public void agregarHijo(NodoGeneral<T> hijo)
        {
            if (hijo != null) {
                this.hijos.add(hijo);
            }
        }
        public boolean esHoja()
        {
            return hijos.isEmpty();
        }
    }
