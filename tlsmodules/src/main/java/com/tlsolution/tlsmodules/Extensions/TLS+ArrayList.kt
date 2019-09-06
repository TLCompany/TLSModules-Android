package com.tlsolution.tlsmodules.Extensions

import com.tlsolution.tlsmodules.Policy.Policy

fun ArrayList<Policy>.sortedOnes(): ArrayList<Policy> {

    var ones = this.sortedWith(Comparator({ p1, p2 ->
        when {
            !p1.isMandatory && p2.isMandatory -> 1
            p1.isMandatory && !p2.isMandatory -> -1
            else -> 0
        }
    }))

    return ArrayList(ones)
}

fun ArrayList<Policy>.mandatoryCounts(): Int {
    var count = 0
    this.forEach {
        count += if (it.isMandatory) 1 else 0
    }

    return count
}