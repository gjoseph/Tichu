import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.junit.Test;

/**
 *
 */
public class ExampleTest {
    @Test
    public void testExampleField() throws Exception {
        FieldLexer l = new FieldLexer(new ANTLRInputStream(getClass().getResourceAsStream("/example.field")));
        FieldParser p = new FieldParser(new CommonTokenStream(l));
        p.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
            }
        });
        final FieldParser.FieldContext field = p.field();
        System.out.println("field.burial() = " + field.burial());
    }
}
