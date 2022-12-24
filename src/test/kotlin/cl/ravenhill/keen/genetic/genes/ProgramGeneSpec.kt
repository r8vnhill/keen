package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.prog.functions.Add
import cl.ravenhill.keen.prog.functions.add
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class ProgramGeneSpec : WordSpec({
    "Creating a program gene" should {
        "have it's dna stored as a breadth first list" {
            val add = Add(0)
            val a = EphemeralConstant(0) { 1.0 }.also { add.left = it }
            val b = Add(1).also { add.right = it }
            val c = EphemeralConstant(1) { 2.0 }.also { b.left = it }
            val d = EphemeralConstant(1) { 3.0 }.also { b.right = it }
            val gene = ProgramGene(add, listOf(add()), listOf(EphemeralConstant(0) { 0.0 }))
            gene.children shouldBe listOf(add, a, b, c, d)
        }
    }
    "Invoking as a function" should {
        "reduce the program tree to a single value" {
            val add = Add(0)
            val a = EphemeralConstant(0) { 1.0 }.also { add.left = it }
            val b = Add(1).also { add.right = it }
            val c = EphemeralConstant(1) { 2.0 }.also { b.left = it }
            val d = EphemeralConstant(1) { 3.0 }.also { b.right = it }
            val gene = ProgramGene(add, listOf(add()), listOf(EphemeralConstant(0) { 0.0 }))
            gene() shouldBe 6.0
        }
    }
})