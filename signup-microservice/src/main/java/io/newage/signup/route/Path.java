package io.newage.signup.route;

/**
 * @author ivan
 */
public class Path {

    private Path() {
    }

    public static class Web {
        private Web() {
        }

        public static final String USER = "/api";

    }

    public static class Service {
        private Service() {
        }

        public static final String SIGNUP = "/signup";
    }
}
