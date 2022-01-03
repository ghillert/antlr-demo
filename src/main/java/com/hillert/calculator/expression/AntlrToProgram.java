package com.hillert.calculator.expression;

import com.hillert.calculator.antlr.ExprBaseVisitor;
import com.hillert.calculator.antlr.ExprParser;

import java.util.ArrayList;
import java.util.List;

public class AntlrToProgram extends ExprBaseVisitor<Program> {


	public List<String> semanticErrors; // to be access by the main application program

	@Override
	public Program visitProgram(ExprParser.ProgramContext ctx) {
		Program program = new Program();

		semanticErrors = new ArrayList<>();
		// a helper visitor for transforming each subtree into an expression object
		AntlrToExpression expressionVisitor = new AntlrToExpression(semanticErrors);

		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (i == ctx.getChildCount() - 1) {
				/* last child of the start symbol prog is EOF */
				// Do not visit this child and attempt to convert it to an Expression object
			}
			else {
				program.addExpression(expressionVisitor.visit(ctx.getChild(i)));
			}
		}

		return program;
	}
}
