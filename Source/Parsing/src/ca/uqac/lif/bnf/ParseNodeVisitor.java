package ca.uqac.lif.bnf;

public interface ParseNodeVisitor
{
  public void visit(ParseNode node);
  
  public void pop();
}
