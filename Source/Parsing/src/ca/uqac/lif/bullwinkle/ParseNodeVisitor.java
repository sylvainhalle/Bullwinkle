package ca.uqac.lif.bullwinkle;

public interface ParseNodeVisitor
{
  public void visit(ParseNode node);
  
  public void pop();
}
