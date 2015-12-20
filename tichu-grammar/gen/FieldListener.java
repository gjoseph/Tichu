// Generated from /Users/gjoseph/Dev/Incongru/Tichu/tichu-grammar/src/main/antlr4/Field.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FieldParser}.
 */
public interface FieldListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FieldParser#field}.
	 * @param ctx the parse tree
	 */
	void enterField(FieldParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link FieldParser#field}.
	 * @param ctx the parse tree
	 */
	void exitField(FieldParser.FieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link FieldParser#points}.
	 * @param ctx the parse tree
	 */
	void enterPoints(FieldParser.PointsContext ctx);
	/**
	 * Exit a parse tree produced by {@link FieldParser#points}.
	 * @param ctx the parse tree
	 */
	void exitPoints(FieldParser.PointsContext ctx);
	/**
	 * Enter a parse tree produced by {@link FieldParser#burial}.
	 * @param ctx the parse tree
	 */
	void enterBurial(FieldParser.BurialContext ctx);
	/**
	 * Exit a parse tree produced by {@link FieldParser#burial}.
	 * @param ctx the parse tree
	 */
	void exitBurial(FieldParser.BurialContext ctx);
	/**
	 * Enter a parse tree produced by {@link FieldParser#location}.
	 * @param ctx the parse tree
	 */
	void enterLocation(FieldParser.LocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link FieldParser#location}.
	 * @param ctx the parse tree
	 */
	void exitLocation(FieldParser.LocationContext ctx);
}