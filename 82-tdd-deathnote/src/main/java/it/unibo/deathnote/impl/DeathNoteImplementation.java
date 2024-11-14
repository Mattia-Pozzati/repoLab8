package it.unibo.deathnote.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImplementation implements DeathNote {

    private final Map<String, DeathInfo> deaths = new HashMap<>();
    private String lastNameWritten;

    public DeathNoteImplementation() {}

    private static class DeathInfo {
        private String cause;
        private String details;
        private static final long TIME_TO_DEATH = 40;
        private static final long TIME_TO_SET_DETAILS = TIME_TO_DEATH + 6000;
        private final long timeOfDeath;

        DeathInfo() {
            this("heart attack","");
        }

        DeathInfo(final String cause, final String details) {
            this.cause = cause;
            this.details = details;
            this.timeOfDeath = System.currentTimeMillis();
        }

        public String getCause() {
            return cause;
        }

        public DeathInfo writeCause(String cause) {
            return System.currentTimeMillis() < this.timeOfDeath + TIME_TO_DEATH 
                ? new DeathInfo(cause, this.details) 
                : this;
        }

        public DeathInfo writeDetails(String details) {
            return System.currentTimeMillis() < this.timeOfDeath + TIME_TO_SET_DETAILS 
                ? new DeathInfo(this.cause, details) 
                : this;
        }

        public String getDetails() {
            return details;
        }

        public long getTimeOfDeath() {
            return timeOfDeath;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof DeathInfo other)) return false;
            return Objects.equals(this.cause, other.cause) 
                && Objects.equals(this.details, other.details)
                && this.timeOfDeath == other.timeOfDeath;
        }

        @Override
        public int hashCode() {
            return Objects.hash(cause, details, timeOfDeath);
        }
    }

    private boolean isInRange(int i, int f, int l) {
        return i >= f && i <= l;
    }

    @Override
    public String getRule(int ruleNumber) {
        Objects.requireNonNull(ruleNumber, "Rule number cannot be null");
        if (isInRange(ruleNumber, 1, RULES.size())) {
            return RULES.get(ruleNumber - 1); // Adjust for 0-based indexing
        } else {
            throw new IllegalArgumentException("Rule number " + ruleNumber + " isn't in range");
        }
    }

    @Override
    public void writeName(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        if (isNameWritten(name)) {
            throw new IllegalArgumentException(name + " is already in the notebook");
        }
        lastNameWritten = name;
        deaths.put(name, new DeathInfo());
    }

    @Override
    public boolean writeDeathCause(String cause) {
        return update(cause, new Updater(){
            @Override
            public DeathInfo call(String update) {
                 return deaths.get(lastNameWritten).writeCause(cause);
            }
        });
    }

    @Override
    public boolean writeDetails(String details) {
        return update(details, new Updater(){
            @Override
            public DeathInfo call(String update) {
                 return deaths.get(lastNameWritten).writeDetails(details);
            }
        });
    }

    private interface  Updater {
        DeathInfo call(String update);
    }

    private boolean update(String update, Updater updater){

        Objects.requireNonNull(update, "Details cannot be null");
        Objects.requireNonNull(updater, "Updater cannot be null");
        if (lastNameWritten == null) {
            throw new IllegalStateException("No name has been written yet");
        }
        DeathInfo currentInfo = deaths.get(lastNameWritten);
        DeathInfo updatedInfo = updater.call(update);
        if (currentInfo.equals(updatedInfo)) {
            return false;
        }
        deaths.put(lastNameWritten, updatedInfo);
        return true;

    }

    @Override
    public String getDeathCause(final String name) {
        return getDeath(name).cause;
    }

    @Override
    public String getDeathDetails(final String name) {
        return getDeath(name).details;
    }


    private DeathInfo getDeath(final String name) {
        final var death = deaths.get(name);
        if (death == null) {
            throw new IllegalArgumentException(name + " has never been written in this notebook");
        }
        return death;
    }

    @Override
    public boolean isNameWritten(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        return deaths.containsKey(name);
    }
}
