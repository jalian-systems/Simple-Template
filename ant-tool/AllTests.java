import org.junit.runner.RunWith;
import org.junit.runners.Suite;

 @RunWith(Suite.class)
 @Suite.SuiteClasses({


    com.jaliansystems.simpletemplate.reader.LexerWithExpressionSeparatorsTest.class,
    com.jaliansystems.simpletemplate.reader.TemplateEvaluationTest.class,
    com.jaliansystems.simpletemplate.reader.TemplateLexerTest.class,
    com.jaliansystems.simpletemplate.reader.TemplateReaderTest.class,
    com.jaliansystems.simpletemplate.reader.TextLexerTest.class,
    com.jaliansystems.simpletemplate.reader.TokenTest.class,
    com.jaliansystems.simpletemplate.templates.CompositeTemplateTest.class,
    com.jaliansystems.simpletemplate.templates.IfTemplateTest.class,
    com.jaliansystems.simpletemplate.templates.IndexedAccessTemplateTest.class,
    com.jaliansystems.simpletemplate.templates.LiteralIntegerTemplateTest.class,
    com.jaliansystems.simpletemplate.templates.LiteralTextTemplateTest.class,
    com.jaliansystems.simpletemplate.templates.LoopTemplateTest.class,
    com.jaliansystems.simpletemplate.templates.ScopeTest.class,
    com.jaliansystems.simpletemplate.templates.VariableScopeTemplateTest.class,
    com.jaliansystems.simpletemplate.templates.VariableTemplateTest.class,
    com.jaliansystems.simpletemplate.templates.WithTemplateTest.class
    })
public class AllTests {
}


