public class CommandParser {
    public CommandParser() {

    }

    public String[] parse(String input) {
        return switch (input) {
            case String s when s.matches("(good)?bye( -f)?") ->
                    new String[] { "bye" , s.matches("-f") ? "force" : null };
            case String s when s.matches("help") -> new String[] { "help" , "" };
            case String s when s.matches("(task(s)?)|(list(s)?)") -> new String[] { "list" , ""};
            // Add Task
            case String s when s.matches("add task .*") ->
                    new String[]{ "add", s.split("add task ", 2)[1] };
            // Add Task subclasses
            case String s when s.matches("(add )?todo .*") ->
                    new String[] { "add", "-t " + s.split("(add )?todo ", 2)[1] };
            case String s when s.matches("(add )?deadline .*") ->
                    new String[] { "add", "-d " + s.split("(add )?deadline ", 2)[1] };
            case String s when s.matches("(add )?event .*") ->
                    new String[] { "add", "-e " + s.split("(add )?event ", 2)[1] };
            // Do Task
            case String s when s.matches("do(ne)? task .*") ->
                    new String[] { "do", s.split("do(ne)? task ", 2)[1] };
            // Undo Task
            case String s when s.matches("undo(ne)? task .*") ->
                    new String[] { "undo", s.split("undo(ne)? task ", 2)[1] };
            // Delete Task
            case String s when s.matches("del(ete)? task .*") ->
                    new String[] { "del", s.split("del(ete)? task ", 2)[1] };
            default -> new String[] { "echo", input.isEmpty() ? "Caw~" : input + "~" };
        };
    }
}
