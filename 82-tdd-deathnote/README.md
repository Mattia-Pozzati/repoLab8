# Test-first

Use the *Test-Driven Development (TDD)* methodology to develop the following.

1. Observe the `DeathNote` interface, understand how it is supposed to work
2. Create an implementation of `DeathNote` in which each method throws an Exception
3. Write a test for the `DeathNote` implementation (the test will fail and that is okay) testing the following:
   1. Rule number 0 and negative rules do not exist in the DeathNote rules.
      * check that the exceptions are thrown correctly, that their type is the expected one, and that the message is not null, empty, or blank.
   2. No rule is empty or null in the DeathNote rules.
      * for all the valid rules, check that none is null or blank
   3. The human whose name is written in the DeathNote will eventually die.
      * verify that the human has not been written in the notebook yet
      * write the human in the notebook
      * verify that the human has been written in the notebook
      * verify that another human has not been written in the notebook
      * verify that the empty string has not been written in the notebook
   4. Only if the cause of death is written within the next 40 milliseconds of writing the person's name, it will happen.
      * check that writing a cause of death before writing a name throws the correct exception
      * write the name of a human in the notebook
      * verify that the cause of death is a heart attack
      * write the name of another human in the notebook
      * set the cause of death to "karting accident"
      * verify that the cause of death has been set correctly (returned true, and the cause is indeed "karting accident")
      * sleep for 100ms
      * try to change the cause of death 
      * verify that the cause of death has not been changed
   5. Only if the cause of death is written within the next 6 seconds and 40 milliseconds of writing the death's details, it will happen.
      * check that writing the death details before writing a name throws the correct exception
      * write the name of a human in the notebook
      * verify that the details of the death are currently empty
      * set the details of the death to "ran for too long"
      * verify that death details have been set correctly (returned true, and the details are indeed "ran for too long")
      * write the name of another human in the notebook
      * sleep for 6100ms
      * try to change the details
      * verify that the details have not been changed
4. Ask for a correction of the tests
5. Verify that all tests fail
6. Modify the implementation of the `DeathNote` in such a way that all tests work

**Important notes**:
* the current time measured as seconds since the Unix epoch is available as `System.currentTimeMillis()`.
* the execution of a program can be paused for a given number of milliseconds using `Thread.sleep(long millis)`.

