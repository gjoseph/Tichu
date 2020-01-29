package net.incongru.tichu.cli;

import org.fusesource.jansi.Ansi;

import java.io.IOException;

/**
 * Some of this stuff is copied from groovysh and it's really terrible.
 */
public class TichuCLI {
    public static void main(String[] args) throws IOException {
        final TichuCLI tichuCLI = new TichuCLI();
        tichuCLI.start();
    }


    public TichuCLI() throws IOException {
    }

    public void start() throws IOException {
        final UI ui = new UI();
/*
        exec(ui, shell, "start");
        exec(ui, shell, "help");
        exec(ui, shell, "player 1 plays Dog"); // works
        exec(ui, shell, "player 'greg' plays dog");  // works
//        exec(ui, shell, "greg");
//        exec(ui, shell, "greg.plays(DraGon)"); // works
//        exec(ui, shell, "greg['plays'](r2)"); // works
//        exec(ui, shell, "greg['plays'] r3"); // works
//        exec(ui, shell, "greg plays r4");  // TODO does not work
        exec(ui, shell, "greg plays(r5)");  // works
        exec(ui, shell, "player 3 plays mahjong,r2,r3,r4,r5");
        exec(ui, shell, "isa plays mahjong,r2,r3,r4,r5"); // TODO does not work
        exec(ui, shell, "score");
        exec(ui, shell, "player 3 passes");
        exec(ui, shell, "player 'isa' passes");
        exec(ui, shell, "isa passes");
*/
        // If we remove the block below, program finishes before console renders.
        String line = null;
        while ((line = ui.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }
            exec(ui, line);
        }
    }

    private void exec(UI ui, String line) throws IOException {
        ui.println("$ " + line);
        // run it in the DSL
        try {
            final Object res = null;//shell.evaluate(line); // TODO for now we just expect everything to be a closure. handle better later.
            // ui.println(" ---> " + res);
            if (res == null) {
                throw new IllegalStateException(line + " returned null, should return a closure instead");
            }
//            if (!(res instanceof Closure)) {
//                throw new IllegalStateException(line + " returned a " + res.getClass().getSimpleName() + " should return a closure instead");
//            }
            // ((Closure) res).call();
        } catch (Exception e) { //GroovyRuntimeException
            ui.printAnsi(a -> a.a(Ansi.Attribute.INTENSITY_BOLD)
                    .fg(Ansi.Color.BLUE).a(line)
                    .fg(Ansi.Color.WHITE).a(" caused ")
                    .fg(Ansi.Color.RED).a(e.toString()));
//            e.printStackTrace();
            // throw e;
        }
        // ui.println("........");
    }

}
