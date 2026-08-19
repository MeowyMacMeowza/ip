public class Korvus {

    private static String banner =
            """
             ,_
             | | _ ____--___   ___   _  ____
             | |/ |    |  __| / / | | |/ __/
             |   <| [] | | \\ ' /| |_| |\\__ \\
             |_|\\_|____|_|  \\_/  \\__,_|\\___/
            """;

    public static void main(String[] args) {
        greeting();
        goodbye();
    }

    private static void greeting() {
        divider();
        System.out.println(banner);
        System.out.println("> Nice to meet you!\n> I am caw-lled Korvus, your personal chatbot for keeping\n  track of shiny things.");
        divider();
    }

    private static void divider() {
        System.out.println("____________________________________________________________");
    }

    private static void goodbye() {
        System.out.println("> Goodbye! Eagle to see you again!");
        divider();

        // For now, redundant code
        System.exit(0);
    }
}

