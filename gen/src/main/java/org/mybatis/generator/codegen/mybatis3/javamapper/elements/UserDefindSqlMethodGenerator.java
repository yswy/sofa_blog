package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import com.zuoer.sofa.blog.base.utils.ArrayUtils;
import com.zuoer.sofa.blog.base.utils.ListUtils;
import com.zuoer.sofa.blog.base.utils.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author zuoer
 * @version UserDefindSqlMethodGenerator, v 0.1 2020/3/18 16:28 zuoer Exp $
 */
public class UserDefindSqlMethodGenerator extends
        AbstractJavaMapperMethodGenerator {
    @Override
    public void addInterfaceElements(Interface interfaze) {

        //找到表名对应的xml文件，规则为当前运行目录下的 tables/table_name.xml
        /**
         * 规则：
         * 1、返回类型
         * name为select/search开头的，若没有指定resultType则解析sql中 select到from 中间的，如果只有一个参数，则返回类型为该参数，否则为生成的do
         *          若有resultType则返回类型为resultType
         * name为update开头的 返回类型为long
         * name为insert开头的，返回类型为主键的类型
         * 另外，如果有multiplicity属性，则表示是多个，否则默认为单个
         * 2、参数
         * 先判断有没有extraParameter节点，若有，则参数为extraParameter
         * 若没有，
         * （1）先截取where后面的，按照and分割，获取等号前面的字段作为参数
         * （2）再判断类型，若为update 则获取where和set中间的字段，按照“，”间隔，等号前面的字段作为参数（这里的顺序要在（1）的前面）
         * （3）若为insert，只支持对象
         */
        try {

            File sqlFile = new File(context.getProperty("targetRunPath") + "/tables", introspectedTable.getTableConfiguration().getTableName() + ".xml");
            if (!sqlFile.exists()) {
                //不存在就算了
                System.out.println("文件不存在，跳过，sqlFile=" + sqlFile.getPath());
                return;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new FileReader(sqlFile)));
            NodeList nodelList = document.getElementsByTagName("operation");
            for (int i = 0; i < nodelList.getLength(); i++) {
                Node node = nodelList.item(i);
                String methodName = node.getAttributes().getNamedItem("name").getNodeValue();
                Node sqlNode = null;
                Node extraParamsNode = null;
                Node sqlMapNode = null;


                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node tmp = node.getChildNodes().item(j);
                    if (StringUtils.equals("sql", tmp.getNodeName())) {
                        sqlNode = tmp;
                    } else if (StringUtils.equals("extraparams", tmp.getNodeName())) {
                        extraParamsNode = tmp;
                    } else if (StringUtils.equals("sqlmap", tmp.getNodeName())) {
                        sqlMapNode = tmp;
                    }

                }

                if (sqlNode == null) {
                    throw new RuntimeException("根据sql生成自定义mapper失败,sql为空，tableName=" + introspectedTable.getTableConfiguration().getTableName() + ",sqlName=" + methodName);
                }

                String sql = sqlNode.getTextContent();
                FullyQualifiedJavaType returnType;
                //确定返回类型
                FullyQualifiedJavaType realType = null;
                String resultTypeInOperation = node.getAttributes().getNamedItem("resultType") == null ? null : node.getAttributes().getNamedItem("resultType").getNodeValue();
                if (StringUtils.isNotEmpty(resultTypeInOperation)) {
                    realType = new FullyQualifiedJavaType(resultTypeInOperation);
                } else {
                    String[] returnColumns = StringUtils.trim(StringUtils.split(StringUtils.substringBetween(sql, "select", "from"), ","));
                    if (ArrayUtils.getLength(returnColumns) == 1) {
                        realType = introspectedTable.getColumn(returnColumns[0]).getFullyQualifiedJavaType();
                    } else {
                        realType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
                    }
                }
                if (methodName.startsWith("select") || methodName.startsWith("search") ) {
                    if (StringUtils.equals(node.getAttributes().getNamedItem("multiplicity") == null ? null : node.getAttributes().getNamedItem("multiplicity").getNodeValue(), "many")) {
                        returnType = FullyQualifiedJavaType.getNewListInstance();
                        //如果是集合，还需要设置泛型
                        returnType.addTypeArgument(realType);
                    } else {
                        //如果不是返回集合，直接确定类型
                        returnType = realType;
                    }
                } else if (methodName.startsWith("insert")) {
                    List<IntrospectedColumn> primaryKeyList=introspectedTable.getPrimaryKeyColumns();
                    if(ListUtils.size(primaryKeyList) !=1){
                        throw new RuntimeException("根据sql生成自定义mapper失败,主键有且只能有一个，tableName=" + introspectedTable.getTableConfiguration().getTableName() + ",sqlName=" + methodName);
                    }

                    returnType =primaryKeyList.get(0).getFullyQualifiedJavaType();
                } else if (methodName.startsWith("update") || methodName.startsWith("delete") ) {
                    returnType = FullyQualifiedJavaType.getIntInstance();
                } else {
                    throw new RuntimeException("根据sql生成自定义mapper失败,请确认name前缀，目前支持select,search,update,insert，tableName=" + introspectedTable.getTableConfiguration().getTableName() + ",sqlName=" + methodName);
                }


                //导入
                Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
                importedTypes.add(returnType);

                Method method = new Method();
                method.setVisibility(JavaVisibility.PUBLIC);

                //确认参数

                String paramtype = node.getAttributes().getNamedItem("paramtype") == null ? null : node.getAttributes().getNamedItem("paramtype").getNodeValue();
                if (methodName.startsWith("insert") || StringUtils.equals("object", paramtype)) {
                    Parameter parameter = new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "insertDO");
                    method.addParameter(parameter);
                } else if (StringUtils.isNotEmpty(paramtype)) {
                    Parameter parameter = new Parameter(new FullyQualifiedJavaType(paramtype), StringUtils.substringAfterLast(paramtype, "."));
                    method.addParameter(parameter);
                } else if (extraParamsNode != null) {

                    for (int k = 0; k < extraParamsNode.getChildNodes().getLength(); k++) {
                        Node tmp = extraParamsNode.getChildNodes().item(k);
                        if(StringUtils.equals(tmp.getNodeName(),"param")){
                            Parameter parameter = new Parameter(new FullyQualifiedJavaType(tmp.getAttributes().getNamedItem("javatype") == null ? null : tmp.getAttributes().getNamedItem("javatype").getNodeValue()), tmp.getAttributes().getNamedItem("name") == null ? null : tmp.getAttributes().getNamedItem("name").getNodeValue());
                            method.addParameter(parameter);
                        }
                    }

                } else {
                    String whereSql=StringUtils.substringAfter(sql, " where ");
                    List<String> parameterInWhere = new ArrayList<>();
                    if(StringUtils.isNotEmpty(whereSql)){
                        int endIndex=whereSql.indexOf("?");
                        while(endIndex>=0){
                            parameterInWhere.add(StringUtils.trim(StringUtils.substringBefore(whereSql.substring(0,endIndex),"=")));
                            whereSql=whereSql.substring(endIndex+1);
                            endIndex=whereSql.indexOf("?");
                        }
                    }

                    String updateSet=StringUtils.substringBetween(sql, "set", " where ");
                    List<String> parameterInIUpdate =new ArrayList<>();
                    if(StringUtils.isNotEmpty(updateSet)){
                        for (String each : StringUtils.split(updateSet, ",")) {
                            if(StringUtils.contains(each,"?")){
                                parameterInIUpdate.add(StringUtils.trim(each.replace("=","").replace("?","")));

                            }
                        }
                    }

                    for (String updateParameter : parameterInIUpdate) {
                        Parameter parameter = new Parameter(introspectedTable.getColumn(updateParameter).getFullyQualifiedJavaType(), updateParameter);
                        method.addParameter(parameter);
                    }
                    for (String whereParameter : parameterInWhere) {
                        Parameter parameter = new Parameter(introspectedTable.getColumn(whereParameter).getFullyQualifiedJavaType(), whereParameter);
                        method.addParameter(parameter);
                    }
                }

                method.setReturnType(returnType);
                method.setName(methodName);

                context.getCommentGenerator().addGeneralMethodComment(method,
                        introspectedTable);

                addMapperAnnotations(interfaze, method);

                if (context.getPlugins().clientSelectAllMethodGenerated(method,
                        interfaze, introspectedTable)) {
                    addExtraImports(interfaze);
                    interfaze.addImportedTypes(importedTypes);
                    interfaze.addMethod(method);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("根据sql生成自定义mapper失败", e);
        }


    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
    }

    public void addExtraImports(Interface interfaze) {
    }
}
