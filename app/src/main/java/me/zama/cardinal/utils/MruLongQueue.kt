package me.zama.cardinal.utils

import android.util.ArrayMap
import java.util.*
import kotlin.NoSuchElementException

class MruLongQueue : Queue<Long> {

    private class Node(val value: Long) {
        var prev: Node? = null
        var next: Node? = null
    }

    private val nodes = ArrayMap<Long, Node>()
    private val head = Node(Long.MIN_VALUE)
    private var tail = head

    override fun add(element: Long): Boolean {
        val current = nodes[element]
        if (current != null) {
            if (tail === current) return true
            current.prev?.next = current.next
            current.next?.prev = current.prev
            current.next = null
            current.prev = tail
            tail = current
            return true
        }
        val node = Node(element)
        node.prev = tail
        tail.next = node
        tail = node
        nodes[element] = node
        return true
    }

    override fun offer(element: Long) = add(element)

    override fun addAll(elements: Collection<Long>): Boolean {
        if (elements.isEmpty()) return false
        elements.forEach(::add)
        return true
    }

    override val size: Int
        get() = nodes.size

    override fun isEmpty() = nodes.isEmpty()

    override fun clear() {
        head.next = null
        tail = head
        nodes.clear()
    }

    override fun iterator() = object : MutableIterator<Long> {
        var prev = head
        var cur = head.next

        override fun hasNext() = cur != null

        override fun next(): Long {
            val res = cur?.value ?: throw NoSuchElementException("Iterator has no next element")
            prev = cur!!
            cur = cur!!.next
            return res
        }

        override fun remove() {
            if (prev === head) throw NoSuchElementException("Iterator has returned no elements so far")
            val oldPrev = prev
            prev = prev.prev!!
            remove(oldPrev.value)
        }
    }

    override fun contains(element: Long) = element in nodes.keys

    override fun containsAll(elements: Collection<Long>) = nodes.keys.containsAll(elements)

    override fun poll(): Long? {
        if (isEmpty()) return null
        val res = tail.value
        nodes.remove(res)
        tail = tail.prev!!
        tail.next = null
        return res
    }

    override fun remove() = poll() ?: throw NoSuchElementException("Queue is empty")

    override fun remove(element: Long): Boolean {
        val current = nodes.remove(element) ?: return false
        current.prev?.next = current.next
        current.next?.prev = current.prev
        if (tail !== current) return true
        tail = tail.prev!!
        tail.next = null
        return true
    }

    override fun removeAll(elements: Collection<Long>) =
        elements.fold(false) { acc, el -> remove(el) || acc }

    override fun retainAll(elements: Collection<Long>): Boolean {
        val set = elements.toSet()
        val keys = nodes.keys.toList()
        return keys.fold(false) { acc, k -> (if (k !in set) remove(k) else false) || acc }
    }

    override fun element() = peek() ?: throw NoSuchElementException("Queue is empty")

    override fun peek() = if (isEmpty()) null else tail.value
}