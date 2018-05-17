package com.example.wuxio.gankexamples.utils.image;

/**
 * @author wuxio 2018-05-16:21:46
 */
public class Node < E > {

    E         item;
    Node< E > next;
    Node< E > prev;


    public Node(Node< E > prev, E element, Node< E > next) {

        this.item = element;
        this.next = next;
        this.prev = prev;
    }


    public boolean linkToHead(E e) {

        if (e != null) {

        }

        return false;
    }
}
