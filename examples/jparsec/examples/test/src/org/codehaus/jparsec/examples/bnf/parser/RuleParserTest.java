/*****************************************************************************
 * Copyright (C) Codehaus.org                                                *
 * ------------------------------------------------------------------------- *
 * Licensed under the Apache License, Version 2.0 (the "License");           *
 * you may not use this file except in compliance with the License.          *
 * You may obtain a copy of the License at                                   *
 *                                                                           *
 * http://www.apache.org/licenses/LICENSE-2.0                                *
 *                                                                           *
 * Unless required by applicable law or agreed to in writing, software       *
 * distributed under the License is distributed on an "AS IS" BASIS,         *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
 * See the License for the specific language governing permissions and       *
 * limitations under the License.                                            *
 *****************************************************************************/

package org.codehaus.jparsec.examples.bnf.parser;

import java.util.List;

import junit.framework.TestCase;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.examples.bnf.ast.AltRule;
import org.codehaus.jparsec.examples.bnf.ast.LiteralRule;
import org.codehaus.jparsec.examples.bnf.ast.Rule;
import org.codehaus.jparsec.examples.bnf.ast.RuleDef;
import org.codehaus.jparsec.examples.bnf.ast.RuleReference;
import org.codehaus.jparsec.examples.bnf.ast.SequentialRule;

/**
 * Unit test for {@link RuleParser}.
 * 
 * @author benyu
 */
public class RuleParserTest extends TestCase {
  
  public void testLiteral() {
    assertParser(RuleParser.LITERAL, "'foo'", LiteralRule.class, "'foo'");
    assertParser(RuleParser.LITERAL, "\"bar\"", LiteralRule.class, "'bar'");
    assertParser(RuleParser.LITERAL, "'\"'", LiteralRule.class, "'\"'");
    assertParser(RuleParser.LITERAL, "\"'\"", LiteralRule.class, "'''");
  }
  
  public void testIdent() {
    assertParser(RuleParser.IDENT, "foo", RuleReference.class, "foo");
  }
  
  public void testRule() {
    Parser<Rule> parser = RuleParser.rule();
    assertParser(parser, "foo", RuleReference.class, "foo");
    assertParser(parser, "'foo'", LiteralRule.class, "'foo'");
    assertParser(parser, "foo bar", SequentialRule.class, "foo bar");
    assertParser(parser, "foo bar | baz |'foo'", AltRule.class, "(foo bar | baz | 'foo')");
    assertParser(parser, "foo bar | \n  baz |'foo'", AltRule.class, "(foo bar | (baz | 'foo'))");
    assertParser(parser, "foo bar  baz |'foo'", AltRule.class, "(foo bar baz | 'foo')");
    assertParser(parser, "foo bar \n  baz |'foo'", SequentialRule.class, "foo bar (baz | 'foo')");
    assertParser(parser, "foo bar (baz |'foo')", SequentialRule.class, "foo bar (baz | 'foo')");
    assertParser(parser, "foo \n| bar", AltRule.class, "(foo | bar)");
    assertParser(parser, "foo | \n  bar | baz \n| 'foo'",
        AltRule.class, "(foo | (bar | baz) | 'foo')");
  }
  
  public void testRuleDef() {
    Parser<RuleDef> parser = RuleParser.RULE_DEF;
    assertParser(parser, "foo ::= bar | 'baz' \n  'a' | 'b'",
        RuleDef.class, "foo ::= (bar | 'baz' ('a' | 'b'))");
  }
  
  public void testRuleDefs() {
    Parser<List<RuleDef>> parser = RuleParser.RULE_DEFS;
    assertParser(parser, "foo ::= bar \n baz | 'baz' \n\n #line comment \nbar::='bar'",
        List.class, "[foo ::= bar (baz | 'baz'), bar ::= 'bar']");
  }
  
  private static void assertParser(
      Parser<?> parser, String source, Class<?> expectedClass, String string) {
    Object result = TerminalParser.parse(parser, source);
    assertTrue(expectedClass.isInstance(result));
    assertEquals(string, result.toString());
  }
}
