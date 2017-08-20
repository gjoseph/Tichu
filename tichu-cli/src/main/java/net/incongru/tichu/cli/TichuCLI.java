package net.incongru.tichu.cli;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import jline.TerminalFactory;
import jline.UnixTerminal;
import jline.UnsupportedTerminal;
import jline.WindowsTerminal;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.tools.shell.AnsiDetector;
import org.fusesource.jansi.Ansi;

import java.io.IOException;

/**
 */
public class TichuCLI {
    public static void main(String[] args) throws IOException {
        try {
            // These are options that could be passed from the command line;
            final String type = TerminalFactory.AUTO;
            final boolean suppressColor = false;
            setTerminalType(type, suppressColor);

            final TichuCLI tichuCLI = new TichuCLI(type, suppressColor);
            tichuCLI.start();
        } finally {
            try {
                TerminalFactory.get().restore();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public TichuCLI(String termType, boolean suppressColor) throws IOException {
    }

    public void start() throws IOException {
        final CompilerConfiguration config = new CompilerConfiguration();
        // This is kinda gross. We use it to expose objects at the "root" of the binding, rather than subclass it, really.
        config.setScriptBaseClass(TichuDSL.class.getName());


        final UI ui = new UI();

        final Binding binding = new Binding();
        // binding.setVariable("game", game);
        binding.setVariable("ui", ui);

        final GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), binding, config);
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
            exec(ui, shell, line);
        }
    }

    private void exec(UI ui, GroovyShell shell, String line) throws IOException {
        ui.println("$ " + line);
        // run it in the DSL
        try {
            final Object res = shell.evaluate(line); // TODO for now we just expect everything to be a closure. handle better later.
            // ui.println(" ---> " + res);
            if (res == null) {
                throw new IllegalStateException(line + " returned null, should return a closure instead");
            }
            if (!(res instanceof Closure)) {
                throw new IllegalStateException(line + " returned a " + res.getClass().getSimpleName() + " should return a closure instead");

            }
            ((Closure) res).call();
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

    /**
     * @param type:         one of 'auto', 'unix', ('win', 'windows'), ('false', 'off', 'none');
     * @param suppressColor only has effect when ansi is enabled;
     */
    static void setTerminalType(String type, boolean suppressColor) {
        assert type != null;

        type = type.toLowerCase();
        boolean enableAnsi = true;
        switch (type) {
            case TerminalFactory.AUTO:
                type = null;
                break;
            case TerminalFactory.UNIX:
                type = UnixTerminal.class.getCanonicalName();
                break;
            case TerminalFactory.WIN:
            case TerminalFactory.WINDOWS:
                type = WindowsTerminal.class.getCanonicalName();
                break;
            case TerminalFactory.FALSE:
            case TerminalFactory.OFF:
            case TerminalFactory.NONE:
                type = UnsupportedTerminal.class.getCanonicalName();
                // Disable ANSI, for some reason UnsupportedTerminal reports ANSI as enabled, when it shouldn't;
                enableAnsi = false;
                break;
            default:
                // Should never happen;
                throw new IllegalArgumentException("Invalid Terminal type: " + type);
        }
        if (enableAnsi) {
            installAnsi(); // must be called before IO(), since it modifies System.in
            Ansi.setEnabled(!suppressColor);
        } else {
            Ansi.setEnabled(false);
        }

        if (type != null) {
            System.setProperty(TerminalFactory.JLINE_TERMINAL, type);
        }
    }

    static void installAnsi() {
        // Install the system adapters, replaces System.out and System.err;
        // Must be called before using IO(), because IO stores refs to System.out and System.err;
        //AnsiConsole.systemInstall();

        // Register jline ansi detector;
        Ansi.setDetector(new AnsiDetector());
    }
}
