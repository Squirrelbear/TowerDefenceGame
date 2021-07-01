/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * Debuff class:
 * Defines a debuff that can be used to store a type of debuff for a specified period of time.
 */
public class Debuff {
    /**
     * Defines the types of debuffs.
     * Slow: Used to apply a slow to the target it is on.
     */
    public enum DebuffType { Slow }

    /**
     * Timer to track when the debuff should expire.
     */
    private ActionTimer durationTimer;
    /**
     * The type of debuff used to apply any effects.
     */
    private DebuffType debuffType;

    /**
     * Creates the debuff with an effect and duration.
     *
     * @param type The type of debuff used to apply any effects.
     * @param duration Time till the debuff expires.
     */
    public Debuff(DebuffType type, int duration) {
        durationTimer = new ActionTimer(duration);
        debuffType = type;
    }

    /**
     * Updates the timer to move it toward expiring.
     *
     * @param deltaTime Time since last update.
     */
    public void update(int deltaTime) {
        durationTimer.update(deltaTime);
    }

    /**
     * Resets the duration timer back to the full time.
     */
    public void refresh() {
        durationTimer.reset();
    }

    /**
     * Test if the debuff should no longer be active.
     *
     * @return True if the debuff has run the full duration.
     */
    public boolean isExpired() {
        return durationTimer.isTriggered();
    }

    /**
     * Gets the type of debuff to check what effect should be applied.
     *
     * @return The type of debuff.
     */
    public DebuffType getDebuffType() {
        return debuffType;
    }

    /**
     * Test if the debuff type is the same as another object.
     *
     * @param o Reference to an object to compare against for equality.
     * @return True if the object is a Debuff and has a matching debuff type.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debuff otherDebuff = (Debuff) o;
        return debuffType == otherDebuff.debuffType;
    }
}
