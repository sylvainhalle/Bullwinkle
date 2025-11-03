import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.output.GraphvizVisitor;

public class CExt {
    public static void main(String[] args) {
        try {
            File bnfFile = new File("/home/sylvain/Workspaces/Bullwinkle/Source/Examples/src/Simple-Math.bnf");
            InputStream bnfStream = new FileInputStream(bnfFile);

    		BnfParser parser = new BnfParser(bnfStream);
            parser.setDebugMode(true);
            ParseNode node2 = parser.parse("10 + (3 - 4)");
            GraphvizVisitor visitor = new GraphvizVisitor();
            node2.prefixAccept(visitor);
            System.out.println(visitor.toOutputString());
        } catch (Exception e) {
            System.err.println("An error occured");
            e.printStackTrace();
        }
    }
}