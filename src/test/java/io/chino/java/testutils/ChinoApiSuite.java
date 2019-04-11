package io.chino.java.testutils;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * Identifies a test suite which can only be executed against *.chino.io API host.
 */
public class ChinoApiSuite extends Suite {
    private final String message;

    public ChinoApiSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);

        if (TestConstants.HOST == null) {
            TestConstants.init();
        }

        this.message = String.format(
                "%s is set up to be run only executed against a '*.chino.io' host - current host is: %s.\n" +
                "If you want to run it anyway, run with property 'chino.test.force_all=true'.",
                klass.getCanonicalName(),
                TestConstants.HOST
        );
    }

    @Override
    public void run(RunNotifier notifier) {
        if (TestConstants.FORCE_ALL_TESTS || TestConstants.HOST.contains(".chino.io")) {
            super.run(notifier);
        } else {
            System.out.println(message);
            notifier.fireTestIgnored(getDescription());
        }
    }
}
