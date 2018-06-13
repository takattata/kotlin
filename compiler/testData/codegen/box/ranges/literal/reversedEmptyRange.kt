// TODO: muted automatically, investigate should it be ran for JS_IR or not
// IGNORE_BACKEND: JS_IR

// Auto-generated by org.jetbrains.kotlin.generators.tests.GenerateRangesCodegenTestData. DO NOT EDIT!
// WITH_RUNTIME


fun box(): String {
    val list1 = ArrayList<Int>()
    for (i in (5..3).reversed()) {
        list1.add(i)
        if (list1.size > 23) break
    }
    if (list1 != listOf<Int>()) {
        return "Wrong elements for (5..3).reversed(): $list1"
    }

    val list2 = ArrayList<Int>()
    for (i in (5.toByte()..3.toByte()).reversed()) {
        list2.add(i)
        if (list2.size > 23) break
    }
    if (list2 != listOf<Int>()) {
        return "Wrong elements for (5.toByte()..3.toByte()).reversed(): $list2"
    }

    val list3 = ArrayList<Int>()
    for (i in (5.toShort()..3.toShort()).reversed()) {
        list3.add(i)
        if (list3.size > 23) break
    }
    if (list3 != listOf<Int>()) {
        return "Wrong elements for (5.toShort()..3.toShort()).reversed(): $list3"
    }

    val list4 = ArrayList<Long>()
    for (i in (5.toLong()..3.toLong()).reversed()) {
        list4.add(i)
        if (list4.size > 23) break
    }
    if (list4 != listOf<Long>()) {
        return "Wrong elements for (5.toLong()..3.toLong()).reversed(): $list4"
    }

    val list5 = ArrayList<Char>()
    for (i in ('c'..'a').reversed()) {
        list5.add(i)
        if (list5.size > 23) break
    }
    if (list5 != listOf<Char>()) {
        return "Wrong elements for ('c'..'a').reversed(): $list5"
    }

    return "OK"
}
