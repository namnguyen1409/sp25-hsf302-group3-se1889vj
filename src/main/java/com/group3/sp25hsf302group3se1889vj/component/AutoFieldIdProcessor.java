package com.group3.sp25hsf302group3se1889vj.component;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

public class AutoFieldIdProcessor extends AbstractAttributeTagProcessor {

    private static final String ATTR_NAME = "auto-id";
    private static int PRECEDENCE = 1000;

    public AutoFieldIdProcessor(String dialectPrefix) {
        super(
                TemplateMode.HTML,
                dialectPrefix,
                null,
                false,
                ATTR_NAME,
                true,
                PRECEDENCE,
                true
        );
    }


    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
        if (attributeValue != null && !attributeValue.isEmpty()) {
            Object value = null;

            if (attributeValue.startsWith("*{") && attributeValue.endsWith("}")) {
                IStandardExpression expression = StandardExpressions.getExpressionParser(context.getConfiguration())
                        .parseExpression(context, attributeValue);
                value = expression.execute(context);

                attributeValue = attributeValue.substring(2, attributeValue.length() - 1);
            }

            String idValue = attributeValue + "Field";
            structureHandler.setAttribute("id", idValue);

            if (value != null) {
                structureHandler.setAttribute("value", value.toString());
            }
        }
    }
}
