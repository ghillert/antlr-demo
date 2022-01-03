package com.hillert.calculator;

import com.hillert.calculator.antlr.ExprLexer;
import com.hillert.calculator.antlr.ExprParser;
import com.hillert.calculator.expression.AntlrToProgram;
import com.hillert.calculator.expression.ExpressionProcessor;
import com.hillert.calculator.expression.Program;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.expression.ExpressionParser;

import java.io.IOException;

@SpringBootApplication
public class CalculatorApplication {

	public static void main(String[] args) {
		//SpringApplication.run(CalculatorApplication.class, args);

		if (args.length != 1) {
			System.err.println("Usage: file name");
		}
		else {
			String fileName = args[0];
			ExprParser parser = getParser(fileName);

			// tell ANTLR to build a parse tree
			// pare from the start symbol 'prog'
			ParseTree antlrAST = parser.prog();

			//Create a visitor for converting the parse tree into Program/Expression object
			AntlrToProgram progVisitor = new AntlrToProgram();
			Program prog = progVisitor.visit(antlrAST);

			if (progVisitor.semanticErrors.isEmpty()) {
				ExpressionProcessor ep = new ExpressionProcessor(prog.expressions);
				for (String evaluation : ep.getEvaluationResults()) {
					System.out.println(evaluation);
				}
			}
			else {
				for (String err : progVisitor.semanticErrors) {
					System.out.println(err);
				}
			}
		}
	}

	/*
	 * Here the types of parser and lexer are specific to the grammar name Expr.g4
	 */
	private static ExprParser getParser(String fileName) {
		ExprParser parser = null;
		try {
			CharStream input = CharStreams.fromFileName(fileName);
			ExprLexer lexer = new ExprLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			parser = new ExprParser(tokens);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return parser;

	}
}
