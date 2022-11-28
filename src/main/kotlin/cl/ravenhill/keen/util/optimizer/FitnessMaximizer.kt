/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.util.optimizer

import cl.ravenhill.keen.genetic.Phenotype


/**
 * [PhenotypeOptimizer] that prioritizes the maximum of two values.
 */
class FitnessMaximizer : PhenotypeOptimizer {

    override fun compare(p1: Phenotype<*>, p2: Phenotype<*>) = p1.fitness compareTo p2.fitness

    override fun toString() = "FitnessMaximizer"
}