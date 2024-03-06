package com.example;

import java.util.Arrays;

public class MyUnsortedList<E> implements UnsortedList<E> {

    private static class Link<E> {

        final E element;
        Link<E> next;

        private Link(E element) {
            this.element = element;
        }
    }

    private Link<E> head;
    private int size;

    private MyUnsortedList() {
        this.head = null;
        this.size = 0;
    }

    @SafeVarargs
    public static <E> MyUnsortedList<E> of(E... elements) {
        return of(Arrays.asList(elements));
    }

    public static <E> MyUnsortedList<E> of(Iterable<E> elements) {
        MyUnsortedList<E> list = new MyUnsortedList<>();
        for (E element : elements) {
            list.append(element);
        }
        return list;
    }

    @Override
    // Retourne true si la liste ne contient aucun élément
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    // Retourne le nombre d'éléments dans la liste
    public int size() {
        return size;
    }

    @Override
    // Ajoute un élément au début de la liste
    public void prepend(E element) {
        size++;
        Link<E> newHead = new Link<>(element);
        newHead.next = head;
        head = newHead;
    }

    @Override
    // Ajoute un élément à la fin de la liste
    public void append(E element) {
        insert(element, size);
    }

    @Override
    // Ajoute un élément à la position donnée
    // Si la valeur de pos est 0, l’élément est inséré en tant que premier élément de la liste
    // Si la valeur de pos est égale à la taille de la liste, l’élément est inséré en tant que dernier élément.
    // Une IndexOutOfBoundsException est levée si la position n’est pas valide
    public void insert(E elem, int pos) throws IndexOutOfBoundsException {
        if (pos < 0 || pos > size) {
            throw new IndexOutOfBoundsException();
        }
        if (pos == 0) {
            prepend(elem);
            return;
        }

        Link<E> prevLink = head;
        while (pos-- > 1) {
            prevLink = prevLink.next;
        }

        size++;
        Link<E> inserted = new Link<>(elem);
        Link<E> nextLink = prevLink.next;
        prevLink.next = inserted;
        inserted.next = nextLink;
    }

    @Override
    // Enlève l’élément en première position dans la liste et le retourne
    // Si la liste est vide, une exception de type EmptyListException doit être levée
    public E pop() {
        if (isEmpty()) {
            throw new EmptyListException();
        }

        size--;
        Link<E> oldHead = head;
        head = oldHead.next;

        return oldHead.element;
    }

    @Override
    // Enlève l’élément en dernière position dans la liste et le retourne
    // Si la liste est vide, une exception de type EmptyListException doit être levée
    public E popLast() {
        return remove(size - 1);
    }

    @Override
    // Enlève et retourne l’élément contenu à la position donnée
    // Une exception IndexOutOfBoundsException est levée si la position n’est pas valide
    public E remove(int pos) throws IndexOutOfBoundsException {
        if (pos < 0 || pos >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (pos == 0) {
            return pop();
        }

        Link<E> prevLink = head;
        while (--pos > 0) {
            prevLink = prevLink.next;
        }

        Link<E> removed = prevLink.next;
        prevLink.next = removed.next;

        // Fix : La méthode remove() ne mettais pas à jour la taille de la liste lorsqu'un élément était retiré ailleur qu'en tête
        size--;

        return removed.element;
    }

    @Override
    // Retourne vrai si deux listes sont égales, c’est-à-dire si elles contiennent les mêmes éléments dans le même ordre
    // Sinon, retourne faux
    public boolean equals(Object obj) {
        if (!(obj instanceof MyUnsortedList)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        MyUnsortedList<E> that = (MyUnsortedList<E>) obj;
        if (this.size != that.size) {
            return false;
        }

        Link<E> thisLink = this.head;
        Link<E> thatLink = that.head;
        while (thisLink != null) {
            if (thatLink == null || !thisLink.element.equals(thatLink.element)) {
                return false;
            }
            thisLink = thisLink.next;
            thatLink = thatLink.next;
        }

        return thatLink == null;
    }

    @Override
    // Retourne une représentation textuelle de la liste
    public String toString() {
        StringBuilder builder = new StringBuilder("MyUnsortedList { size = ");
        builder.append(size);
        builder.append(", [");

        MyUnsortedList.Link<E> link = head;
        while (link != null) {
            builder.append(link.element);
            link = link.next;
            if (link != null) {
                builder.append(", ");
            }
        }

        return builder.append("] }").toString();
    }
}
