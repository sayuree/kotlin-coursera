package games.game2048

fun <T : Any> List<T?>.moveAndMergeEqual(merge: (T) -> T): List<T> {
    val movedAndMerged = mutableListOf<T>()
    val iter = filterNotNull().listIterator()

    var first = iter.nextOrNull()
    var second = iter.nextOrNull()
    while (first != null) {
        if (first == second) {
            movedAndMerged.add(merge(first))
            first = iter.nextOrNull()
            second = iter.nextOrNull()
        } else {
            movedAndMerged.add(first)
            first = second
            second = iter.nextOrNull()
        }
    }

    return movedAndMerged.toList()
}

fun <E> ListIterator<E?>.nextOrNull(): E? {
    return if (hasNext()) next() else null
}


