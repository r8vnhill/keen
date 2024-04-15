/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.roomscheduling

/**
 * Represents a time-bound meeting with a defined start and end time.
 *
 * The `Meeting` class encapsulates the concept of a scheduled meeting by storing its start and end times.
 * It is designed for use in scheduling applications or time management systems where accurate time management
 * is crucial. Time values are represented as integers for flexibility, allowing representation in various
 * time formats such as hours of the day, minutes since midnight, or other domain-specific metrics.
 *
 * Complexity: The complexity of scheduling is often NP-Hard when optimizing multiple constraints, like minimizing
 * the number of rooms and avoiding time conflicts. This class forms the basis of representing the problem space.
 *
 * @property start The start time of the meeting, represented as an integer.
 * @property end The end time of the meeting, also an integer. It is assumed to be greater than or equal to `start`.
 * @constructor Creates a new `Meeting` instance with specified start and end times.
 */
internal data class Meeting(val start: Int, val end: Int)

/**
 * List of meetings for scheduling.
 *
 * This variable holds a list of Meeting objects, each representing a scheduled meeting. These meetings are to be
 * allocated to rooms with the goal of minimizing overlap and the number of rooms used, illustrating a typical
 * instance of the room scheduling problem.
 */
internal val RoomSchedulingProblem.meetings: List<Meeting>
    get() = listOf(
        Meeting(start = 1, end = 3),
        Meeting(start = 2, end = 3),
        Meeting(start = 5, end = 6),
        Meeting(start = 7, end = 9),
        Meeting(start = 4, end = 7),
        Meeting(start = 8, end = 10),
        Meeting(start = 2, end = 7),
        Meeting(start = 3, end = 4),
        Meeting(start = 1, end = 5),
        Meeting(start = 3, end = 6),
        Meeting(start = 4, end = 5)
    )
