package net.incongru.tichu.cli;

import jline.DefaultTerminal2;
import jline.Terminal;
import jline.Terminal2;
import jline.TerminalFactory;
import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;
import net.incongru.tichu.model.Card;
import org.fusesource.jansi.Ansi;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

import static org.fusesource.jansi.Ansi.ansi;

/**
 *
 */
public class UI {
    private final ConsoleReader console;

    public UI() throws IOException {
        Terminal terminal = TerminalFactory.get();
        terminal = terminal instanceof Terminal2 ? (Terminal2) terminal : new DefaultTerminal2(terminal);

        // Not sure why this isnt using System.in, copied from an example
        console = new ConsoleReader(
                TichuCLI.class.getSimpleName(),
                // Not sure why this isn't using System.in, copied from an example
                new FileInputStream(FileDescriptor.in),
                System.out,
                terminal);
        //console.getOutput().append("terminal.isEchoEnabled() = " + terminal.isEchoEnabled() + "\n");
        console.setBellEnabled(false);
        console.addCompleter(new StringsCompleter("salut", "pouet"));
        console.clearScreen();
        console.setPrompt("tichu> ");
    }

    // Extend ansi ? return or void ?
    Ansi append(Card c, Ansi a) {
        if (c.getVal().isSpecial()) {
            a = a.fg(Ansi.Color.WHITE);
        } else {
            switch (c.getSuit()) {
                case Jade:
                    a = a.fg(Ansi.Color.GREEN);
                    break;
                case Pagoda:
                    a = a.fg(Ansi.Color.BLUE);
                    break;
                case Star:
                    a = a.fg(Ansi.Color.RED);
                    break;
                case Sword:
                    a = a.fg(Ansi.Color.BLACK);
                    break;
            }
        }
        return a.a(c.getVal().shortName()).reset();
    }

    public String readLine() throws IOException {
        return console.readLine();
    }

    @FunctionalInterface
    interface WithAnsi {
        Ansi doIt(Ansi a);
    }

    void printAnsi(WithAnsi a) throws IOException {
        final Ansi ansi = a.doIt(ansi()).reset();
        println(ansi);
    }

    void println(Ansi ansi) throws IOException {
        println(ansi.toString());
    }

    void println(String s) throws IOException {
        console.getOutput().append(s).append("\n");
    }

}
