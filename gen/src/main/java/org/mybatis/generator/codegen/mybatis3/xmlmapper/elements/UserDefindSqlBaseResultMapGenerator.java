package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * @author zuoer
 * @version UserDefindSqlElementGenerator, v 0.1 2020/3/18 12:11 zuoer Exp $
 */
public class UserDefindSqlBaseResultMapGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        //找到表名对应的xml文件，规则为当前运行目录下的 tables/table_name.xml
        XmlElement answer = new XmlElement("resultMap"); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "id", "BaseResultMap")); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "type", new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()).getFullyQualifiedName())); //$NON-NLS-1$
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            XmlElement columnXml = new XmlElement("result"); //$NON-NLS-1$
            columnXml.addAttribute(new Attribute(
                    "column", column.getActualColumnName()));
            columnXml.addAttribute(new Attribute(
                    "jdbcType", column.getJdbcTypeName()));
            columnXml.addAttribute(new Attribute(
                    "property", column.getJavaProperty()));
            answer.addElement(columnXml);

        }
        context.getCommentGenerator().addComment(answer);
        parentElement.addElement(answer);

    }
}
