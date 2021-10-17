package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.TravellingEvent;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;

import java.util.*;

/**
 * Handles routes/connections between islands.
 * Stores the destination, distance, and travelling events.
 * Connections are a directed edge to an island in the
 * island graph kept by the game world.
 */
public class Connection {
    /**
     * The destination of this connection.
     */
    private final Island DESTINATION;
    /**
     * How long this connection is.
     */
    private final int DISTANCE;
    /**
     * A list of all the possible events the player might encounter.
     */
    private final List<TravellingEvent<?>> possibleEvents;
    /**
     * The probability of encountering an event while travelling. This
     * is a percentage between 0 and 1, where 0 is 0% and 1 is 100%.
     * This is separate from the probability of each event.
     */
    private double eventProbability = 0;

    //* Constructors *//

    /**
     * Creates a new connection to an island.
     *
     * @param distance    The length of the connection.
     * @param destination The destination of the connection.
     * @param probability The probability of an event occuring along this connection.
     */
    public Connection(int distance, Island destination, double probability) {
        this.DISTANCE = distance;
        this.DESTINATION = destination;
        possibleEvents = new ArrayList<>();
        setEventProbability(probability);
    }

    //* On sailed methods *//

    /**
     * Called when the route is sailed. Randomly determines whether or
     * not to select and event, based on the even probability. If an
     * event is to run, then chooses and returns a random event to run.
     *
     * @return Returns the event to run if an event should run, otherwise returns null.
     */
    public TravellingEvent<?> onSail() {
        double roll = Math.random();
        if (roll < eventProbability) {
            return chooseEvent();
        }
        return null;
    }

    /**
     * Chooses an event to run from the event pool, accounting for weights.
     *
     * @return The event chosen to run.
     */
    private TravellingEvent<?> chooseEvent() {
        ArrayList<TravellingEvent<?>> weightedEvents = new ArrayList<>();
        for (TravellingEvent<?> event : possibleEvents) {
            for (int i = 0; i < event.getChanceWeight(); i++) {
                weightedEvents.add(event);
            }
        }
        if (weightedEvents.size() == 0)
            return null;
        int randEvent = new Random().nextInt(weightedEvents.size());
        return weightedEvents.get(randEvent);
    }

    //* Add to events *//

    /**
     * Adds an event to this connection's event pool.
     *
     * @param event The event to add.
     */
    public void addEvent(TravellingEvent<?> event) {
        possibleEvents.add(event);
    }

    /**
     * Adds a collection of travelling events to the event pool.
     *
     * @param events The events to add.
     */
    public void addEvents(Collection<? extends TravellingEvent<?>> events) {
        possibleEvents.addAll(events);
    }

    /**
     * Creates a new connection that is identical to this connection, except that the destination
     * of that connection is the given source island.
     *
     * @param source Source island that will be the destination of the reversed connection.
     * @return Returns the reversed connection of this connection.
     */
    public Connection reverseConnection(Island source) {
        Connection reversed = new Connection(DISTANCE, source, eventProbability);
        reversed.addEvents(possibleEvents);
        return reversed;
    }

    //* Getters and Setters *//

    /**
     * @return Returns the destination of this connection.
     */
    public Island getDestination() {
        return DESTINATION;
    }

    /**
     * Sets the event probability. If the probability is more than 1, sets the probability to 1.
     * If the probability is less than 0, sets the probability to 0.
     *
     * @param probability New event probability of this route.
     */
    public void setEventProbability(double probability) {
        if (probability > 1.0)
            probability = 1.0;
        else if (probability < 0.0)
            probability = 0.0;
        this.eventProbability = probability;
    }

    /**
     * @return Returns the event probability.
     */
    public double getEventProbability() {
        return eventProbability;
    }

    /**
     *
     * @return Returns the distance of this connection.
     */
    public int getDistance() {
        return DISTANCE;
    }

    /**
     * Gets a label of this route including the name and distance of the
     * destination of this route and how long it will take a given ship to
     * sail it.
     *
     * @param ship The ship trying to sail this route.
     * @return Returns the label of this event.
     */
    public String getSailingLabel(PlayerShip ship) {
        return DESTINATION.getName() +
                " (" + DISTANCE + " km, " +
                ship.getSailTime(DISTANCE) + " days)";
    }

    /**
     * Provides a description of events that a ship may
     * encounter when sailing this route.
     *
     * @return Returns a description of events of this route.
     */
    public String getDescription() {
        StringBuilder builder = new StringBuilder();
        if (possibleEvents.size() == 0) {
            builder.append("No possible events on this route!");
            return builder.toString();
        }

        builder.append("\n\t");
        builder.append(String.format("There is a %.0f%% chance of encounter one of the following hazards when travelling to "
                , eventProbability * 100.0));
        builder.append(DESTINATION.getName()).append(':');

        int totalWeight = 0;
        for (TravellingEvent<?> event : possibleEvents) {
            totalWeight += event.getChanceWeight();
        }

        for (TravellingEvent<?> event : possibleEvents) {
            builder.append("\n\t - ");
            builder.append(event.getName());
            int chance = (int) (100 * ((float) event.getChanceWeight() / totalWeight));
            builder.append(" (").append(chance).append("%)");
        }

        return builder.toString();
    }

    /**
     * Gets the list of events in this connection.
     *
     * @return Returns an unmodifiable list of events this connection has.
     */
    public List<TravellingEvent<?>> getEvents() {
        return Collections.unmodifiableList(possibleEvents);
    }

    /**
     *
     * @return Returns a string representation of the connection.
     */
    @Override
    public String toString() {
        return "Connection{" +
                "DESTINATION=" + DESTINATION +
                ", DISTANCE=" + DISTANCE +
                ", possibleEvents=" + possibleEvents +
                ", eventProbability=" + eventProbability +
                '}';
    }

    /**
     * Determines if a connection is equal to another.
     *
     * @param o Other connection to compare to.
     * @return Returns true if the connections are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return DISTANCE == that.DISTANCE
                && Double.compare(that.eventProbability, eventProbability) == 0
                && Objects.equals(DESTINATION, that.DESTINATION)
                && Objects.equals(possibleEvents, that.possibleEvents);
    }

    /**
     * Hashes the connection.
     *
     * @return Returns the hashcode of this connection.
     */
    @Override
    public int hashCode() {
        return Objects.hash(DESTINATION, DISTANCE, possibleEvents, eventProbability);
    }
}
