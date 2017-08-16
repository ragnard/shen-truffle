package com.github.ragnard.shen.klambda.runtime;

import java.util.ArrayList;

public class Cons {

    public static Cons EMPTY = new Cons(null, null);

    public final Object hd;
    public final Object tl;

    public Cons(Object hd, Object tl) {
        this.hd = hd;
        this.tl = tl;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (hd != null) sb.append(hd.toString());
        if (hd != null && tl != null) sb.append(" ");
        if (tl != null) sb.append(tl.toString());
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cons cons = (Cons) o;

        if (hd != null ? !hd.equals(cons.hd) : cons.hd != null) return false;
        return tl != null ? tl.equals(cons.tl) : cons.tl == null;
    }

    @Override
    public int hashCode() {
        int result = hd != null ? hd.hashCode() : 0;
        result = 31 * result + (tl != null ? tl.hashCode() : 0);
        return result;
    }

    public <T> java.util.List<T> toList() {
        ArrayList list = new ArrayList<>();

        for(Cons l = this; l != EMPTY; l = (Cons)l.tl) {
            list.add(l.hd);
        }

        return list;
    }
}

//public final class Cons extends AbstractCollection {
//
//    public static Object EMPTY = new Object();
//
//    public final Object head, tail;
//    public final int size;
//
//    public Cons(Object head, Object tail) {
//        this.head = head;
//        this.tail = tail;
//        this.size = tail instanceof ConsOld ? 1 + (((ConsOld) tail).size) : EMPTY_LIST.equals(tail) ? 1 : 2;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        ConsOld consOld = (ConsOld) o;
//
//        if (head != null ? !head.equals(consOld.head) : consOld.head != null) return false;
//        return tail != null ? tail.equals(consOld.tail) : consOld.tail == null;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = head != null ? head.hashCode() : 0;
//        result = 31 * result + (tail != null ? tail.hashCode() : 0);
//        return result;
//    }
//
//    boolean isList() {
//        return tail instanceof ConsOld || EMPTY_LIST.equals(tail);
//    }
//
//
//    @SuppressWarnings("NullableProblems")
//    public Iterator iterator() {
//        if (!isList()) throw new IllegalStateException("consOld pair is not a list: " + this);
//        return new ConsIterator();
//    }
//
//    public int size() {
//        return size;
//    }
//
////    public String toString() {
////        if (isList()) return vec(toList().stream().map(Numbers::maybeNumber)).toString();
////        return "[" + maybeNumber(head) + " | " + maybeNumber(tail) + "]";
////    }
//
//    public java.util.Cons<Object> toList() {
//        return new ArrayList<>(this);
//    }
////
////    public static Collection toCons(java.util.Cons<?> list) {
////        if (list.isEmpty()) return list;
////        ConsOld consOld = null;
////        list = new ArrayList<>(list);
////        reverse(list);
////        list.
////        for (Object o : list) {
////            if (o instanceof Cons) o = toCons((Cons<?>) o);
////            if (consOld == null) consOld = new ConsOld(o, EMPTY_LIST);
////            else consOld = new ConsOld(o, consOld);
////        }
////        return consOld;
////    }
//
//    class ConsIterator implements Iterator {
//        Cons consOld = Cons.this;
//
//        public boolean hasNext() {
//            return consOld != null;
//        }
//
//        public Object next() {
//            if (consOld == null) throw new NoSuchElementException();
//            try {
//                if (!consOld.isList()) return consOld;
//                return consOld.head;
//            } finally {
//                consOld =!consOld.isList() || EMPTY_LIST.equals(consOld.tail) ? null : (ConsOld) consOld.tail;
//            }
//        }
//    }
//}

