package it.unibo.deathnote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImplementation;

import static it.unibo.deathnote.api.DeathNote.RULES;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class TestDeathNote {

    private DeathNote deathNote;
    private static final String DANILO_PIANINI = "Danilo Pianini";
    private static final String LIGHT_YAGAMI = "Light Yagami";
    private static final int INVALID_CAUSE_TIME = 100;
    private static final int INVALID_DETAILS_TIME = 6000 + INVALID_CAUSE_TIME;

    @BeforeEach
    void init() {
        deathNote = new DeathNoteImplementation();
    }

    /**
     * Tests that rule number 0 and negative rules do not exist.
     */
    @Test
    void testIllegalRule() {
        for (final var index: List.of(-1, 0, RULES.size() + 1)) {
            assertThrows(
                new IllegalArgumentThrower() {
                    @Override
                    public void run() {
                        deathNote.getRule(index);
                    }
                }
            );
        }
    }

    /**
     * Checks that no rule is empty or null.
     */
    @Test
    void testRules() {
        for (int i = 1; i <= RULES.size(); i++) {
            final var rule = deathNote.getRule(i);
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }
    }

    /**
     * Checks that the human whose name is written in this DeathNote will die.
     */
    @Test
    void testActualDeath() {
        assertFalse(deathNote.isNameWritten(DANILO_PIANINI));
        deathNote.writeName(DANILO_PIANINI);
        assertTrue(deathNote.isNameWritten(DANILO_PIANINI));
        assertFalse(deathNote.isNameWritten(LIGHT_YAGAMI));
        assertFalse(deathNote.isNameWritten(""));
    }

    /**
     * Checks that only if the cause of death is written within the next 40 milliseconds
     * of writing the person's name, it will happen.
     */
    @Test
    void testDeathCause() throws InterruptedException {
        assertThrows(
            new IllegalStateThrower() {
                @Override
                public void run() {
                    deathNote.writeDeathCause("spontaneous combustion");
                }
            }
        );
        deathNote.writeName(LIGHT_YAGAMI);
        assertEquals("heart attack", deathNote.getDeathCause(LIGHT_YAGAMI));
        deathNote.writeName(DANILO_PIANINI);
        assertTrue(deathNote.writeDeathCause("karting accident"));
        // Assuming the method can be executed in less than 40ms
        assertEquals("karting accident", deathNote.getDeathCause(DANILO_PIANINI));
        // Wait for more than 40 ms
        sleep(INVALID_CAUSE_TIME);
        assertFalse(deathNote.writeDeathCause("Spontaneous human combustion"));
        assertEquals("karting accident", deathNote.getDeathCause(DANILO_PIANINI));
    }

    /**
     * Checks that only if the cause of death is written within the next 6 seconds and
     * 40 milliseconds of writing the death's details, it will happen.
     */
    @Test
    void testDeathDetails() throws InterruptedException {
        assertThrows(
            new IllegalStateThrower() {
                @Override
                public void run() {
                    deathNote.writeDetails(LIGHT_YAGAMI);
                }
            }
        );
        deathNote.writeName(LIGHT_YAGAMI);
        assertEquals("", deathNote.getDeathDetails(LIGHT_YAGAMI));
        assertTrue(deathNote.writeDetails("ran for too long"));
        // Assuming the method can be executed in less than 6040ms
        assertEquals("ran for too long", deathNote.getDeathDetails(LIGHT_YAGAMI));
        // Wait for more than 6040 ms
        deathNote.writeName(DANILO_PIANINI);
        sleep(INVALID_DETAILS_TIME);
        assertFalse(deathNote.writeDetails("wrote many tests before dying"));
        assertEquals("", deathNote.getDeathDetails(DANILO_PIANINI));
    }

    static void assertThrows(final RuntimeExceptionThrower exceptionThrower) {
        try {
            exceptionThrower.run();
            fail("Exception was expected, but not thrown");
        } catch (IllegalStateException | IllegalArgumentException e) {
            assertTrue(
                exceptionThrower instanceof IllegalArgumentThrower && e instanceof IllegalArgumentException // NOPMD
                || exceptionThrower instanceof IllegalStateThrower && e instanceof IllegalStateException // NOPMD
            );
            assertNotNull(e.getMessage());
            assertFalse(e.getMessage().isBlank());
        }
    }

    private interface RuntimeExceptionThrower {
        void run();
    }

    private interface IllegalStateThrower extends RuntimeExceptionThrower { }

    private interface IllegalArgumentThrower extends RuntimeExceptionThrower { }
}